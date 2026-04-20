package comp603;

/**
 *
 * @author archy
 */
class HallwayScene extends Scene {

    public void enter(GameEngine engine) {
        System.out.println(engine.dm.getDialogue("hallway_intro"));
        System.out.println(engine.player.name + ": " + engine.dm.getDialogue("hallway_intro_d1"));
        System.out.println(engine.dm.getDialogue("hallway_choice"));

        boolean validChoice = false;

        while (!validChoice) {
            String choice = GameUI.promptInput(engine.dm.getDialogue("choice_a")).toLowerCase();

            if (choice.equals("c")) {
                GameUI.clearScreen();
                GameUI.printColored(engine.dm.getDialogue("hallway_fail"), GameUI.RED);
                engine.player.takeDamage(10);
                validChoice = true; // Exit loop
            } else if (choice.equals("a") || choice.equals("b")) {
                GameUI.clearScreen();
                GameUI.printColored("You made it through!", GameUI.GREEN);
                System.out.println(engine.player.name + ": \"Gotta hide.\"");
                GameUI.pressEnterToContinue();
                engine.setScene(new StorageScene());
                validChoice = true; // Exit loop to move to the next scene
            } else {
                // Invalid input, loop restarts/continues
                GameUI.printColored("Invalid input. You must choose your method of escape (a, b, or c)", GameUI.RED);
            }
        }
    }
}
