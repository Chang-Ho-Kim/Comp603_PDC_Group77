package smarthome.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Service for formatting dates and times for display.
 * Centralizes formatting logic to avoid duplication.
 */
public interface IFormatterService {
    
    /**
     * Format LocalDateTime to string.
     * @param dateTime the datetime to format
     * @return formatted datetime string
     */
    String formatDateTime(LocalDateTime dateTime);
    
    /**
     * Format LocalTime to string.
     * @param time the time to format
     * @return formatted time string
     */
    String formatTime(LocalTime time);
    
    /**
     * Format a decimal number for currency display.
     * @param value the value to format
     * @return formatted currency string
     */
    String formatCurrency(double value);
}
