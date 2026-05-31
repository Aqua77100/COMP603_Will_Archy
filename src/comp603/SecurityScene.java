package comp603;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

class SecurityScene extends Scene {

    private boolean wireSolved = false;
    private boolean revealDone = false;

    @Override
    public void buildUI(GameEngine engine) {
        engine.window.setBackground("src/images/security1.jpg");

        loadTextQueue(
                engine.dm.getDialogue("security_intro"),
                engine.dm.getDialogue("security_intro2"),
                engine.dm.getDialogue("security_intro3"),
                engine.dm.getDialogue("security_intro4"),
                engine.dm.getDialogue("security_wires")
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
                    System.out.println("Queue: " + queueIndex);
                    if(queueIndex == 3 && !wireSolved){
                        engine.window.setBackground("src/images/robotwire_closeup1.jpg");
                    } else if(queueIndex == 5 && !wireSolved){
                        engine.window.setBackground("");
                    }
                    else if(queueIndex == 2 && wireSolved && !revealDone){
                        engine.window.setBackground("src/images/robotface.jpg"); 
                        System.out.println("first image");
                    } else if(queueIndex == 3 && wireSolved && !revealDone){
                        engine.window.setBackground("src/images/robotmouth1.jpg"); 
                        System.out.println("second image");
                    } else if(queueIndex == 4 && wireSolved && !revealDone){
                        engine.window.setBackground("src/images/securityrobot_death1.jpg"); 
                        System.out.println("terminal with word image");
                    }  else if(queueIndex == 2 && revealDone){
                        engine.window.setBackground("src/images/factorydoor1.jpg"); 
                        System.out.println("arrive at door");
                    } else if(queueIndex == 3 && revealDone){
                        engine.window.setBackground("src/images/password_terminal1.jpg"); 
                        System.out.println("pasword");
                    } else if(queueIndex == 5 && revealDone){
                        engine.window.setBackground("src/images/factorydoor_open.jpg"); 
                        System.out.println("granted");
                    } 
                    
                    showContinueButton(engine);
                } else {
                    if (!wireSolved) {
                        showWirePuzzle(engine);
                    } else if (!revealDone) {
                        revealDone = true;
                        goToPasswordReveal(engine);
                    } else {
                        List<String[]> cont = new ArrayList<>();
                        cont.add(new String[]{"Continue →", "continue_to_storage"});
                        engine.window.setChoices(cont);
                    }
                }
                break;

            case "wire_solved":
                wireSolved = true;
                engine.state.securityWirePuzzleDone = true;
                engine.window.setBackground("src/images/terminal1.jpg");

                // Show the jumbled password as a reward
                String jumbled = GameMechanics.shuffleString(engine.state.correctPassword);
                loadTextQueue(
                        engine.dm.getDialogue("wires_complete"),
                        engine.dm.getDialogue("wires_complete2"),
                        engine.dm.getDialogue("security_final"),
                        engine.dm.getDialogue("security_solved") + " " + jumbled
                );
                nextLine(engine);
                showContinueButton(engine);
                break;

            case "wire_failed":
                engine.player.takeDamage(3);
                engine.window.updateHealth();
                engine.window.showText(engine.dm.getDialogue("security_fail"));
                if (!engine.player.isAlive()) {
                    engine.handleDeath();
                }
                break;

            case "continue_to_storage":
                engine.setScene(new StorageScene());
                break;
        }
    }

    private void showWirePuzzle(GameEngine engine) {
        engine.window.clearChoices();
        engine.window.setBackground("src/images/wiregame2.jpg");
        engine.window.showText(engine.player.name + ": I only have one shot to connect these correctly.");

        WirePanel wires = new WirePanel(
                () -> SwingUtilities.invokeLater(() -> {
                    engine.window.hideFullScreenPanel();
                    engine.window.showDialogue();
                    engine.handleChoice("wire_solved");
                }),
                () -> SwingUtilities.invokeLater(() -> {
                    engine.window.hideFullScreenPanel();
                    engine.window.showDialogue();
                    engine.window.showText("The robot turns around and sees me. Everything fades to black...");
                    engine.player.takeDamage(10);
                    engine.window.updateHealth();
                    if (!engine.player.isAlive()) {
                        engine.handleDeath();
                    }
                })
        );

        engine.window.showFullScreenPanel(wires);
    }

    private void goToPasswordReveal(GameEngine engine) {
        engine.window.setBackground("");
        loadTextQueue(
                engine.dm.getDialogue("hallway_factory"),
                engine.dm.getDialogue("hallway_factory3"),
                engine.dm.getDialogue("hallway_factory4")
        );
        nextLine(engine);
        showContinueButton(engine);
    }
}
