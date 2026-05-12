package smarthome.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL =
            "jdbc:derby:SmartHomeDB;create=true";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}