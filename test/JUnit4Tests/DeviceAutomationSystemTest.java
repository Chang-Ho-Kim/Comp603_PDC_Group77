package JUnit4Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import smarthome.controller.CentralController;
import smarthome.dao.DeviceDAO;
import smarthome.model.*;
import smarthome.view.SmartHomeGUIView;

import static org.junit.Assert.*;
import smarthome.dao.DeviceUsageDAO;

/**
 * DeviceAutomationSystemTest
 *
 * This test verifies the full automation pipeline of the Smart Home system.
 *
 * The test validates:
 * - Simulation temperature changes affecting automation logic
 * - Heater sensor-based automation behaviour
 * - CentralController automation cycle execution
 * - Correct ON/OFF transitions based on temperature thresholds
 * - Proper cleanup of test data from persistence layer
 *
 * The test simulates real system behaviour while avoiding GUI interaction.
 * Note: mockView had to be used as the test uses the system's automation behavior (instead of directly using functions) and view cannot be null
 *       The test actually has nothing to do with the view GUI. 
 */
public class DeviceAutomationSystemTest {

    private DeviceDAO dao;
    private DeviceUsageDAO usageDAO;
    private SmartHomeSystem system;
    private CentralController controller;
    private SmartHomeGUIView mockView;

    @Before
    public void setUp() {
        dao = new DeviceDAO();
        usageDAO = new DeviceUsageDAO();

        system = new SmartHomeSystem();

        // Lightweight GUI stub to prevent NullPointerException
        mockView = new SmartHomeGUIView();

        controller = new CentralController(system, mockView);
    }

    @After
    public void tearDown() {
        // No global teardown required
    }

    /**
     * Tests Heater automation behaviour through CentralController.
     *
     * This test:
     * - Creates and registers a Heater device in the system
     * - Enables sensor-based automation mode
     * - Sets lower temperature threshold to 5
     * - Verifies heater remains OFF at high temperature (30)
     * - Verifies heater turns ON at low temperature (2)
     * - Verifies heater turns OFF again at mid temperature (20)
     * - Cleans up test device from database and system
     */
    @Test
    public void testHeaterAutomationThroughCentralController() {

        String testId = "TEST_AUTO_" + System.currentTimeMillis();

        // CREATE HEATER DEVICE
        Heater heater = new Heater("Automation Heater");
        heater.setId(testId);

        // REGISTER DEVICE IN SYSTEM
        system.addDevice(heater);

        // CONFIGURE AUTOMATION SETTINGS
        heater.setLower(5);
        heater.setSensorOn(true);

        // ENSURE KNOWN STARTING STATE
        heater.turnOff();

        // HIGH TEMPERATURE SCENARIO (should remain OFF)
        system.getSimulation().setTemperature(30);
        controller.checkAutomation();

        assertFalse(heater.isOn());

        // LOW TEMPERATURE SCENARIO (should turn ON)
        system.getSimulation().setTemperature(2);
        controller.checkAutomation();

        assertTrue(heater.isOn());

        // MID TEMPERATURE SCENARIO (should turn OFF again)
        system.getSimulation().setTemperature(20);
        controller.checkAutomation();

        assertFalse(heater.isOn());

        // CLEANUP TEST DATA
        dao.delete(testId);
        system.removeDevice(testId);
        usageDAO.deleteByDeviceId(testId);
    }
}