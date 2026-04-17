package comp603;

/**
 *
 * @author archy
 */
import java.util.Scanner;

public class GameUI {

    // Set the string colours we can utilise
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    // Initialise a scanner for user input
    private static Scanner scanner = new Scanner(System.in);

    // Constructor for making coloured strings
    public static void printColored(String text, String color) {
        System.out.println(color + text + RESET);
    }

    // Constructor for the prompting with a special colour
    public static String promptInput(String message) {
        System.out.print(BLUE + message + " >> " + RESET);
        return scanner.nextLine();
    }

    // Constructor method press 'enter' to add a pause/break 
    public static void pressEnterToContinue() {
        System.out.print(BLUE + "\n<< Press Enter to continue >>" + RESET);
        scanner.nextLine();
    }

    // 'Clears' screen by printing lines to make it seems like new terminal + declutter (user can still scroll back)
    public static void clearScreen() {
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                System.out.println("--------");
            } else {
                System.out.println();
            }
        }
    }
}
