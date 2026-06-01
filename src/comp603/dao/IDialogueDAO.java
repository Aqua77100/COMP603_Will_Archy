/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comp603.dao;

import java.util.Map;

/**
 *
 * @author willpurdon
 */
public interface IDialogueDAO {
    void seedFromFile(String filePath);
    String getDialogue(String key);
    Map<String, String> loadAll();
}
