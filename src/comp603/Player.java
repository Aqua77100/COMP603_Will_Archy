package comp603;

/**
 *
 * @author archy
 */
import java.util.*;

class Player {

    String name;
    int health = 10;

    // Inventory not used, but can be used in future versions
    List<String> inventory = new ArrayList<>(Arrays.asList("Old Shoe", "Soda Can", "Broken Metal Pipe"));

    public void takeDamage(int amount) {
        health -= amount;
        // Can print standardised health message here
        // E.g. GameUI.printColored("That hurt. Health: " + health + "/10", GameUI.RED);
    }

    public boolean isAlive() {
        return health > 0;
    }
}
