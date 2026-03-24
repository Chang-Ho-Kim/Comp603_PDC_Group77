package smarthome.service;

import smarthome.model.Device;
import java.time.LocalTime;
import java.util.Collection;

/**
 * Simple implementation of IAutomationService.
 * Responsible for checking automation conditions on devices.
 */
public class SimpleAutomationService implements IAutomationService {
    
    @Override
    public void checkAllDevicesAutomation(Collection<Device> devices, int temperature, LocalTime time) {
        for (Device device : devices) {
            checkDeviceAutomation(device, temperature, time);
        }
    }
    
    @Override
    public void checkDeviceAutomation(Device device, int temperature, LocalTime time) {
        device.checkAutomation(temperature, time);
    }
}
