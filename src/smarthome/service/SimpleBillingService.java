package smarthome.service;

import smarthome.model.Device;
import smarthome.model.UsageRecord;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Simple implementation of IBillingService.
 * Responsible for all billing and electricity usage calculations.
 */
public class SimpleBillingService implements IBillingService {
    
    @Override
    public double calculateTotalBill(Collection<Device> devices, double electricityCostPerWattHour) {
        return devices.stream()
                .mapToDouble(device -> calculateDeviceBill(device, electricityCostPerWattHour))
                .sum();
    }
    
    @Override
public double calculateDeviceBill(Device device, double electricityCostPerWattHour) {
    double totalCost = 0;
    
    for (UsageRecord record : device.getUsageHistory()) {
        if (record.getStart() == null) {
            continue;
        }
        
        LocalDateTime endTime = (record.getEnd() == null) 
            ? LocalDateTime.now() 
            : record.getEnd();
        
        long durationSeconds = java.time.temporal.ChronoUnit.SECONDS
                .between(record.getStart(), endTime);
        
        double hoursDuration = durationSeconds / 3600.0;
        
        double deviceCost = (device.getElectricityUsage() * hoursDuration) 
                * electricityCostPerWattHour;
        
        totalCost += deviceCost;
    }
    
    return totalCost;
}
    
    @Override
    public int calculateTotalElectricityUsage(Collection<Device> devices) {
        return devices.stream()
                .filter(Device::isOn)
                .mapToInt(Device::getElectricityUsage)
                .sum();
    }
}
