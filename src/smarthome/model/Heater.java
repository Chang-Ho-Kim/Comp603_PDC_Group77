package smarthome.model;

public class Heater extends SensorDevice {
    public Heater(String name){ 
        super(name); 
        this.type = "Heater"; 
        this.electricityUsage = 1500; 
    }
    
    @Override
    public void checkInThreshold(int currentTemp) {
        // Turn on if temperature is below or equal to the lower threshold
        if (currentTemp <= this.getLower()) {
            scheduledAction();
        } else if (currentTemp >= this.getUpper()) {
            // Turn off if temperature is above or equal to the upper threshold
            descheduledAction();
        }
    }
}
