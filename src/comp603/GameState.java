/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

/**
 *
 * @author archy
 */


class GameState {
    boolean laserActive = true;
    boolean securityWirePuzzleDone = false;
    String correctPassword = "KNOCKKNOCK";
    int passwordAttempts = 0;

    void reset() {
        laserActive = true;
        securityWirePuzzleDone = false;
        passwordAttempts = 0;
    }
}
