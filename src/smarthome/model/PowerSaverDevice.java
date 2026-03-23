/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.time.LocalTime;
import smarthome.service.IThresholdManager;
import smarthome.service.DependencyContainer;

/**
 * PowerSaverDevice - A device that turns off when power threshold is exceeded.
 * Now uses dependency injection for threshold management instead of static fields.
 */
public class PowerSaverDevice extends Device implements IPowerSaveable {
    
    private boolean psOn = true;
    private transient IThresholdManager thresholdManager;
    
    public PowerSaverDevice(String name) {
        super(name);
        this.type = "Power Saver";
        this.electricityUsage = 0;
        this.thresholdManager = DependencyContainer.getInstance().getThresholdManager();
    }
    
    @Override
    public void checkAutomation(int temp, LocalTime time) {
        if (thresholdManager.isThresholdExceeded()) {
            this.turnOff();
        }
    }
    
    @Override
    public boolean isAutoOn() {
        return psOn;
    }

    @Override
    public void turnPsOn() {
        psOn = true;
    }

    @Override
    public void turnPsOff() {
        psOn = false;
    }
}
