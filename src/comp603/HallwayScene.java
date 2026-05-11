package comp603;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author archy
 */
class HallwayScene extends Scene {

    private boolean madeIt = false;
    
    @Override
    public void buildUI(GameEngine engine) {
        engine.window.setBackground("src/images/hallwayScene.jpg");

        // Load all intro lines into the queue
        loadTextQueue(
                engine.dm.getDialogue("hallway_intro"),
                engine.player.name + ": " + engine.dm.getDialogue("hallway_intro_d1"),
                engine.dm.getDialogue("hallway_intro_d2"),
                engine.dm.getDialogue("hallway_choice")
        );

        // Show first line + continue button
        nextLine(engine);
        showContinueButton(engine);
    }

    @Override
    public void onChoice(GameEngine engine, String key) {
        switch (key) {

            case "continue":
                // If there are more lines, show next and keep continue button
                if (nextLine(engine)) {
                    if (queueIndex == 3) {
                        engine.window.setBackground("src/images/hallwaylazer1.jpg");
                    }
                    showContinueButton(engine);
                } else {
                    if (madeIt) {
                        engine.setScene(new StorageScene());
                    } else {
                        List<String[]> choices = new ArrayList<>();
                        choices.add(new String[]{"A) Crawl under the laser", "a"});
                        choices.add(new String[]{"B) Sprint through the gap", "b"});
                        choices.add(new String[]{"C) Walk straight through", "c"});
                        engine.window.setChoices(choices);
                    }
                }
                break;

            case "a":
            case "b":
                madeIt = true;
                loadTextQueue(
                        "You made it through!",
                        engine.player.name + ": \"Gotta hide.\""
                );
                nextLine(engine);
                showContinueButton(engine);
                break;

            case "c":
                engine.window.showText(engine.dm.getDialogue("hallway_fail"));
                engine.player.takeDamage(10);
                engine.window.updateHealth();
                if (!engine.player.isAlive()) {
                    engine.handleDeath();
                }
                break;

            case "next":
                // If success queue still has lines, keep advancing
                if (nextLine(engine)) {
                    showContinueButton(engine);
                } else {
                    engine.setScene(new StorageScene());
                }
                break;
        }
    }
}
