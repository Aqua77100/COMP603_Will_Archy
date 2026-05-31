package comp603;

import comp603.dao.PlayerDAO;
import comp603.dao.DeathLogDAO;
import comp603.dao.DialogueDAO;
import comp603.dao.SessionDAO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author archy
 */
public class GameEngine {

    // game objects
    public Player player = new Player();
    public GameState state = new GameState();
    public DialogueManager dm = new DialogueManager();
    public GameWindow window;
    private Scene currentScene;

    // database layer
    public DatabaseManager db = new DatabaseManager();
    public PlayerDAO playerDAO;
    public SessionDAO sessionDAO;
    public DeathLogDAO deathLogDAO;
    public DialogueDAO dialogueDAO;

    // session tracking
    public int currentPlayerId = -1;
    public int currentSessionId = -1;
    public int deathCount = 0;

    public void startGame() {
        db.connect();
        playerDAO = new PlayerDAO(db);
        sessionDAO = new SessionDAO(db);
        deathLogDAO = new DeathLogDAO(db);
        dialogueDAO = new DialogueDAO(db);
        
        dialogueDAO.seedFromFile("dialogue.txt");
        dm.loadFromDatabase(dialogueDAO);

        window.showTitleScreen(this);

        // for testing
        //currentScene = new FactoryScene();
        //currentScene.buildUI(this);
    }

    public void registerPlayer(String name) {
        int existingId = playerDAO.findPlayerName(name);
        System.out.println("findPlayerByName('" + name + "') returned: " + existingId);
        
        if (existingId != -1) {
            // returning player (reuse their ID)
            currentPlayerId = existingId;
            System.out.println("Returning player: " + name + " | playerId: " + currentPlayerId);
        } else {
            // new player (create them)
            currentPlayerId = playerDAO.createPlayer(name);
            System.out.println("New player: " + name + " | playerId: " + currentPlayerId);
        }

        currentSessionId = sessionDAO.createSession(currentPlayerId);
        System.out.println("player registered: " + name + " - player ID: " + currentPlayerId + " - session ID: " + currentSessionId);
    }

    // Method for changing the scene to the next
    public void setScene(Scene newScene) {
        this.currentScene = newScene;
        newScene.buildUI(this);
    }

    public void handleChoice(String key) {
        currentScene.onChoice(this, key);
    }

    public void handleDeath() {
        // log death to db
        if (currentSessionId != -1) {
            deathCount++;
            String sceneName = currentScene.getClass().getSimpleName();
            deathLogDAO.logDeath(currentSessionId, sceneName);
            sessionDAO.incrementDeaths(currentSessionId);
            System.out.println("death logged: " + sceneName + " - total deaths: " + deathCount);
        }

        int result = JOptionPane.showConfirmDialog(window,
                dm.getDialogue("death") + "\n\nRetry?", "Game over",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            player = new Player();
            state.reset();
            // start new sesh for retry
            if (currentPlayerId != -1) {
                currentSessionId = sessionDAO.createSession(currentPlayerId);
            }
            startGame();
        } else {
            quit();
        }
    }

    public void completeGame(String endingChosen) {
        if (currentSessionId == -1) {
            return;
        }

        // Check players best previous score
        int bestHealth = sessionDAO.getBestHealth(currentPlayerId);

        if (bestHealth == -1 || player.health > bestHealth) {
            // New high score — save it
            sessionDAO.completeSession(currentSessionId, player.health, deathCount, endingChosen);
            System.out.println("New high score saved: " + player.health + " HP");
        } else {
            // Not a high score — still complete the session but mark it
            sessionDAO.completeSession(currentSessionId, player.health, deathCount, endingChosen);
            System.out.println("Score saved but not a new high score. Best: " + bestHealth + " HP");
        }
    }

    public void quit() {
        db.close();
        System.exit(0);
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
