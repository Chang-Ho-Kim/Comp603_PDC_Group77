/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package smarthome.model;


import java.time.LocalTime;


/**
 *
 * @author rlack
 */
public interface ISchedulable {
    void updateSchedule(LocalTime start, LocalTime end);
    boolean checkInSchedule(LocalTime currentTime);
    void scheduledAction();
    void descheduledAction();
}
