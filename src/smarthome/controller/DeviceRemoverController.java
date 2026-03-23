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
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeCLIView;
import java.util.ArrayList;

public class DeviceRemoverController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
   
    public DeviceRemoverController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view){
        this.controller = controller;
        this.system = system;
    }

  
    @Override
    public String getMenuContents(){
        StringBuilder menu = new StringBuilder();

        int i = 1;
        ArrayList<Device> deviceList = new ArrayList<>(system.getAllDevices());
        for (Device d : deviceList) {
            menu.append(i)
                .append(". ")
                .append(d.getName())
                .append(" [")
                .append(d.isOn() ? "ON" : "OFF")
                .append("]\n");
            i++;
        }
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        return "What is the name of the device would you like to remove?:\n"
                + "\n                          (Type back to return to dashboard)";
    }

    @Override
    public void handleCommand(String command){
        if(system.getDeviceNames().contains(command)){
            system.removeDevice(command);
            controller.setCurrentMessage(command + " was removed");
            controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + command + " was removed\n");
            controller.showDashboard();
        }
        else if(command.equals("back")){
            System.out.println("Back to Dashboard");
            controller.showDashboard();
        }
        else{
            System.out.println(command + " does not exist in the system");
        }
    }
}