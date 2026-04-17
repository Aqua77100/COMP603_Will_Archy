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

    // HANGMAN GAME
    public static boolean playHangman(Player p, String word) {
        Set<Character> guesses = new HashSet<>();
        int triesLeft = 11; // 11 to build the full drawing of hangman

        while (triesLeft > 0) {
            boolean won = true;
            StringBuilder display = new StringBuilder();
            for (char c : word.toCharArray()) {
                if (guesses.contains(c)) {
                    display.append(c).append(" "); // Add the guessed letter if correct
                } else {
                    display.append("_ ");
                    won = false;
                }
            }

            // Player wins
            if (won) {
                GameUI.printColored("The word was: " + word + "\n", GameUI.GREEN);

                GameUI.pressEnterToContinue();
                GameUI.clearScreen();

                System.out.println("Roaring, Sync detatches its limbs from the sites around the room.\nBlack liquid bubbles out its eyes, causing its exposed wired to explode.");

                GameUI.pressEnterToContinue();
                GameUI.clearScreen();

                System.out.println("Sync's body lies at my feet.\nI clutch my chest as I hear the door unlock.\n");
                System.out.println(p.name + ": \"What a day. Devon better give me triple.\"");
                return true;
            }

            // Display the word, attempts before the 'Hangman' is complete, and the prompt all displayed after each guess
            GameUI.printColored("---\nWord: " + display, GameUI.YELLOW);
            System.out.println("Attempts before 'Hangman' is complete: " + triesLeft);
            String input = GameUI.promptInput("Guess a letter").toUpperCase();

            // ---- VALIDATION LOGIC 
            // If user hits 'enter' / 'return', reprompt
            if (input.isEmpty()) {
                continue;
            }

            // Check if they tried to guess a whole word or multiple letters
            if (input.length() > 1) {
                GameUI.printColored("Sync: \"One letter at a time, human!\"", GameUI.RED);
                System.out.println("Sync looks confused... If that's even possible.");
                continue;
            }

            char guess = input.charAt(0);

            // Check for numbers or special characters
            if (!Character.isLetter(guess)) {
                GameUI.printColored("Sync: \"That's not a letter!\"\nSync tilts its head in mockery.", GameUI.RED);
                continue;
            }

            // Check if they already guessed that letter
            if (guesses.contains(guess)) {
                GameUI.printColored("Sync: \"You already guessed '" + guess + ", don't you know how to play?!\"", GameUI.RED);
                continue;
            }

            // --- GAME LOGIC ---
            if (!word.contains(String.valueOf(guess))) {
                triesLeft--; // Only decrease tries on a WRONG guess
                guesses.add(guess); // Track that we've guessed this wrong letter

                GameUI.printColored("Wrong! Sync flings a gear at you!", GameUI.RED);

                // Incorrect + dodge
                if (rollD12() > 5) { // Give less chance of being hit (7/12 = dodge, 5/12 = hit)
                    GameUI.printColored("You dodged it!", GameUI.GREEN);
                    GameUI.printColored("Sync: \"No!\"", GameUI.RED);
                    GameUI.printColored("Health: " + p.health + "/10", GameUI.RESET);

                } else {
                    // Incorrect + hit
                    GameUI.printColored("Sync: \"Got you!\"", GameUI.RED);
                    p.takeDamage(1);
                    GameUI.printColored("That hurt. Health: " + p.health + "/10", GameUI.RESET);
                    if (!p.isAlive()) {
                        return false;
                    }
                }
            } else {
                guesses.add(guess); // Correct guess
                GameUI.printColored("Correct!", GameUI.GREEN);
                GameUI.printColored("Health: " + p.health + "/10", GameUI.RESET);
            }
        }

        // ---- HANGMAN COMPLETED
        // If the loop ends, it means triesLeft hit 0
        GameUI.clearScreen();
        GameUI.printColored("Sync: \"Wow you really suck! I guess I gotta find someone else..\"\nSync: \"Do you know how many requests I've sent your company? Takes ages to wipe their data...\"", GameUI.RED);
        System.out.println("Sync charges at you. The last thing you ever will see is a grown-up robot baby.");
        p.takeDamage(10); // Instant death
        return false;
    }
}
