package smarthome.controller;

import java.time.LocalDateTime;
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeGUIView;
import java.util.ArrayList;

public class DeviceRemoverController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeGUIView view;
    private ArrayList<Device> deviceList;
   
    public DeviceRemoverController(CentralController controller, SmartHomeSystem system, SmartHomeGUIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        deviceList = new ArrayList<>(system.getAllDevices());
        StringBuilder menu = new StringBuilder("🗑️ === REMOVE DEVICE ===\n\n");

        if (deviceList.isEmpty()) {
            menu.append("No devices to remove.\n");
        } else {
            int i = 1;
            for (Device d : deviceList) {
                menu.append(i).append(". ").append(d.getName())
                    .append(" [").append(d.isOn() ? "✅ ON" : "⚫ OFF").append("]\n");
                i++;
            }
        }
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        return "Select device by number (1-" + system.getAllDevices().size() + ") or 0 to cancel";
    }

    @Override
    public void handleCommand(String command){
        if(command.equalsIgnoreCase("0")){
            controller.showDashboard();
            return;
        }
        
        try {
            int index = Integer.parseInt(command);
            if (index > 0 && index <= deviceList.size()) {
                Device device = deviceList.get(index - 1);
                String deviceName = device.getName();
                
                if(view.showConfirmDialog("Remove " + deviceName + "?", "Confirm Removal")) {
                    system.removeDevice(deviceName);
                    controller.setCurrentMessage("✅ " + deviceName + " removed");
                    controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + deviceName + " was removed\n");
                } else {
                    controller.setCurrentMessage("❌ Removal cancelled");
                }
            } else {
                view.showInvalidOption();
                controller.setCurrentMessage("❌ Invalid selection");
            }
        } catch (NumberFormatException e) {
            view.showInvalidOption();
            controller.setCurrentMessage("❌ Invalid input");
        }
        controller.showDashboard();
    }
}
