/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 * @author willpurdon
 */
public class IntroScene extends Scene {
    @Override
    public void buildUI(GameEngine engine) {
        engine.window.setBackground("src/images/outside1.jpg");
        loadTextQueue(
            engine.dm.getDialogue("intro1"),
            engine.dm.getDialogue("intro2"),
            engine.dm.getDialogue("intro3"),
            engine.dm.getDialogue("intro4"),
            engine.dm.getDialogue("intro5")
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
                    
                    switch(queueIndex){
                        case 3:
                            engine.window.setBackground("src/images/sighting1.jpg");
                            break;
                        case 4:
                            engine.window.setBackground("src/images/toolbox2.jpg");
                            break;
                        case 5:
                            engine.window.setBackground("src/images/buildingdoor.jpg");
                            break;
                    }
                    
                    showContinueButton(engine);
                } else {
                    // Queue exhausted — show name input
                    showNameInput(engine);
                }
                break;

            case "submit_name":
                break;

            case "next_scene":
                engine.setScene(new HallwayScene());
                break;
        }
    }

    private void showNameInput(GameEngine engine) {
        engine.window.showText(engine.dm.getDialogue("username"));
        engine.window.setInputActive(true);

        JPanel namePanel = new JPanel(new BorderLayout(8, 0));
        namePanel.setOpaque(false);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Monospaced", Font.PLAIN, 14));
        nameField.setBackground(new Color(30, 30, 30));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setPreferredSize(new Dimension(300, 40));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        JButton submitBtn = new JButton("Confirm");
        submitBtn.setFont(new Font("Monospaced", Font.PLAIN, 13));
        submitBtn.setBackground(new Color(40, 40, 40));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setPreferredSize(new Dimension(110, 40));
        submitBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));

        Runnable submit = () -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                engine.window.showText("You must enter a name to proceed.");
                return;
            }
            engine.window.setBackground("");
            engine.player.name = name;
            engine.registerPlayer(name);
            engine.window.setInputActive(false);
            engine.window.showPanel(new JPanel()); // clear input
            engine.window.showText("Welcome, " + engine.player.name + ".");
            engine.window.updateHealth();

            // Short delay then move to hallway
            List<String[]> cont = new ArrayList<>();
            cont.add(new String[]{"Continue →", "next_scene"});
            engine.window.setChoices(cont);
        };

        submitBtn.addActionListener(e -> submit.run());
        nameField.addActionListener(e -> submit.run()); // enter key

        namePanel.add(nameField, BorderLayout.CENTER);
        namePanel.add(submitBtn, BorderLayout.EAST);

        engine.window.showPanel(namePanel);
        SwingUtilities.invokeLater(() -> nameField.requestFocusInWindow());
    }
}
