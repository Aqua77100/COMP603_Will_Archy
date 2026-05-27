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
        if (endingChosen == null) {
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
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, healthRemaining);
            ps.setInt(2, deathCount);
            ps.setString(3, endingChosen);
            ps.setInt(4, sessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("completeSession error: " + e.getMessage());
        }
    }

    public void incrementDeaths(int sessionId) {
        String sql = "UPDATE game_sessions SET death_count = death_count + 1 WHERE id = ?";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("incrementDeaths error: " + e.getMessage());
        }
    }

    public List<SessionRecord> getLeaderboard() {
        System.out.println("Running leaderboard query...");
        List<SessionRecord> list = new ArrayList<>();
        String sql
                = "SELECT p.name as player_name, "
                + "MAX(gs.health_remaining) as health_remaining, "
                + "MIN(gs.death_count) as death_count, "
                + "gs.ending_chosen "
                + "FROM game_sessions gs "
                + "JOIN players p ON gs.player_id = p.id "
                + "WHERE gs.completed = 1 "
                + "AND gs.health_remaining IS NOT NULL "
                + "AND gs.ending_chosen IS NOT NULL "
                + "GROUP BY p.name, gs.ending_chosen "
                + "ORDER BY health_remaining DESC, death_count ASC "
                + "FETCH FIRST 10 ROWS ONLY";
        try (Statement stmt = db.getConnection().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new SessionRecord(
                        0, // id not needed 
                        rs.getString("player_name"),
                        rs.getInt("health_remaining"),
                        rs.getInt("death_count"),
                        rs.getString("ending_chosen"),
                        null
                ));
            }
        } catch (SQLException e) {
            System.out.println("getLeaderboard error: " + e.getMessage());
        }
        return list;
    }

    public int getBestHealth(int playerId) {
        String sql
                = "SELECT MAX(health_remaining) as best "
                + "FROM game_sessions "
                + "WHERE player_id = ? AND completed = 1";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("best"); // returns 0 if no completed sessions
            }
        } catch (SQLException e) {
            System.out.println("getBestHealth error: " + e.getMessage());
        }
        return -1; // no previous sessions
    }
}
