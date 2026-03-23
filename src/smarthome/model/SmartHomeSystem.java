/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

/**
 *
 * @author rlack
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.time.Duration;
import java.time.LocalDateTime;

public class SmartHomeSystem implements Serializable {

    private HashMap<String, Device> devices;
    private SimulationSettings simulation;
    private ArrayList<String> messages;
    private int totalElectricityUsage;
    private double electricityBill;
    
    public SmartHomeSystem(){
        devices = new HashMap<>();
        simulation = new SimulationSettings();
        messages = new ArrayList();
        totalElectricityUsage = 0;
    }

    public Collection<Device> getAllDevices(){ return devices.values(); }
    
    public Collection<String> getDeviceNames(){ return devices.keySet(); }

    public Device getDevice(String id){ return devices.get(id); }

    public SimulationSettings getSimulation(){ return simulation; }
    
    public void addDevice(Device device){
        devices.put(device.name, device);
    }
    
    public void removeDevice(String name){
        devices.remove(name);
    }
    
    public void addMessage(String log){
        messages.add(log);
    }
    
    public ArrayList<String> getMessages(){
        return messages;
    }
    
    public void deleteLog(){
        messages = new ArrayList();
    }

    public int getTotalElectricityUsage() {
        return totalElectricityUsage;
    }
    
     public void calculateTotalElectricityUsage() {
        int i = 0;
        for(Device d : devices.values()){
            if(d.isOn()){
                i+= d.getElectricityUsage();
            }
        }
        totalElectricityUsage = i;
        
        if(totalElectricityUsage > simulation.getPowerThreshold()){
            PowerSaverDevice.setThresholdOver(true);
        }else{
            PowerSaverDevice.setThresholdOver(false);
        }
    }
    


public double getElectricityBill(double rate) {
    double totalCost = 0;

    for (Device d : devices.values()) {
        
            totalCost += d.calculateCost(rate);
        }
    return totalCost;
}

public double getECost(){
    return simulation.getElectricityCost();
}
    
}