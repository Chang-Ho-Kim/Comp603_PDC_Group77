package smarthome;

import smarthome.controller.CentralController;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeCLIView;
import smarthome.view.View;
import smarthome.persistence.SaveLoadService;

public class Main {

    public static void main(String[] args) {
        // Load previous system state or create new
        SmartHomeSystem system = SaveLoadService.loadSystem();

        // Create view (implements View interface)
        View view = new SmartHomeCLIView();
        
        // Create controller
        CentralController controller = new CentralController(system, view);

        // Hook to save system on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SaveLoadService.saveSystem(system);
            System.out.println("System saved on exit.");
        }));

        controller.start();
    }
}