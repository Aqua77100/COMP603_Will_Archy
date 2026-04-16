/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author archy
 */
// --- SCENE 2: STORAGE ---
class StorageScene extends Scene {

    public void enter(GameEngine engine) {

        // 1. LASER PUZZLE SECTION
        // This only runs if the laser is still active.
        if (engine.state.laserActive) {
            GameUI.clearScreen();
            System.out.println(engine.dm.getDialogue("storage_intro"));
            System.out.println(engine.dm.getDialogue("storage_choice"));

            boolean validItem = false;
            while (!validItem) {
                String choice = GameUI.promptInput(engine.dm.getDialogue("choice_a")).toLowerCase();

                if (choice.equals("a")) {
                    GameUI.printColored(engine.dm.getDialogue("storage_fail"), GameUI.RED);
                    engine.player.takeDamage(10);
                    validItem = true;
                    return;
                } else if (choice.equals("b") || choice.equals("c")) {
                    GameUI.clearScreen();
                    GameUI.printColored(engine.dm.getDialogue("storage_success"), GameUI.GREEN);
                    engine.state.laserActive = false; // Flag set to false so this block is skipped next time
                    validItem = true;
                } else {
                    GameUI.printColored("Invalid choice. You must throw an item (a, b, or c)!", GameUI.RED);
                }
            }
        }

        // Now handle where the player goes NEXT
        if (!engine.state.securityWirePuzzleDone) {
            // First time here: move to Security
            engine.setScene(new SecurityScene());
        } else {
            // Returning from Security: go straight to the password prompt
            // Notice we DON'T print the storage intro here anymore
            boolean authenticated = false;
            while (!authenticated) {
                String pass = GameUI.promptInput(engine.dm.getDialogue("factory_prompt"));

                if (pass.equalsIgnoreCase(engine.state.correctPassword)) {
                    GameUI.printColored("Access Granted. Entering Factory...", GameUI.GREEN);
                    GameUI.pressEnterToContinue();
                    GameUI.clearScreen();
                    engine.setScene(new FactoryScene());
                    authenticated = true;
                } else {
                    engine.state.passwordAttempts++;
                    GameUI.printColored("ACCESS DENIED.", GameUI.RED);
                    if (engine.state.passwordAttempts == 3) {
                        System.out.println(engine.dm.getDialogue("factory_hint"));
                    } else if(engine.state.passwordAttempts > 3){
                        GameUI.printColored(engine.dm.getDialogue("factory_hint2"), GameUI.YELLOW);
                    }
                    // Loop continues until they get it right or die elsewhere
                }
            }
        }
    }
}
