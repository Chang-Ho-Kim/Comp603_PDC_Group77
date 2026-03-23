package smarthome.controller;

import java.time.LocalTime;

/**
 * Interface for handling user input requests.
 * Segregated from other controller concerns - only exposes input-related methods.
 */
public interface IInputHandler {
    String setDeviceProcedure();
    LocalTime setTime();
    int setTemp();
    void checkAutomation();
}
