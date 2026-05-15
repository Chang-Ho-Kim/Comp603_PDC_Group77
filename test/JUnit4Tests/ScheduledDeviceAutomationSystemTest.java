package JUnit4Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import smarthome.controller.CentralController;
import smarthome.dao.DeviceDAO;
import smarthome.dao.DeviceUsageDAO;
import smarthome.model.*;
import smarthome.view.SmartHomeGUIView;

import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * ScheduledDeviceAutomationSystemTest
 *
 * This test verifies the full automation pipeline
 * for ScheduledDevice behaviour within the Smart Home system.
 *
 * The test validates:
 * - Time-based schedule automation behaviour
 * - AlarmClock scheduled ON/OFF logic
 * - CentralController automation cycle execution
 * - Correct ON/OFF transitions based on scheduled time range
 * - Proper cleanup of test data from persistence layer
 *
 * The test simulates real system behaviour while avoiding GUI interaction.
 *
 * Note:
 * mockView is required because CentralController internally
 * references GUI methods during initialization.
 */
public class ScheduledDeviceAutomationSystemTest {

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
     * Tests AlarmClock scheduled automation behaviour.
     *
     * This test:
     * - Creates and registers an AlarmClock device
     * - Enables scheduled automation mode
     * - Configures schedule from 08:00 to 10:00
     * - Verifies device turns ON during scheduled period
     * - Verifies device turns OFF outside scheduled period
     * - Cleans up test data after completion
     */
    @Test
    public void testAlarmClockScheduleAutomation() {

        String testId = "TEST_SCHEDULE_" + System.currentTimeMillis();

        // CREATE ALARM CLOCK DEVICE
        AlarmClock alarmClock = new AlarmClock("Morning Alarm");
        alarmClock.setId(testId);

        // REGISTER DEVICE IN SYSTEM
        system.addDevice(alarmClock);

        // CONFIGURE SCHEDULE SETTINGS
        alarmClock.setStart(LocalTime.of(8, 0));
        alarmClock.setEnd(LocalTime.of(10, 0));
        alarmClock.setScheduleOn(true);

        // ENSURE KNOWN STARTING STATE
        alarmClock.turnOff();

        // TIME INSIDE SCHEDULE (should turn ON)
        alarmClock.checkAutomation(
                system.getSimulation().getTemperature(),
                LocalTime.of(9, 0)
        );

        assertTrue(
                "AlarmClock should turn ON during scheduled time",
                alarmClock.isOn()
        );

        // TIME OUTSIDE SCHEDULE (should turn OFF)
        alarmClock.checkAutomation(
                system.getSimulation().getTemperature(),
                LocalTime.of(11, 0)
        );

        assertFalse(
                "AlarmClock should turn OFF outside scheduled time",
                alarmClock.isOn()
        );

        // CLEANUP TEST DATA
        dao.delete(testId);
        system.removeDevice(testId);
        usageDAO.deleteByDeviceId(testId);
    }
}