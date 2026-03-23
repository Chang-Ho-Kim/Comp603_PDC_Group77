package smarthome.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import smarthome.model.SmartHomeSystem;
import smarthome.model.Device;
import smarthome.view.ISmartHomeView;
import smarthome.persistence.IPersistenceService;

public class CentralController {

    private final SmartHomeSystem system;
    private final ISmartHomeView view;
    private final IPersistenceService persistenceService;
    private final AutomationService automationService;

    private IInterfaceController currentInterface;
    private final DashboardController dashboardController;
    private final DeviceDetailController deviceController;
    private final SimulationController simulationController;
    private final LogController logController;
    private final DeviceAdderController deviceAdderController;
    private final DeviceRemoverController deviceRemoverController;
    private final AutomationListController automationController;
    
    private String currentMessage;
    private final DateTimeFormatter dateTimeFormatter;
  
    public CentralController(SmartHomeSystem system, ISmartHomeView view, IPersistenceService persistenceService){
        this.system = system;
        this.view = view;
        this.persistenceService = persistenceService;
        this.automationService = new AutomationService(system, this);

        dashboardController = new DashboardController(this, system, view);
        deviceController = new DeviceDetailController(this, system, view);
        simulationController = new SimulationController(this, system, view);
        logController = new LogController(this, system, view);
        deviceAdderController = new DeviceAdderController(this, system, view);
        deviceRemoverController = new DeviceRemoverController(this, system, view);
        automationController = new AutomationListController(this, system, view);
                
        currentInterface = dashboardController;
        currentMessage = "How may I be of assistance?";
        
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        system.addMessage("[" + LocalDateTime.now().format(dateTimeFormatter) + "] Smart Home Simulator Started\n");
    }

    public void start() {
        automationService.startAutomation();
        
        while (true) {
            String title = "Smart Home System";
            String menu = currentInterface.getMenuContents();
            String options = currentInterface.getOptionsContents();
            
            view.displayMenu(title, menu, options);
            view.displayMessage(currentMessage);
            
            String input = view.getInput("\nSelect option: ");
            currentInterface.handleCommand(input);
            persistenceService.saveSystem(system);
        }
    }

    public void showDashboard(){ currentInterface = dashboardController; }
    public void showDevice(Device device){ deviceController.setDevice(device); currentInterface = deviceController; }
    public void showSimulation(){ currentInterface = simulationController; }
    public void showLog(){ currentInterface = logController; }
    public void showDeviceAdder(){ currentInterface = deviceAdderController; }  
    public void showDeviceRemover(){ currentInterface = deviceRemoverController; } 
    public void showAutomation(){ currentInterface = automationController; } 
    
    public String getCurrentMessage(){ return currentMessage; }
    public void setCurrentMessage(String message){ currentMessage = message; }
    
    public void addMessage(String log){ system.addMessage(log); }
    public ArrayList<String> getMessages(){ return system.getMessages(); }
    public void deleteLog(){ system.deleteLog(); currentMessage = "Log was deleted"; }
    
    public void exit(){
        system.addMessage("[" + LocalDateTime.now().format(dateTimeFormatter) + "] Smart Home Simulator Ended\n");
        System.out.println("Exiting...");
        System.exit(0);
    }

    public SmartHomeSystem getSystem() { return system; }
    public ISmartHomeView getView() { return view; }
    public DateTimeFormatter getDateTimeFormatter() { return dateTimeFormatter; }
}
