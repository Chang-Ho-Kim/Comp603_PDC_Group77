package JUnit4Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import smarthome.dao.DeviceDAO;
import smarthome.model.Device;
import smarthome.model.Heater;

/**
 * SensorDeviceUpdateDAOTest
 *
 * This test class verifies the correct update behavior
 * of Sensor devices (using Heater device for test) within the DeviceDAO layer.
 *
 * The tests validate:
 * - Persistence of Heater threshold values
 * - Persistence of electricity usage values
 * - Correct retrieval of updated Heater devices
 * - Proper cleanup of database test data
 */

public class SensorDeviceUpdateDAOTest {

    private DeviceDAO dao;

    @Before
    public void setUp() {
        dao = new DeviceDAO();
    }

    @After
    public void tearDown() {
        // nothing global to clean up
    }

    /**
     * Tests updating Heater threshold values and electricity usage
     * in the database via DeviceDAO.
     *
     * This test:
     * - Creates and saves a Heater device
     * - Updates the Heater threshold value to 5
     * - Updates the Heater electricity usage to 67
     * - Saves the updated Heater using the same device ID
     * - Retrieves the updated Heater from the database
     * - Verifies that the updated threshold values persist correctly
     * - Verifies that the updated electricity usage persists correctly
     * - Removes the test data after completion
     *
     * This ensures that DeviceDAO correctly updates
     * SensorDevice-specific and inherited Device attributes.
     */

    @Test
    public void testUpdateHeaterThresholdAndElectricityUsage() {

        String testId = "TEST_HEATER_" + System.currentTimeMillis();

        Heater heater = new Heater("Bedroom Heater");

        // INITIAL SAVE
        dao.save(heater, testId);

        // UPDATE VALUES
        heater.setLower(5);
        heater.setElectricityUsage(67);

        // SAVE UPDATED DEVICE
        dao.save(heater, testId);

        // RETRIEVE UPDATED DEVICE
        Device retrieved = dao.getAll().stream()
                .filter(d -> testId.equals(d.getId()))
                .findFirst()
                .orElse(null);

        // ASSERT: device exists
        assertNotNull(retrieved);

        // ASSERT: correct type restored
        assertTrue(retrieved instanceof Heater);

        Heater updatedHeater = (Heater) retrieved;

        // ASSERT: threshold values updated
        assertEquals(5, updatedHeater.getLower());
        assertEquals(5, updatedHeater.getUpper());

        // ASSERT: electricity usage updated
        assertEquals(67, updatedHeater.getElectricityUsage());

        // CLEANUP
        dao.delete(testId);
    }
}