package smarthome.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;

/**
 * Simple implementation of IFormatterService.
 * Responsible for all formatting concerns.
 */
public class SimpleFormatterService implements IFormatterService {
    
    private final DateTimeFormatter dateTimeFormatter;
    private final DateTimeFormatter timeFormatter;
    private final DecimalFormat currencyFormatter;
    
    public SimpleFormatterService() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.currencyFormatter = new DecimalFormat("0.000000000");
    }
    
    @Override
    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }
    
    @Override
    public String formatTime(LocalTime time) {
        return time.format(timeFormatter);
    }
    
    @Override
    public String formatCurrency(double value) {
        return currencyFormatter.format(value);
    }
}
