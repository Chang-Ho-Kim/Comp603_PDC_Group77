package smarthome.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import smarthome.model.SmartHomeSystem;
import smarthome.model.Device;
import smarthome.service.*;
import smarthome.view.View;
import smarthome.view.SmartHomeGUIView;
import smarthome.view.ViewData;
import smarthome.persistence.SaveLoadService;

/**
 * CentralController - Refactored to follow SOLID principles with full GUI support.
 * 
 * Responsibilities:
 * - Main application loop and screen navigation
 * - GUI button handling and event processing
 * - Delegation to sub-controllers
 * 
 * Now implements segregated interfaces:
 * - IMessageManager: For UI messages
 * - IScreenNavigator: For screen navigation
 * - IInputHandler: For GUI-based input dialogs
 */
public class CentralController implements ICentralController, IMessageManager, IScreenNavigator, IInputHandler {

    private final SmartHomeSystem system;
    private final SmartHomeGUIView view;
    
    // Services (injected)
    private final ILoggingService loggingService;
    private final IAutomationService automationService;
    private final IBillingService billingService;
    private final IThresholdManager thresholdManager;
    
    // Sub-controllers (lazily created)
    private IInterfaceController currentInterface;
    private DashboardController dashboardController;
    private DeviceDetailController deviceController;
    private SimulationController simulationController;
    private LogController logController;
    private DeviceAdderController deviceAdderController;
    private DeviceRemoverController deviceRemoverController;
    private AutomationListController automationController;
    
    // State
    private String currentMessage;
    private boolean running;
    
    // For backward compatibility
    public final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public CentralController(SmartHomeSystem system, View view) {
        this.system = system;
        this.view = (SmartHomeGUIView) view;
        
        // Get services from dependency container
        DependencyContainer container = DependencyContainer.getInstance();
        this.loggingService = container.getLoggingService();
        this.automationService = container.getAutomationService();
        this.billingService = container.getBillingService();
        this.thresholdManager = container.getThresholdManager();
        
        // Initialize state
        this.currentMessage = "Welcome to Smart Home Simulator!";
        this.running = true;
        
        // Set up button command handler
        this.view.setCommandHandler(this::handleButtonCommand);
        
        // Log startup
        loggingService.addMessage("[" + dateTimeFormatter.format(LocalDateTime.now()) + "] " +
                "Smart Home Simulator Started\n");
    }

