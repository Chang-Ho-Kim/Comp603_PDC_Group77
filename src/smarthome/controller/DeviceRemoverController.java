package smarthome.controller;

import java.time.LocalDateTime;
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeGUIView;
import java.util.ArrayList;

public class DeviceRemoverController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeGUIView view;
    private ArrayList<Device> deviceList;

    public DeviceRemoverController(CentralController controller, SmartHomeSystem system, SmartHomeGUIView view){
        this.controller = controller;
        this.system = system;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        deviceList = new ArrayList<>(system.getAllDevices());

        StringBuilder menu = new StringBuilder("=== REMOVE DEVICE ===\n\n");

        if (deviceList.isEmpty()) {
            menu.append("No devices to remove.\n");
        } else {
            menu.append("Select a device to remove:\n\n");

            menu.append("A. Remove ALL Devices\n\n");

            int i = 1;
            for (Device d : deviceList) {
                menu.append(i)
                    .append(". ")
                    .append(d.getName())
                    .append(" [")
                    .append(d.isOn() ? "ON" : "OFF")
                    .append("]\n");
                i++;
            }
        }

        return menu.toString();
    }

    @Override
    public String getOptionsContents() {

        deviceList = new ArrayList<>(system.getAllDevices());

        if (deviceList.isEmpty()) {
            return "0. Back to Dashboard";
        }

        StringBuilder options = new StringBuilder();

        options.append("A. Remove ALL Devices\n");

        int i = 1;
        for (Device d : deviceList) {
            options.append(i)
                    .append(". Remove ")
                    .append(d.getName())
                    .append("\n");
            i++;
        }

        options.append("0. Cancel");

        return options.toString();
    }

    @Override
    public void handleCommand(String command){

        if (command.equals("0")) {
            controller.setCurrentMessage("Removal cancelled");
            controller.showDashboard();
            return;
        }

        // =========================
        // 🚨 REMOVE ALL DEVICES
        // =========================
        if (command.equalsIgnoreCase("A")) {

            deviceList = new ArrayList<>(system.getAllDevices());

            if (deviceList.isEmpty()) {
                controller.setCurrentMessage("No devices to remove");
                controller.showDashboard();
                return;
            }

            boolean confirm = view.showConfirmDialog(
                    "Remove ALL devices?",
                    "Confirm Bulk Removal"
            );

            if (confirm) {

                for (Device d : deviceList) {
                    system.removeDevice(d.getId());

                    controller.addLogMessage(
                        "[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] "
                        + d.getName() + " was removed\n"
                    );
                }

                controller.setCurrentMessage("All devices removed");

            } else {
                controller.setCurrentMessage("Bulk removal cancelled");
            }

            controller.showDashboard();
            return;
        }

        // =========================
        // 🧩 REMOVE SINGLE DEVICE
        // =========================
        try {
            int index = Integer.parseInt(command);

            deviceList = new ArrayList<>(system.getAllDevices());

            if (index > 0 && index <= deviceList.size()) {

                Device device = deviceList.get(index - 1);
                String deviceName = device.getName();

                boolean confirm = view.showConfirmDialog(
                        "Remove " + deviceName + "?",
                        "Confirm Removal"
                );

                if (confirm) {
                    system.removeDevice(device.getId());

                    controller.setCurrentMessage(deviceName + " removed");

                    controller.addLogMessage(
                        "[" + LocalDateTime.now().format(controller.dateTimeFormatter) + "] "
                        + deviceName + " was removed\n"
                    );
                } else {
                    controller.setCurrentMessage("Removal cancelled");
                }

            } else {
                view.showInvalidOption();
                controller.setCurrentMessage("Invalid selection");
            }

        } catch (NumberFormatException e) {
            view.showInvalidOption();
            controller.setCurrentMessage("Invalid input");
        }

        controller.showDashboard();
    }
}