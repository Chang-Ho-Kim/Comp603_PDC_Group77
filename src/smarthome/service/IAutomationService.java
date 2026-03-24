package smarthome.service;

import smarthome.model.Device;
import java.time.LocalTime;
import java.util.Collection;

/**
 * Service for managing automation checks on devices.
 * Separates automation logic from device classes.
 */
public interface IAutomationService {
    
    /**
     * Check automation for all devices based on temperature and time.
     * @param devices collection of devices to check
     * @param temperature current temperature
     * @param time current time
     */
    void checkAllDevicesAutomation(Collection<Device> devices, int temperature, LocalTime time);
    
    /**
     * Check automation for a single device.
     * @param device the device to check
     * @param temperature current temperature
     * @param time current time
     */
    void checkDeviceAutomation(Device device, int temperature, LocalTime time);
}
