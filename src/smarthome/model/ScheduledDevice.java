/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.model;

import java.time.LocalTime;
import smarthome.controller.IInputHandler;

/**
 * ScheduledDevice - A device that can be scheduled to turn on/off at specific times.
 * Implements IDeviceUIHandler for Open/Closed Principle - UI can interact without knowing concrete type.
 */
public abstract class ScheduledDevice extends Device implements ISchedulable {
    
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
    
    // IDeviceUIHandler implementation
    @Override
    public String getAdditionalMenuContent() {
        StringBuilder content = new StringBuilder();
        if (isScheduleOn()) {
            content.append("\nSchedule Mode: On");
            content.append("\nSchedule: ").append(start).append(" - ").append(end);
        } else {
            content.append("\nSchedule: Off");
        }
        return content.toString();
    }
    
    @Override
    public String getAdditionalOptions() {
        return "3. Set scheduled start time\n4. Set scheduled end time\n5. Set Schedule Mode On/Off\n";
    }
    
    @Override
    public boolean handleDeviceCommand(String command, IInputHandler handler) {
        switch (command) {
            case "3":
                LocalTime original = getStart();
                setStart(handler.setTime());
                if(!getEnd().equals(LocalTime.of(0, 0, 0)) && getStart().isAfter(getEnd())) {
                    setStart(original);
                    return false; // Let controller handle error
                }
                return true;
                
            case "4":
                LocalTime originalEnd = getEnd();
                setEnd(handler.setTime());
                if(!start.equals(LocalTime.of(0, 0, 0)) && getEnd().isBefore(getStart())) {
                    setEnd(originalEnd);
                    return false; // Let controller handle error
                }
                return true;
                
            case "5":
                setScheduleOn(!isScheduleOn());
                return true;
                
            default:
                return false;
        }
    }
}

