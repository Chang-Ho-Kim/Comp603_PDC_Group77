package smarthome.controller;

import java.time.LocalDateTime;
import smarthome.model.*;
import smarthome.view.ISmartHomeView;

public class DeviceAdderController implements IInterfaceController {

    private final CentralController controller;
    private final SmartHomeSystem system;
    private final ISmartHomeView view;
   
    public DeviceAdderController(CentralController controller, SmartHomeSystem system, ISmartHomeView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
       return "Select a device type to add:\n"
            + "1. Heater\n"
            + "2. Light\n"
            + "3. Air Conditioner\n"
            + "4. Alarm Clock\n"
            + "5. Door\n"
            + "6. Music Player\n"
            + "7. Television\n"
            + "8. Robot Cleaner\n"
            + "                                    (0. Back to Dashboard)";
    }
    
    @Override
    public String getOptionsContents() {
        return "Enter number to select device type";
    }

    @Override
    public void handleCommand(String command){
        if (command.equals("0")) {
            controller.showDashboard();
            return;
        }

        String name = view.getInput("Enter name for the device: ");
        if (name == null || name.trim().isEmpty()) {
            controller.setCurrentMessage("Device name cannot be empty");
            controller.showDashboard();
            return;
        }

        Device newDevice = DeviceFactory.createDevice(command, name);
        if (newDevice != null) {
            system.addDevice(newDevice);
            String msg = name + " was added";
            controller.setCurrentMessage(msg);
            system.addMessage("[" + LocalDateTime.now().format(controller.getDateTimeFormatter()) + "] " + msg + "\n");
        } else {
            controller.setCurrentMessage("Invalid device type");
        }
        
        controller.showDashboard();
    }
}
