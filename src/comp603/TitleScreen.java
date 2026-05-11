/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author willpurdon
 */
class TitleScreen extends Scene {
    @Override
    public void buildUI(GameEngine engine) {
        // 1. Swap background image for this scene
        engine.window.setBackground("src/comp603/images/outside1.jpg");

        // 2. Show opening dialogue
        engine.window.showText(
            engine.dm.getDialogue("hallway_intro") + "\n\n"
            + engine.player.name + ": " + engine.dm.getDialogue("hallway_intro_d1")
            + "\n\n" + engine.dm.getDialogue("hallway_choice")
        );

        // 3. Show the three choices as buttons
        List<String[]> choices = new ArrayList<>();
        choices.add(new String[]{"A) Crawl under the laser", "a"});
        choices.add(new String[]{"B) Sprint through the gap", "b"});
        choices.add(new String[]{"C) Walk straight through", "c"});
        engine.window.setChoices(choices);
    }

     @Override
    public void onChoice(GameEngine engine, String key) {
        switch (key) {
            case "a":
            case "b":
                // Success — show reaction text, then a Continue button
                engine.window.showText(
                    "You made it through!\n\n"
                    + engine.player.name + ": \"Gotta hide.\""
                );
                List<String[]> next = new ArrayList<>();
                next.add(new String[]{"Continue →", "next"});
                engine.window.setChoices(next);
                break;

            case "c":
                // Fail — take damage, then handle death or continue
                engine.window.showText(engine.dm.getDialogue("hallway_fail"));
                engine.player.takeDamage(10);
                engine.window.updateHealth();
                if (!engine.player.isAlive()) {
                    engine.handleDeath();
                }
                break;

            case "next":
                // Move to the next scene
                engine.setScene(new StorageScene());
                break;
        }
    }
}
