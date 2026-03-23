package smarthome.model;

import smarthome.controller.IInputHandler;

/**
 * Interface for device-specific UI behavior.
 * Allows devices to define how they should be displayed and handled without requiring instanceof checks.
 * Supports the Open/Closed Principle - new device types can be added without modifying UI code.
 */
public interface IDeviceUIHandler {
    
    /**
     * Get additional menu content specific to this device type.
     * For example, ScheduledDevice would return schedule info, SensorDevice would return sensor info.
     * @return string content to display in the menu, or empty string if no additional content
     */
    String getAdditionalMenuContent();
    
    /**
     * Get additional options specific to this device type.
     * @return string with additional command options, or empty string if no additional options
     */
    String getAdditionalOptions();
    
    /**
     * Handle device-specific commands.
     * @param command the command input
     * @param handler the input handler for collecting user input (setTime, setTemp, etc)
     * @return true if the command was handled by this device, false to defer to default handling
     */
    boolean handleDeviceCommand(String command, IInputHandler handler);
}
