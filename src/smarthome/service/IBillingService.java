package smarthome.service;

import smarthome.model.Device;
import java.util.Collection;

/**
 * Service for managing and calculating electricity billing.
 * Separates billing logic from the model layer.
 */
public interface IBillingService {
    
    /**
     * Calculate total electricity bill for all devices.
     * @param devices collection of devices to calculate billing for
     * @param electricityCostPerWattHour the cost per watt-hour
     * @return total bill cost
     */
    double calculateTotalBill(Collection<Device> devices, double electricityCostPerWattHour);
    
    /**
     * Calculate bill for a single device.
     * @param device the device to calculate billing for
     * @param electricityCostPerWattHour the cost per watt-hour
     * @return bill cost for the device
     */
    double calculateDeviceBill(Device device, double electricityCostPerWattHour);
    
    /**
     * Calculate total electricity usage for active devices.
     * @param devices collection of devices to check
     * @return total electricity usage in watts
     */
    int calculateTotalElectricityUsage(Collection<Device> devices);
}
