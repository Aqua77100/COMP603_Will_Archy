/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author willpurdon
 */
public class GameState {
    public boolean laserActive = true;
    public boolean securityWirePuzzleDone = false;
    public String correctPassword;
    public int passwordAttempts = 0;
    
    public void reset(){
        laserActive = true;
        securityWirePuzzleDone = false;
        passwordAttempts = 0;
        correctPassword = null;
    }
}
