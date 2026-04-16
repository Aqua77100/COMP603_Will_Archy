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
        GameUI.clearScreen();
        System.out.println(engine.dm.getDialogue("hallway_intro"));
        System.out.println(engine.player.name +": " + engine.dm.getDialogue("hallway_intro_d1"));
        System.out.println(engine.dm.getDialogue("hallway_choice"));

        boolean validChoice = false;

        while (!validChoice) {
            String choice = GameUI.promptInput(engine.dm.getDialogue("choice_a")).toLowerCase();
            
            if (choice.equals("c")) {
                GameUI.printColored(engine.dm.getDialogue("hallway_fail"), GameUI.RED);
                
                engine.player.takeDamage(10);
                validChoice = true; // Exit loop because they died/finished this scene
            } 
            else if (choice.equals("a") || choice.equals("b")) {
                GameUI.clearScreen();
                GameUI.printColored("You made it through!", GameUI.GREEN);
                System.out.println(engine.player.name + ": \"Gotta hide.\"");
                GameUI.pressEnterToContinue();
                engine.setScene(new StorageScene());
                validChoice = true; // Exit loop to move to the next scene
            } 
            else {
                // No validChoice = true here, so the loop runs again!
                GameUI.printColored("Invalid input. Please choose a, b, or c.", GameUI.RED);
            }
        }
    }
}
