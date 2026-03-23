/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import smarthome.service.*;

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
    
    // Cached services for backward compatibility (transient - not serialized)
    private transient IBillingService billingService;
    private transient ILoggingService loggingService;
    
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
    
    // ===== Backward compatibility methods (delegate to services) =====
    // These methods are kept for backward compatibility with existing controllers
    
    /**
     * @deprecated Use DependencyContainer.getInstance().getBillingService().calculateTotalElectricityUsage()
     */
    @Deprecated
    public int calculateTotalElectricityUsage() {
        if (billingService == null) {
            billingService = DependencyContainer.getInstance().getBillingService();
        }
        return billingService.calculateTotalElectricityUsage(devices.values());
    }
    
    /**
     * @deprecated Use calculateTotalElectricityUsage() instead
     */
    @Deprecated
    public int getTotalElectricityUsage() {
        return calculateTotalElectricityUsage();
    }
    
    /**
     * @deprecated Use DependencyContainer.getInstance().getBillingService().calculateTotalBill()
     */
    @Deprecated
    public double getElectricityBill(double rate) {
        if (billingService == null) {
            billingService = DependencyContainer.getInstance().getBillingService();
        }
        return billingService.calculateTotalBill(devices.values(), rate);
    }
    
    /**
     * @deprecated Use simulation.getElectricityCost()
     */
    @Deprecated
    public double getECost() {
        return simulation.getElectricityCost();
    }
    
    /**
     * @deprecated Use DependencyContainer.getInstance().getLoggingService()
     */
    @Deprecated
    public void addMessage(String log) {
        if (loggingService == null) {
            loggingService = DependencyContainer.getInstance().getLoggingService();
        }
        loggingService.addMessage(log);
    }
    
    /**
     * @deprecated Use DependencyContainer.getInstance().getLoggingService().getMessages()
     */
    @Deprecated
    public java.util.ArrayList<String> getMessages() {
        if (loggingService == null) {
            loggingService = DependencyContainer.getInstance().getLoggingService();
        }
        return loggingService.getMessages();
    }
    
    /**
     * @deprecated Use DependencyContainer.getInstance().getLoggingService().clearMessages()
     */
    @Deprecated
    public void deleteLog() {
        if (loggingService == null) {
            loggingService = DependencyContainer.getInstance().getLoggingService();
        }
        loggingService.clearMessages();
    }
}
