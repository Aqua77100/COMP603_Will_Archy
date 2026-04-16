/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;


/**
 *
 * @author archy
 */
// --- SCENE 4: FACTORY ---
class FactoryScene extends Scene {
    public void enter(GameEngine engine) {
        System.out.println(engine.player.name + ": " + engine.dm.getDialogue("factory_enter"));
        GameUI.printColored(engine.dm.getDialogue("factory_boss1"), GameUI.RED);
        System.out.println(engine.player.name + ": " + engine.dm.getDialogue("factory_d1"));
        GameUI.printColored(engine.dm.getDialogue("factory_boss2"), GameUI.RED);
        
        GameUI.pressEnterToContinue();
        GameUI.clearScreen();
        GameUI.printColored(engine.dm.getDialogue("factory_boss3"), GameUI.RED);
        boolean victory = GameMechanics.playHangman(engine.player, "SHAME");
        
if (victory) {
            boolean finalChoiceMade = false;

            while (!finalChoiceMade) {
                System.out.println("\nSync sparks weakly on the floor. What is your choice?");
                String choice = GameUI.promptInput("1) Save Sync  2) Abandon Sync").toLowerCase();

                if (choice.equals("1") || choice.contains("save")) { 
                    GameUI.clearScreen();
                    System.out.println(engine.dm.getDialogue("win_save"));
                    System.out.println(engine.player.name + ": " + engine.dm.getDialogue("win_save_d1"));
                    GameUI.printColored(engine.dm.getDialogue("win_save_d2"), GameUI.RED);
                    GameUI.printColored(engine.dm.getDialogue("the_end"), GameUI.GREEN);
                    finalChoiceMade = true;

                } else if (choice.equals("2") || choice.contains("abandon")) {
                    GameUI.clearScreen();
                    System.out.println(engine.dm.getDialogue("win_abandon"));
                    GameUI.printColored(engine.dm.getDialogue("win_abandon_d1"), GameUI.RED);
                    System.out.println(engine.player.name + ": " + engine.dm.getDialogue("win_abandon_d2"));
                    GameUI.printColored(engine.dm.getDialogue("the_end"), GameUI.RED);
                    finalChoiceMade = true;

                } else {
                    GameUI.printColored("Invalid input. You must decide Sync's fate: 1 or 2.", GameUI.RED);
                }
            }
            
            // Wait for user to read the final dialogue before closing
            GameUI.pressEnterToContinue();
            System.exit(0);
        } else {
            // If victory is false, the player has died. 
            // The GameEngine loop will handle the "Death/Restart" prompt automatically.
            return; 
        }
    }
}