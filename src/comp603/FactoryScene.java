package comp603;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author archy
 */
// --- SCENE 4: FACTORY ---
class FactoryScene extends Scene {
    
    @Override
    public void buildUI(GameEngine engine) {
        // 1. Swap background image for this scene
        engine.window.setBackground("src/comp603/images/hallway.jpg");

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

//    public void enter(GameEngine engine) { // Enter factory scene
//        System.out.println(engine.player.name + ": " + engine.dm.getDialogue("factory_enter"));
//        GameUI.printColored(engine.dm.getDialogue("factory_boss1"), GameUI.RED);
//        System.out.println(engine.player.name + ": " + engine.dm.getDialogue("factory_d1"));
//        GameUI.printColored(engine.dm.getDialogue("factory_boss2"), GameUI.RED);
//
//        GameUI.pressEnterToContinue(); // Pause for immersive feeling
//        GameUI.clearScreen(); // Print some lines to differenciate scenes
//
//        GameUI.printColored(engine.dm.getDialogue("factory_boss3"), GameUI.RED);
//
//        // Initiate hangman game with the specified word, game is played out as written in GameMechanics
//        boolean victory = GameMechanics.playHangman(engine.player, "SHAME");
//
//        // If the player defeats the final boss, star this loop
//        if (victory) {
//            boolean finalChoiceMade = false;
//
//            while (!finalChoiceMade) {
//                System.out.println("\n---\nSync sparks weakly on the floor. What is your choice?");
//                String choice = GameUI.promptInput("1) Save Sync 2) Abandon Sync").toLowerCase();
//
//                if (choice.equals("1")) {
//                    GameUI.clearScreen();
//                    System.out.println(engine.dm.getDialogue("win_save"));
//                    System.out.println(engine.player.name + ": " + engine.dm.getDialogue("win_save_d1"));
//                    GameUI.printColored(engine.dm.getDialogue("win_save_d2"), GameUI.RED);
//                    System.out.println(engine.dm.getDialogue("the_end"));
//                    finalChoiceMade = true; // Exit loop
//
//                } else if (choice.equals("2")) {
//                    GameUI.clearScreen();
//                    System.out.println(engine.dm.getDialogue("win_abandon"));
//                    GameUI.printColored(engine.dm.getDialogue("win_abandon_d1"), GameUI.RED);
//                    System.out.println(engine.player.name + ": " + engine.dm.getDialogue("win_abandon_d2"));
//                    System.out.println(engine.dm.getDialogue("the_end"));
//                    finalChoiceMade = true; // Exit loop
//                } else {
//                    // If player puts any other input, restart the prompt again
//                    GameUI.printColored("\nInvalid input. You must decide Sync's fate: 1 or 2.", GameUI.RED);
//                }
//            }
//
//            // Wait for user to read the final dialogue before closing
//            GameUI.pressEnterToContinue();
//            System.out.println("Thank you for playing, " + engine.player.name);
//            System.exit(0); // Exit safely
//        } else {
//            // If victory is false, the player has died. 
//            // The GameEngine loop will handle the "Death/Restart" prompt automatically.
//            return;
//        }
//    }
}
