package smarthome.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimulationSettingsTest {

    private SimulationSettings instance;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new SimulationSettings();
    }

    @After
    public void tearDown() {
        instance = null;
    }

    // -----------------------------------
    // powerThreshold
    // -----------------------------------

    @Test
    public void testGetPowerThreshold_DefaultValue() {
        assertEquals(6000, instance.getPowerThreshold());
    }

    @Test
    public void testSetPowerThreshold_UpdatesValue() {
        instance.setPowerThreshold(150);

        assertEquals(150, instance.getPowerThreshold());
    }

    // -----------------------------------
    // electricityCost
    // -----------------------------------

    @Test
    public void testGetElectricityCost_DefaultValue() {
        assertEquals(0.00039, instance.getElectricityCost(), 0.0000001);
    }

    @Test
    public void testSetElectricityCost_UpdatesValue() {
        instance.setElectricityCost(0.25);

        assertEquals(0.25, instance.getElectricityCost(), 0.0000001);
    }

    // -----------------------------------
    // temperature
    // -----------------------------------

    @Test
    public void testGetTemperature_DefaultValue() {
        assertEquals(20, instance.getTemperature());
    }

    @Test
    public void testSetTemperature_UpdatesValue() {
        instance.setTemperature(22);

        assertEquals(22, instance.getTemperature());
    }
}