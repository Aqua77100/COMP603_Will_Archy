/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author archy
 */
public class GameEngine {

    public Player player = new Player();
    public GameState state = new GameState();
    public DialogueManager dm = new DialogueManager();
    private Scene currentScene;

    public void setScene(Scene newScene) {
        this.currentScene = newScene;
    }

    public void startGame() {
        dm.loadFile("dialogue.txt");
        System.out.println(dm.getDialogue("intro"));
        player.name = GameUI.promptInput(dm.getDialogue("welcome"));
        currentScene = new HallwayScene();
        gameLoop();
    }

    private void gameLoop() {
        while (player.isAlive()) {
            currentScene.enter(this);
        }

        GameUI.printColored("\n---\n" + dm.getDialogue("death"), GameUI.RED);

        boolean validRetryInput = false;

        while (!validRetryInput) {
            String retry = GameUI.promptInput(dm.getDialogue("retry")).toLowerCase();

            if (retry.equals("y") || retry.equals("yes")) {
                validRetryInput = true;
                player = new Player(); // Reset player stats
                state.reset();         // Reset game flags (lasers, etc.)
                startGame();           // Restart from Scene 1
            } else if (retry.equals("n") || retry.equals("no")) {
                validRetryInput = true;
                System.out.println("Thank you for playing, " + player.name + ".");
                System.exit(0); // Safely close the game
            } else {
                GameUI.printColored("Invalid input. Please enter 'y' for Yes or 'n' for No.", GameUI.RED);
            }
        }

    }

    public static void main(String[] args) {
        new GameEngine().startGame();
    }
}
