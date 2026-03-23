package smarthome.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.view.ISmartHomeView;

public class DeviceRemoverController implements IInterfaceController {

    private final CentralController controller;
    private final SmartHomeSystem system;
    private final ISmartHomeView view;
    private List<Device> deviceList;
   
    public DeviceRemoverController(CentralController controller, SmartHomeSystem system, ISmartHomeView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        deviceList = new ArrayList<>(system.getAllDevices());
        StringBuilder menu = new StringBuilder("=== REMOVE DEVICE ===\n\n");

        int i = 1;
        for (Device d : deviceList) {
            menu.append(i).append(". ").append(d.getName()).append("\n");
            i++;
        }
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        return "Enter device name or index to remove\n"
             + "Enter 'back' to return to dashboard";
    }

    @Override
    public void handleCommand(String command){
        String cmd = command.trim();
        if (cmd.equalsIgnoreCase("back")) {
            controller.showDashboard();
            return;
        }

        Device toRemove = null;
        
        // Try by name first
        if (system.getDeviceNames().contains(cmd)) {
            toRemove = system.getDevice(cmd);
        } else {
            // Try by index
            try {
                int index = Integer.parseInt(cmd);
                if (index > 0 && index <= deviceList.size()) {
                    toRemove = deviceList.get(index - 1);
                }
            } catch (NumberFormatException ignored) {}
        }

        if (toRemove != null) {
            String name = toRemove.getName();
            system.removeDevice(name);
            String msg = name + " was removed";
            controller.setCurrentMessage(msg);
            system.addMessage("[" + LocalDateTime.now().format(controller.getDateTimeFormatter()) + "] " + msg + "\n");
            controller.showDashboard();
        } else {
            controller.setCurrentMessage("Device '" + cmd + "' not found");
        }
    }
}
