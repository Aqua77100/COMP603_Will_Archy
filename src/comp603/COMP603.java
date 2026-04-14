/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package comp603;

/**
 *
 * @author archy
 */
public class COMP603 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Player player = new Player("Player1");
        GameEngine engine = new GameEngine(player);
        
        engine.startGame();
    }
    
}
