/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.time.LocalTime;

/**
 *
 * @author rlack
 */
public class PowerSaverDevice extends Device implements IPowerSaveable{
    
    
    private static boolean thresholdOver = false;
    private boolean psOn = true;
    
    public PowerSaverDevice(String name) {
        super(name);
    }
    
    public static boolean isThresholdOver() {
        return thresholdOver;
    }

    public static void setThresholdOver(boolean thresholdOver) {
        PowerSaverDevice.thresholdOver = thresholdOver;
    }
    
    
    
     @Override
    public void checkAutomation(int temp, LocalTime time) {
        if(thresholdOver){
            this.turnOff();
        }
    }
    
    @Override
    public boolean isAutoOn() {
        return psOn;
    }

    @Override
    public void turnPsOn() {
        psOn=true;
    }

    @Override
    public void turnPsOff() {
        psOn=false;
    }
    
}
