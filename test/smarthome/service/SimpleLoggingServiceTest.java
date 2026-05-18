/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package smarthome.service;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rlack
 */
public class SimpleLoggingServiceTest {
    
    private SimpleLoggingService instance;
    
    public SimpleLoggingServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new SimpleLoggingService();
    }
    
    @After
    public void tearDown() {
        instance = null;
    }

    /**
     * Test of addMessage method, of class SimpleLoggingService.
     */
    @Test
    public void testAddMessage() {
        instance.addMessage("Hello");

        assertEquals(1, instance.getMessageCount());
    }

    /**
     * Test of getMessages method, of class SimpleLoggingService.
     */
    @Test
    public void testGetMessages() {
        instance.addMessage("One");
        instance.addMessage("Two");

        ArrayList<String> result = instance.getMessages();

        assertEquals(2, result.size());
        assertTrue(result.contains("One"));
        assertTrue(result.contains("Two"));
    }

    /**
     * Test of clearMessages method, of class SimpleLoggingService.
     */
    @Test
    public void testClearMessages() {
        instance.addMessage("A");
        instance.addMessage("B");

        instance.clearMessages();

        assertEquals(0, instance.getMessageCount());
        assertTrue(instance.getMessages().isEmpty());
    }

    /**
     * Test of getMessageCount method, of class SimpleLoggingService.
     */
    @Test
    public void testGetMessageCount() {
        assertEquals(0, instance.getMessageCount());

        instance.addMessage("X");
        assertEquals(1, instance.getMessageCount());
    }

    /**
     * Extra test: ensures getMessages returns a copy (defensive copy check)
     */
    @Test
    public void testGetMessages_ReturnsCopy() {
        instance.addMessage("Test");

        ArrayList<String> copy = instance.getMessages();
        copy.add("Hacked");

        assertEquals(1, instance.getMessageCount());
        assertFalse(instance.getMessages().contains("Hacked"));
    }
}