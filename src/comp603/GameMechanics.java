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

public class GameMechanics {
    private static Random rand = new Random();

    public static int rollD12() { return rand.nextInt(12) + 1; }

    public static String shuffleString(String input) {
        List<String> letters = Arrays.asList(input.split(""));
        Collections.shuffle(letters);
        return String.join("", letters);
    }

    public static boolean playHangman(Player p, String word) {
        Set<Character> guesses = new HashSet<>();
        int tries = 5;
        
        while (tries > 0) {
            boolean won = true;
            StringBuilder display = new StringBuilder();
            for (char c : word.toCharArray()) {
                if (guesses.contains(c)) display.append(c);
                else { display.append("_"); won = false; }
            }
            
            if (won) return true;
            
            GameUI.printColored("Word: " + display, GameUI.YELLOW);
            String input = GameUI.promptInput("Guess a letter").toUpperCase();
            if (input.isEmpty()) continue;
            char guess = input.charAt(0);
            
            if (!word.contains(String.valueOf(guess))) {
                tries--;
                GameUI.printColored("Wrong! The robot flings a gear at you!", GameUI.RED);
                if (rollD12() > 4) {
                    GameUI.printColored("You dodged it!", GameUI.GREEN);
                } else {
                    p.takeDamage(1);
                    if (!p.isAlive()) return false;
                }
            } else {
                guesses.add(guess);
            }
        }
        return false;
    }
}
