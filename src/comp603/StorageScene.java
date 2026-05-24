package comp603;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author archy
 */
// --- SCENE 2: STORAGE ---
class StorageScene extends Scene {

    private boolean succeeded = false;
    private boolean accessGranted = false;

    @Override
    public void buildUI(GameEngine engine) {
        System.out.println("StorageScene.buildUI() called");
        System.out.println("securityWirePuzzleDone: " + engine.state.securityWirePuzzleDone);
        System.out.println("laserActive: " + engine.state.laserActive);

        // Returning from security — show password prompt
        if (engine.state.securityWirePuzzleDone) {
            System.out.println("going to password prompt");
            showPasswordPrompt(engine);
            return;
        }

        // Laser already dealt with — go straight to security
        if (!engine.state.laserActive) {
            System.out.println("→ going to SecurityScene");
            engine.setScene(new SecurityScene());
            return;
        }

        System.out.println("→ showing laser puzzle");

        // First visit — show laser puzzle
        engine.window.setBackground("src/images/storageentry_lazeroff.jpg");
        loadTextQueue(
                engine.dm.getDialogue("storage_intro"),
                engine.dm.getDialogue("storage_intro2"),
                engine.dm.getDialogue("storage_intro3"),
                engine.dm.getDialogue("storage_choice")
        );
        nextLine(engine);
        showContinueButton(engine);
    }

    @Override
    public void onChoice(GameEngine engine, String key) {
        switch (key) {

            case "continue":
                if (queueIndex < textQueue.size()) {
                    nextLine(engine);
                    if (!succeeded) {
                        // Intro queue background swaps
                        switch (queueIndex) {
                            case 2:
                                engine.window.setBackground("src/images/storageentry1.jpg");
                                break;
                            case 3:
                                engine.window.setBackground("src/images/storagehide1.jpg");
                                break;
                        }
                        if(queueIndex == 2 && !accessGranted){
                            engine.window.setBackground("src/images/storageentry1.jpg");
                        } else if(queueIndex == 3 && !accessGranted){
                            engine.window.setBackground("src/images/storagehide1.jpg");
                        } else if(queueIndex == 2){
                            engine.window.setBackground("src/images/factorydoor_open.jpg");
                        } else if(queueIndex == 3){
                            engine.window.setBackground("");
                        }
                        
                    } else {
                        // Success queue background swaps
                        switch (queueIndex) {
                            case 2:
                                // keep same background, do nothing
                                System.out.println(queueIndex);
                                break;
                            case 3:
                                engine.window.setBackground(""); 
                                System.out.println(queueIndex);
                                break;
                        }
                    }
                    showContinueButton(engine);
                } else {
                    if (accessGranted) {
                        engine.setScene(new FactoryScene());
                    } else if (succeeded) {
                        engine.setScene(new SecurityScene());
                    } else {
                        List<String[]> choices = new ArrayList<>();
                        choices.add(new String[]{"A) An old shoe", "a"});
                        choices.add(new String[]{"B) An empty soda can", "b"});
                        choices.add(new String[]{"C) A broken metal pipe", "c"});
                        engine.window.setChoices(choices);
                    }
                }
                break;

            case "b": // can
                engine.window.setBackground("src/images/storageentry_can.jpg");
                succeeded = true;
                loadTextQueue(
                        "The laser follows it as the can rolls across the floor and to the wall.",
                        engine.dm.getDialogue("storage_success2"),
                        engine.dm.getDialogue("storage_success3")
                );
                nextLine(engine);
                showContinueButton(engine);
                break;

            case "c": // pipe
                engine.window.setBackground("src/images/storageentry_pipe.jpg");
                succeeded = true;
                loadTextQueue(
                        "The laser follows it as the pipe clangs across the floor and to the wall.",
                        engine.dm.getDialogue("storage_success2"),
                        engine.dm.getDialogue("storage_success3")
                );
                nextLine(engine);
                showContinueButton(engine);
                break;

            case "a":
                engine.window.showText(engine.dm.getDialogue("storage_fail"));
                engine.player.takeDamage(10);
                engine.window.updateHealth();
                if (!engine.player.isAlive()) {
                    engine.handleDeath();
                }
                break;
        }
    }

    private void showPasswordPrompt(GameEngine engine) {
        engine.window.showText(engine.dm.getDialogue("factory_prompt"));
        engine.window.setInputActive(true);

        JPanel passPanel = new JPanel(new BorderLayout(8, 0));
        passPanel.setOpaque(false);

        JTextField passField = new JTextField();
        passField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        passField.setBackground(new Color(30, 30, 30));
        passField.setForeground(Color.WHITE);
        passField.setCaretColor(Color.WHITE);
        passField.setPreferredSize(new Dimension(300, 40));
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        JButton submitBtn = new JButton("Submit");
        submitBtn.setFont(new Font("Monospaced", Font.PLAIN, 13));
        submitBtn.setBackground(new Color(40, 40, 40));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setPreferredSize(new Dimension(100, 40));
        submitBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));

        Runnable submit = () -> {
            engine.window.setInputActive(false);
            handlePasswordAttempt(engine, passField);
        };

        submitBtn.addActionListener(e -> submit.run());
        passField.addActionListener(e -> submit.run());

        passPanel.add(passField, BorderLayout.CENTER);
        passPanel.add(submitBtn, BorderLayout.EAST);

        engine.window.showPanel(passPanel);
        SwingUtilities.invokeLater(() -> passField.requestFocusInWindow());
    }

    private void handlePasswordAttempt(GameEngine engine, JTextField passField) {
        String attempt = passField.getText().trim();
        if (attempt.equalsIgnoreCase(engine.state.correctPassword)) {
            accessGranted = true;
            engine.window.showPanel(new JPanel());
            loadTextQueue(
                    "Access Granted.",
                    engine.player.name + ": \"Hopefully the power grid in here is still intact.\"",
                    "I enter the factory..."
            );
            nextLine(engine);
            showContinueButton(engine); // "continue" key → queue plays out → FactoryScene
        } else {
            engine.state.passwordAttempts++;
            String hint = "";
            if (engine.state.passwordAttempts == 3) {
                hint = "\n\n" + engine.dm.getDialogue("factory_hint");
            } else if (engine.state.passwordAttempts > 3) {
                hint = "\n\n" + engine.dm.getDialogue("factory_hint2");
            }
            engine.window.showText("ACCESS DENIED." + hint);
            passField.setText("");
            SwingUtilities.invokeLater(() -> passField.requestFocusInWindow());
        }
    }
}
