package JUnit4Tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import smarthome.dao.DeviceDAO;
import smarthome.model.Device;
import smarthome.model.Light;

import java.util.List;

/**
 * DeviceDAOTest
 *
 * This test class verifies the correct behavior of the DeviceDAO layer,
 * specifically focusing on database persistence for device objects.
 *
 * It ensures that devices can be successfully added to the database,
 * correctly retrieved, and properly cleaned up after testing.
 *
 * The tests validate:
 * - Database insertion of Device objects (Light)
 * - Correct persistence of device attributes (name and type)
 * - Accurate retrieval of stored devices using getAll()
 * - Proper change in database state after insertion
 * - Cleanup of test data to maintain database consistency
 */

public class DeviceDAOTest {

    private DeviceDAO dao;
    private String testId;

    @Before
    public void setUp() {
        dao = new DeviceDAO();
    }

    @After
    public void tearDown() {

        // cleanup test data after each test
        if (testId != null) {
            dao.delete(testId);
        }
    }

    
    
 /**
 * Tests adding a Light device to the database via DeviceDAO.
 *
 * This test:
 * - Records the initial number of devices in the database
 * - Saves a new Light device using a unique test ID
 * - Retrieves all devices from the database after insertion
 * - Verifies that the number of devices has increased by one
 * - Confirms that the inserted device exists and has correct attributes
 *   (name and type)
 * - Removes the test data to avoid affecting other tests
 *
 * This ensures that the DeviceDAO save and retrieval operations
 * work correctly with persistent storage (Derby database).
 */
    
    @Test
    public void testAddLightDeviceToDatabase() {

        testId = "TEST_" + System.currentTimeMillis();
        Light light = new Light("Test Bedroom Light");

        // BEFORE state
        List<Device> before = dao.getAll();

        // ACTION
        dao.save(light, testId);

        // AFTER state
        List<Device> after = dao.getAll();

        // ASSERT: size increased by 1
        assertEquals(before.size() + 1, after.size());

        // ASSERT: correct object exists
        Device inserted = after.stream()
                .filter(d -> testId.equals(d.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(inserted);
        assertEquals("Test Bedroom Light", inserted.getName());
        assertEquals("Light", inserted.getType());
    }
}