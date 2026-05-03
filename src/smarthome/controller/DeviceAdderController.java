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

    // 🔧 FIX: Now GUI can generate buttons from this
    @Override
    public String getOptionsContents() {
        return ""
                + "1. Heater\n"
                + "2. Light\n"
                + "3. Air Conditioner\n"
                + "4. Alarm Clock\n"
                + "5. Door\n"
                + "6. Music Player\n"
                + "7. Television\n"
                + "8. Robot Cleaner\n"
                + "0. Cancel";
    }

    @Override
    public void handleCommand(String command){

        String name;

        switch(command){

            case "1":
                name = controller.setDeviceProcedure();
                system.addDevice(new Heater(name));
                break;

            case "2":
                name = controller.setDeviceProcedure();
                system.addDevice(new Light(name));
                break;

            case "3":
                name = controller.setDeviceProcedure();
                system.addDevice(new AirCon(name));
                break;

            case "4":
                name = controller.setDeviceProcedure();
                system.addDevice(new AlarmClock(name));
                break;

            case "5":
                name = controller.setDeviceProcedure();
                system.addDevice(new Door(name));
                break;

            case "6":
                name = controller.setDeviceProcedure();
                system.addDevice(new MusicPlayer(name));
                break;

            case "7":
                name = controller.setDeviceProcedure();
                system.addDevice(new TV(name));
                break;

            case "8":
                name = controller.setDeviceProcedure();
                system.addDevice(new RobotCleaner(name));
                break;

            case "0":
                controller.setCurrentMessage("🚫 Device addition cancelled");
                controller.showDashboard();
                return;

            default:
                view.showInvalidOption();
                return;
        }

        // ✅ shared success handling (no duplication)
        controller.setCurrentMessage("✅ " + name + " added");

        controller.addLogMessage(
            "[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] "
            + name + " was added\n"
        );

        controller.showDashboard();
    }
}