package smarthome.service;

import java.util.ArrayList;

/**
 * Service for managing application logs and messages.
 * Separates logging concern from the model layer.
 */
public interface ILoggingService {
    
    /**
     * Add a message to the log.
     * @param message the message to log
     */
    void addMessage(String message);
    
    /**
     * Get all logged messages.
     * @return list of all messages
     */
    ArrayList<String> getMessages();
    
    /**
     * Clear all logged messages.
     */
    void clearMessages();
    
    /**
     * Get the count of logged messages.
     * @return number of messages
     */
    int getMessageCount();
}
