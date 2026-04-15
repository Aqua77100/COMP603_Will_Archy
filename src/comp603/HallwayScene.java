/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author archy
 */
class HallwayScene extends Scene {
    public void enter(GameEngine engine) {
        GameUI.printColored(engine.dm.getDialogue("hallway_intro"), GameUI.CYAN);
        String choice = GameUI.promptInput(engine.dm.getDialogue("hallway_choice")).toLowerCase();
        
        if (choice.equals("c")) {
            GameUI.printColored(engine.dm.getDialogue("hallway_fail"), GameUI.RED);
            engine.player.takeDamage(10);
        } else {
            GameUI.printColored("You made it through!", GameUI.GREEN);
            engine.setScene(new StorageScene());
        }
    }
}
