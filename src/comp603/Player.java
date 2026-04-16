/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;
/**
 *
 * @author archy
 */
import java.util.*;

class Player {
    String name;
    int health = 10;
    List<String> inventory = new ArrayList<>(Arrays.asList("Old Shoe", "Soda Can", "Broken Metal Pipe"));

    public void takeDamage(int amount) {
        health -= amount;
        //GameUI.printColored("That hurt. Health: " + health + "/10", GameUI.RED);
    }
    public boolean isAlive() { return health > 0; }
}
