/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603.dao;

import comp603.DatabaseManager;
import comp603.DatabaseManager;
import comp603.model.PlayerRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author willpurdon
 */
public class PlayerDAO {

    private final DatabaseManager db;

    public PlayerDAO(DatabaseManager db) {
        this.db = db;
    }

    // insert new player
    public int createPlayer(String name) {
        String sql = "INSERT INTO players (name) VALUES (?)";
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("createPlayer error: " + e.getMessage());
        }
        return -1;
    }

    public List<PlayerRecord> getAllPlayers() {
        List<PlayerRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM players ORDER BY created_date DESC";
        try (Statement stmt = db.getConnection().createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PlayerRecord(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println("getAllPlayers error: " + e.getMessage());
        }
        return list;
    }
}
