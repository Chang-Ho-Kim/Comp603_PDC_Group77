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
import smarthome.model.*;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeCLIView;

public class DeviceAdderController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeCLIView view;
   
    public DeviceAdderController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

  
    @Override
    public String getMenuContents(){
       return "What device would you like to add?: \n"
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
        return "Adding a Device";
    }

    @Override
    public void handleCommand(String command){
       
      String name;
        
        switch(command){
            case "1":name = controller.setDeviceProcedure(); system.addDevice(new Heater(name)); controller.setCurrentMessage(name + " was added\n"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+ controller.getCurrentMessage()); //SaveLoadService.saveSystem(system);  
                        break;
            case "2":name = controller.setDeviceProcedure(); system.addDevice(new Light(name)); controller.setCurrentMessage(name + " was added\n"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+ controller.getCurrentMessage()); //SaveLoadService.saveSystem(system); 
                        break;
            case "3":name = controller.setDeviceProcedure(); system.addDevice(new AirCon(name)); controller.setCurrentMessage(name + " was added\n"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+ controller.getCurrentMessage()); //SaveLoadService.saveSystem(system);  
                        break;
            case "4":name = controller.setDeviceProcedure(); system.addDevice(new AlarmClock(name)); controller.setCurrentMessage(name + " was added\n"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+ controller.getCurrentMessage()); //SaveLoadService.saveSystem(system); 
                        break;
            case "5":name = controller.setDeviceProcedure(); system.addDevice(new Door(name)); controller.setCurrentMessage(name + " was added\n"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+ controller.getCurrentMessage()); //SaveLoadService.saveSystem(system);  
                        break;
            case "6":name = controller.setDeviceProcedure(); system.addDevice(new MusicPlayer(name)); controller.setCurrentMessage(name + " was added\n"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+ controller.getCurrentMessage()); //SaveLoadService.saveSystem(system); 
                        break;            
            case "7":name = controller.setDeviceProcedure(); system.addDevice(new TV(name)); controller.setCurrentMessage(name + " was added\n"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+ controller.getCurrentMessage()); //SaveLoadService.saveSystem(system);  
                        break;
            case "8":name = controller.setDeviceProcedure(); system.addDevice(new RobotCleaner(name)); controller.setCurrentMessage(name + " was added\n"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+ controller.getCurrentMessage()); //SaveLoadService.saveSystem(system); 
                        break;            
            case "0": controller.showDashboard(); break;            
                        
            default: view.showInvalidOption();
        }
        controller.showDashboard();
    }

    
    
}