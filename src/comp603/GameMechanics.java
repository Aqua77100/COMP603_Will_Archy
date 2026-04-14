/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

import java.util.Random;

/**
 *
 * @author willpurdon
 */
public class GameMechanics {
    public int rollDice(int sides){ // passing the number of sides of dice in if we wanna change it later
        return new Random().nextInt(sides) + 1;
    }
    
    public String shuffleString(String input) {
        char[] chars = input.toCharArray();
        Random rand = new Random();

        for (int i = 0; i < chars.length; i++) {
            int j = rand.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }
    
    public boolean playHangman(Player p) {
        // will do later
        return true;
    }
    
    public boolean connectWiresPuzzle() {
        // same
        return true;
    }
}
