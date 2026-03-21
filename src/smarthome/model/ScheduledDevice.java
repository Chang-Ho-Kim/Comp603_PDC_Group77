/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 *
 * @author rlack
 */
public abstract class ScheduledDevice extends Device implements ISchedulable{
    
    private LocalTime start;
    private LocalTime end;
    private boolean scheduleOn;
    
    
    public ScheduledDevice(String name) {
        super(name);
        start = LocalTime.of(0, 0, 0);
        end = LocalTime.of(0, 0, 0);
    }

    @Override
    public void updateSchedule(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

   @Override
    public boolean checkInSchedule(LocalTime currentTime) {
        boolean wasOn = this.isOn();

        if (currentTime.isAfter(start) && currentTime.isBefore(end)) {
            this.turnOn();
        } else {
            this.turnOff();
        }

        return wasOn != this.isOn(); // tells controller if UI update needed
    }

    @Override
    public void scheduledAction() {
       
    }
    
    @Override
    public void descheduledAction() {
        this.turnOff();
        //this.setScheduleOn(false);
    }
    
    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public boolean isScheduleOn() {
        return scheduleOn;
    }

    public void setScheduleOn(boolean scheduleOn) {
        this.scheduleOn = scheduleOn;
    }

    @Override
    public void checkAutomation(int temp, LocalTime time) {
        if(isScheduleOn()){
            checkInSchedule(time);
        }
       
    }

    @Override
    public boolean isAutoOn() {
        return isScheduleOn();
    }
    
   
    
}
