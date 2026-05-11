package smarthome.model;

import smarthome.dao.DeviceDAO;
import smarthome.dao.SimulationDAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import smarthome.dao.DeviceUsageDAO;

/**
 * SmartHomeSystem - Core model managing devices and simulation settings.
 * Now properly separated concerns:
 * - Device management: addDevice, removeDevice, getDevice
 * - Simulation settings access: getSimulation
 * - Business logic delegated to services (billing, automation, logging)
 *
 * UPDATED FOR JDBC EMBEDDED DERBY (NO SERIALIZATION)
 */
public class SmartHomeSystem {

    // NOW: DB-backed cache instead of pure in-memory source of truth
    private HashMap<String, Device> devices;

    private SimulationSettings simulation;

    private HashMap<String, Device> removedDevices;

    // DAO LAYER
    private final DeviceDAO deviceDAO = new DeviceDAO();
    private final SimulationDAO simulationDAO = new SimulationDAO();

    public SmartHomeSystem() {
        devices = new HashMap<>();
        simulation = new SimulationSettings();
        removedDevices = new HashMap<>();
    }

    /**
     * Load everything from database into memory
     */
    public void load() {
        List<Device> list = deviceDAO.getAll();
        devices.clear();

        for (Device d : list) {
            devices.put(d.getName(), d);
        }

        simulation = simulationDAO.get();
    }

    /**
     * Save everything from memory into database
     */
    public void save() {
        for (Device d : devices.values()) {
            deviceDAO.save(d, d.getName());
        }
        simulationDAO.save(simulation);
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
    devices.put(device.getId(), device);
}

    private String getUniqueName(String name) {
        while (removedDevices.containsKey(name)) {
            name += "V";
        }
        return name;
    }

   public void removeDevice(String id) {

        Device d = devices.get(id);

        if (d != null) {
            d.turnOff();

            new DeviceDAO().delete(id);   // 👈 THIS is the missing piece

            devices.remove(id);
        }
    }

    public Device getRemovedDevice(String id) {
        return removedDevices.get(id);
    }

    public Collection<Device> getAllRemovedDevices() {
        return removedDevices.values();
    }

    public void clearRemovedDevices() {
        removedDevices.clear();
    }

   public void resetAllDeviceUsageHistory() {

        DeviceUsageDAO dao = new DeviceUsageDAO();
        dao.deleteAll();

        // IMPORTANT: restart tracking for ON devices
        for (Device d : devices.values()) {
            if (d.isOn()) {
                new DeviceUsageDAO().startUsage(d.getName());
            }
        }
    }
}