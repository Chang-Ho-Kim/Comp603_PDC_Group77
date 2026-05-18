package JUnit4Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import smarthome.controller.CentralController;
import smarthome.dao.DeviceDAO;
import smarthome.dao.DeviceUsageDAO;
import smarthome.model.*;
import smarthome.service.DependencyContainer;
import smarthome.service.IThresholdManager;
import smarthome.service.SimpleThresholdManager;
import smarthome.view.SmartHomeGUIView;

import static org.junit.Assert.*;

/**
 * PowerSaverDeviceAutomationSystemTest
 *
 * This test verifies the full PowerSaver automation pipeline:
 *
 * - CentralController triggers automation cycle
 * - ThresholdManager determines overload state
 * - PowerSaverDevice reacts to threshold changes
 * - Device turns OFF when system is overloaded
 * - Device remains ON when system is within safe limits
 *
 * The test uses the real SimpleThresholdManager implementation
 * to ensure correct integration with the system architecture.
 */
public class PowerSaverDeviceAutomationSystemTest {

    private DeviceDAO dao;
    private DeviceUsageDAO usageDAO;
    private SmartHomeSystem system;
    private CentralController controller;
    private SmartHomeGUIView mockView;

    private SimpleThresholdManager thresholdManager;
    private String testId;

    @Before
    public void setUp() {

        dao = new DeviceDAO();
        usageDAO = new DeviceUsageDAO();

        system = new SmartHomeSystem();

        // GUI not relevant for automation test
        mockView = new SmartHomeGUIView();

        controller = new CentralController(system, mockView);

        // Get REAL threshold manager from dependency container
        thresholdManager = (SimpleThresholdManager)
                DependencyContainer.getInstance().getThresholdManager();
    }

    @After
    public void tearDown() {

        // cleanup test data after each test
        if (testId != null) {
            system.removeDevice(testId);
            dao.delete(testId);
            usageDAO.deleteByDeviceId(testId);
        }
    }

    @Test
    public void testPowerSaverAutomationThroughCentralController() {

        testId = "TEST_PS_" + System.currentTimeMillis();

        // CREATE POWER SAVER DEVICE (TV extends PowerSaverDevice)
        TV tv = new TV("Automation TV");
        tv.setId(testId);

        system.addDevice(tv);

        // START IN ON STATE
        tv.turnOn();

        // =========================
        // STEP 1: NO THRESHOLD BREACH
        // =========================
        thresholdManager.setThresholdExceeded(false);

        controller.checkAutomation();

        assertTrue(
                "TV should stay ON when threshold is NOT exceeded",
                tv.isOn()
        );

        // =========================
        // STEP 2: THRESHOLD BREACH
        // =========================
        thresholdManager.setThresholdExceeded(true);

        controller.checkAutomation();

        assertFalse(
                "TV should turn OFF when power threshold is exceeded",
                tv.isOn()
        );
    }
}