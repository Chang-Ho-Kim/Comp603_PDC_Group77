package smarthome.controller;

import java.time.LocalDateTime;
import smarthome.model.SimulationSettings;
import smarthome.model.SmartHomeSystem;
import smarthome.view.ISmartHomeView;

public class SimulationController implements IInterfaceController {

    private final CentralController controller;
    private final SimulationSettings simulation;
    private final ISmartHomeView view;

    public SimulationController(CentralController controller, SmartHomeSystem system, ISmartHomeView view){
        this.controller = controller;
        this.simulation = system.getSimulation();
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        return "=== SIMULATION SETTINGS ===\n\n" +
        "Temperature: " + simulation.getTemperature() + "°C\n" +
        "Electricity Rate: $" + simulation.getElectricityCost() + " / Watt-hour\n" +
        "Power Saver Threshold: " + simulation.getPowerThreshold() + " Watts";
    }

    @Override
    public String getOptionsContents() {
        return "1. Increase Temperature\n"+
        "2. Decrease Temperature\n"+
        "3. Set Power Saver Threshold\n" +
        "                                   (0. Back to Dashboard)";
    }
    
    @Override
    public void handleCommand(String command){
        switch(command){
            case "1":
                simulation.setTemperature(simulation.getTemperature() + 1);
                updateMessage("Temperature increased to " + simulation.getTemperature() + "°C");
                break;
            case "2":
                simulation.setTemperature(simulation.getTemperature() - 1);
                updateMessage("Temperature decreased to " + simulation.getTemperature() + "°C");
                break;
            case "3":
                setThreshold();
                break;
            case "0":
                controller.showDashboard();
                break;
            default:
                controller.setCurrentMessage("Invalid option");
        }
    }

    private void updateMessage(String msg) {
        controller.setCurrentMessage(msg);
        controller.addMessage("[" + LocalDateTime.now().format(controller.getDateTimeFormatter()) + "] " + msg + "\n");
    }

    private void setThreshold() {
        String input = view.getInput("Enter new power threshold (Watts): ");
        try {
            int threshold = Integer.parseInt(input);
            simulation.setPowerThreshold(threshold);
            updateMessage("Power threshold set to " + threshold + " Watts");
        } catch (NumberFormatException e) {
            controller.setCurrentMessage("Invalid threshold value");
        }
    }
}
