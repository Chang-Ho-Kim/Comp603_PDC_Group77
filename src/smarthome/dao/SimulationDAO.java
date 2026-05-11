package smarthome.dao;

import smarthome.model.SimulationSettings;

import java.sql.*;

public class SimulationDAO {

    public SimulationSettings get() {

        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT * FROM simulation WHERE id=1")) {

            if (rs.next()) {
                SimulationSettings s = new SimulationSettings();
                s.setTemperature(rs.getInt("temperature"));
                s.setElectricityCost(rs.getDouble("electricity_cost"));
                s.setPowerThreshold(rs.getInt("power_threshold"));
                return s;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new SimulationSettings();
    }

    public void save(SimulationSettings s) {

        try (Connection conn = DatabaseManager.getConnection()) {

            String updateSql = """
                UPDATE simulation
                SET temperature = ?,
                    electricity_cost = ?,
                    power_threshold = ?
                WHERE id = 1
            """;

            PreparedStatement update = conn.prepareStatement(updateSql);

            update.setInt(1, s.getTemperature());
            update.setDouble(2, s.getElectricityCost());
            update.setInt(3, s.getPowerThreshold());

            int rows = update.executeUpdate();

            if (rows == 0) {

                String insertSql = """
                    INSERT INTO simulation (id, temperature, electricity_cost, power_threshold)
                    VALUES (1, ?, ?, ?)
                """;

                PreparedStatement insert = conn.prepareStatement(insertSql);

                insert.setInt(1, s.getTemperature());
                insert.setDouble(2, s.getElectricityCost());
                insert.setInt(3, s.getPowerThreshold());

                insert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}