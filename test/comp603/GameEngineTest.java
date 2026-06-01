/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package comp603;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author archy
 */


/**
 * Unit and Integration tests for GameEngine logic.
 */
public class GameEngineTest {

    // BEFORE any tests, we have to establish the necessary primitives, objects,connections, etc
    private GameEngine engine;

    @Before
    public void setUp() {
        // Initialise engine and establish a clean connection
        engine = new GameEngine();
        engine.db.connect();
        
        // Manual instantiation of the DAOs
        engine.playerDAO = new comp603.dao.PlayerDAO(engine.db);
        engine.sessionDAO = new comp603.dao.SessionDAO(engine.db);
        engine.deathLogDAO = new comp603.dao.DeathLogDAO(engine.db);
        engine.dialogueDAO = new comp603.dao.DialogueDAO(engine.db);
    }

    @After
    public void tearDown() {
        // Only close the connection between test runs, don't shut down the whole driver
        if (engine != null && engine.db != null && engine.db.getConnection() != null) {
            try {
                engine.db.getConnection().close();
            } catch (SQLException e) {
                // We will just silently ignore close issues during test cleanup
            }
        }
    }

    @AfterClass
    public static void tearDownClass() {
        // Shut down the entire embedded Derby engine ONCE when the WHOLE CLASS finishes
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            // Derby typically always throws an exception on system shutdown, we will print a message showing this:
            System.out.println("Global Derby engine system shut down successfully for GameEngineTest.");
        }
    }

    // Engine Test Case 1: New PLayers Generate an ID
    @Test
    public void testRegisterNewPlayerGeneratesIds() {
        // Create a unique name to guarantee entering the "New Player" code block
        String newPlayerName = "PDCTest1Player" + System.currentTimeMillis();

        // Register the player
        engine.registerPlayer(newPlayerName);

        // Use assert to verify that the veriables have been updated from their default -1 states
        assertTrue("New player registration should yield a valid, positive player ID", engine.currentPlayerId > 0); // Cant have a negative ID
        assertTrue("New player registration should establish an active session ID", engine.currentSessionId > 0); // In use?
        assertEquals("Initial death tracker state should start at zero", 0, engine.deathCount); // Death count on register should be 0
    }

    // Engine Test Case 2: Reusing IDs For Reccuring Players
    @Test
    public void testRegisterExistingPlayerReusesId() {
        String recurringPlayer = "PDCTest2Player" + System.currentTimeMillis();

        // Register the profile once to establish it in the DB tables
        engine.registerPlayer(recurringPlayer);
        int originalPlayerId = engine.currentPlayerId;
        int firstSessionId = engine.currentSessionId;

        // Register the exact same name a second time to trigger the "Returning Player" branch
        engine.registerPlayer(recurringPlayer);

        // Using assert to test if the ID numbers match up properly
        assertEquals("Returning player registration must preserve and reuse the original Player ID", 
                     originalPlayerId, engine.currentPlayerId);
        assertNotEquals("Returning player should be assigned a fresh, distinct Session ID slot", 
                        firstSessionId, engine.currentSessionId);
    }
}