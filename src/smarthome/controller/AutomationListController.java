package smarthome.controller;

import java.util.ArrayList;
import java.util.List;
import smarthome.model.Device;
import smarthome.model.ISensorable;
import smarthome.model.ScheduledDevice;
import smarthome.model.PowerSaverDevice;
import smarthome.model.SensorDevice;
import smarthome.model.SmartHomeSystem;
import smarthome.view.ISmartHomeView;

public class AutomationListController implements IInterfaceController {
    private final CentralController controller;
    private final SmartHomeSystem system;
    private final ISmartHomeView view;
    
    private enum AutomationType { ALL, POWERSAVER, SENSOR, SCHEDULER }
    private AutomationType dType = AutomationType.ALL;
    
    public AutomationListController(CentralController controller, SmartHomeSystem system, ISmartHomeView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }
    
    @Override
    public String getMenuContents() {
        StringBuilder menu = new StringBuilder("=== AUTOMATABLE DEVICES (" + dType + ") ===\n\n");
        int i = 1;
        
        for (Device d : system.getAllDevices()) {
            boolean include = false;
            String detail = "";

            switch (dType) {
                case ALL:
                    if (d instanceof ISensorable || d instanceof ScheduledDevice || d instanceof PowerSaverDevice) {
                        include = true;
                        detail = d.isOn() ? "[ON]" : "[OFF]";
                    }
                    break;
                case SCHEDULER:
                    if (d instanceof ScheduledDevice sd) {
                        include = true;
                        detail = sd.getStart() + " - " + sd.getEnd();
                    }
                    break;
                case SENSOR:
                    if (d instanceof SensorDevice sd) {
                        include = true;
                        detail = "Lower: " + sd.getLower() + ", Upper: " + sd.getUpper();
                    }
                    break;
                case POWERSAVER:
                    if (d instanceof PowerSaverDevice) {
                        include = true;
                        detail = "Enabled";
                    }
                    break;
            }

            if (include) {
                menu.append(i++).append(". ").append(d.getName()).append(" ").append(detail).append("\n");
            }
        }
        
        if (i == 1) {
            menu.append("No devices found for this category.");
        }
        
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        return "1. View scheduled devices\n" +
               "2. View sensor-based devices\n" +
               "3. View power-saving devices\n" +
               "4. View all automatable devices\n" +
               "                                    (0. Return to dashboard)";
    }

    @Override
    public void handleCommand(String command) {
       switch(command){
            case "1": dType = AutomationType.SCHEDULER; break;
            case "2": dType = AutomationType.SENSOR; break;
            case "3": dType = AutomationType.POWERSAVER; break;
            case "4": dType = AutomationType.ALL; break;
            case "0": controller.showDashboard(); break;
            default: controller.setCurrentMessage("Invalid option");
        }
    }
}
