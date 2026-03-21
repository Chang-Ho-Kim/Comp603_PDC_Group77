/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rlack
 */

public abstract class Device implements Serializable, ISwitchable {
    protected String name;
    protected boolean isOn;
    protected String type;
    protected int electricityUsage;
    private List<UsageRecord> usageHistory = new ArrayList<>();
    private UsageRecord currentUsage;
    
    public Device(String name){ this.name = name; }

    @Override
    public void turnOn(){ if (!isOn) {
        isOn = true;

        currentUsage = new UsageRecord(LocalDateTime.now());
        usageHistory.add(currentUsage);
    } }

    @Override
    public void turnOff(){ if (isOn) {
        isOn = false;

        if (currentUsage != null) {
            currentUsage.endRecord(LocalDateTime.now());
        }
    } }

    @Override
    public boolean isOn(){ return isOn; }

    public String getName(){ return name; }
    
    public String getType(){ return type; }

    public int getElectricityUsage() {
        return electricityUsage;
    }

    public void setElectricityUsage(int electricityUsage) {
        this.electricityUsage = electricityUsage;
    }
    
    public void checkAutomation(int temp, LocalTime time){
        
    }

    public boolean isAutoOn(){
        return false;
    }
    
    public List<UsageRecord> getUsageHistory() {
        return usageHistory;
    }
    
    public double calculateCost(double rate) {
        double totalCost = 0;

        for (UsageRecord r : usageHistory) {

            if (r.getStart() == null) continue;

            LocalDateTime endTime = (r.getEnd() == null)
                    ? LocalDateTime.now()
                    : r.getEnd();

            long seconds = Math.max(0,
                    Duration.between(r.getStart(), endTime).getSeconds());

            double hours = seconds / 3600.0;

            double powerWatts = getElectricityUsage(); // watts
            totalCost += powerWatts * hours * rate;
        }

        return totalCost;
    }
}