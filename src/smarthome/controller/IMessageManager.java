package smarthome.controller;

/**
 * Interface for managing UI messages.
 * Segregated from other controller concerns - only exposes message-related methods.
 */
public interface IMessageManager {
    void setCurrentMessage(String message);
    String getCurrentMessage();
    void addLogMessage(String message);
}
