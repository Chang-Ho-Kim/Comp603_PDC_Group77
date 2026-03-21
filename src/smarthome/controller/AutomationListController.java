/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.controller;

/**
 *
 * @author rlack
 */
import java.time.LocalDateTime;
import java.util.ArrayList;
import smarthome.model.Device;
import smarthome.model.*;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeCLIView;

public class AutomationListController implements IInterfaceController {
    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeCLIView view;
    
    private enum automationType{ALL, POWERSAVER, SENSOR, SCHEDULER};
    private automationType dType;
    
    private ArrayList<Device> deviceList;
    
    public AutomationListController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
        dType = automationType.ALL;
        
    }
    
    
    @Override
    public String getMenuContents() {
        deviceList = new ArrayList();
        StringBuilder menu = new StringBuilder();
        int i = 1;
        
            if(((dType == automationType.ALL))){
                for (Device d : system.getAllDevices()) {
                    if((d instanceof ISensorable) ||(d instanceof ScheduledDevice) || (d instanceof IPowerSaveable)){
                        deviceList.add(i-1, d);
                        menu.append(i)
                            .append(". ")
                            .append(d.getName())
                            .append(" [")
                            .append(d.isOn() ? "ON" : "OFF")
                            .append("]\n");
                        i++;
                    }
                }
            }
            else if(((dType == automationType.SCHEDULER))){
                for (Device d : system.getAllDevices()) {
                    if(d instanceof ScheduledDevice sd){
                        deviceList.add(i-1, sd);
                        menu.append(i)
                            .append(". ")
                            .append(sd.getName())
                            .append(" [")
                            .append(sd.getStart())
                            .append(" - ")
                            .append(sd.getEnd())
                            .append("]\n");
                        i++;
                    }
                }
            }
            else if(((dType == automationType.SENSOR))){
                for (Device d : system.getAllDevices()) {
                    if(d instanceof SensorDevice sd){
                        deviceList.add(i-1, sd);
                        menu.append(i)
                            .append(". ")
                            .append(sd.getName())
                            .append(" [")
                            .append(String.valueOf(sd.getLower()))
                            .append("]\n");
                        i++;
                    }
                }
            }
            
            
            
            
        
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        return "1. Devices that can be scheduled\n" +
                "2. Devices that can react to sensor data\n" +
                "3. Devices that have power saver mode\n" +
                "4. State of all automatable Devices\n" +
                "\n                              (0. Return to dashboard)";
    }

    @Override
    public void handleCommand(String command) {
       switch(command){
            case "1": dType = automationType.SCHEDULER; view.renderView(controller); break;
                      
            case "2": dType = automationType.SENSOR; view.renderView(controller);break;
            
            
            case "3": dType = automationType.POWERSAVER; view.renderView(controller);break;
            case "4": dType = automationType.ALL; view.renderView(controller);break;
                      
            case "0": controller.showDashboard(); return;
            default: view.showInvalidOption();
        }
    }
    
    
    
    
}
