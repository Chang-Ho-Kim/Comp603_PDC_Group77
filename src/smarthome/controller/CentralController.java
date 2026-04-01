package smarthome.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import smarthome.model.SmartHomeSystem;
import smarthome.model.Device;
import smarthome.service.*;
import smarthome.view.View;
import smarthome.view.ViewData;
import smarthome.persistence.SaveLoadService;

/**
 * CentralController - Refactored to follow SOLID principles.
 * 
 * Responsibilities:
 * - Main application loop and screen navigation
 * - Delegation to sub-controllers
 * 
 * Now implements segregated interfaces:
 * - IMessageManager: For UI messages
 * - IScreenNavigator: For screen navigation
 * - IInputHandler: For user input procedures
 * 
 * Depends on services via dependency injection instead of doing work itself.
 */
public class CentralController implements ICentralController, IMessageManager, IScreenNavigator, IInputHandler {

    private final SmartHomeSystem system;
    private final View view;
    private final Scanner scanner;
    
    // Services (now injected)
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
    
    // For backward compatibility
    public final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public CentralController(SmartHomeSystem system, View view) {
        this.system = system;
        this.view = view;
        this.scanner = new Scanner(System.in);
        
        // Get services from dependency container
        DependencyContainer container = DependencyContainer.getInstance();
        this.loggingService = container.getLoggingService();
        this.automationService = container.getAutomationService();
        this.billingService = container.getBillingService();
        this.thresholdManager = container.getThresholdManager();
        
        // Initialize state
        this.currentMessage = "How may I be of assistance?";
        
        // Initialize sub-controllers on first use (lazy)
        currentInterface = null;
        
        // Log startup
        loggingService.addMessage("[" + dateTimeFormatter.format(LocalDateTime.now()) + "] " +
                "Smart Home Simulator Started\n");
    }

    @Override
    public void start() {
        // Ensure dashboard is shown first
        showDashboard();
        
        // Start automation thread
        Thread automationThread = new Thread(() -> {
            while (true) {
                try {
                    synchronized (system) {
                        checkAutomation();
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        automationThread.setDaemon(true);
        automationThread.start();
        
        // Main application loop
        while (true) {
            try {
                renderCurrentScreen();
                System.out.print("\nSelect option: ");
                String input = scanner.nextLine();
                currentInterface.handleCommand(input);
                System.out.println("\n\n");
                SaveLoadService.saveSystem(system);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    public void renderCurrentScreen() {
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
            dashboardController = new DashboardController(this, system, (smarthome.view.SmartHomeCLIView) view);
        }
        currentInterface = dashboardController;
    }

    @Override
    public void showDevice(Device device) {
        if (deviceController == null) {
            deviceController = new DeviceDetailController(this, system, (smarthome.view.SmartHomeCLIView) view);
        }
        deviceController.setDevice(device);
        currentInterface = deviceController;
    }

    @Override
    public void showSimulation() {
        if (simulationController == null) {
            simulationController = new SimulationController(this, system, (smarthome.view.SmartHomeCLIView) view);
        }
        currentInterface = simulationController;
    }

    @Override
    public void showLog() {
        if (logController == null) {
            logController = new LogController(this, system, (smarthome.view.SmartHomeCLIView) view);
        }
        currentInterface = logController;
    }

    @Override
    public void showDeviceAdder() {
        if (deviceAdderController == null) {
            deviceAdderController = new DeviceAdderController(this, system, (smarthome.view.SmartHomeCLIView) view);
        }
        currentInterface = deviceAdderController;
    }

    @Override
    public void showDeviceRemover() {
        if (deviceRemoverController == null) {
            deviceRemoverController = new DeviceRemoverController(this, system, (smarthome.view.SmartHomeCLIView) view);
        }
        currentInterface = deviceRemoverController;
    }

    @Override
    public void showAutomation() {
        if (automationController == null) {
            automationController = new AutomationListController(this, system, (smarthome.view.SmartHomeCLIView) view);
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

    // IInputHandler implementation
    @Override
    public String setDeviceProcedure() {
        System.out.println("What is its name?: ");
        return scanner.nextLine();
    }

    @Override
    public LocalTime setTime() {
        System.out.println("Set Time: (HH:mm:ss)");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        while (true) {
            String time = scanner.nextLine();
            try {
                return LocalTime.parse(time, timeFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use HH:mm:ss");
                System.out.print("Try again: ");
            }
        }
    }

    @Override
    public int setTemp() {
        System.out.println("Set Temperature: ");
        try {
            String temp = scanner.nextLine();
            return Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            System.out.println("Invalid temperature. Please enter a number");
            return -1;
        }
    }

    @Override
    public void checkAutomation() {
        LocalTime currentTime = LocalTime.now();
        int currentTemp = system.getSimulation().getTemperature();
        
        automationService.checkAllDevicesAutomation(system.getAllDevices(), currentTemp, currentTime);
        
        // Update threshold manager based on current usage
        int totalUsage = billingService.calculateTotalElectricityUsage(system.getAllDevices());
        thresholdManager.setThresholdExceeded(totalUsage > system.getSimulation().getPowerThreshold());
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
        System.out.println("Exiting...");
        loggingService.addMessage("[" + dateTimeFormatter.format(LocalDateTime.now()) + "] " +
                "Smart Home Simulator Ended\n");
        System.exit(0);
    }
    
    /**
     * Get the logging service for direct access to logs
     * @return The ILoggingService instance
     */
    public ILoggingService getLoggingService() {
        return loggingService;
    }
}
