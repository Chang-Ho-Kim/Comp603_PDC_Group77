package smarthome.controller;

import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeGUIView;
import java.io.FileWriter;
import java.io.IOException;

public class LogController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeGUIView view;
   
    public LogController(CentralController controller, SmartHomeSystem system, SmartHomeGUIView view){
        this.controller = controller;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        String logs = controller.getLoggingService().getMessages().toString();
        if (logs.isEmpty()) {
            return "📋 === APPLICATION LOG ===\n\nNo logs yet.";
        }
        return "📋 === APPLICATION LOG ===\n\n" + logs;
    }

    @Override
    public String getOptionsContents() {
        return "1. Delete Log\n" +
                "2. Export Log\n" +
                "0. Back to Dashboard";
    }

    @Override
    public void handleCommand(String command){
        switch(command){
            case "0":
                controller.showDashboard();
                return;
            case "1":
                if(view.showConfirmDialog("Delete all logs?", "Confirm")) {
                    controller.getLoggingService().clearMessages();
                    controller.setCurrentMessage("✅ Log deleted");
                } else {
                    controller.setCurrentMessage("❌ Deletion cancelled");
                }
                return;
            case "2":
                try (FileWriter writer = new FileWriter("Log.txt")) {
                    writer.write(controller.getLoggingService().getMessages().toString());
                    controller.setCurrentMessage("✅ Log exported to Log.txt");
                } catch (IOException e) {
                    controller.setCurrentMessage("❌ Export failed: " + e.getMessage());
                }
                return;
            default:
                view.showInvalidOption();
        }
    }
}
