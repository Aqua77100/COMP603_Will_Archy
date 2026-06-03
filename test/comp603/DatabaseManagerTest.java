/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package comp603;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author archy
 */

public class DatabaseManagerTest {
    
    private DatabaseManager dbManager;

    // Set up the DB manager BEFORE testing anything related to the DB
    @Before
    public void setUp() {
        dbManager = DatabaseManager.getInstance();
    }

    @After
    public void tearDown() {
        // Just close the individual connection instance loop between tests
        if (dbManager != null && dbManager.getConnection() != null) {
            try {
                dbManager.getConnection().close();
            } catch (SQLException e) {
                // We will just silently ignore close issues during test cleanup
            }
        }
    }

    @AfterClass
    public static void tearDownClass() {
        // Shut down the entire embedded Derby engine ONCE when EVERYTHING is done
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            // Derby typically always throws an exception on system shutdown, we will print a message showing this:
            System.out.println("Global Derby engine system shut down successfully.");
        }
    }

    // DB Test Case 1: Database Connectivity (we want successfull connection)
    @Test
    public void testDatabaseConnectionSuccess() {
        dbManager.connect();
        Connection connection = dbManager.getConnection();
        
        assertNotNull("Database connection should not be null after connecting", connection);
        try {
            assertFalse("Database connection should be open and active", connection.isClosed());
        } catch (SQLException e) {
            fail("Failed to verify connection status: " + e.getMessage());
        }
    }

    // DB Test Case 2: Creating the Tables (all tables should be created sucesfully, with none missing)
    @Test
    public void testTablesSchemaCreation() {
        dbManager.connect();
        Connection connection = dbManager.getConnection();
        
        // As mentioned int Test Case 1: DB connection should not be null after connecting
        assertNotNull("Connection must be active to check metadata", connection); 
        // Check with assertTrue (will only give error if false)
        assertTrue("PLAYERS table missing", tableExists(connection, "PLAYERS"));
        assertTrue("GAME_SESSIONS table missing", tableExists(connection, "GAME_SESSIONS"));
        assertTrue("DEATH_LOG table missing", tableExists(connection, "DEATH_LOG"));
        assertTrue("DIALOGUE table missing", tableExists(connection, "DIALOGUE"));
    }

    // Private T/F to test if the table exists for Test Case 2 (above ^)
    private boolean tableExists(Connection conn, String tableName) {
        try {
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, null, tableName.toUpperCase(), new String[]{"TABLE"})) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}