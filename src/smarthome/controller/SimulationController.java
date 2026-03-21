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
import smarthome.model.SmartHomeSystem;
import smarthome.model.SimulationSettings;
import smarthome.view.SmartHomeCLIView;

public class SimulationController implements IInterfaceController {

    private CentralController controller;
    private SimulationSettings simulation;
    private SmartHomeCLIView view;

    public SimulationController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view){
        this.controller = controller;
        this.simulation = system.getSimulation();
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        return "=== SIMULATION SETTINGS ===\n\n" +
        "Temperature: " + simulation.getTemperature();
    }

    @Override
    public String getOptionsContents() {
        return "1. Increase Temperature by 1\n"+
        "2. Decrease Temperature by 1\n"+
        "                                   (0. Back to Dashboard)";
    }
    
    @Override
    public void handleCommand(String command){
        switch(command){
            case "1": simulation.setTemperature(simulation.getTemperature()+1); controller.setCurrentMessage("Temperature increased to " + simulation.getTemperature()); controller.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] " +"Temperature increased to "+simulation.getTemperature()+"\n"); controller.checkAutomation();break;
            case "2": simulation.setTemperature(simulation.getTemperature()-1);controller.setCurrentMessage("Temperature decreased to " + simulation.getTemperature());controller.addMessage("[" +LocalDateTime.now().format(controller.dateTimeFormatter) +"] " +"Temperature decreased to "+simulation.getTemperature()+"\n"); controller.checkAutomation(); break;
            case "0": controller.showDashboard();controller.checkAutomation(); return;
            default: view.showInvalidOption();
        }
    }

    
}