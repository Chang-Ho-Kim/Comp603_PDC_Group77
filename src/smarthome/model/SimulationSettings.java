/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.io.Serializable;


/**
 *
 * @author rlack
 */

public class SimulationSettings implements Serializable {
    private int temperature = 20;
    private double electricityCost = 0.00039;
    private int powerThreshold = 6000;

    public int getPowerThreshold() {
        return powerThreshold;
    }

    public void setPowerThreshold(int powerThreshold) {
        this.powerThreshold = powerThreshold;
    }
    
    
    public double getElectricityCost() {
        return electricityCost;
    }

    public void setElectricityCost(double electricityCost) {
        this.electricityCost = electricityCost;
    }

  
    
    public int getTemperature(){ return temperature; }

    public void setTemperature(int temperature){ this.temperature = temperature; }
}
