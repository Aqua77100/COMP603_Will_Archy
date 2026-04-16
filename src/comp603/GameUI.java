/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author archy
 */

import java.util.Scanner;

public class GameUI {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";

    private static Scanner scanner = new Scanner(System.in);

    public static void printColored(String text, String color) {
        System.out.println(color + text + RESET);
    }

    public static String promptInput(String message) {
        System.out.print(CYAN + message + " >> " + RESET);
        return scanner.nextLine();
    }
    
    public static void pressEnterToContinue() {
    System.out.print(CYAN + "\n<< Press Enter to continue >>" + RESET);
    scanner.nextLine();
    }
 

    public static void clearScreen() {
        for (int i = 0; i < 5; i++){ 
            if(i == 2){
                System.out.println("--------");
            } else{
                System.out.println();
            }
        }
    }
}
