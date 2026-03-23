package smarthome.persistence;

import java.io.*;
import smarthome.model.SmartHomeSystem;

public class FilePersistenceService implements IPersistenceService {
    private static final String FILE_NAME = "SmartHomeSystem.ser";

    @Override
    public SmartHomeSystem loadSystem() {
        try (FileInputStream fileIn = new FileInputStream(FILE_NAME);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            System.out.println("System loaded successfully.");
            return (SmartHomeSystem) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous system found. Creating new system.");
            return new SmartHomeSystem();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading system: " + e.getMessage());
            return new SmartHomeSystem();
        }
    }

    @Override
    public void saveSystem(SmartHomeSystem system) {
        try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(system);
        } catch (IOException e) {
            System.err.println("Error saving system: " + e.getMessage());
        }
    }
}
