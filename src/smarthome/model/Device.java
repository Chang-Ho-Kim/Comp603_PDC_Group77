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
import smarthome.service.DependencyContainer;
import smarthome.service.IBillingService;

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

    public boolean isAutoOn() {
        return false;
    }
    
    public List<UsageRecord> getUsageHistory() {
        return usageHistory;
    }
    
    /**
     * @deprecated Use DependencyContainer.getInstance().getBillingService().calculateDeviceBill()
     * Calculate the cost for this device.
     */
    @Deprecated
    public double calculateCost(double rate) {
        IBillingService billingService = DependencyContainer.getInstance().getBillingService();
        return billingService.calculateDeviceBill(this, rate);
    }
}