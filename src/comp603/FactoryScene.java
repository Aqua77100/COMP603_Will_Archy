package comp603;

/**
 *
 * @author archy
 */
// --- SCENE 4: FACTORY ---
class FactoryScene extends Scene {

    public void enter(GameEngine engine) { // Enter factory scene
        System.out.println(engine.player.name + ": " + engine.dm.getDialogue("factory_enter"));
        GameUI.printColored(engine.dm.getDialogue("factory_boss1"), GameUI.RED);
        System.out.println(engine.player.name + ": " + engine.dm.getDialogue("factory_d1"));
        GameUI.printColored(engine.dm.getDialogue("factory_boss2"), GameUI.RED);

        GameUI.pressEnterToContinue(); // Pause for immersive feeling
        GameUI.clearScreen(); // Print some lines to differenciate scenes

        GameUI.printColored(engine.dm.getDialogue("factory_boss3"), GameUI.RED);

        // Initiate hangman game with the specified word, game is played out as written in GameMechanics
        boolean victory = GameMechanics.playHangman(engine.player, "SHAME");

        // If the player defeats the final boss, star this loop
        if (victory) {
            boolean finalChoiceMade = false;

            while (!finalChoiceMade) {
                System.out.println("\n---\nSync sparks weakly on the floor. What is your choice?");
                String choice = GameUI.promptInput("1) Save Sync 2) Abandon Sync").toLowerCase();

                if (choice.equals("1")) {
                    GameUI.clearScreen();
                    System.out.println(engine.dm.getDialogue("win_save"));
                    System.out.println(engine.player.name + ": " + engine.dm.getDialogue("win_save_d1"));
                    GameUI.printColored(engine.dm.getDialogue("win_save_d2"), GameUI.RED);
                    System.out.println(engine.dm.getDialogue("the_end"));
                    finalChoiceMade = true; // Exit loop

                } else if (choice.equals("2")) {
                    GameUI.clearScreen();
                    System.out.println(engine.dm.getDialogue("win_abandon"));
                    GameUI.printColored(engine.dm.getDialogue("win_abandon_d1"), GameUI.RED);
                    System.out.println(engine.player.name + ": " + engine.dm.getDialogue("win_abandon_d2"));
                    System.out.println(engine.dm.getDialogue("the_end"));
                    finalChoiceMade = true; // Exit loop
                } else {
                    // If player puts any other input, restart the prompt again
                    GameUI.printColored("\nInvalid input. You must decide Sync's fate: 1 or 2.", GameUI.RED);
                }
            }

            // Wait for user to read the final dialogue before closing
            GameUI.pressEnterToContinue();
            System.out.println("Thank you for playing, " + engine.player.name);
            System.exit(0); // Exit safely
        } else {
            // If victory is false, the player has died. 
            // The GameEngine loop will handle the "Death/Restart" prompt automatically.
            return;
        }
    }
}
