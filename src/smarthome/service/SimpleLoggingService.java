package smarthome.service;

import java.util.ArrayList;

/**
 * Simple implementation of ILoggingService.
 * Responsible for managing application logs and messages.
 */
public class SimpleLoggingService implements ILoggingService {
    
    private ArrayList<String> messages = new ArrayList<>();
    
    @Override
    public void addMessage(String message) {
        messages.add(message);
    }
    
    @Override
    public ArrayList<String> getMessages() {
        return new ArrayList<>(messages);
    }
    
    @Override
    public void clearMessages() {
        messages.clear();
    }
    
    @Override
    public int getMessageCount() {
        return messages.size();
    }
}
