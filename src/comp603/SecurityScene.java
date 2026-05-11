package comp603;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author archy
 */
// --- SCENE 3: SECURITY ---
class SecurityScene extends Scene {
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
//        System.out.println(engine.dm.getDialogue("security_intro"));
//
//        GameUI.pressEnterToContinue();
//        GameUI.clearScreen();
//
//        System.out.println(engine.dm.getDialogue("security_wires"));
//
//        // ---- WIRE PUZZLE
//        boolean wiresConnected = false;
//        while (!wiresConnected) {
//            String input = GameUI.promptInput(engine.dm.getDialogue("security_choice")).toLowerCase();
//            if (input.equals("blue")) {
//                wiresConnected = true;
//
//                GameUI.clearScreen();
//
//                GameUI.printColored("The terminal beeps. Connection established!", GameUI.GREEN);
//
//            } else if (input.equals("yellow") || input.equals("green")) {
//                GameUI.clearScreen();
//                GameUI.printColored(engine.dm.getDialogue("security_fail"), GameUI.RED);
//                engine.player.takeDamage(10); // Will prompt retry
//                wiresConnected = true;
//
//            } else {
//                // Reprompt, remaining in loop until correct answer
//                GameUI.printColored("Incorrect Input. " + engine.dm.getDialogue("choice_c"), GameUI.RED);
//                wiresConnected = false;
//            }
//
//        }
//
//        if (!engine.player.isAlive()) {
//            return;
//        }
//
//        // Shuffle the password from GameState to create an anagram 
//        String jumbled = GameMechanics.shuffleString(engine.state.correctPassword);
//
//        GameUI.printColored(engine.dm.getDialogue("security_final"), GameUI.RED);
//        System.out.println(engine.dm.getDialogue("security_solved"));
//        GameUI.printColored(jumbled, GameUI.RED);
//        engine.state.securityWirePuzzleDone = true;
//
//        GameUI.pressEnterToContinue();
//        GameUI.clearScreen();
//
//        GameUI.printColored("Exiting to Hallway through Storage Room...", GameUI.YELLOW);
//
//        GameUI.pressEnterToContinue();
//        GameUI.clearScreen();
//
//        System.out.println(engine.dm.getDialogue("hallway_factory"));
//        System.out.println(engine.player.name + ": \"This must be what the password was for.\"\n");
//
//        // Exit scene
//        engine.setScene(new StorageScene());
//    }
}
