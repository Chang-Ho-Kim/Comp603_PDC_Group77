package smarthome.model;

public class AirCon extends SensorDevice {
    public AirCon(String name){ 
        super(name); 
        this.type = "Air Conditioner"; 
        this.electricityUsage = 2000;
    }

    @Override
    public void checkInThreshold(int currentTemp) {
        // Turn on if temperature is above or equal to the upper threshold
        if (currentTemp >= this.getUpper()) {
            scheduledAction();
        } else if (currentTemp <= this.getLower()) {
            // Turn off if temperature is below or equal to the lower threshold
            descheduledAction();
        }
    }
}
