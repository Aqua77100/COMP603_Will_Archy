/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603.dao;

import comp603.DatabaseManager;
import comp603.DatabaseManager;
import java.sql.*;
/**
 *
 * @author willpurdon
 */
public class DeathLogDAO {
    private final DatabaseManager db;
    
    public DeathLogDAO(DatabaseManager db){
        this.db = db;
    }
    
    public void logDeath(int sessionId, String sceneName){
        String sql = "INSERT INTO death_log (session_id, scene_name) VALUES (?, ?)";
        try(PreparedStatement ps = db.getConnection().prepareStatement(sql)){
            ps.setInt(1, sessionId);
            ps.setString(2, sceneName);
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println("logDeath error: " + e.getMessage());
        }
    }
}
