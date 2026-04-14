/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author willpurdon
 */
public class Player {
    private String name;
    private int health = 10;
    private List<String> inventory = new ArrayList<>();
    
    public Player(String name){
        this.name = name;
    }
    
    public void takeDamage(int amount){
        health -= amount;
    }
    
    public boolean isAlive(){
        return health > 0;
    }
    
    public String getName(){
        return name;
    }
    
    public int getHealth(){
        return health;
    }
    
    public List<String> getInventory(){
        return inventory;
    }
}
