package smarthome.service;

/**
 * Simple implementation of IThresholdManager.
 * Replaces static field in PowerSaverDevice.
 * Responsible for managing power threshold state.
 */
public class SimpleThresholdManager implements IThresholdManager {
    
    private boolean thresholdExceeded = false;
    private int threshold = 6000;
    
    @Override
    public boolean isThresholdExceeded() {
        return thresholdExceeded;
    }
    
    @Override
    public void setThresholdExceeded(boolean exceeded) {
        this.thresholdExceeded = exceeded;
    }
    
    @Override
    public int getThreshold() {
        return threshold;
    }
    
    @Override
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
