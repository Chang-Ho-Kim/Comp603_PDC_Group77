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

public class DeviceRemoverController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeCLIView view;
   
    public DeviceRemoverController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

  
    @Override
    public String getMenuContents(){
        StringBuilder menu = new StringBuilder();

        int i = 1;
        for (Device d : controller.getDashboardController().getDeviceList()) {
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
            controller.setCurrentMessage(command + " was removed\n");
            system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+command + " was removed\n");
            controller.showDashboard();
        }
       else if(command.equals("back")){
           System.out.println("Back to Dashboard"); 
           controller.showDashboard();
       }
        else{
           System.out.println(command +" does not exist in the system"); 
           
        }
    }

    
    
}