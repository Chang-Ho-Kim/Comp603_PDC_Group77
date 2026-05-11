package smarthome.dao;

import smarthome.model.*;
import smarthome.factory.DeviceFactory;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {

    // =========================
    // SAVE (UPSERT SAFE FOR DERBY)
    // =========================
    public void save(Device d, String id) {

        String updateSql = """
            UPDATE devices
            SET name=?, is_on=?, type=?, electricity_usage=?,
                upper_threshold=?, lower_threshold=?, sensor_on=?,
                start_time=?, end_time=?, schedule_on=?, power_saver=?
            WHERE id=?
        """;

        String insertSql = """
            INSERT INTO devices (
                id, name, is_on, type, electricity_usage,
                upper_threshold, lower_threshold, sensor_on,
                start_time, end_time, schedule_on, power_saver
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseManager.getConnection()) {

            // 1. TRY UPDATE FIRST
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {

                ps.setString(1, d.getName());
                ps.setBoolean(2, d.isOn());
                ps.setString(3, normalizeType(d.getType()));
                ps.setInt(4, d.getElectricityUsage());

                // SENSOR
                if (d instanceof SensorDevice s) {
                    ps.setInt(5, s.getUpper());
                    ps.setInt(6, s.getLower());
                    ps.setBoolean(7, s.isSensorOn());
                } else {
                    ps.setNull(5, Types.INTEGER);
                    ps.setNull(6, Types.INTEGER);
                    ps.setBoolean(7, false);
                }

                // SCHEDULED
                if (d instanceof ScheduledDevice s) {
                    ps.setString(8, s.getStart() != null ? s.getStart().toString() : null);
                    ps.setString(9, s.getEnd() != null ? s.getEnd().toString() : null);
                    ps.setBoolean(10, s.isScheduleOn());
                } else {
                    ps.setNull(8, Types.VARCHAR);
                    ps.setNull(9, Types.VARCHAR);
                    ps.setBoolean(10, false);
                }

                // POWER SAVER
                ps.setBoolean(11, d instanceof PowerSaverDevice);

                ps.setString(12, id);

                int rows = ps.executeUpdate();

                // 2. IF NOT FOUND → INSERT
                if (rows == 0) {

                    try (PreparedStatement ins = conn.prepareStatement(insertSql)) {

                        ins.setString(1, id);
                        ins.setString(2, d.getName());
                        ins.setBoolean(3, d.isOn());
                        ins.setString(4, normalizeType(d.getType()));
                        ins.setInt(5, d.getElectricityUsage());

                        // SENSOR
                        if (d instanceof SensorDevice s) {
                            ins.setInt(6, s.getUpper());
                            ins.setInt(7, s.getLower());
                            ins.setBoolean(8, s.isSensorOn());
                        } else {
                            ins.setNull(6, Types.INTEGER);
                            ins.setNull(7, Types.INTEGER);
                            ins.setBoolean(8, false);
                        }

                        // SCHEDULED
                        if (d instanceof ScheduledDevice s) {
                            ins.setString(9, s.getStart() != null ? s.getStart().toString() : null);
                            ins.setString(10, s.getEnd() != null ? s.getEnd().toString() : null);
                            ins.setBoolean(11, s.isScheduleOn());
                        } else {
                            ins.setNull(9, Types.VARCHAR);
                            ins.setNull(10, Types.VARCHAR);
                            ins.setBoolean(11, false);
                        }

                        // POWER SAVER
                        ins.setBoolean(12, d instanceof PowerSaverDevice);

                        ins.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // GET ALL
    // =========================
    public List<Device> getAll() {

        List<Device> list = new ArrayList<>();

        String sql = "SELECT * FROM devices";

        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                String type = rs.getString("type");

                Device d = DeviceFactory.create(type, rs.getString("name"));
                d.setId(rs.getString("id"));

                d.setElectricityUsage(rs.getInt("electricity_usage"));
                d.restoreState(rs.getBoolean("is_on"));

                // SENSOR
                if (d instanceof SensorDevice s) {
                    s.setUpper(rs.getInt("upper_threshold"));
                    s.setLower(rs.getInt("lower_threshold"));
                    s.setSensorOn(rs.getBoolean("sensor_on"));
                }

                // SCHEDULED
                if (d instanceof ScheduledDevice s) {

                    String start = rs.getString("start_time");
                    String end = rs.getString("end_time");

                    if (start != null)
                        s.setStart(LocalTime.parse(start));

                    if (end != null)
                        s.setEnd(LocalTime.parse(end));

                    s.setScheduleOn(rs.getBoolean("schedule_on"));
                }

                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // DELETE
    // =========================
    public void delete(String id) {

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement("DELETE FROM devices WHERE id=?")) {

            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // TYPE NORMALIZER
    // =========================
    private String normalizeType(String type) {
        return switch (type) {
            case "Light" -> "LIGHT";
            case "Door" -> "DOOR";
            case "Air Conditioner" -> "AIR_CON";
            case "Heater" -> "HEATER";
            case "Alarm Clock" -> "ALARM_CLOCK";
            case "Robot Cleaner" -> "ROBOT_CLEANER";
            case "Music Player" -> "MUSIC_PLAYER";
            case "Television" -> "TELEVISION";
            default -> type.toUpperCase().replace(" ", "_");
        };
    }
}