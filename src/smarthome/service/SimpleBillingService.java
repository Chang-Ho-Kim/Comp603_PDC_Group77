package smarthome.service;

import smarthome.model.Device;
import smarthome.dao.DatabaseManager;

import java.time.LocalDateTime;
import java.util.Collection;

public class SimpleBillingService implements IBillingService {

    @Override
    public double calculateTotalBill(Collection<Device> devices, double costPerWattHour) {
        return devices.stream()
                .mapToDouble(d -> calculateDeviceBill(d, costPerWattHour))
                .sum();
    }

    @Override
    public double calculateDeviceBill(Device device, double costPerWattHour) {

        double total = 0;

        String sql = """
            SELECT start_time, end_time
            FROM device_usage
            WHERE device_id = ?
        """;

        try (java.sql.Connection conn = DatabaseManager.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, device.getName());

            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                java.sql.Timestamp startTs = rs.getTimestamp("start_time");
                java.sql.Timestamp endTs = rs.getTimestamp("end_time");

                if (startTs == null) continue;

                LocalDateTime start = startTs.toLocalDateTime();
                LocalDateTime end = (endTs == null)
                        ? LocalDateTime.now()
                        : endTs.toLocalDateTime();

                long seconds = java.time.temporal.ChronoUnit.SECONDS
                        .between(start, end);

                double hours = seconds / 3600.0;

                total += device.getElectricityUsage() * hours * costPerWattHour;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    @Override
    public int calculateTotalElectricityUsage(Collection<Device> devices) {
        return devices.stream()
                .filter(Device::isOn)
                .mapToInt(Device::getElectricityUsage)
                .sum();
    }
}