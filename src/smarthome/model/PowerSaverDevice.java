package smarthome.model;

import java.time.LocalTime;

public class PowerSaverDevice extends Device implements IPowerSaveable {
    
    private boolean psOn = true;
    
    public PowerSaverDevice(String name) {
        super(name);
        this.type = "Power Saver";
    }
    
    @Override
    public void checkAutomation(AutomationContext context) {
        if (psOn && context.isPowerSavingMode()) {
            this.turnOff();
        }
    }
    
    @Override
    public boolean isAutoOn() {
        return psOn;
    }

    @Override
    public void turnPsOn() {
        psOn = true;
    }

    @Override
    public void turnPsOff() {
        psOn = false;
    }
}
