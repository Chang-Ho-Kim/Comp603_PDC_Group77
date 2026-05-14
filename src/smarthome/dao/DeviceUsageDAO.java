package smarthome.dao;

import java.sql.*;
import java.time.LocalDateTime;

public class DeviceUsageDAO {

    public void startUsage(String deviceId) {

        String checkSql = """
            SELECT COUNT(*) FROM device_usage
            WHERE device_id = ? AND end_time IS NULL
        """;

        String insertSql = """
            INSERT INTO device_usage (device_id, start_time, end_time)
            VALUES (?, ?, NULL)
        """;

        try (Connection conn = DatabaseManager.getConnection()) {

            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setString(1, deviceId);

            ResultSet rs = check.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                return;
            }

            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setString(1, deviceId);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void endUsage(String deviceId) {

        String sql = """
            UPDATE device_usage
            SET end_time = ?
            WHERE device_id = ? AND end_time IS NULL
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, deviceId);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteByDeviceId(String deviceId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM device_usage WHERE device_id = ?")) {

            ps.setString(1, deviceId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteAll() {

        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("DELETE FROM device_usage");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}