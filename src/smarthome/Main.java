package smarthome;

import smarthome.controller.CentralController;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeGUIView;
import smarthome.view.View;

import smarthome.dao.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {

        // 1. Ensure embedded Derby tables exist
        DatabaseInitializer.init();

        // 2. Create system
        SmartHomeSystem system = new SmartHomeSystem();

        // 3. Load data from database (NOT file anymore)
        system.load();

        // 4. Create view
        View view = new SmartHomeGUIView();

        // 5. Create controller
        CentralController controller =
                new CentralController(system, view);

        // 6. Optional: save on exit (DB version)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            system.save();
            System.out.println("System saved to Derby on exit.");
        }));

        // 7. Start app
        controller.start();
    }
}