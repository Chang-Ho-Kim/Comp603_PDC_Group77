package smarthome.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

public class BillingService {

    public static double calculateDeviceCost(Device device, double rate) {
        double totalCost = 0;
        for (UsageRecord r : device.getUsageHistory()) {
            if (r.getStart() == null) continue;

            LocalDateTime endTime = (r.getEnd() == null) ? LocalDateTime.now() : r.getEnd();
            long seconds = Math.max(0, Duration.between(r.getStart(), endTime).getSeconds());
            double hours = seconds / 3600.0;
            totalCost += device.getElectricityUsage() * hours * rate;
        }
        return totalCost;
    }

    public static double calculateTotalBill(Collection<Device> devices, double rate) {
        double total = 0;
        for (Device d : devices) {
            total += calculateDeviceCost(d, rate);
        }
        return total;
    }
}
