/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.persistence;

/**
 *
 * @author rlack
 */

import smarthome.model.SmartHomeSystem;
import java.io.*;

public class SaveLoadService {

    private static final String FILE_PATH = "smarthome.dat";

    // Save the entire system
    public static void saveSystem(SmartHomeSystem system){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(system);
            //System.out.println("System saved successfully.");
        } catch (IOException e){
            System.out.println("Failed to save system: " + e.getMessage());
        }
    }

    // Load the entire system
    public static SmartHomeSystem loadSystem(){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            SmartHomeSystem system = (SmartHomeSystem) in.readObject();
            System.out.println("System loaded successfully.");
            return system;
        } catch (FileNotFoundException e){
            System.out.println("Save file not found. Starting new system.");
            return new SmartHomeSystem();
        } catch (IOException | ClassNotFoundException e){
            System.out.println("Failed to load system: " + e.getMessage());
            return new SmartHomeSystem();
        }
    }
}