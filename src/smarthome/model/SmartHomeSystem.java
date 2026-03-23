package smarthome.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SmartHomeSystem implements Serializable {

    private final HashMap<String, Device> devices;
    private final SimulationSettings simulation;
    private ArrayList<String> messages;
    private int totalElectricityUsage;
    private boolean isPowerSavingMode;
    
    public SmartHomeSystem(){
        devices = new HashMap<>();
        simulation = new SimulationSettings();
        messages = new ArrayList<>();
        totalElectricityUsage = 0;
        isPowerSavingMode = false;
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
        messages = new ArrayList<>();
    }

    public int getTotalElectricityUsage() {
        return totalElectricityUsage;
    }
    
     public void calculateTotalElectricityUsage() {
        int i = 0;
        for(Device d : devices.values()){
            if(d.isOn()){
                i += d.getElectricityUsage();
            }
        }
        totalElectricityUsage = i;
        isPowerSavingMode = totalElectricityUsage > simulation.getPowerThreshold();
    }

    public boolean isPowerSavingMode() {
        return isPowerSavingMode;
    }

    public double getECost(){
        return simulation.getElectricityCost();
    }
}
