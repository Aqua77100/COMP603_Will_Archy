package comp603;

/**
 *
 * @author archy
 */
import java.util.*;

public class GameMechanics {

    // Setting up the 12-sided dice roll for probability
    private static Random rand = new Random();

    public static int rollD12() {
        return rand.nextInt(12) + 1;
    }

    // Shuffle the anagram string (password given by robot in security room)
    public static String shuffleString(String input) {
        List<String> letters = Arrays.asList(input.split(""));
        Collections.shuffle(letters);
        return String.join("", letters);
    }
}
