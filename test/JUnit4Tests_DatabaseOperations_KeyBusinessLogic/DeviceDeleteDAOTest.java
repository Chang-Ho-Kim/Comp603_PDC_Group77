package JUnit4Tests_DatabaseOperations_KeyBusinessLogic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import smarthome.dao.DeviceDAO;
import smarthome.model.Device;
import smarthome.model.Light;

/**
 * DeviceDeleteDAOTest
 *
 * This test class verifies the correct delete behavior
 * of devices within the DeviceDAO layer.
 *
 * The tests validate:
 * - Successful insertion of a device before deletion
 * - Correct removal of a device from the database
 * - Accurate retrieval state after deletion
 * - Proper cleanup of database test data
 */

public class DeviceDeleteDAOTest {

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
     * Tests deleting a device from the database via DeviceDAO.
     *
     * This test:
     * - Creates and saves a Light device using a unique test ID
     * - Confirms the device exists in the database
     * - Deletes the device using DeviceDAO
     * - Verifies that the device is no longer present in the database
     *
     * This ensures that DeviceDAO correctly removes
     * persisted Device records from the Derby database.
     */

    @Test
    public void testDeleteDeviceFromDatabase() {

        testId = "TEST_DELETE_" + System.currentTimeMillis();

        Light light = new Light("Delete Test Light");

        // INSERT DEVICE
        dao.save(light, testId);

        // VERIFY EXISTS
        Device inserted = dao.getAll().stream()
                .filter(d -> testId.equals(d.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(inserted);

        // DELETE DEVICE
        dao.delete(testId);

        // VERIFY DELETED
        Device deleted = dao.getAll().stream()
                .filter(d -> testId.equals(d.getId()))
                .findFirst()
                .orElse(null);

        assertNull(deleted);
    }
}