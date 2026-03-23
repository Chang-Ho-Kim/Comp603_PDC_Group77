package smarthome.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import smarthome.model.Device;
import smarthome.model.PowerSaverDevice;
import smarthome.model.ScheduledDevice;
import smarthome.model.SensorDevice;
import smarthome.model.SmartHomeSystem;
import smarthome.model.BillingService;
import smarthome.view.ISmartHomeView;

public class DeviceDetailController implements IInterfaceController {

    private final CentralController controller;
    private final SmartHomeSystem system;
    private final ISmartHomeView view;
    private Device device;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public DeviceDetailController(CentralController controller, SmartHomeSystem system, ISmartHomeView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

    public void setDevice(Device device){
        this.device = device;
    }

    @Override
    public String getMenuContents(){
        DecimalFormat df = new DecimalFormat("0.000000000");
        
        StringBuilder menu = new StringBuilder("=== DEVICE DETAILS ===\n" +
        "\nName: " + device.getName()+
        "\nType: " + device.getType()+
        "\nElectricity Rate: " + device.getElectricityUsage() + " Watts/Hour"+
        "\nTotal Usage Cost: $" + df.format(BillingService.calculateDeviceCost(device, system.getECost())) +  
        "\nState: " + (device.isOn() ? "ON" : "OFF"));
        
        if(device instanceof ScheduledDevice sd){
            menu.append("\nSchedule Mode: ").append(sd.isScheduleOn() ? "On" : "Off");
            menu.append("\nSchedule: ").append(sd.getStart()).append(" - ").append(sd.getEnd());
        }
        if(device instanceof SensorDevice s){
            menu.append("\nSensor Mode: ").append(s.isSensorOn() ? "On" : "Off");
            menu.append("\nSensor range: ").append(s.getLower()).append(" - ").append(s.getUpper());
        }
        if(device instanceof PowerSaverDevice){
            menu.append("\n\nNote: This powersaver device will turn off if \n      total electricity usage exceeds Threshold");
        }
        
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        StringBuilder options = new StringBuilder( 
        "1. Turn ON\n"+
        "2. Turn OFF\n");
        if(device instanceof ScheduledDevice){
            options.append("3. Set scheduled start time\n" + "4. Set scheduled end time \n"+"5. Toggle Schedule Mode\n");
        }
        else if(device instanceof SensorDevice){
            options.append("3. Set sensor lower threshold\n" + "4. Set sensor upper threshold \n" +"5. Toggle Sensor Mode\n");
        }
        
        options.append("                                   (0. Back to Dashboard)");
        return options.toString();
    }

    @Override
    public void handleCommand(String command){
        switch(command){
            case "1":
                if(!device.isOn()){
                    device.turnOn(); 
                    String msg = device.getName() + " was turned on";
                    controller.setCurrentMessage(msg); 
                    system.addMessage("[" + LocalDateTime.now().format(controller.getDateTimeFormatter()) + "] " + msg + "\n"); 
                } else {
                    controller.setCurrentMessage(device.getName() + " is already on");
                }
                break;
            case "2":
                if(device.isOn()){
                    device.turnOff(); 
                    String msg = device.getName() + " was turned off";
                    controller.setCurrentMessage(msg);
                    system.addMessage("[" + LocalDateTime.now().format(controller.getDateTimeFormatter()) + "] " + msg + "\n"); 
                } else {
                    controller.setCurrentMessage(device.getName() + " is already off");
                }
                break;
             case "3":
                handleOptionThree();
                break;
             case "4":
                handleOptionFour();
                break;
             case "5":
                handleOptionFive();
                break;
            case "0":
                controller.showDashboard();
                break;
            default:
                controller.setCurrentMessage("Invalid option");
        }
    }

    private void handleOptionThree() {
        if (device instanceof ScheduledDevice sd) {
            LocalTime time = promptForTime("Enter start time (HH:mm:ss): ");
            if (time != null) {
                if (sd.getEnd() != null && time.isAfter(sd.getEnd()) && !sd.getEnd().equals(LocalTime.MIDNIGHT)) {
                    controller.setCurrentMessage("Start time must be before end time!");
                } else {
                    sd.setStart(time);
                    controller.setCurrentMessage(device.getName() + "'s start time was set");
                }
            }
        } else if (device instanceof SensorDevice s) {
            Integer temp = promptForInt("Enter lower threshold: ");
            if (temp != null) {
                if (s.getUpper() != 0 && temp > s.getUpper()) {
                    controller.setCurrentMessage("Lower threshold must be less than upper");
                } else {
                    s.setLower(temp);
                    controller.setCurrentMessage(device.getName() + "'s lower threshold was set");
                }
            }
        }
    }

    private void handleOptionFour() {
        if (device instanceof ScheduledDevice sd) {
            LocalTime time = promptForTime("Enter end time (HH:mm:ss): ");
            if (time != null) {
                if (sd.getStart() != null && time.isBefore(sd.getStart())) {
                    controller.setCurrentMessage("End time must be after start time!");
                } else {
                    sd.setEnd(time);
                    controller.setCurrentMessage(device.getName() + "'s end time was set");
                }
            }
        } else if (device instanceof SensorDevice s) {
            Integer temp = promptForInt("Enter upper threshold: ");
            if (temp != null) {
                if (temp < s.getLower()) {
                    controller.setCurrentMessage("Upper threshold must be higher than lower");
                } else {
                    s.setUpper(temp);
                    controller.setCurrentMessage(device.getName() + "'s upper threshold was set");
                }
            }
        }
    }

    private void handleOptionFive() {
        if (device instanceof ScheduledDevice sd) {
            sd.setScheduleOn(!sd.isScheduleOn());
            controller.setCurrentMessage("Schedule mode is now " + (sd.isScheduleOn() ? "ON" : "OFF"));
        } else if (device instanceof SensorDevice s) {
            s.setSensorOn(!s.isSensorOn());
            controller.setCurrentMessage("Sensor mode is now " + (s.isSensorOn() ? "ON" : "OFF"));
        }
    }

    private LocalTime promptForTime(String prompt) {
        while (true) {
            String input = view.getInput(prompt);
            if (input.equalsIgnoreCase("cancel")) return null;
            try {
                return LocalTime.parse(input, timeFormatter);
            } catch (DateTimeParseException e) {
                view.displayError("Invalid time format (HH:mm:ss). Type 'cancel' to abort.");
            }
        }
    }

    private Integer promptForInt(String prompt) {
        while (true) {
            String input = view.getInput(prompt);
            if (input.equalsIgnoreCase("cancel")) return null;
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                view.displayError("Invalid number format. Type 'cancel' to abort.");
            }
        }
    }
}
