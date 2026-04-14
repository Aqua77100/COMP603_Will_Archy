/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author willpurdon
 */
public class GameEngine {
    private Player player;
    private GameState state;
    private DialogueManager dialogue;
    private Scene currentScene;
    
    public GameEngine(Player player){
        this.player = player;
        this.state = new GameState();
        this.dialogue = new DialogueManager();
    }
    
    public void startGame(){
        setScene(new HallwayScene());
        gameLoop();
    }
    
    public void gameLoop(){
        while(player.isAlive()){
            if(currentScene != null){
                currentScene.enter(this);
            }
        }
    }
    
    public void setScene(Scene newScene){
        this.currentScene = newScene;
    }
    
    public Player getPlayer()
    { 
        return player; 
    }
    
    public GameState getState() 
    { 
        return state; 
    }
    
    public DialogueManager getDialogue() 
    { 
        return dialogue; 
    }
}
