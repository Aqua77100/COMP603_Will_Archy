/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603.dao;

import comp603.DatabaseManager;
import comp603.DatabaseManager;
import comp603.model.PlayerRecord;
import comp603.model.SessionRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author willpurdon
 */
public class SessionDAO {

    private final DatabaseManager db;

    public SessionDAO(DatabaseManager db) {
        this.db = db;
    }

    public int createSession(int playerId) {
        String sql = "INSERT INTO game_sessions (player_id, death_count) VALUES (?, 0)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, playerId);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("createSession error: " + e.getMessage());
        }
        return -1;
    }

    public void completeSession(int sessionId, int healthRemaining, int deathCount, String endingChosen) {
        if(endingChosen == null){
            endingChosen = "UNKNOWN";
        }
        
        String sql = """
                UPDATE game_sessions
                            SET health_remaining = ?,
                                death_count = ?,
                                ending_chosen = ?,
                                completed = 1,
                                end_time = CURRENT_TIMESTAMP
                            WHERE id = ?
                     """;
        try(PreparedStatement ps = db.getConnection().prepareStatement(sql)){
            ps.setInt(1, healthRemaining);
            ps.setInt(2, deathCount);
            ps.setString(3, endingChosen);
            ps.setInt(4, sessionId);
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println("completeSession error: " + e.getMessage());
        }
    }
    
    public void incrementDeaths(int sessionId){
        String sql = "UPDATE game_sessions SET death_count = death_count + 1 WHERE id = ?";
        try(PreparedStatement ps = db.getConnection().prepareStatement(sql)){
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        } catch(SQLException e){
            System.out.println("incrementDeaths error: " + e.getMessage());
        }
    }
    
    public List<SessionRecord> getLeaderboard(){
        List<SessionRecord> list = new ArrayList<>();
        String sql = """
                     UPDATE game_sessions
                                 SET health_remaining = ?,
                                     death_count = ?,
                                     ending_chosen = ?,
                                     completed = 1,
                                     end_time = CURRENT_TIMESTAMP
                                 WHERE id = ?
                     """;
        try(Statement stmt = db.getConnection().createStatement(); 
                ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                list.add(new SessionRecord(
                    rs.getInt("id"),
                    rs.getString("player_name"),
                    rs.getInt("health_remaining"),
                    rs.getInt("death_count"),
                    rs.getString("ending_chosen"),
                    rs.getTimestamp("end_time")
                ));
            }
        } catch(SQLException e){
            System.out.println("getLeaderboard error: " + e.getMessage());
        }
        return list;
    }
}
