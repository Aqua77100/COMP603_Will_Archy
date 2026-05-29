/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603.dao;

import comp603.DatabaseManager;
import java.sql.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author willpurdon
 */
public class DialogueDAO {
    private final DatabaseManager db;
    
    public DialogueDAO(DatabaseManager db){
        this.db = db;
    }
    
    public void seedFromFile(String filePath){
        // only seed if table is empty
        if(getCount() > 0){
            System.out.println("Dialogue already seeded - skipping.");
            return;
        }
        
        System.out.println("Seeding dialogue from file");
        String sql = "INSERT INTO dialogue (key_name, value) VALUES (?, ?)";
        
        try(BufferedReader br = new BufferedReader(new FileReader(filePath));
                PreparedStatement ps = db.getConnection().prepareStatement(sql)){
            String line;
            while((line = br.readLine()) != null){
                if(line.startsWith("#") || !line.contains("=")) continue;
                String[] parts = line.split("=", 2);
                ps.setString(1, parts[0].trim());
                ps.setString(2, parts[1].trim().replace("\\n", "\n"));
                ps.executeUpdate();
            }
            System.out.println("Dialogue seeded successfully");
        } catch(Exception e){
            System.out.println("Dialogue seed error: " + e.getMessage());
        }
    }
    
    // Get a single dialogue line by key
    public String getDialogue(String key) {
        String sql = "SELECT value FROM dialogue WHERE key_name = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("value");
            }
        } catch (SQLException e) {
            System.out.println("getDialogue error for key '" + key + "': " + e.getMessage());
        }
        return "Missing text for: " + key;
    }
    
    // Load all dialogue into a map (faster for bulk access)
    public Map<String, String> loadAll() {
        Map<String, String> map = new HashMap<>();
        String sql = "SELECT key_name, value FROM dialogue";
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("key_name"), rs.getString("value"));
            }
            System.out.println("Loaded " + map.size() + " dialogue entries.");
        } catch (SQLException e) {
            System.out.println("loadAll error: " + e.getMessage());
        }
        return map;
    }

    private int getCount() {
        try (Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM dialogue")) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("getCount error: " + e.getMessage());
        }
        return 0;
    }
}
