package smarthome;

import smarthome.controller.CentralController;
import smarthome.model.SmartHomeSystem;
import smarthome.view.ISmartHomeView;
import smarthome.view.SmartHomeCLIView;
import smarthome.persistence.FilePersistenceService;
import smarthome.persistence.IPersistenceService;

public class Main {

    public static void main(String[] args) {

        IPersistenceService persistenceService = new FilePersistenceService();
        SmartHomeSystem system = persistenceService.loadSystem();

        ISmartHomeView view = new SmartHomeCLIView();
        CentralController controller = new CentralController(system, view, persistenceService);

        // Hook to save system on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            persistenceService.saveSystem(system);
        }));

        controller.start();
    }
}
