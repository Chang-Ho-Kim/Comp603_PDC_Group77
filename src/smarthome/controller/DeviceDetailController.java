package smarthome.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.service.DependencyContainer;
import smarthome.service.IBillingService;
import smarthome.view.SmartHomeGUIView;

/**
 * DeviceDetailController - Shows device details and handles device-specific commands.
 */
public class DeviceDetailController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeGUIView view;
    private Device device;
    private IBillingService billingService;
    private DecimalFormat currencyFormatter = new DecimalFormat("0.000000000");

    public DeviceDetailController(CentralController controller, SmartHomeSystem system, SmartHomeGUIView view) {
        this.controller = controller;
        this.system = system;
        this.view = view;
        this.billingService = DependencyContainer.getInstance().getBillingService();
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public String getMenuContents() {
        StringBuilder menu = new StringBuilder("=== DEVICE DETAILS ===\n\n");
        menu.append("Name: ").append(device.getName()).append("\n");
        menu.append("Type: ").append(device.getType()).append("\n");
        menu.append("Electricity Rate: ").append(device.getElectricityUsage()).append(" Watts/Hour\n");
        menu.append("Total Usage Cost: $").append(
            currencyFormatter.format(
                billingService.calculateDeviceBill(device, system.getSimulation().getElectricityCost())
            )
        ).append("\n");
        menu.append("State: ").append(device.isOn() ? "ON" : "OFF").append("\n");
        menu.append(device.getAdditionalMenuContent());
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        StringBuilder options = new StringBuilder("1. Turn ON\n2. Turn OFF\n");
        options.append("3. Set Electricity Rate\n");
        options.append(device.getAdditionalOptions());
        options.append("0. Back to Dashboard");
        return options.toString();
    }

    @Override
    public void handleCommand(String command) {
        switch (command) {

            case "1":
                handleTurnOn();
                break;

            case "2":
                handleTurnOff();
                break;

            case "3":
                setDeviceElectricityRate();
                break;

            case "4":
            case "5":
            case "6":
                boolean handled = device.handleDeviceCommand(command, controller);
                if (handled) {
                    controller.setCurrentMessage(device.getName() + " updated");
                } else {
                    view.showInvalidOption();
                    controller.setCurrentMessage("Failed to update " + device.getName());
                }
                break;

            case "0":
                controller.showDashboard();
                return;

            default:
                view.showInvalidOption();
        }
    }

    // =========================
    // ⚡ NEW FEATURE
    // =========================
    private void setDeviceElectricityRate() {

        Integer newRate = view.showElectricityRateDialog(); 
        // reuse dialog style (you can rename later if you want)

        if (newRate == null) {
            controller.setCurrentMessage("Electricity rate update cancelled");
            return;
        }

        if (newRate < 0) {
            view.showErrorMessage("Rate cannot be negative", "Input Error");
            return;
        }

        device.setElectricityUsage(newRate);

        controller.setCurrentMessage(
                device.getName() + " electricity rate set to " + newRate + "W"
        );

        controller.addLogMessage(
                "[" + controller.dateTimeFormatter.format(LocalDateTime.now()) + "] "
                        + device.getName() + " electricity rate changed to " + newRate + "W\n"
        );
    }

    private void handleTurnOn() {
        if (!device.isOn()) {
            device.turnOn();
            controller.setCurrentMessage(device.getName() + " turned ON");
            controller.addLogMessage("[" + controller.dateTimeFormatter.format(LocalDateTime.now()) + "] " +
                    device.getName() + " was turned on\n");
        } else {
            controller.setCurrentMessage("ℹ️ " + device.getName() + " is already ON");
        }
    }

    private void handleTurnOff() {
        if (device.isOn()) {
            device.turnOff();
            controller.setCurrentMessage(device.getName() + " turned OFF");
            controller.addLogMessage("[" + controller.dateTimeFormatter.format(LocalDateTime.now()) + "] " +
                    device.getName() + " was turned off\n");
        } else {
            controller.setCurrentMessage(device.getName() + " is already OFF");
        }
    }
}