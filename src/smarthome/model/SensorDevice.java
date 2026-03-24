/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.time.LocalTime;
import smarthome.controller.IInputHandler;

/**
 * SensorDevice - A device that can react to sensor data (temperature thresholds).
 * Implements IDeviceUIHandler for Open/Closed Principle - UI can interact without knowing concrete type.
 */
public abstract class SensorDevice extends Device implements ISensorable {
    
    private int upper;
    private int lower;
    private boolean sensorOn;

    public SensorDevice(String name) {
        super(name);
    }

    @Override
    public void descheduledAction(){
        this.turnOff();
    }

    @Override
    public void scheduledAction(){
        if(!this.isOn()){
            this.turnOn();
        }
    }

    @Override
    public abstract void checkInThreshold(int currentTemp);

    @Override
    public void updateThreshold(int lower, int upper){
        this.lower = lower;
        this.upper = upper;
    }
    
    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public boolean isSensorOn() {
        return sensorOn;
    }

    public void setSensorOn(boolean sensorOn) {
        this.sensorOn = sensorOn;
    }

    @Override
    public void checkAutomation(int temp, LocalTime time) {
        if(isSensorOn()){
            checkInThreshold(temp);
        }
    }

    @Override
    public boolean isAutoOn() {
        return isSensorOn();
    }
    
    // IDeviceUIHandler implementation
    @Override
    public String getAdditionalMenuContent() {
        StringBuilder content = new StringBuilder();
        if (isSensorOn()) {
            content.append("\nSensor Mode: On");
            content.append("\nSensor range: ").append(lower).append(" - ").append(upper);
        } else {
            content.append("\nSensor: Off");
        }
        return content.toString();
    }
    
    @Override
    public String getAdditionalOptions() {
        return "3. Set sensor lower threshold\n4. Set sensor upper threshold\n5. Set Sensor Mode On/Off\n";
    }
    
    @Override
    public boolean handleDeviceCommand(String command, IInputHandler handler) {
        switch (command) {
            case "3":
                int originalLower = getLower();
                setLower(handler.setTemp());
                if (getUpper() != 0 && getLower() > getUpper()) {
                    setLower(originalLower);
                    return false; // Let controller handle error
                }
                return true;
                
            case "4":
                int originalUpper = getUpper();
                setUpper(handler.setTemp());
                if (getLower() != 0 && getUpper() < getLower()) {
                    setUpper(originalUpper);
                    return false; // Let controller handle error
                }
                return true;
                
            case "5":
                setSensorOn(!isSensorOn());
                return true;
                
            default:
                return false;
        }
    }
}

