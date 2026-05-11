package comp603;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author archy
 */
// --- SCENE 2: STORAGE ---
class StorageScene extends Scene {
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

//    public void enter(GameEngine engine) {
//
//        // LASER ESCAPE
//        // --> only runs if the laser is still active
//        if (engine.state.laserActive) {
//            GameUI.clearScreen();
//            System.out.println(engine.dm.getDialogue("storage_intro"));
//            System.out.println(engine.dm.getDialogue("storage_choice"));
//
//            boolean validItem = false;
//            while (!validItem) {
//                String choice = GameUI.promptInput(engine.dm.getDialogue("choice_a")).toLowerCase();
//
//                if (choice.equals("a")) {
//                    GameUI.clearScreen();
//                    GameUI.printColored(engine.dm.getDialogue("storage_fail"), GameUI.RED);
//                    engine.player.takeDamage(10);
//                    validItem = true;
//                    return;
//                } else if (choice.equals("b") || choice.equals("c")) {
//                    GameUI.clearScreen();
//
//                    GameUI.printColored(engine.dm.getDialogue("storage_success"), GameUI.GREEN);
//                    System.out.println(engine.dm.getDialogue("storage_success2"));
//
//                    GameUI.pressEnterToContinue();
//                    GameUI.clearScreen();
//
//                    GameUI.printColored("Opening door...", GameUI.YELLOW);
//
//                    GameUI.pressEnterToContinue();
//                    GameUI.clearScreen();
//
//                    // Flag set to false so this block is skipped next time
//                    engine.state.laserActive = false;
//                    validItem = true;
//                } else {
//                    GameUI.printColored("Invalid choice. You must throw an item (a, b, or c)", GameUI.RED);
//                }
//            }
//        }
//
//        // Player goes to HALLWAY via STORAGE ROOM
//        if (!engine.state.securityWirePuzzleDone) {
//            // First time here: move to Security
//            engine.setScene(new SecurityScene());
//        } else {
//            // Returning from Security: go straight to the password prompt
//            // Notice we DON'T print the storage intro here anymore
//            boolean authenticated = false;
//            while (!authenticated) {
//                String pass = GameUI.promptInput(engine.dm.getDialogue("factory_prompt"));
//
//                if (pass.equalsIgnoreCase(engine.state.correctPassword)) {
//
//                    GameUI.clearScreen();
//                    GameUI.printColored("Access Granted.", GameUI.GREEN);
//                    System.out.println(engine.player.name + ": \"Hopefully the power grid in here is still intact.\"");
//
//                    GameUI.pressEnterToContinue();
//                    GameUI.clearScreen();
//
//                    GameUI.printColored("Entering Factory...", GameUI.YELLOW);
//
//                    GameUI.pressEnterToContinue();
//                    GameUI.clearScreen();
//                    engine.setScene(new FactoryScene());
//                    authenticated = true;
//                } else {
//                    engine.state.passwordAttempts++;
//                    GameUI.printColored("ACCESS DENIED.", GameUI.RED);
//                    if (engine.state.passwordAttempts == 3) {
//                        // Player finds hint
//                        System.out.println(engine.dm.getDialogue("factory_hint"));
//                    } else if (engine.state.passwordAttempts > 3) {
//                        // Shortened hint
//                        GameUI.printColored(engine.dm.getDialogue("factory_hint2"), GameUI.YELLOW);
//                    }
//                    // Loop continues until they get it right
//                }
//            }
//        }
//    }
}
