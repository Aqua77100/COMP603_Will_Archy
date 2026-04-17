package comp603;

/**
 *
 * @author archy
 */
// --- SCENE 3: SECURITY ---
class SecurityScene extends Scene {

    public void enter(GameEngine engine) {
        System.out.println(engine.dm.getDialogue("security_intro"));

        GameUI.pressEnterToContinue();
        GameUI.clearScreen();

        System.out.println(engine.dm.getDialogue("security_wires"));

        // ---- WIRE PUZZLE
        boolean wiresConnected = false;
        while (!wiresConnected) {
            String input = GameUI.promptInput(engine.dm.getDialogue("security_choice")).toLowerCase();
            if (input.equals("blue")) {
                wiresConnected = true;

                GameUI.clearScreen();

                GameUI.printColored("The terminal beeps. Connection established!", GameUI.GREEN);

            } else if (input.equals("yellow") || input.equals("green")) {
                GameUI.clearScreen();
                GameUI.printColored(engine.dm.getDialogue("security_fail"), GameUI.RED);
                engine.player.takeDamage(10); // Will prompt retry
                wiresConnected = true;

            } else {
                // Reprompt, remaining in loop until correct answer
                GameUI.printColored("Incorrect Input. " + engine.dm.getDialogue("choice_c"), GameUI.RED);
                wiresConnected = false;
            }

        }

        if (!engine.player.isAlive()) {
            return;
        }

        // Shuffle the password from GameState to create an anagram 
        String jumbled = GameMechanics.shuffleString(engine.state.correctPassword);

        GameUI.printColored(engine.dm.getDialogue("security_final"), GameUI.RED);
        System.out.println(engine.dm.getDialogue("security_solved"));
        GameUI.printColored(jumbled, GameUI.RED);
        engine.state.securityWirePuzzleDone = true;

        GameUI.pressEnterToContinue();
        GameUI.clearScreen();

        GameUI.printColored("Exiting to Hallway through Storage Room...", GameUI.YELLOW);

        GameUI.pressEnterToContinue();
        GameUI.clearScreen();

        System.out.println(engine.dm.getDialogue("hallway_factory"));
        System.out.println(engine.player.name + ": \"This must be what the password was for.\"\n");

        // Exit scene
        engine.setScene(new StorageScene());
    }
}
