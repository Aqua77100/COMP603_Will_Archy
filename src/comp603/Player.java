package comp603;

/**
 *
 * @author archy
 */
import java.util.*;

class Player {

    private String name;
    private int health = 10;
    
    // Inventory not used, but can be used in future versions
    List<String> inventory = new ArrayList<>(Arrays.asList("Old Shoe", "Soda Can", "Broken Metal Pipe"));

    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public int getHealth(){
        return health;
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
