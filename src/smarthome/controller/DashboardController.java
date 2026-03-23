package smarthome.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.model.ScheduledDevice;
import smarthome.model.SensorDevice;
import smarthome.model.BillingService;
import smarthome.view.ISmartHomeView;

public class DashboardController implements IInterfaceController {

    private final CentralController controller;
    private final SmartHomeSystem system;
    private final ISmartHomeView view;
    private List<Device> deviceList;
    
    public DashboardController(CentralController controller, SmartHomeSystem system, ISmartHomeView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        deviceList = new ArrayList<>(system.getAllDevices());
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
            
            if (d instanceof ScheduledDevice sd && sd.isScheduleOn()) {
                menu.append(" Auto-Mode [ ")
                    .append(sd.getStart())
                    .append(" - ")
                    .append(sd.getEnd())
                    .append(" ] ");
            }
            if (d instanceof SensorDevice srd && srd.isSensorOn()) {
                 menu.append(" Auto-Mode [")
                    .append(srd.getLower())
                    .append("] ");
            }
            
            i++;
        }
        menu.append("\n\nTotal Electricity Rate: ").append(system.getTotalElectricityUsage()).append(" Watts/Hour\n");
        
        DecimalFormat df = new DecimalFormat("0.000000000");
        menu.append("Current Total Bill: $").append(df.format(BillingService.calculateTotalBill(system.getAllDevices(), system.getECost())));
        return menu.toString();
    }
    
    @Override
    public String getOptionsContents(){
        return "[Device index] View device details\n" +
               "[Enter] Refresh Screen\n" +
               "[W] Turn on all devices  |  [E] Turn off all devices\n" +
               "[A] Add Device  |  [R] Remove Device\n" +
               "[F] View Automatable Devices\n" +
               "[S] Simulation Settings\n" +
               "[L] Smart Home Usage Log\n" +
               "[Q] Quit";
    }
    
    @Override
    public void handleCommand(String command){
        String cmd = command.trim().toLowerCase();
        
        switch (cmd) {
            case "s":
                controller.showSimulation();
                controller.setCurrentMessage("Setting Simulation environment");
                break;
            case "w":
                turnAllDevices(true);
                break;
            case "e":
                turnAllDevices(false);
                break;
            case "a":
                controller.showDeviceAdder();
                break;
            case "r":
                controller.showDeviceRemover();
                break;
            case "f":
                controller.showAutomation();
                break;
            case "l":
                controller.setCurrentMessage("Viewing log");
                controller.showLog();
                break;
            case "q":
                controller.exit();
                break;
            case "":
                // Just refresh
                break;
            default:
                try {
                    int index = Integer.parseInt(cmd);
                    if (index > 0 && index <= deviceList.size()) {
                        controller.showDevice(deviceList.get(index - 1));
                    } else {
                        view.displayError("Invalid device index");
                    }
                } catch (NumberFormatException e) {
                    controller.setCurrentMessage("Invalid option input");
                }
                break;
        }
    }

    private void turnAllDevices(boolean on) {
        if (system.getAllDevices().isEmpty()) {
            controller.setCurrentMessage("You don't have any devices!");
            return;
        }
        
        for (Device d : system.getAllDevices()) {
            if (on) d.turnOn(); else d.turnOff();
        }
        
        String status = on ? "on" : "off";
        controller.setCurrentMessage("All devices turned " + status);
        system.addMessage("[" + LocalDateTime.now().format(controller.getDateTimeFormatter()) + "] All devices turned " + status + "\n");
    }
}
