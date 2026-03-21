/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

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
}