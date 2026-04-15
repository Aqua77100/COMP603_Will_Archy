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
        GameUI.printColored(engine.dm.getDialogue("storage_intro"), GameUI.CYAN);
        
        if (engine.state.laserActive) {
            GameUI.promptInput(engine.dm.getDialogue("storage_choice"));
            GameUI.printColored(engine.dm.getDialogue("storage_success"), GameUI.GREEN);
            engine.state.laserActive = false;
        }

        if (!engine.state.securityWirePuzzleDone) {
            engine.setScene(new SecurityScene());
        } else {
            String pass = GameUI.promptInput(engine.dm.getDialogue("factory_prompt"));
            if (pass.equalsIgnoreCase(engine.state.correctPassword)) {
                engine.setScene(new FactoryScene());
            } else {
                engine.state.passwordAttempts++;
                if (engine.state.passwordAttempts >= 3) 
                    GameUI.printColored(engine.dm.getDialogue("factory_hint"), GameUI.YELLOW);
                this.enter(engine);
            }
        }
    }
}
