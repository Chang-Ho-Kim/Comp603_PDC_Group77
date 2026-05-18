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

public class AirCon extends SensorDevice {
    public AirCon(String name){ super(name); this.type = "Air Conditioner"; this.electricityUsage = 2000;}
    
  

   @Override
    public void checkInThreshold(int currentTemp) {
        if (this.getLower() <= currentTemp) {
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
        return "4. Set temperature threshold\n5. Set Sensor Mode On/Off\n";
    }
    
    @Override
    public boolean handleDeviceCommand(String command, IInputHandler handler) {
        switch (command) {

            case "4":
                Integer newUpper = handler.setTemp();
                if (newUpper == null) return false;

                setUpper(newUpper);
                return true;

            case "5":
                setSensorOn(!isSensorOn());
                return true;

            default:
                return false;
        }
    }
}