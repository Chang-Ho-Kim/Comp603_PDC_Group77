package smarthome.controller;

import smarthome.model.Device;

/**
 * Interface for screen navigation.
 * Segregated from other controller concerns - only exposes screen navigation methods.
 */
public interface IScreenNavigator {
    void showDashboard();
    void showDevice(Device device);
    void showSimulation();
    void showLog();
    void showDeviceAdder();
    void showDeviceRemover();
    void showAutomation();
}
