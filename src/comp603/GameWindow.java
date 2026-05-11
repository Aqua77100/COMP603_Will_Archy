/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author willpurdon
 */
public class GameWindow extends JFrame {

    private JLayeredPane layeredPane;
    private JLabel backgroundImage;
    private JPanel dialogueOverlay;
    private JTextArea storyArea;
    private JPanel choicesPanel;
    private JLabel healthLabel;
    private GameEngine engine;

    public GameWindow(GameEngine engine) {
        this.engine = engine;
        setTitle("Sync");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane() {
            @Override
            public void doLayout() {
                int w = getWidth();
                int h = getHeight();

                // Background fills entire window
                backgroundImage.setBounds(0, 0, w, h);

                // Dialogue overlay pinned to bottom
                int overlayHeight = (int) (h * 0.4);
                int margin = 20;
                dialogueOverlay.setBounds(
                        margin, // x (left margin)
                        h - overlayHeight - margin, // y (pushed to bottom)
                        w - (margin * 2), // width (full width minus margins)
                        overlayHeight // height
                );
            }
        };
        setContentPane(layeredPane);

        buildBackgroundLayer();
        buildDialogueOverlay();
    }

    private void buildBackgroundLayer() {
        backgroundImage = new JLabel();
        backgroundImage.setBounds(0, 0, 800, 600);
        backgroundImage.setBackground(Color.BLACK);
        backgroundImage.setOpaque(true);
        layeredPane.add(backgroundImage, JLayeredPane.DEFAULT_LAYER);
    }

    private void buildDialogueOverlay() {
        // Position: sits at the bottom, ~40% of screen height
        int overlayY = 680 - 280;
        dialogueOverlay = new JPanel(new BorderLayout(0, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                // Semi-transparent dark background
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 200)); // 200/255 opacity
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        dialogueOverlay.setOpaque(false); // let paintComponent handle bg
        dialogueOverlay.setBounds(20, overlayY, 880, 250);
        dialogueOverlay.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        // Story text
        storyArea = new JTextArea();
        storyArea.setEditable(false);
        storyArea.setOpaque(false);
        storyArea.setLineWrap(true);
        storyArea.setWrapStyleWord(true);
        storyArea.setForeground(Color.WHITE);
        storyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // Choices
        choicesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        choicesPanel.setOpaque(false);

        // HUD (name + health) at the top of dialogue box
        healthLabel = new JLabel("◆◆◆◆◆◆◆◆◆◆  10/10");
        healthLabel.setForeground(new Color(80, 180, 80));
        healthLabel.setFont(new Font("Monospaced", Font.BOLD, 12));

        dialogueOverlay.add(healthLabel, BorderLayout.NORTH);
        dialogueOverlay.add(storyArea, BorderLayout.CENTER);
        dialogueOverlay.add(choicesPanel, BorderLayout.SOUTH);

        layeredPane.add(dialogueOverlay, JLayeredPane.PALETTE_LAYER); // layer 1
    }

    public void setBackground(String imagePath) {
        SwingUtilities.invokeLater(() -> {
            java.io.File f = new java.io.File(imagePath);
            System.out.println("Loading image: " + f.getAbsolutePath()
                    + " | exists: " + f.exists()); // ← check this output
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage()
                    .getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            backgroundImage.setIcon(new ImageIcon(scaled));
            backgroundImage.repaint();
        });
    }

    public void showText(String text) {
        SwingUtilities.invokeLater(() -> {
            storyArea.setText(text);
            updateHealth();
        });
    }

    public void setChoices(List<String[]> choices) {
        SwingUtilities.invokeLater(() -> {
            choicesPanel.removeAll();
            for (String[] c : choices) {
                choicesPanel.add(makeChoiceButton(c[0], c[1]));
            }
            choicesPanel.revalidate();
            choicesPanel.repaint();
        });
    }

    void updateHealth() {
        int hp = Math.max(0, engine.player.health);
        StringBuilder pips = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            pips.append(i < hp ? "◆" : "◇");
        }
        healthLabel.setText(pips + "  " + hp + "/10  |  " + engine.player.name);
        healthLabel.setForeground(hp > 6
                ? new Color(80, 180, 80) : hp > 3
                        ? new Color(210, 170, 50)
                        : new Color(200, 60, 50));
    }

    public void showPanel(JPanel panel) {
        SwingUtilities.invokeLater(() -> {
            choicesPanel.removeAll();
            choicesPanel.add(panel);
            choicesPanel.revalidate();
            choicesPanel.repaint();
        });
    }

    private JButton makeChoiceButton(String label, String key) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Monospaced", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 0, 0, 160));
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(255, 255, 255, 40));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(0, 0, 0, 160));
            }
        });
        btn.addActionListener(e -> engine.handleChoice(key));
        return btn;
    }

    public void showTitleScreen(GameEngine engine) {
        //applyWindowFade();
        setBackground("src/images/outside1.jpg");
        dialogueOverlay.setVisible(false); // hide the dialogue box entirely

        JButton startBtn = new JButton("▶  Start Game");
        // style it, position it centered on screen
        startBtn.addActionListener(e -> {
            dialogueOverlay.setVisible(true); // bring it back
            engine.setScene(new HallwayScene());
        });

        layeredPane.add(startBtn, JLayeredPane.MODAL_LAYER);
        // position it centered
        startBtn.setBounds(300, 400, 200, 50);
    }
    
    private void applyWindowFade(){
        this.setOpacity(0.0f);
        Timer timer = new Timer(30, new ActionListener(){
            float opacity = 0.0f;
            @Override
            public void actionPerformed(ActionEvent e){
                opacity += 0.02f;
                if(opacity >= 1.0f){
                    setOpacity(1.0f);
                    ((Timer)e.getSource()).stop();
                } else{
                    setOpacity(opacity);
                }
            }
        });
        timer.start();
    }
}
