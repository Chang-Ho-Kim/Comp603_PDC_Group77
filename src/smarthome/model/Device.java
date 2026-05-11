package smarthome.model;

import java.time.LocalTime;
import java.time.LocalDateTime;
import smarthome.dao.DeviceUsageDAO;

public abstract class Device implements ISwitchable, IDeviceUIHandler {

    protected String name;
    protected boolean isOn;
    protected String type;
    protected int electricityUsage;
    protected String id;
    
    private static final DeviceUsageDAO usageDAO = new DeviceUsageDAO();

    public Device(String name) {
        this.name = name;
    }
    
    public Device(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
    this.id = id;
}
    
    @Override
    public void turnOn() {
        if (!isOn) {
            isOn = true;
            usageDAO.startUsage(name);
        }
    }

    @Override
    public void turnOff() {
        if (isOn) {
            isOn = false;
            usageDAO.endUsage(name);
        }
    }

    @Override
    public boolean isOn() {
        return isOn;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getElectricityUsage() {
        return electricityUsage;
    }

    public void setElectricityUsage(int electricityUsage) {
        this.electricityUsage = electricityUsage;
    }

    public void checkAutomation(int temp, LocalTime time) {
        // default
    }

    public boolean isAutoOn() {
        return false;
    }

    @Override
    public String getAdditionalMenuContent() {
        return "";
    }

    @Override
    public String getAdditionalOptions() {
        return "";
    }

    @Override
    public boolean handleDeviceCommand(String command, smarthome.controller.IInputHandler handler) {
        return false;
    }

    /**
     * IMPORTANT FIX:
     * restore ON state must also re-open usage tracking in DB
     */
    public void restoreState(boolean on) {
        this.isOn = on;

        if (on) {
            usageDAO.startUsage(name);
        }
    }
}