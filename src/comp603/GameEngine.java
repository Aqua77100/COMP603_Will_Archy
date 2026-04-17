package comp603;

/**
 *
 * @author archy
 */
public class GameEngine {

    // player, state, and dm (DialogueManager) object creation
    public Player player = new Player();
    public GameState state = new GameState();
    public DialogueManager dm = new DialogueManager();
    private Scene currentScene;

    // Method for changing the scene to the next
    public void setScene(Scene newScene) {
        this.currentScene = newScene;
    }

    // Game start-up with dialogue, which then begins the game loop
    public void startGame() {
        // Use the dm object (holding the BufferedReader to read from this txt file)
        dm.loadFile("dialogue.txt");
        System.out.println(dm.getDialogue("intro1"));

        GameUI.pressEnterToContinue();
        GameUI.clearScreen();

        System.out.println(dm.getDialogue("intro2"));

        GameUI.pressEnterToContinue();
        GameUI.clearScreen();

        GameUI.printColored("*Click-click. Zrrt-whirrr-click.*", GameUI.RED);

        GameUI.pressEnterToContinue();
        GameUI.clearScreen();

        System.out.println(dm.getDialogue("intro3"));

        GameUI.pressEnterToContinue();
        GameUI.clearScreen();

        System.out.println(dm.getDialogue("intro4"));

        boolean validUser = false;
        while (!validUser) {
            String inputName = GameUI.promptInput(dm.getDialogue("username"));

            // Check if input is not null and actually contains characters
            if (inputName != null && !inputName.trim().isEmpty()) {
                player.name = inputName.trim();
                validUser = true; // Break loop
            } else {
                GameUI.printColored("Error: You must enter a valid username to proceed.", GameUI.RED);
            }
        }

        // Move these OUTSIDE the while loop so they only run once the name is set
        currentScene = new HallwayScene();
        gameLoop();// Enter the game loop, which goes until you reach one of the endings

    }

    // Game loops until the player dies and selects 'n' for retrying - if 'y', then restart game
    private void gameLoop() {
        while (player.isAlive()) {
            // Bring player to the intro scene in this class - loop starts via startGame() 
            currentScene.enter(this);
        }

        GameUI.printColored("\n---\n" + dm.getDialogue("death"), GameUI.RED); // Player dies, print death message

        boolean validRetryInput = false; // Use this for the reset while loop
        while (!validRetryInput) {
            String retry = GameUI.promptInput(dm.getDialogue("retry")).toLowerCase(); // Retry text + get input from player

            if (retry.equals("y") || retry.equals("yes")) { // Player says 'y'
                validRetryInput = true; // Exit loop
                player = new Player(); // Reset player stats
                state.reset();         // Reset game flags (lasers, etc.)
                startGame();           // Restart from Scene 1 (intro scenes in GameEngine)
            } else if (retry.equals("n") || retry.equals("no")) {
                validRetryInput = true; // Exit loop
                System.out.println("Thank you for playing, " + player.name + ".");
                System.exit(0); // Safely close the game
            } else {
                // Remain in loop, reprompt player
                GameUI.printColored("Invalid input. Please enter 'y' for Yes or 'n' for No.", GameUI.RED);
            }
        }

    }

    // Main method that starts the game
    public static void main(String[] args) {
        new GameEngine().startGame();
    }
}
