/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author willpurdon
 */
public class DialogueManager {
    private Map<String, String> dialogueData = new HashMap<>();
    
    public void loadFile(String filePath){
        // load dialogue
    }
    
    public String getDialogue(String key) {
        return dialogueData.getOrDefault(key, "Missing dialogue.");
    }
}
