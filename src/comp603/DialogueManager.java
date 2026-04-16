/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;
/**
 *
 * @author archy
 */

import java.io.*;
import java.util.*;

public class DialogueManager {
    private Map<String, String> dialogueData = new HashMap<>();

    public void loadFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || !line.contains("=")) continue;
                String[] parts = line.split("=", 2);
                dialogueData.put(parts[0].trim(), parts[1].trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading dialogue: " + e.getMessage());
        }
    }

    public String getDialogue(String key) {
        return dialogueData.getOrDefault(key, "Missing text for: " + key).replace("\\n", "\n");
    }
}
