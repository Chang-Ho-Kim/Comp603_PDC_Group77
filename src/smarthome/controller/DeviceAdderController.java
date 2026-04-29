package smarthome.controller;

import java.time.LocalDateTime;
import smarthome.model.*;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeGUIView;

public class DeviceAdderController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeGUIView view;
   
    public DeviceAdderController(CentralController controller, SmartHomeSystem system, SmartHomeGUIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
       return "➕ === ADD DEVICE ===\n\nSelect device type:\n\n" +
      "1. 🔥 Heater\n" +
      "2. 💡 Light\n" +
      "3. ❄️ Air Conditioner\n" +
      "4. ⏰ Alarm Clock\n" +
      "5. 🚪 Door\n" +
      "6. 🎵 Music Player\n" +
      "7. 📺 Television\n" +
      "8. 🤖 Robot Cleaner\n";
    }
    
    @Override
    public String getOptionsContents() {
        return "Select device type (1-8) or 0 to cancel";
    }

    @Override
    public void handleCommand(String command){
        String name;
        
        switch(command){
            case "1":
                name = controller.setDeviceProcedure();
                system.addDevice(new Heater(name));
                controller.setCurrentMessage("✅ " + name + " added");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + name + " was added\n");
                break;
            case "2":
                name = controller.setDeviceProcedure();
                system.addDevice(new Light(name));
                controller.setCurrentMessage("✅ " + name + " added");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + name + " was added\n");
                break;
            case "3":
                name = controller.setDeviceProcedure();
                system.addDevice(new AirCon(name));
                controller.setCurrentMessage("✅ " + name + " added");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + name + " was added\n");
                break;
            case "4":
                name = controller.setDeviceProcedure();
                system.addDevice(new AlarmClock(name));
                controller.setCurrentMessage("✅ " + name + " added");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + name + " was added\n");
                break;
            case "5":
                name = controller.setDeviceProcedure();
                system.addDevice(new Door(name));
                controller.setCurrentMessage("✅ " + name + " added");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + name + " was added\n");
                break;
            case "6":
                name = controller.setDeviceProcedure();
                system.addDevice(new MusicPlayer(name));
                controller.setCurrentMessage("✅ " + name + " added");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + name + " was added\n");
                break;
            case "7":
                name = controller.setDeviceProcedure();
                system.addDevice(new TV(name));
                controller.setCurrentMessage("✅ " + name + " added");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + name + " was added\n");
                break;
            case "8":
                name = controller.setDeviceProcedure();
                system.addDevice(new RobotCleaner(name));
                controller.setCurrentMessage("✅ " + name + " added");
                controller.addLogMessage("[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] " + name + " was added\n");
                break;
            case "0":
                controller.showDashboard();
                return;
            default:
                view.showInvalidOption();
                return;
        }
        controller.showDashboard();
    }
}
