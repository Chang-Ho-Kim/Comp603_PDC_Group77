/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * SmartHomeSystem - Core model managing devices and simulation settings.
 * Now properly separated concerns:
 * - Device management: addDevice, removeDevice, getDevice
 * - Simulation settings access: getSimulation
 * - Business logic delegated to services (billing, automation, logging)
 */
public class SmartHomeSystem implements Serializable {

    private HashMap<String, Device> devices;
    private SimulationSettings simulation;
    
    public SmartHomeSystem() {
        devices = new HashMap<>();
        simulation = new SimulationSettings();
    }

    public Collection<Device> getAllDevices() {
        return devices.values();
    }
    
    public Collection<String> getDeviceNames() {
        return devices.keySet();
    }

    public Device getDevice(String id) {
        return devices.get(id);
    }

    public SimulationSettings getSimulation() {
        return simulation;
    }
    
    public void addDevice(Device device) {
        devices.put(device.name, device);
    }
    
    public void removeDevice(String name) {
        devices.remove(name);
    }
    
    // Device and system management methods
}
