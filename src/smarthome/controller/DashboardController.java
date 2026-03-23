/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.controller;

/**
 *
 * @author rlack
 */

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeCLIView;

import java.util.ArrayList;
import smarthome.model.ScheduledDevice;
import smarthome.model.SensorDevice;

public class DashboardController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeCLIView view;
    private ArrayList<Device> deviceList;
    
    public DashboardController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        deviceList = new ArrayList(system.getAllDevices());
        system.calculateTotalElectricityUsage();
        StringBuilder menu = new StringBuilder("=== SMART HOME DASHBOARD ===\n");

        int i = 1;
        for (Device d : deviceList) {
            menu.append("\n")
                .append(i)
                .append(". ")
                .append(d.getName())
                .append(" [")
                .append(d.isOn() ? "ON" : "OFF")
                .append("] ");
            if(d instanceof ScheduledDevice sd && sd.isScheduleOn()==true){
                menu.append(" Auto-Mode [ ")
                    .append(sd.getStart())
                    .append(" - ")
                    .append(sd.getEnd())
                     .append(" ] ");
            }
            if(d instanceof SensorDevice srd && srd.isSensorOn()==true){
                 menu.append(" Auto-Mode [")
                    .append(srd.getLower())
                     .append("] ");
            }
            
            i++;
        }
        menu.append("\n\nTotal Electricity Rate: " + system.getTotalElectricityUsage() + " Watts/Hour\n");
        
        DecimalFormat df = new DecimalFormat("0.000000000");
        
        menu.append("Current Total Bill: $" + df.format(system.getElectricityBill(system.getECost())));
        return menu.toString();
    }
    
    @Override
    public String getOptionsContents(){
        return "[Device index] View device details>\n"+
        "[Enter] Refresh Screen (Simulating time pass)>\n"+
        "[W] Turn on all devices  |  "+
        "[E] Turn off all devices\n"+
        "[A] Add Device  |  "+
        "[R] Remove Device\n"+
        "[F] View Automatable Devices\n"+
        "[S] Simulation Settings\n"+
        "[L] Smart Home Usage Log\n"+
        "[Q] Quit";
    }
    
    @Override
    public void handleCommand(String command){
        if(command.equalsIgnoreCase("s")){
            controller.showSimulation();
            controller.setCurrentMessage("Setting Simulation environment"); //system.addMessage("Entered Simulation Settings\n");
            return;
        }
        if(command.equalsIgnoreCase("w")){
            for(Device d: system.getAllDevices()){
                d.turnOn();
            }
            if(!system.getAllDevices().isEmpty()){
                controller.setCurrentMessage("All devices turned on"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] " + "All devices turned on\n");
            }
            else{
                controller.setCurrentMessage("You don't have any devices to turn on!");
            }
            return;
        }
        if(command.equalsIgnoreCase("e")){
            for(Device d: system.getAllDevices()){
                d.turnOff();
            }
            if(!system.getAllDevices().isEmpty()){
                controller.setCurrentMessage("All devices turned off"); system.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] "+ "All devices turned off\n");
            }
            else{
                controller.setCurrentMessage("You don't have any devices to turn off!");
            }
            return;
        }
         if(command.equalsIgnoreCase("a")){
            controller.showDeviceAdder();
            return;
        }
        if(command.equalsIgnoreCase("r")){
            controller.showDeviceRemover();
            return;
        }
        if(command.equalsIgnoreCase("f")){
            controller.showAutomation();
            return;
        } 
         
        if(command.equalsIgnoreCase("l")){
            controller.setCurrentMessage("View Log"); //system.addMessage("View Log\n");
            controller.showLog();
            return;
        } 
         //if(command.equalsIgnoreCase("d")){ 
           // controller.deleteLog();
           // return;
        //} 
        if(command.equalsIgnoreCase("")){
            controller.checkAutomation();
            return;
        }
        if(command.equalsIgnoreCase("q")){
            controller.exit();
        }
        //if(command.equalsIgnoreCase("e")){
        //    controller.setCurrentMessage("Check total electricity usage"); system.addMessage("Electricity Usage\n");
        //    controller.displayTotalEnergyUsage();
        //    return;
        //}
        try{
            int index = Integer.parseInt(command);
            Device device = deviceList.get(index-1);
            controller.showDevice(device);
        }catch(Exception e){
            view.showInvalidOption();
            controller.setCurrentMessage("Invalid option input");
        }
    }
    
     public ArrayList<Device> getDeviceList() {
        return deviceList;
    }
}