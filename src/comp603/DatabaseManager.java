/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603;

import java.sql.*;

/**
 *
 * @author willpurdon
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:derby:GameDB;create=true";
    private Connection conn;

    public Connection getConnection() {
        return conn;
    }

    public void connect() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Derby connected!");
            createTables();
        } catch (Exception e) {
            System.out.println("DB connection error: " + e.getMessage());
        }
    }

    private void createTables() {
        try (Statement stmt = conn.createStatement()) {
            // creates players table if doesn't exist
            if (!tableExists("PLAYERS")) {
                stmt.execute("""
                    CREATE TABLE players (
                                            id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                            name VARCHAR(50) NOT NULL,
                                            created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                                        )
                             """);

            }

            if (!tableExists("GAME_SESSIONS")) {
                stmt.execute("""
                    CREATE TABLE game_sessions (
                        id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        player_id INTEGER REFERENCES players(id),
                        health_remaining INTEGER,
                        death_count INTEGER,
                        ending_chosen VARCHAR(20),
                        completed INTEGER DEFAULT 0,
                        start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        end_time TIMESTAMP
                    )
                """);
            }

            if (!tableExists("DEATH_LOG")) {
                stmt.execute("""
                    CREATE TABLE death_log (
                        id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        session_id INTEGER REFERENCES game_sessions(id),
                        scene_name VARCHAR(50),
                        death_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """);
            }

            if (!tableExists("DIALOGUE")) {
                stmt.execute(
                        "CREATE TABLE dialogue ("
                        + "key_name VARCHAR(100) PRIMARY KEY, "
                        + "value VARCHAR(2000) NOT NULL"
                        + ")"
                );
                System.out.println("dialogue table created lowk");
            }
            System.out.println("Tables ready");
        } catch (SQLException e) {
            System.out.println("Table creation error: " + e.getMessage());
        }
    }

    private boolean tableExists(String tableName) {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, tableName, new String[]{"TABLE"});
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            }
        } catch (SQLException e) {
            System.out.println("Derby shut down");
        }

    }
}
