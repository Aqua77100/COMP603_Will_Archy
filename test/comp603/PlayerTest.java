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

/**
 *
 * @author archy
 */

/**
 * Unit tests for Player business logic.
 */
public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        // Initialise a clean default player profile before every execution loop
        player = new Player();
        player.name = "Test Player";
    }

    // Player Test Case 1: Ensuring the Variables are Initialised Correct 
    // (e.g. health = 10, alive, and inventory [even though it is not currently used due to time-constraints])
    @Test
    public void testPlayerInitialState() {
        // Asserts: Ensure clean instance state defaults match what is expected
        assertEquals("Initial health should start at 10", 10, player.health);   // Expected: 10
        assertTrue("Player should be alive initially", player.isAlive());       // Expected: Alive
        assertNotNull("Inventory list should be initialised", player.inventory);    // Expected: Inventory made
        assertEquals("Inventory should start with 3 items (shoe, can, pipe)", 3, player.inventory.size()); // Expected: 3 items
    }

    // Player Test Case 2: Player Takes Damage Correctly (reduction, not death)
    @Test
    public void testPlayerTakesDamageReducesHealth() {
        // Test Player takes hypothetical damage
        player.takeDamage(4);

        // Use assert to check the health, ensure that is has gone down by 4 only
        assertEquals("Health should drop to 6 after taking 4 damage", 6, player.health);
        assertTrue("Player should still be alive with health remaining", player.isAlive());
    }

    // Player Test Case 3: Player Death (health hitting 0)
    @Test
    public void testPlayerDiesWhenHealthHitsZero() {
        // Drop Test Player exactly to the health lower limit
        player.takeDamage(10);

        // Use assert to test if they health is at 0, and then see if the player is alive
        assertEquals("Health should be exactly at 0", 0, player.health);
        assertFalse("Player should not be alive when health hits 0", player.isAlive());
    }

    // Player Test Case 4: Player Death (health reaching negative numbers/< 0)
    @Test
    public void testPlayerDiesWhenHealthDropsBelowZero() {
        // Test Player experiences overkill
        player.takeDamage(15);

        // Should do the same as 3 (player should not be alive)
        assertTrue("Health can drop below zero currently", player.health < 0);
        assertFalse("Player should be dead when health is negative", player.isAlive());
    }
}