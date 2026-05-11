package comp603;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author archy
 */
public class GameEngine {

    // player, state, and dm (DialogueManager) object creation
    public Player player = new Player();
    public GameState state = new GameState();
    public DialogueManager dm = new DialogueManager();
    public GameWindow window;
    private Scene currentScene;

    // Game start-up with dialogue, which then begins the game loop
    public void startGame() {
        // Use the dm object (holding the BufferedReader to read from this txt file)
        dm.loadFile("dialogue.txt");
        //window.setBackground("src/images/sighting1.jpg");
        //window.showText(dm.getDialogue("intro1"));
        
        window.showTitleScreen(this);
        //currentScene = new HallwayScene();
        //currentScene.buildUI(this);
    }

    // Method for changing the scene to the next
    public void setScene(Scene newScene) {
        this.currentScene = newScene;
        newScene.buildUI(this);
    }
    
    public void handleChoice(String key) {
        currentScene.onChoice(this, key);
    }
    
    public void handleDeath(){
        int result = JOptionPane.showConfirmDialog(window,
                dm.getDialogue("death") + "\n\nRetry?", "Game over",
                JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.YES_OPTION){
            player = new Player();
            state.reset();
            startGame();
        } else{
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameEngine engine = new GameEngine();
            GameWindow window = new GameWindow(engine);
            engine.window = window;
            window.setVisible(true);
            engine.startGame();
        });
    }
}
