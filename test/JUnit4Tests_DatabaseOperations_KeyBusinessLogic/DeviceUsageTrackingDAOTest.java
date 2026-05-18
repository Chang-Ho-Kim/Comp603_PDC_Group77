package JUnit4Tests_DatabaseOperations_KeyBusinessLogic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import smarthome.dao.DeviceDAO;
import smarthome.dao.DeviceUsageDAO;
import smarthome.dao.DatabaseManager;
import smarthome.model.Light;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * DeviceUsageTrackingDAOTest
 *
 * This test class verifies correct integration between Device model
 * and DeviceUsageDAO for tracking device usage sessions.
 *
 * It ensures:
 * - ON triggers usage start
 * - OFF triggers usage end
 * - Correct timestamp persistence in database
 * - Accurate duration tracking with tolerance
 * - Proper cleanup of test data only
 */

public class DeviceUsageTrackingDAOTest {

    private DeviceDAO deviceDAO;
    private DeviceUsageDAO usageDAO;
    private String testId;

    @Before
    public void setUp() {
        deviceDAO = new DeviceDAO();
        usageDAO = new DeviceUsageDAO();
    }

    @After
    public void tearDown() {

        // cleanup test data after each test
        if (testId != null) {
            deviceDAO.delete(testId);
            usageDAO.deleteByDeviceId(testId);
        }
    }

    @Test
    public void testDeviceOnOffUsageTracking() throws Exception {

        testId = "TEST_" + System.currentTimeMillis();

        Light device = new Light("Usage Test Light");
        device.setId(testId);

        // SAVE DEVICE
        deviceDAO.save(device, testId);

        // START TRACKING (ON)
        device.turnOn();

        long systemStart = System.currentTimeMillis();

        // simulate usage
        Thread.sleep(300);

        // END TRACKING (OFF)
        device.turnOff();

        long systemEnd = System.currentTimeMillis();

        // VERIFY DATABASE RECORD
        try (Connection conn = DatabaseManager.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT start_time, end_time FROM device_usage " +
                    "WHERE device_id = ? ORDER BY start_time DESC"
            );

            ps.setString(1, testId);

            ResultSet rs = ps.executeQuery();

            assertTrue(rs.next());

            Timestamp startTime = rs.getTimestamp("start_time");
            Timestamp endTime = rs.getTimestamp("end_time");

            assertNotNull(startTime);
            assertNotNull(endTime);

            // 1. Ensure correct ordering
            assertTrue(endTime.after(startTime));

            // 2. Calculate durations
            long dbDuration = endTime.getTime() - startTime.getTime();
            long systemDuration = systemEnd - systemStart;

            // 3. Allow tolerance for execution + DB delay
            long tolerance = 1000;

            assertTrue(dbDuration >= systemDuration - tolerance);
            assertTrue(dbDuration <= systemDuration + tolerance);
        }
    }
}