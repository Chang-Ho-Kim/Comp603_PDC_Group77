package smarthome.service;

/**
 * Service for managing power threshold state.
 * Replaces static fields in PowerSaverDevice to improve testability and thread safety.
 */
public interface IThresholdManager {
    
    /**
     * Check if the total electricity usage exceeds the threshold.
     * @return true if threshold is exceeded, false otherwise
     */
    boolean isThresholdExceeded();
    
    /**
     * Set whether the threshold is exceeded.
     * @param exceeded true if threshold is exceeded
     */
    void setThresholdExceeded(boolean exceeded);
    
    /**
     * Get the current power threshold value.
     * @return the power threshold in watts
     */
    int getThreshold();
    
    /**
     * Set the power threshold.
     * @param threshold the new power threshold in watts
     */
    void setThreshold(int threshold);
}
