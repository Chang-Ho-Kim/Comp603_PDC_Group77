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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import smarthome.model.SmartHomeSystem;
import smarthome.model.Device;
import smarthome.view.SmartHomeCLIView;

import java.util.Scanner;
import smarthome.model.*;
import smarthome.persistence.SaveLoadService;

// OBVIOUSLY MAKE THIS A SINGLETON AS THERE SHOULD ONLY BE ONE CENTRAL CONTROLLER
public class CentralController {

    private SmartHomeSystem system;
    private SmartHomeCLIView view;
    private Scanner scanner = new Scanner(System.in);

    private IInterfaceController currentInterface;
    private DashboardController dashboardController;
    private DeviceDetailController deviceController;
    private SimulationController simulationController;
    private LogController logController;
    private DeviceAdderController deviceAdderController;
    private DeviceRemoverController deviceRemoverController;
    private AutomationListController automationController;
    
    private String currentMessage;
    DateTimeFormatter dateTimeFormatter;
    DateTimeFormatter timeFormatter;
  
    public CentralController(SmartHomeSystem system, SmartHomeCLIView view){
        this.system = system;
        this.view = view;

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
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        system.addMessage("[" +LocalDateTime.now().format(dateTimeFormatter) +"] " + "Smart Home Simulator Started\n");
        
    }

    public void start(){
        // inside CentralController.start()
        Thread automationThread = new Thread(() -> {
            while (true) {
                try {
                    synchronized(system) {
                        checkAutomation();
                        // updates devices, logs, messages
                    }
                    Thread.sleep(1000);     // 1 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        automationThread.setDaemon(true); // stops with main thread
        automationThread.start();
        
        while(true){
            view.renderView(this);
            System.out.print("\nSelect option: ");
            String input = scanner.nextLine();
            currentInterface.handleCommand(input);
            System.out.println("\n\n");
            SaveLoadService.saveSystem(system); // save after each interaction, disable if too many writes
        }
    }

    public void showDashboard(){
        currentInterface = dashboardController;
    }

    public void showDevice(Device device){
        deviceController.setDevice(device);
        currentInterface = deviceController;
    }

    public void showSimulation(){
        currentInterface = simulationController;
    }
    
    public void showLog(){
        currentInterface = logController;
    }
      
    public void showDeviceAdder(){
        currentInterface = deviceAdderController;
    }  
    
    public void showDeviceRemover(){
        currentInterface = deviceRemoverController;
    } 
    
    public void showAutomation(){
        currentInterface = automationController;
    } 
    
      
    
    public String getCurrentMessage(){
        return currentMessage;
    }
    
    public void setCurrentMessage(String message){
        currentMessage = message;
    }
    
    public String setDeviceProcedure(){
   
       System.out.println("What is it's name?: ");
        String name = scanner.nextLine();
        return name;
    }
    
    public void removeDeviceProcedure(){
     
        
     
    }
    
    public void addMessage(String log){
        system.addMessage(log);
    }
    
    public ArrayList<String> getMessages(){
        return system.getMessages();
    }
    
    public void deleteLog(){
        system.deleteLog();
        currentMessage = "Log was deleted";
    }
    
    public void displayTotalEnergyUsage(){
        system.calculateTotalElectricityUsage();
        System.out.println(system.getTotalElectricityUsage() + " Watts/Hour" );
    }
    
     public IInterfaceController getCurrentInterface() {
        return currentInterface;
    }
    
     public DashboardController getDashboardController() {
        return dashboardController;
    } 
     
   public LocalTime setTime() {
    System.out.println("Set Time: (HH:mm:ss)");

    while (true) {
        String time = scanner.nextLine();

        try {
            LocalTime parsedTime = LocalTime.parse(time, timeFormatter);
            return parsedTime; 
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please use HH:mm:ss");
            System.out.print("Try again: ");
        }
    }
}
    
    public int checkTemp(){
        return system.getSimulation().getTemperature();
    }
    
    public int setTemp(){
        System.out.println("Set Temperature: ");
        String temp = scanner.nextLine();

        try {
            int parsedTemp = Integer.parseInt(temp);
            return parsedTemp;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format. Please use HH:mm:ss");
        }
        return -67777;
    }
    
    public void checkAutomation(){
        for (Device d: system.getAllDevices()){
                
                if(d.isAutoOn()){
                    boolean wasOn = d.isOn();
                    d.checkAutomation(checkTemp(), LocalTime.now());
                    if(wasOn != d.isOn()){
                        system.addMessage("[" +LocalDateTime.now().format(dateTimeFormatter) +"] " + "Automation action: " + d.getName() +"\n");
                            currentMessage =  "Automation action: " + d.getName() +"\n";
                    }
                }
          
        }
    }
    
    public DateTimeFormatter getFormatter() {
        return dateTimeFormatter;
    }

    public SmartHomeCLIView getView() {
        return view;
    }
    
    public void exit(){
        System.out.println("Exiting...");
        system.addMessage("[" +LocalDateTime.now().format(dateTimeFormatter) +"] " + "Smart Home Simulator Ended\n");
        System.exit(0);
    }
}