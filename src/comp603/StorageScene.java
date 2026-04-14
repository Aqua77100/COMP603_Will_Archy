/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author willpurdon
 */
public class StorageScene {

    public StorageScene() {
    }
    public void enter(GameEngine engine){
        // stuff goes here
        
        // next scene 
        engine.setScene(new SecurityScene());
    }
}
