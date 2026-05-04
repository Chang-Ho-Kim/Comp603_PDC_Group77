/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import smarthome.controller.IInputHandler;

/**
 *
 * @author rlack
 */

public class Heater extends SensorDevice {
    public Heater(String name){ super(name); this.type = "Heater"; this.electricityUsage = 1500; }
    
    @Override
    public void checkInThreshold(int currentTemp) {
        if (this.getLower() >= currentTemp) {
            scheduledAction();
        } else {
            descheduledAction();
        }
    }
    
    public void setUpper(int upper) {
        super.setUpper(upper);
        super.setLower(upper);
    }
    
    public void setLower(int lower) {
        super.setUpper(lower);
        super.setLower(lower);
    }
    
    @Override
    public String getAdditionalOptions() {
        return "3. Set temperature threshold\n4. Set Sensor Mode On/Off\n";
    }
    
    @Override
    public boolean handleDeviceCommand(String command, IInputHandler handler) {
        switch (command) {

            case "3":
                Integer newLower = handler.setTemp();
                if (newLower == null) return false;

                setLower(newLower);
                return true;

            case "4":
                setSensorOn(!isSensorOn());
                return true;

            default:
                return false;
        }
    }
}