package smarthome.controller;

import java.time.LocalDateTime;
import smarthome.model.SmartHomeSystem;
import smarthome.model.SimulationSettings;
import smarthome.view.SmartHomeGUIView;

public class SimulationController implements IInterfaceController {

    private CentralController controller;
    private SimulationSettings simulation;
    private SmartHomeGUIView view;

    public SimulationController(CentralController controller, SmartHomeSystem system, SmartHomeGUIView view){
        this.controller = controller;
        this.simulation = system.getSimulation();
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        return "=== SIMULATION SETTINGS ===\n\n" +
        "Temperature: " + simulation.getTemperature() + "°C\n"+
        "Electricity Rate: $" + simulation.getElectricityCost()+ " / Watt-hour\n"+
        "Power Saver Threshold: " + simulation.getPowerThreshold() + " Watts";
    }

    @Override
    public String getOptionsContents() {
        return "1. Increase Temperature by 1\n"+
        "2. Decrease Temperature by 1\n"+
        "3. Set Custom Temperature\n"+
        "0. Back to Dashboard";
    }
    
    @Override
    public void handleCommand(String command){
        switch(command){
            case "1":
                simulation.setTemperature(simulation.getTemperature()+1);
                controller.setCurrentMessage("Temperature increased to " + simulation.getTemperature());
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] Temperature increased to " + simulation.getTemperature() + "\n");
                controller.checkAutomation();
                break;
            case "2":
                simulation.setTemperature(simulation.getTemperature()-1);
                controller.setCurrentMessage("Temperature decreased to " + simulation.getTemperature());
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] Temperature decreased to " + simulation.getTemperature() + "\n");
                controller.checkAutomation();
                break;
            case "3":
                int temp = controller.setTemp();
                if (temp != -1) {
                    simulation.setTemperature(temp);
                    controller.setCurrentMessage("Temperature set to " + temp);
                    controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] Temperature set to " + temp + "\n");
                    controller.checkAutomation();
                }
                break;
            case "0":
                controller.showDashboard();
                controller.checkAutomation();
                return;
            default:
                view.showInvalidOption();
        }
    }
}
