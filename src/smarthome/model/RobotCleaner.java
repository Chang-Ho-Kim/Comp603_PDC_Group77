/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

/**
 *
 * @author rlack
 */

public class RobotCleaner extends ScheduledDevice {
    public RobotCleaner(String name){ super(name); this.type = "Robot Cleaner"; this.electricityUsage = 100;}
}