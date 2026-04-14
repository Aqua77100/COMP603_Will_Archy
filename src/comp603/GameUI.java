/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

import java.util.Scanner;

/**
 *
 * @author willpurdon
 */
public class GameUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printColored(String text, String colour) {
        // simple placeholder (no real colours yet)
        System.out.println(text);
    }

    public static String promptInput() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    public static void clearScreen() {
        System.out.println("\n\n\n");
    }

    public static void displayStats(Player p) {
        System.out.println("Health: " + p.getHealth());
        System.out.println("Inventory: " + p.getInventory());
    }
}
