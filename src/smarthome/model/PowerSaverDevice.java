/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.time.LocalTime;
import smarthome.service.IThresholdManager;
import smarthome.service.DependencyContainer;
import smarthome.controller.IInputHandler;

/**
 * PowerSaverDevice - A device that turns off when power threshold is exceeded.
 * Now uses dependency injection for threshold management instead of static fields.
 * Implements IDeviceUIHandler for Open/Closed Principle.
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
        if (thresholdManager == null) {
            thresholdManager = DependencyContainer.getInstance().getThresholdManager();
        }

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
    
    // IDeviceUIHandler implementation
    @Override
    public String getAdditionalMenuContent() {
        return "\n\nNote: This powersaver device will turn off if " +
               "\n      total electricity usage exceeds Threshold";
    }
    
    @Override
    public String getAdditionalOptions() {
        return ""; // PowerSaver has no additional options
    }
    
    @Override
    public boolean handleDeviceCommand(String command, IInputHandler handler) {
        // PowerSaver doesn't handle any device-specific commands
        return false;
    }
}

