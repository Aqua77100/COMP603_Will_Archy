package comp603;

/**
 *
 * @author archy
 */
import comp603.dao.DialogueDAO;
import java.io.*;
import java.util.*;

public class DialogueManager {

    // Create HashMap for the dialogue text
    private Map<String, String> dialogueData = new HashMap<>();
    private DialogueDAO dialogueDAO;

    public void loadFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // Reads from the file in GameEngine (dialogue.txt)
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || !line.contains("=")) {
                    continue;
                }
                String[] parts = line.split("=", 2); // Split from/after the '=' sign
                dialogueData.put(parts[0].trim(), parts[1].trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading dialogue: " + e.getMessage()); // Throw this if an error occurs
        }
    }
    
    public void loadFromDatabase(DialogueDAO dao){
        this.dialogueDAO = dao;
        dialogueData = dao.loadAll(); // load everything into memory at startup 
        System.out.println("Dialogue loaded from db");
    }

    public String getDialogue(String key) {
        // There is a dialogue key mentioned that isn't present
        return dialogueData.getOrDefault(key, "Missing text for: " + key).replace("\\n", "\n");
    }
}
