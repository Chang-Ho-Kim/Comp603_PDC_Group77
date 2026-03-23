package smarthome.model;

import java.time.LocalTime;

public class AutomationContext {
    private final LocalTime currentTime;
    private final int currentTemperature;
    private final boolean isPowerSavingMode;

    public AutomationContext(LocalTime currentTime, int currentTemperature, boolean isPowerSavingMode) {
        this.currentTime = currentTime;
        this.currentTemperature = currentTemperature;
        this.isPowerSavingMode = isPowerSavingMode;
    }

    public LocalTime getCurrentTime() {
        return currentTime;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public boolean isPowerSavingMode() {
        return isPowerSavingMode;
    }
}
