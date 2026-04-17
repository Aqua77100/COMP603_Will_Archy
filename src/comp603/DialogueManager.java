package comp603;

/**
 *
 * @author archy
 */
import java.io.*;
import java.util.*;

public class DialogueManager {

    // Create HashMap for the dialogue text
    private Map<String, String> dialogueData = new HashMap<>();

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
            System.out.println("Error loading dialogue: " + e.getMessage()); // Trow this if an error occurs
        }
    }

    public String getDialogue(String key) {
        // There is a dialogue key mentioned that isn't present
        return dialogueData.getOrDefault(key, "Missing text for: " + key).replace("\\n", "\n");
    }
}
