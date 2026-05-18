package smarthome.model;

import java.time.LocalDateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UsageRecordTest {

    private UsageRecord instance;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = new UsageRecord(LocalDateTime.of(2025, 1, 1, 10, 0));
    }

    @After
    public void tearDown() {
        instance = null;
    }

    // -----------------------------------
    // endRecord
    // -----------------------------------

    @Test
    public void testEndRecord_SetsEndTime() {

        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);

        instance.endRecord(end);

        assertEquals(end, instance.getEnd());
    }

    // -----------------------------------
    // getStart
    // -----------------------------------

    @Test
    public void testGetStart_ReturnsStartTime() {

        LocalDateTime expected = LocalDateTime.of(2025, 1, 1, 10, 0);

        assertEquals(expected, instance.getStart());
    }

    // -----------------------------------
    // getEnd
    // -----------------------------------

    @Test
    public void testGetEnd_BeforeEndRecord_IsNull() {

        assertNull(instance.getEnd());
    }

    @Test
    public void testGetEnd_AfterEndRecord_ReturnsValue() {

        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 11, 0);

        instance.endRecord(end);

        assertEquals(end, instance.getEnd());
    }

    // -----------------------------------
    // isComplete
    // -----------------------------------

    @Test
    public void testIsComplete_BeforeEnd_IsFalse() {

        assertFalse(instance.isComplete());
    }

    @Test
    public void testIsComplete_AfterEnd_IsTrue() {

        instance.endRecord(LocalDateTime.of(2025, 1, 1, 11, 0));

        assertTrue(instance.isComplete());
    }
}