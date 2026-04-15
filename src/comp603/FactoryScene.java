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
        GameUI.printColored(engine.dm.getDialogue("factory_boss"), GameUI.RED);
        boolean victory = GameMechanics.playHangman(engine.player, "ROBOT");
        
        if (victory) {
            String choice = GameUI.promptInput("1) Save Robot  2) Abandon Robot");
            if (choice.equals("1")) GameUI.printColored(engine.dm.getDialogue("win_save"), GameUI.GREEN);
            else GameUI.printColored(engine.dm.getDialogue("win_abandon"), GameUI.YELLOW);
            System.exit(0);
        }
    }
}
