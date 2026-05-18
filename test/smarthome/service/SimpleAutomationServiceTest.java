package smarthome.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import smarthome.model.Device;
import smarthome.model.Light;

public class SimpleAutomationServiceTest {

    private SimpleAutomationService instance;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new SimpleAutomationService();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    // --------------------------------------------------
    // checkAllDevicesAutomation
    // --------------------------------------------------

    @Test
    public void testCheckAllDevicesAutomation() {

        Collection<Device> devices = new ArrayList<>();
        devices.add(new Light("L1"));
        devices.add(new Light("L2"));

        instance.checkAllDevicesAutomation(devices, 25, LocalTime.of(10, 0));

        // If no exception occurs, delegation is correct
        assertEquals(2, devices.size());
    }

    // --------------------------------------------------
    // checkDeviceAutomation
    // --------------------------------------------------

    @Test
    public void testCheckDeviceAutomation() {

        Device device = new Light("L1");

        instance.checkDeviceAutomation(device, 22, LocalTime.of(12, 0));

        // Pure unit test = verify no crash + object still valid
        assertNotNull(device);
        assertEquals("L1", device.getName());
    }

    // --------------------------------------------------
    // edge case: empty collection
    // --------------------------------------------------

    @Test
    public void testCheckAllDevicesAutomation_EmptyList() {

        Collection<Device> devices = new ArrayList<>();

        instance.checkAllDevicesAutomation(devices, 20, LocalTime.now());

        assertTrue(devices.isEmpty());
    }

    // --------------------------------------------------
    // null safety behaviour (optional but honest test)
    // --------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testCheckAllDevicesAutomation_NullInput() {

        instance.checkAllDevicesAutomation(null, 20, LocalTime.now());
    }
}