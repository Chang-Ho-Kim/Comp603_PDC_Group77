package smarthome.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void init() {

        createDevicesTable();
        createDeviceUsageTable();
        createSimulationTable();
    }

    private static void createDevicesTable() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("""
                CREATE TABLE devices (
                    id VARCHAR(50) PRIMARY KEY,
                    name VARCHAR(100),
                    is_on BOOLEAN,
                    type VARCHAR(30),
                    electricity_usage INT,
                    upper_threshold INT,
                    lower_threshold INT,
                    sensor_on BOOLEAN,
                    start_time VARCHAR(10),
                    end_time VARCHAR(10),
                    schedule_on BOOLEAN,
                    power_saver BOOLEAN
                )
            """);

        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        }
    }

    private static void createDeviceUsageTable() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("""
                CREATE TABLE device_usage (
                    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    device_id VARCHAR(50),
                    start_time TIMESTAMP,
                    end_time TIMESTAMP
                )
            """);

        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        }
    }

    private static void createSimulationTable() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("""
                CREATE TABLE simulation (
                    id INT PRIMARY KEY,
                    temperature INT,
                    electricity_cost DOUBLE,
                    power_threshold INT
                )
            """);

        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                e.printStackTrace();
            }
        }
    }
}