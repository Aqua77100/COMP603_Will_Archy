package comp603;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author archy
 */
// --- SCENE 4: FACTORY ---
class FactoryScene extends Scene {

    private boolean choseSave = false;
    private boolean endingStarted = false;

    @Override
    public void buildUI(GameEngine engine) {
        // 1. Swap background image for this scene
        engine.window.setBackground("src/comp603/images/factorydoor1.jpg");

        // 2. Show opening dialogue
        loadTextQueue(
                engine.player.name + ": \"What the-\"",
                engine.dm.getDialogue("factory_enter"),
                engine.dm.getDialogue("factory_enter2"),
                engine.dm.getDialogue("factory_boss1"),
                engine.player.name + ": \"What? L-Look, I just need to shut this system down!\"",
                engine.dm.getDialogue("factory_d1"),
                engine.dm.getDialogue("factory_boss2"),
                engine.dm.getDialogue("factory_boss3")
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
                    System.out.println("queue exhausted → " + (endingStarted ? "credits" : "hangman"));
                    if (endingStarted) {
                        showCredits(engine);
                    } else {
                        showHangman(engine);
                    }
                }
                break;

            case "hangman_won":
                engine.window.hideFullScreenPanel(); 
                engine.window.hideDialogue();        
                SwingUtilities.invokeLater(() -> {
                    engine.window.showDialogue();   
                    engine.window.showText("Sync sparks weakly on the floor. What is your choice?");
                    List<String[]> endings = new ArrayList<>();
                    endings.add(new String[]{"1) Save Sync", "save"});
                    endings.add(new String[]{"2) Abandon Sync", "abandon"});
                    engine.window.setChoices(endings);
                });
                break;

            case "hangman_lost":
                engine.window.hideFullScreenPanel();
                engine.window.showDialogue();
                engine.handleDeath();
                break;

            case "save":               
                choseSave = true;
                loadTextQueue(
                        engine.dm.getDialogue("win_save"),
                        engine.player.name + ": " + engine.dm.getDialogue("win_save_d1"),
                        engine.dm.getDialogue("win_save_d2"),
                        engine.dm.getDialogue("win_save_d3")
                );
                endingStarted = true;
                System.out.println("save chosen | queue size: " + textQueue.size() 
        + " | endingStarted: " + endingStarted);
                nextLine(engine);
                showContinueButton(engine);
                break;

            case "abandon":
                choseSave = false;
                loadTextQueue(
                        engine.dm.getDialogue("win_abandon"),
                        engine.dm.getDialogue("win_abandon_d1"),
                        engine.player.name + ": " + engine.dm.getDialogue("win_abandon_d2")
                );
                endingStarted = true;
                nextLine(engine);
                showContinueButton(engine);
                break;
        }

    }

    private void showHangman(GameEngine engine) {
        engine.window.clearChoices();
        HangmanPanel hangman = new HangmanPanel(
                "SHAME",
                engine,
                () -> SwingUtilities.invokeLater(() -> engine.handleChoice("hangman_won")),
                () -> SwingUtilities.invokeLater(() -> engine.handleChoice("hangman_lost"))
        );

        engine.window.showFullScreenPanel(hangman);
    }

    private void showCredits(GameEngine engine) {
        engine.window.setBackground("src/images/credits.jpg");
        engine.window.showText(
                engine.dm.getDialogue("the_end") + "\n\n"
                + "Thank you for playing, " + engine.player.name + "."
        );
        engine.window.setChoices(new ArrayList<>());
    }
}
