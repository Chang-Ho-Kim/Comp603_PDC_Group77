/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.controller;

/**
 *
 * @author rlack
 */

import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeCLIView;
import java.io.FileWriter;
import java.io.IOException;

public class LogController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeCLIView view;
   
    public LogController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view){
        this.controller = controller;
        this.view = view;
    }

  
    @Override
    public String getMenuContents(){
        return controller.getLoggingService().getMessages().toString();
    }

    @Override
    public String getOptionsContents() {
        return "1. Delete Log\n"+
                "2. Export Log\n"+
                "                                    (0. Back to Dashboard)";
    }

    @Override
    public void handleCommand(String command){
        switch(command){
            case "0":
                controller.showDashboard();
                return;
            case "1":
                controller.getLoggingService().clearMessages();
                controller.setCurrentMessage("Log was deleted");
                return;
            case "2":
                try (FileWriter writer = new FileWriter("Log.txt")) {
                    writer.write(controller.getLoggingService().getMessages().toString());
                    controller.setCurrentMessage("Log successfully exported to Log.txt");
                } catch (IOException e) {
                    controller.setCurrentMessage("Failed to export...");
                }
                return;
            default:
                view.showInvalidOption();
        }
    }
}