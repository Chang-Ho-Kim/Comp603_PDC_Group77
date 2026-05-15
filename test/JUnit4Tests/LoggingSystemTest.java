package JUnit4Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import smarthome.controller.CentralController;
import smarthome.controller.LogController;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeGUIView;

import static org.junit.Assert.*;

/**
 * LoggingSystemTest
 *
 * This test verifies the logging functionality of the Smart Home system.
 *
 * The test validates:
 * - Log message insertion through CentralController
 * - Retrieval of log contents through LogController
 * - Proper storage of log entries in logging service
 * - Correct display formatting of application logs
 *
 * The test simulates real logging behaviour while avoiding
 * direct GUI interaction.
 */
public class LoggingSystemTest {

    private SmartHomeSystem system;
    private CentralController controller;
    private SmartHomeGUIView mockView;
    private LogController logController;

    @Before
    public void setUp() {

        system = new SmartHomeSystem();

        // Lightweight GUI stub to prevent NullPointerException
        mockView = new SmartHomeGUIView();

        controller = new CentralController(system, mockView);

        logController = new LogController(
                controller,
                system,
                mockView
        );
    }

    @After
    public void tearDown() {
        // No global teardown required
    }

    /**
     * Tests logging behaviour through CentralController and LogController.
     *
     * This test:
     * - Adds log messages to the logging service
     * - Retrieves logs through LogController
     * - Verifies log messages are correctly stored
     * - Verifies application log header formatting
     * - Verifies multiple log entries persist correctly
     */
    @Test
    public void testLoggingSystemFunctionality() {

        // ADD TEST LOG MESSAGES
        controller.addLogMessage("Device turned ON\n");
        controller.addLogMessage("Temperature changed to 22\n");

        // RETRIEVE LOG OUTPUT
        String logs = logController.getMenuContents();

        // VERIFY LOG HEADER EXISTS
        assertTrue(
                "Log output should contain application log header",
                logs.contains("=== APPLICATION LOG ===")
        );

        // VERIFY FIRST LOG ENTRY EXISTS
        assertTrue(
                "Log output should contain first log entry",
                logs.contains("Device turned ON")
        );

        // VERIFY SECOND LOG ENTRY EXISTS
        assertTrue(
                "Log output should contain second log entry",
                logs.contains("Temperature changed to 22")
        );
    }
}