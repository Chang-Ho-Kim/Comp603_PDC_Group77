package smarthome.model;

import java.time.LocalTime;

public abstract class SensorDevice extends Device implements ISensorable {
    
    private int upper;
    private int lower;
    private boolean sensorOn;

    public SensorDevice(String name) {
        super(name);
    }

    @Override
    public void descheduledAction(){
        this.turnOff();
    }

    @Override
    public void scheduledAction(){
        if(!this.isOn()){
            this.turnOn();
        }
    }

    @Override
    public abstract void checkInThreshold(int currentTemp);

    @Override
    public void updateThreshold(int lower, int upper){
        this.lower = lower;
        this.upper = upper;
    }
    
    public int getUpper() { return upper; }
    public void setUpper(int upper) { this.upper = upper; }

    public int getLower() { return lower; }
    public void setLower(int lower) { this.lower = lower; }

    public boolean isSensorOn() { return sensorOn; }
    public void setSensorOn(boolean sensorOn) { this.sensorOn = sensorOn; }

    @Override
    public void checkAutomation(AutomationContext context) {
        if(isSensorOn()){
            checkInThreshold(context.getCurrentTemperature());
        }
    }

    @Override
    public boolean isAutoOn() {
        return isSensorOn();
    }
}
