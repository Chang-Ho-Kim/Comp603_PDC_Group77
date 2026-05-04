package smarthome.controller;

import java.util.ArrayList;
import smarthome.model.Device;
import smarthome.model.*;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeGUIView;

public class AutomationListController implements IInterfaceController {
    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeGUIView view;
    
    private enum automationType{ALL, POWERSAVER, SENSOR, SCHEDULER};
    private automationType dType;
    
    private ArrayList<Device> deviceList;
    
    public AutomationListController(CentralController controller, SmartHomeSystem system, SmartHomeGUIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
        dType = automationType.ALL;
    }
    
    @Override
    public String getMenuContents() {
        deviceList = new ArrayList<>();
        StringBuilder menu = new StringBuilder("=== AUTOMATION SETTINGS ===\n\nCurrent View: ");
        
        switch(dType) {
            case SCHEDULER: menu.append("Scheduled Devices\n\n"); break;
            case SENSOR: menu.append("Sensor Devices\n\n"); break;
            case POWERSAVER: menu.append("Power Saver Devices\n\n"); break;
            default: menu.append("All Automatable Devices\n\n");
        }
        
        int i = 1;
        
        if(dType == automationType.ALL){
            for (Device d : system.getAllDevices()) {
                if((d instanceof ISensorable) ||(d instanceof ScheduledDevice) || (d instanceof IPowerSaveable)){
                    deviceList.add(i-1, d);
                    menu.append(i).append(". ").append(d.getName())
                        .append(" [").append(d.isOn() ? "ON" : "OFF").append("]\n");
                    i++;
                }
            }
        }
        else if(dType == automationType.SCHEDULER){
            for (Device d : system.getAllDevices()) {
                if(d instanceof ScheduledDevice sd){
                    deviceList.add(i-1, sd);
                    menu.append(i).append(". ").append(sd.getName())
                        .append(" [").append(sd.getStart()).append(" - ")
                        .append(sd.getEnd()).append("]\n");
                    i++;
                }
            }
        }
        else if(dType == automationType.SENSOR){
            for (Device d : system.getAllDevices()) {
                if(d instanceof SensorDevice sd){
                    deviceList.add(i-1, sd);
                    menu.append(i).append(". ").append(sd.getName())
                        .append(" [Range: ").append(String.valueOf(sd.getLower()))
                        .append("-").append(sd.getUpper()).append("]\n");
                    i++;
                }
            }
        }
        else if(dType == automationType.POWERSAVER){
            for (Device d : system.getAllDevices()) {
                if(d instanceof PowerSaverDevice psd){
                    deviceList.add(i-1, psd);
                    menu.append(i).append(". ").append(psd.getName()).append("\n");
                    i++;
                }
            }
        }
        
        if (i == 1) {
            menu.append("No devices of this type.");
        }
        
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        return "1. Scheduled Devices\n" +
                "2. Sensor Devices\n" +
                "3. Power Saver Devices\n" +
                "4. All Automatable Devices\n" +
                "0. Back to Dashboard";
    }

    @Override
    public void handleCommand(String command) {
        switch(command){
            case "1": dType = automationType.SCHEDULER; break;
            case "2": dType = automationType.SENSOR; break;
            case "3": dType = automationType.POWERSAVER; break;
            case "4": dType = automationType.ALL; break;
            case "0": controller.showDashboard(); return;
            default: view.showInvalidOption();
        }
    }
}
