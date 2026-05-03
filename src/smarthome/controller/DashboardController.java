package smarthome.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.service.DependencyContainer;
import smarthome.service.IBillingService;
import smarthome.view.SmartHomeGUIView;
import java.util.ArrayList;

public class DashboardController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeGUIView view;
    private ArrayList<Device> deviceList;
    private IBillingService billingService;
    
    public DashboardController(CentralController controller, SmartHomeSystem system, SmartHomeGUIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
        this.billingService = DependencyContainer.getInstance().getBillingService();
    }

    @Override
    public String getMenuContents(){
        deviceList = new ArrayList<>(system.getAllDevices());
        StringBuilder menu = new StringBuilder("🏠 === SMART HOME DASHBOARD ===\n\n");

        if (deviceList.isEmpty()) {
            menu.append("No devices installed yet.\n");
        } else {
            menu.append("📱 DEVICES:\n");
            int i = 1;
            for (Device d : deviceList) {
                String status = d.isOn() ? "✅ ON" : "⚫ OFF";
                menu.append(i).append(". ").append(d.getName())
                    .append(" [").append(status).append("]\n");
                i++;
            }
        }
        
        int totalUsage = billingService.calculateTotalElectricityUsage(system.getAllDevices());
        menu.append("\n⚡ Electricity Usage: ").append(totalUsage).append(" Watts/Hour\n");
        
        double totalBill = billingService.calculateTotalBill(
            system.getAllDevices(),
            system.getSimulation().getElectricityCost()
        );
        double removedDevicesAccruedBill = billingService.calculateTotalBill(
            system.getAllRemovedDevices(),
            system.getSimulation().getElectricityCost()
        );
        DecimalFormat df = new DecimalFormat("0.000000000");
        menu.append("💰 Total Bill: $").append(df.format(totalBill+removedDevicesAccruedBill));
        return menu.toString();
    }
    
    @Override
    public String getOptionsContents(){
        StringBuilder options = new StringBuilder();
        
        // Device selection
        int i = 1;
        for (Device d : deviceList) {
            options.append(i).append(". ").append(d.getName()).append("\n");
            i++;
        }
        
        options.append("\nW. Turn On All\n");
        options.append("E. Turn Off All\n");
        options.append("A. Add Device\n");
        options.append("R. Remove Device\n");
        options.append("F. View Automation\n");
        options.append("S. Simulation Settings\n");
        options.append("L. View Log\n");
        options.append("Q. Quit");
        
        return options.toString();
    }
    
    @Override
    public void handleCommand(String command){
        // Device selection (1-9)
        try {
            int index = Integer.parseInt(command);
            if (index > 0 && index <= deviceList.size()) {
                Device device = deviceList.get(index-1);
                controller.showDevice(device);
                return;
            }
        } catch (NumberFormatException e) {
            // Not a number, check letter commands
        }
        
        if(command.equalsIgnoreCase("s")){
            controller.showSimulation();
            controller.setCurrentMessage("📊 Simulation Settings");
            return;
        }
        if(command.equalsIgnoreCase("w")){
            if(system.getAllDevices().isEmpty()){
                controller.setCurrentMessage("❌ No devices to turn on");
                return;
            }
            for(Device d: system.getAllDevices()){
                d.turnOn();
            }
            controller.setCurrentMessage("✅ All devices turned ON");
            controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] All devices turned on\n");
            return;
        }
        if(command.equalsIgnoreCase("e")){
            if(system.getAllDevices().isEmpty()){
                controller.setCurrentMessage("❌ No devices to turn off");
                return;
            }
            for(Device d: system.getAllDevices()){
                d.turnOff();
            }
            controller.setCurrentMessage("✅ All devices turned OFF");
            controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] All devices turned off\n");
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
            if(view.showConfirmDialog("Are you sure you want to exit?", "Confirm Exit")) {
                controller.exit();
            }
            return;
        }
        view.showInvalidOption();
        controller.setCurrentMessage("❌ Invalid option");
    }
}
