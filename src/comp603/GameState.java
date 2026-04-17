package comp603;

/**
 *
 * @author archy
 */
class GameState {

    // Set the game states (world flags)
    boolean laserActive = true;
    boolean securityWirePuzzleDone = false;
    String correctPassword = "KNOCKKNOCK"; // Set the correct password here
    int passwordAttempts = 0;

    // Set the reset method, sets everything to how it initially was
    void reset() {
        laserActive = true;
        securityWirePuzzleDone = false;
        passwordAttempts = 0;
        GameUI.clearScreen();
    }
}
