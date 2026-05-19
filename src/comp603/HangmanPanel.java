/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author willpurdon
 */
import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HangmanPanel extends JPanel {

    private final String word;
    private final Set<Character> guesses = new HashSet<>();
    private int triesLeft = 11;
    private boolean puzzleComplete = false;
    private String message = "";
    private Color messageColor = Color.WHITE;

    private final Runnable onWin;
    private final Runnable onLose;
    private final Player player;
    private final GameEngine engine;

    private JLabel wordLabel;
    private JLabel triesLabel;
    private JLabel wrongLabel;
    private JPanel keyboardPanel;

    private static final Random rand = new Random();

    public HangmanPanel(String word, GameEngine engine, Runnable onWin, Runnable onLose) {
        this.word = word;
        this.engine = engine;
        this.player = engine.player;
        this.onWin = onWin;
        this.onLose = onLose;

        setOpaque(false);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buildTopSection();
        buildKeyboard();
    }

    // ── Top: drawing + word info ─────────────────────────────────────────────
    private void buildTopSection() {
        JPanel top = new JPanel(new BorderLayout(20, 0));
        top.setOpaque(false);

        // Hangman drawing (left)
        HangmanDrawing drawing = new HangmanDrawing();
        drawing.setPreferredSize(new Dimension(160, 200));
        drawing.setOpaque(false);
        top.add(drawing, BorderLayout.WEST);

        // Word + info (right)
        JPanel info = new JPanel(new GridLayout(4, 1, 0, 8));
        info.setOpaque(false);

        wordLabel = new JLabel(buildWordDisplay(), SwingConstants.LEFT);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 26));
        wordLabel.setForeground(Color.WHITE);

        triesLabel = new JLabel("Tries left: " + triesLeft, SwingConstants.LEFT);
        triesLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        triesLabel.setForeground(new Color(210, 170, 50));

        wrongLabel = new JLabel("Wrong: ", SwingConstants.LEFT);
        wrongLabel.setFont(new Font("Monospaced", Font.PLAIN, 13));
        wrongLabel.setForeground(new Color(200, 60, 50));

        JLabel msgLabel = new JLabel(" ", SwingConstants.LEFT) {
            @Override
            public String getText() {
                return message;
            }

            @Override
            public Color getForeground() {
                return messageColor;
            }
        };
        msgLabel.setFont(new Font("Monospaced", Font.ITALIC, 13));

        info.add(wordLabel);
        info.add(triesLabel);
        info.add(wrongLabel);
        info.add(msgLabel);

        top.add(info, BorderLayout.CENTER);

        // Store drawing ref so we can repaint it
        add(top, BorderLayout.CENTER);

        // Keep drawing ref for updates
        this.drawing = drawing;
    }

    // ── Keyboard ─────────────────────────────────────────────────────────────
    private void buildKeyboard() {
        keyboardPanel = new JPanel(new GridLayout(2, 13, 4, 4));
        keyboardPanel.setOpaque(false);

        for (char c = 'A'; c <= 'Z'; c++) {
            JButton btn = makeLetterButton(c);
            keyboardPanel.add(btn);
        }

        add(keyboardPanel, BorderLayout.SOUTH);
    }

    private JButton makeLetterButton(char letter) {
        JButton btn = new JButton(String.valueOf(letter));
        btn.setFont(new Font("Monospaced", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(40, 40, 40));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> handleGuess(letter, btn));
        return btn;
    }

    // ── Game logic ───────────────────────────────────────────────────────────
    private void handleGuess(char letter, JButton btn) {
        if (puzzleComplete || guesses.contains(letter)) {
            return;
        }

        guesses.add(letter);
        btn.setEnabled(false);

        if (word.contains(String.valueOf(letter))) {
            // Correct guess
            btn.setBackground(new Color(40, 100, 40));
            message = "Correct!\nSync: \"NO!\"";
            messageColor = new Color(80, 200, 80);
        } else {
            // Wrong guess
            btn.setBackground(new Color(100, 30, 30));
            triesLeft--;
            drawing.setStage(11 - triesLeft);

            // Dice roll for damage (from original GameMechanics)
            if (GameMechanics.rollD12() <= 5) {
                int dmg = rand.nextInt(3) + 1;
                player.takeDamage(dmg);
                engine.window.updateHealth();
                message = "Sync throws equipment at you! Sync: \"Got you!\"";
                messageColor = new Color(200, 60, 50);
                if(!player.isAlive()){
                    puzzleComplete = true;
                    Timer t = new Timer(800, e -> onLose.run());
                    t.setRepeats(false);
                    t.start();
                    return;
                }
            } else {
                message = "Sync throws equipment at you... But you dodge it! Sync: \"No!\"";
                messageColor = new Color(210, 170, 50);
            }
        }

        // Update displays
        wordLabel.setText(buildWordDisplay());
        triesLabel.setText("Tries left: " + triesLeft);
        updateWrongLetters();
        repaint();

        // Check win/loss
        if (isWordGuessed()) {
            puzzleComplete = true;
            message = "The word was: " + word;
            messageColor = new Color(80, 200, 80);
            repaint();
            Timer t = new Timer(1200, e -> onWin.run());
            t.setRepeats(false);
            t.start();
        } else if (triesLeft <= 0 || !player.isAlive()) {
            puzzleComplete = true;
            message = "Sync: \"Wow you really suck!\"";
            messageColor = new Color(200, 60, 50);
            repaint();
            Timer t = new Timer(1200, e -> onLose.run());
            t.setRepeats(false);
            t.start();
        }
    }

    private String buildWordDisplay() {
        StringBuilder sb = new StringBuilder();
        for (char c : word.toCharArray()) {
            sb.append(guesses.contains(c) ? c : "_");
            sb.append("  ");
        }
        return sb.toString().trim();
    }

    private boolean isWordGuessed() {
        for (char c : word.toCharArray()) {
            if (!guesses.contains(c)) {
                return false;
            }
        }
        return true;
    }

    private void updateWrongLetters() {
        StringBuilder sb = new StringBuilder("Wrong: ");
        for (char c : guesses) {
            if (!word.contains(String.valueOf(c))) {
                sb.append(c).append(" ");
            }
        }
        wrongLabel.setText(sb.toString());
    }

    // ── Hangman drawing component ────────────────────────────────────────────
    private HangmanDrawing drawing;

    class HangmanDrawing extends JComponent {

        private int stage = 0; // 0 = empty, 11 = full hangman

        public void setStage(int stage) {
            this.stage = stage;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(200, 200, 190));

            int w = getWidth();
            int h = getHeight();

            // Base positions
            int baseY = h - 20;
            int poleX = w / 4;
            int topY = 20;
            int armX = w / 2;
            int headCX = armX;
            int headCY = topY + 20;
            int headR = 15;
            int bodyTopY = headCY + headR;
            int bodyBotY = bodyTopY + 50;
            int midX = headCX;

            // Stage 1: base
            if (stage >= 1) {
                g2.drawLine(poleX - 30, baseY, poleX + 30, baseY);
            }
            // Stage 2: vertical pole
            if (stage >= 2) {
                g2.drawLine(poleX, baseY, poleX, topY);
            }
            // Stage 3: horizontal arm
            if (stage >= 3) {
                g2.drawLine(poleX, topY, armX, topY);
            }
            // Stage 4: rope
            if (stage >= 4) {
                g2.drawLine(armX, topY, armX, headCY - headR);
            }
            // Stage 5: head
            if (stage >= 5) {
                g2.drawOval(headCX - headR, headCY - headR, headR * 2, headR * 2);
            }
            // Stage 6: body
            if (stage >= 6) {
                g2.drawLine(midX, bodyTopY, midX, bodyBotY);
            }
            // Stage 7: left arm
            if (stage >= 7) {
                g2.drawLine(midX, bodyTopY + 15, midX - 20, bodyTopY + 35);
            }
            // Stage 8: right arm
            if (stage >= 8) {
                g2.drawLine(midX, bodyTopY + 15, midX + 20, bodyTopY + 35);
            }
            // Stage 9: left leg
            if (stage >= 9) {
                g2.drawLine(midX, bodyBotY, midX - 20, bodyBotY + 30);
            }
            // Stage 10: right leg
            if (stage >= 10) {
                g2.drawLine(midX, bodyBotY, midX + 20, bodyBotY + 30);
            }
            // Stage 11: face (X eyes)
            if (stage >= 11) {
                g2.setColor(new Color(200, 60, 50));
                g2.drawLine(headCX - 8, headCY - 5, headCX - 4, headCY - 1);
                g2.drawLine(headCX - 4, headCY - 5, headCX - 8, headCY - 1);
                g2.drawLine(headCX + 4, headCY - 5, headCX + 8, headCY - 1);
                g2.drawLine(headCX + 8, headCY - 5, headCX + 4, headCY - 1);
            }
        }
    }
}
