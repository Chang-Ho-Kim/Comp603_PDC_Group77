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
    private HashMap<String, Device> removedDevices;
    
    public SmartHomeSystem() {
        devices = new HashMap<>();
        simulation = new SimulationSettings();
        removedDevices = new HashMap<>();
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
    
    private String getUniqueName(String name) {
        while (removedDevices.containsKey(name)) {
            name += "V";
        }
        return name;
    }
    
    public void removeDevice(String name) {
        String uniqueName = getUniqueName(name);
        removedDevices.put(uniqueName, getDevice(name));

        devices.get(name).turnOff();
        devices.remove(name);
    }
    
    public Device getRemovedDevice(String id) {
        return removedDevices.get(id);
    }
    
     public Collection<Device> getAllRemovedDevices() {
        return removedDevices.values();
    }
    
    // Device and system management methods
}