    @Override
    public void start() {
        // Show dashboard first
        showDashboard();
        
        // Start automation thread
        Thread automationThread = new Thread(() -> {
            while (running) {
                try {
                    synchronized (system) {
                        checkAutomation();
                    }
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        automationThread.setDaemon(true);
        automationThread.start();
        
        // Main GUI refresh loop
        Thread guiThread = new Thread(() -> {
            while (running) {
                try {
                    renderCurrentScreen();
                    Thread.sleep(500);
                    //SaveLoadService.saveSystem(system);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        guiThread.setDaemon(true);
        guiThread.start();
    }

    /**
     * Handle button commands from GUI
     */
    private void handleButtonCommand(String command) {
        if (currentInterface != null) {
            currentInterface.handleCommand(command);
        }
    }

    public void renderCurrentScreen() {
        if (currentInterface == null) return;
        
        String menuContent = currentInterface.getMenuContents();
        String optionsContent = currentInterface.getOptionsContents();
        String formattedDateTime = dateTimeFormatter.format(LocalDateTime.now());
        
        ViewData data = new ViewData(currentMessage, menuContent, optionsContent, formattedDateTime);
        view.renderView(data);
    }

    // IScreenNavigator implementation
    @Override
    public void showDashboard() {
        if (dashboardController == null) {
            dashboardController = new DashboardController(this, system, view);
        }
        currentInterface = dashboardController;
    }

    @Override
    public void showDevice(Device device) {
        if (deviceController == null) {
            deviceController = new DeviceDetailController(this, system, view);
        }
        deviceController.setDevice(device);
        currentInterface = deviceController;
    }

    @Override
    public void showSimulation() {
        if (simulationController == null) {
            simulationController = new SimulationController(this, system, view);
        }
        currentInterface = simulationController;
    }

    @Override
    public void showLog() {
        if (logController == null) {
            logController = new LogController(this, system, view);
        }
        currentInterface = logController;
    }

    @Override
    public void showDeviceAdder() {
        if (deviceAdderController == null) {
            deviceAdderController = new DeviceAdderController(this, system, view);
        }
        currentInterface = deviceAdderController;
    }

    @Override
    public void showDeviceRemover() {
        if (deviceRemoverController == null) {
            deviceRemoverController = new DeviceRemoverController(this, system, view);
        }
        currentInterface = deviceRemoverController;
    }

    @Override
    public void showAutomation() {
        if (automationController == null) {
            automationController = new AutomationListController(this, system, view);
        }
        currentInterface = automationController;
    }

    // IMessageManager implementation
    @Override
    public void setCurrentMessage(String message) {
        this.currentMessage = message;
    }

    @Override
    public String getCurrentMessage() {
        return currentMessage;
    }

    @Override
    public void addLogMessage(String message) {
        loggingService.addMessage(message);
    }

    // IInputHandler implementation - GUI-based dialogs
   @Override
public String setDeviceProcedure() {
    String name = view.showDeviceNameDialog();

    // Cancel pressed
    if (name == null) {
        return null;
    }

    // Empty input
    if (name.trim().isEmpty()) {
        view.showErrorMessage("Device name cannot be empty", "Input Error");
        return null;
    }

    return name.trim();
}

    @Override
    public LocalTime setTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timeStr = view.showTimeDialog();

        if (timeStr == null) {
            return null; // ✅ user cancelled
        }

        try {
            return LocalTime.parse(timeStr, timeFormatter);
        } catch (DateTimeParseException e) {
            view.showErrorMessage("Invalid time format. Please use HH:mm:ss", "Error");
            return null; // also treat invalid input as cancel/fail
        }
    }

    @Override
    public Integer setTemp() {
        Integer temp = view.showTemperatureDialog();

        if (temp == null) {
            return null;
        }

        return temp;
    }

   @Override
    public void checkAutomation() {
        LocalTime currentTime = LocalTime.now();
        int currentTemp = system.getSimulation().getTemperature();

        for (Device device : system.getAllDevices()) {

            boolean before = device.isOn();

            automationService.checkDeviceAutomation(device, currentTemp, currentTime);

            boolean after = device.isOn();

           if (before != after) {
                String state = after ? "ON" : "OFF";

                String msg =
                    device.getName() + " auto turned " + state;

                // LOG FILE
                loggingService.addMessage(
                    "[" + dateTimeFormatter.format(LocalDateTime.now()) + "] "
                    + msg + "\n"
                );

                // GUI MESSAGE (what user sees)
                setCurrentMessage(msg);
            }
        }

        int totalUsage = billingService.calculateTotalElectricityUsage(system.getAllDevices());

        thresholdManager.setThresholdExceeded(
            totalUsage > system.getSimulation().getPowerThreshold()
        );
    }

    // Backward compatibility getters
    public IInterfaceController getCurrentInterface() {
        return currentInterface;
    }

    public SmartHomeSystem getSystem() {
        return system;
    }

    public int checkTemp() {
        return system.getSimulation().getTemperature();
    }
    
    public void addMessage(String log) {
        loggingService.addMessage(log);
    }

    public void exit() {
        running = false;
        loggingService.addMessage("[" + dateTimeFormatter.format(LocalDateTime.now()) + "] " +
                "Smart Home Simulator Ended\n");
        SaveLoadService.saveSystem(system);
        System.exit(0);
    }
    
    public ILoggingService getLoggingService() {
        return loggingService;
    }
}
