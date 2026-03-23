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
import smarthome.service.DependencyContainer;
import smarthome.service.IBillingService;
import smarthome.view.SmartHomeCLIView;
import java.util.ArrayList;

public class DashboardController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeCLIView view;
    private ArrayList<Device> deviceList;
    private IBillingService billingService;
    
    public DashboardController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
        this.billingService = DependencyContainer.getInstance().getBillingService();
    }

    @Override
    public String getMenuContents(){
        deviceList = new ArrayList<>(system.getAllDevices());
        StringBuilder menu = new StringBuilder("=== SMART HOME DASHBOARD ===\n");

        int i = 1;
        for (Device d : deviceList) {
            menu.append("\n").append(i).append(". ")
                .append(d.getName())
                .append(" [")
                .append(d.isOn() ? "ON" : "OFF")
                .append("]");
            i++;
        }
        
        int totalUsage = billingService.calculateTotalElectricityUsage(system.getAllDevices());
        menu.append("\n\nTotal Electricity Rate: ").append(totalUsage).append(" Watts/Hour\n");
        
        double totalBill = billingService.calculateTotalBill(
            system.getAllDevices(),
            system.getSimulation().getElectricityCost()
        );
        DecimalFormat df = new DecimalFormat("0.000000000");
        menu.append("Current Total Bill: $").append(df.format(totalBill));
        return menu.toString();
    }
    
    @Override
    public String getOptionsContents(){
        return "[Device index] View device details\n"+
        "[Enter] Refresh Screen (Simulating time pass)\n"+
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
                controller.setCurrentMessage("All devices turned on");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + "All devices turned on\n");
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
                controller.setCurrentMessage("All devices turned off");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + "All devices turned off\n");
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
            controller.showLog();
            return;
        }
        if(command.equalsIgnoreCase("q")){
            controller.exit();
        }
        try{
            int index = Integer.parseInt(command);
            Device device = deviceList.get(index-1);
            controller.showDevice(device);
        }catch(Exception e){
            view.showInvalidOption();
            controller.setCurrentMessage("Invalid option input");
        }
    }
}