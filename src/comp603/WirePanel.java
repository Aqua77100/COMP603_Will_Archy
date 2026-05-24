package comp603;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class WirePanel extends JPanel {

    // ── Data ────────────────────────────────────────────────────────────────
    private static final int PLUG_RADIUS = 10;

    // Wire plugs on the left (source)
    private final String[] wireLabels = {"RED", "BLUE", "GREEN", "YELLOW"};
    private final Color[]  wireColors = {Color.RED, new Color(80, 140, 255), new Color(80, 200, 80), Color.YELLOW};

    // Ports on the right (destination)
    private final String[] portLabels = {"PORT A", "PORT B", "PORT C", "PORT D"};

    // Correct answer: wire index → port index
    private final int[] correctAnswer = {1, 0, 2, 3}; // BLUE->A, RED->B, GREEN->C, YELLOW->D
    // i.e. wire 0 (RED)→port 1, wire 1 (BLUE)→port 0, wire 2 (GREEN)→port 2

    // Current connections: wire index → port index (-1 = unconnected)
    private final int[] connections = {-1, -1, -1, -1};

    // Drag state
    private int draggingWire = -1;      // which wire is being dragged
    private Point dragPoint = null;     // current mouse position during drag

    // Positions (calculated in paintComponent)
    private Point[] wirePluPoints;      // left side plug centres
    private Point[] portPoints;         // right side port centres

    // Callback when puzzle is solved
    private Runnable onSolved;
    private Runnable onFailed; // optional: if you want wrong answer feedback

    
    // timer stuff
    private javax.swing.Timer countdownTimer;
    private int timeLeft = 60; // time to solve puzzle
    private boolean puzzleComplete = false;

    // ────────────────────────────────────────────────────────────────────────
    public WirePanel(Runnable onSolved, Runnable onFailed) {
        this.onSolved = onSolved;
        this.onFailed = onFailed;
        setOpaque(false);
        setPreferredSize(new Dimension(500, 200));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check if click is near a wire plug
                for (int i = 0; i < wirePluPoints.length; i++) {
                    if (wirePluPoints[i] != null &&
                        e.getPoint().distance(wirePluPoints[i]) < PLUG_RADIUS + 5) {
                        draggingWire = i;
                        connections[i] = -1; // disconnect if re-dragging
                        dragPoint = e.getPoint();
                        repaint();
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggingWire == -1) return;

                // Check if released near a port
                boolean snapped = false;
                for (int p = 0; p < portPoints.length; p++) {
                    if (portPoints[p] != null &&
                        e.getPoint().distance(portPoints[p]) < PLUG_RADIUS + 10) {

                        // Disconnect any wire already in this port
                        for (int w = 0; w < connections.length; w++) {
                            if (connections[w] == p) connections[w] = -1;
                        }

                        connections[draggingWire] = p;
                        snapped = true;
                        break;
                    }
                }

                draggingWire = -1;
                dragPoint = null;
                repaint();

                if (snapped) checkSolution();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingWire != -1) {
                    dragPoint = e.getPoint();
                    repaint();
                }
            }
        });
        
        startCountdown();
    }

    // ── Drawing ──────────────────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelW = getWidth();
        int panelH = getHeight();
        int rows = wireLabels.length;
        int rowH = panelH / (rows + 1);

        // Calculate positions
        wirePluPoints = new Point[rows];
        portPoints    = new Point[rows];

        for (int i = 0; i < rows; i++) {
            wirePluPoints[i] = new Point(80,        rowH * (i + 1));
            portPoints[i]    = new Point(panelW - 80,    rowH * (i + 1));
        }

        // ── Draw completed connections ───────────────────────────────────────
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < connections.length; i++) {
            if (connections[i] != -1) {
                g2.setColor(wireColors[i]);
                Point from = wirePluPoints[i];
                Point to   = portPoints[connections[i]];
                drawCurvedWire(g2, from, to);
            }
        }

        // ── Draw active drag line ────────────────────────────────────────────
        if (draggingWire != -1 && dragPoint != null) {
            g2.setColor(wireColors[draggingWire]);
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                         0, new float[]{6, 4}, 0)); // dashed while dragging
            drawCurvedWire(g2, wirePluPoints[draggingWire], dragPoint);
        }

        // ── Draw wire plugs (left) ───────────────────────────────────────────
        g2.setStroke(new BasicStroke(2));
        for (int i = 0; i < rows; i++) {
            Point p = wirePluPoints[i];

            // Label
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.drawString(wireLabels[i], p.x - 70, p.y + 5);

            // Plug circle
            g2.setColor(wireColors[i]);
            g2.fillOval(p.x - PLUG_RADIUS, p.y - PLUG_RADIUS,
                        PLUG_RADIUS * 2, PLUG_RADIUS * 2);
            g2.setColor(Color.WHITE);
            g2.drawOval(p.x - PLUG_RADIUS, p.y - PLUG_RADIUS,
                        PLUG_RADIUS * 2, PLUG_RADIUS * 2);
        }

        // ── Draw ports (right) ───────────────────────────────────────────────
        for (int i = 0; i < rows; i++) {
            Point p = portPoints[i];

            // Label
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Monospaced", Font.BOLD, 13));
            g2.drawString(portLabels[i], p.x + PLUG_RADIUS + 8, p.y + 5);

            // Port circle (hollow = empty, filled = connected)
            boolean connected = false;
            int connectedWire = -1;
            for (int w = 0; w < connections.length; w++) {
                if (connections[w] == i) { connected = true; connectedWire = w; break; }
            }
            if (connected) {
                g2.setColor(wireColors[connectedWire]);
                g2.fillOval(p.x - PLUG_RADIUS, p.y - PLUG_RADIUS,
                            PLUG_RADIUS * 2, PLUG_RADIUS * 2);
            }
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawOval(p.x - PLUG_RADIUS, p.y - PLUG_RADIUS,
                        PLUG_RADIUS * 2, PLUG_RADIUS * 2);
        }
        
        // countdown timer display
        String timeText = "Time left: " + timeLeft + "s";
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        
        if(timeLeft > 10){
            g2.setColor(Color.GREEN);
        } else if(timeLeft > 5){
            g2.setColor(Color.YELLOW);
        } else{
            g2.setColor(Color.RED);
        }
        
        FontMetrics fm = g2.getFontMetrics();
        int timeX = (panelW - fm.stringWidth(timeText)) / 2;
        g2.drawString(timeText, timeX, 30);
    }

    // Draws a smooth curved wire between two points
    private void drawCurvedWire(Graphics2D g2, Point from, Point to) {
        int cx1 = from.x + (to.x - from.x) / 3;
        int cx2 = to.x   - (to.x - from.x) / 3;
        g2.draw(new java.awt.geom.CubicCurve2D.Float(
            from.x, from.y, cx1, from.y, cx2, to.y, to.x, to.y
        ));
    }

    // ── Solution check ───────────────────────────────────────────────────────
    private void checkSolution() {
        // Check all wires are connected
        for (int c : connections) {
            if (c == -1) return; // not all connected yet
        }

        // Check correctness
        boolean correct = Arrays.equals(connections, correctAnswer);
        if (correct) {
            puzzleComplete = true;
            countdownTimer.stop();
            repaint();
            // Small delay so player sees the success message
            Timer t = new Timer(800, e -> onSolved.run());
            t.setRepeats(false);
            t.start();
        } else {
            repaint();
            // Reset after a moment
            Timer t = new Timer(1000, e -> {
                Arrays.fill(connections, -1);
                repaint();
                if (onFailed != null) onFailed.run();
            });
            t.setRepeats(false);
            t.start();
        }
    }
    
    private void startCountdown(){
        countdownTimer = new javax.swing.Timer(1000, e -> {
            if(puzzleComplete){
                countdownTimer.stop();
                return;
            }
            timeLeft--;
            System.out.println(timeLeft);
            repaint(); // update display each second
            if(timeLeft <= 0){
                countdownTimer.stop();
                if(!puzzleComplete){
                    repaint();
                    javax.swing.Timer deathDelay = new javax.swing.Timer(800, d -> onFailed.run());
                    deathDelay.setRepeats(false);
                    deathDelay.start();
                }
            }
        });
        countdownTimer.start();
    }
    
    public void stop(){
        if(countdownTimer != null){
            countdownTimer.stop();
        }
        puzzleComplete = true;
    }
}