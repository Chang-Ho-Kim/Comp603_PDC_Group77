package smarthome.controller;

import smarthome.model.SmartHomeSystem;
import smarthome.view.ISmartHomeView;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LogController implements IInterfaceController {

    private final CentralController controller;
    private final SmartHomeSystem system;
    private final ISmartHomeView view;
   
    public LogController(CentralController controller, SmartHomeSystem system, ISmartHomeView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        List<String> messages = system.getMessages();
        if (messages.isEmpty()) {
            return "No log entries found.";
        }
        
        StringBuilder log = new StringBuilder("=== SMART HOME LOG ===\n\n");
        for (String msg : messages) {
            log.append(msg);
        }
        return log.toString();
    }

    @Override
    public String getOptionsContents() {
        return "1. Delete Log\n"+
               "2. Export Log to file\n"+
               "                                    (0. Back to Dashboard)";
    }

    @Override
    public void handleCommand(String command){
        switch(command){
            case "1":
                system.deleteLog();
                controller.setCurrentMessage("Log was deleted");
                break;
            case "2":
                exportLog();
                break;
            case "0":
                controller.showDashboard();
                break;
            default:
                controller.setCurrentMessage("Invalid option");
        }
    }

    private void exportLog() {
        try (FileWriter writer = new FileWriter("Log.txt")) {
            for (String msg : system.getMessages()) {
                writer.write(msg);
            }
            controller.setCurrentMessage("Log successfully exported to Log.txt");
        } catch (IOException e) {
            controller.setCurrentMessage("Failed to export: " + e.getMessage());
        }
    }
}
