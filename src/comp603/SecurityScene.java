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
                if (nextLine(engine)) {
                    showContinueButton(engine);
                } else {
                    if (!wireSolved) {
                        // Wire puzzle already solved, move on
                        showWirePuzzle(engine);
                    } else if(!revealDone) {
                        revealDone = true;
                        goToPasswordReveal(engine);
                    }
                }
                break;

            case "wire_solved":
                wireSolved = true;
                engine.state.securityWirePuzzleDone = true;
                engine.window.setBackground("src/images/robotface.jpg");

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
        engine.window.setBackground("src/images/wiregame1.jpg");
        engine.window.showText("Three wires hang from the terminal. Connect them correctly.");

        WirePanel wires = new WirePanel(
                () -> SwingUtilities.invokeLater(() -> {
                    engine.window.hideFullScreenPanel();
                    engine.window.showDialogue();
                    engine.handleChoice("wire_solved");
                }),
                () -> SwingUtilities.invokeLater(() -> {
                    engine.window.hideFullScreenPanel();
                    engine.window.showDialogue();
                    engine.window.showText("The alarm triggers. Security floods the room...");
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
        loadTextQueue(
                engine.dm.getDialogue("hallway_factory"),
                engine.dm.getDialogue("hallway_factory2"),
                engine.dm.getDialogue("hallway_factory3"),
                engine.dm.getDialogue("hallway_factory4")
        );
        nextLine(engine);

        // Override continue to go to StorageScene when this queue finishes
        List<String[]> cont = new ArrayList<>();
        cont.add(new String[]{"Continue →", "continue_to_storage"});
        engine.window.setChoices(cont);
    }
}
