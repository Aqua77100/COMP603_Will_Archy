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
        
        String retry = GameUI.promptInput(dm.getDialogue("death")).toLowerCase();
        if (retry.equals("y")) {
            player = new Player();
            state.reset();
            startGame();
        } else {
            System.out.println("Goodbye, "+player.name);
        }
    }

    public static void main(String[] args) {
        new GameEngine().startGame();
    }
}
