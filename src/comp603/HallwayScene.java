package comp603;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author archy
 */
class HallwayScene extends Scene {
    @Override
    public void buildUI(GameEngine engine) {
        engine.window.setBackground("hallway.jpg");

        // Load all intro lines into the queue
        loadTextQueue(
            engine.dm.getDialogue("hallway_intro"),
            engine.player.name + ": " + engine.dm.getDialogue("hallway_intro_d1"),
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
                    showContinueButton(engine);
                } else {
                    // Queue exhausted — show the actual choices
                    List<String[]> choices = new ArrayList<>();
                    choices.add(new String[]{"A) Crawl under the laser", "a"});
                    choices.add(new String[]{"B) Sprint through the gap", "b"});
                    choices.add(new String[]{"C) Walk straight through", "c"});
                    engine.window.setChoices(choices);
                }
                break;

            case "a":
            case "b":
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
