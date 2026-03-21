/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

/**
 *
 * @author rlack
 */

public class Light extends Device {
    public Light(String name){ super(name); this.type = "Light"; this.electricityUsage = 12;}
}