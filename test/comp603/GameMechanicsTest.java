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
 * Unit tests for GameMechanics business logic.
 */
public class GameMechanicsTest {

    // Mechanics Test Case 1: Ensure the 12-Sided Dice is Correct 
    // (so we aren't accidently putting too much weighting on the probabilities)
    @Test
    public void testRollD12Boundaries() {
        // Since it's random (we can test individual rolls effectively), we can execute
        // the roll multiple times to verify bounds
        for (int i = 0; i < 1000; i++) {
            int roll = GameMechanics.rollD12();
            
            // Use assert to verify that a 12-sided die never rolls lower than 1 or higher than 12 
            assertTrue("Roll should be greater than or equal to 1", roll >= 1); // If false, then these errors appear
            assertTrue("Roll should be less than or equal to 12", roll <= 12);
        }
    }

    // Mechanics Test Case 2: Security Passowrd Shuffle Correctness 
    // (ensure that the player isn't getting the answer straight away)
    @Test
    public void testShuffleStringPreservesLength() {
        String original = "SECURITY_PASSWORD";
        
        // Shuffle the character array
        String shuffled = GameMechanics.shuffleString(original);
       
        // Test length changes (characters shouldn't be lost or added during the shuffle)
        assertEquals("Shuffled string must maintain identical length", original.length(), shuffled.length());
    }

    // Mechanics Test Case 3: Shuffled String Contains Same Characters
    @Test
    public void testShuffleStringContainsSameCharacters() {
        String testString = "TESTCASETHREE";
        
        // Shuffle character the test string
        String shuffled = GameMechanics.shuffleString(testString);
        
        // Convert to sorted arrays to verify contents match perfectly
        char[] originalArray = testString.toCharArray();
        char[] shuffledArray = shuffled.toCharArray();
        java.util.Arrays.sort(originalArray); // Sort them both alphabetically
        java.util.Arrays.sort(shuffledArray);
        
        // The exact same letters must remain present, otherwise, show the error message
        assertArrayEquals("Shuffled string must contain the exact same characters", originalArray, shuffledArray);
    }
}