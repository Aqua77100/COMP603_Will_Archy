/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author archy
 */
// --- SCENE 3: SECURITY ---
class SecurityScene extends Scene {
    public void enter(GameEngine engine) {
        GameUI.printColored(engine.dm.getDialogue("security_intro"), GameUI.CYAN);
        GameUI.printColored(engine.dm.getDialogue("security_wires"), GameUI.YELLOW);
        GameUI.promptInput("Connect Red to... (Type 'Blue')");
        
        String jumbled = GameMechanics.shuffleString(engine.state.correctPassword);
        GameUI.printColored(engine.dm.getDialogue("security_solved") + jumbled, GameUI.GREEN);
        engine.state.securityWirePuzzleDone = true;
        
        GameUI.printColored("Returning to Storage...", GameUI.YELLOW);
        engine.setScene(new StorageScene());
    }
}
