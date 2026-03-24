package smarthome.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.service.DependencyContainer;
import smarthome.service.IBillingService;
import smarthome.view.SmartHomeCLIView;

/**
 * DeviceDetailController - Shows device details and handles device-specific commands.
 * Now uses IDeviceUIHandler for Open/Closed Principle - no more instanceof checks!
 */
public class DeviceDetailController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeCLIView view;
    private Device device;
    private IBillingService billingService;
    private DecimalFormat currencyFormatter = new DecimalFormat("0.000000000");

    public DeviceDetailController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view) {
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
        StringBuilder menu = new StringBuilder("=== DEVICE DETAILS ===\n");
        menu.append("\nName: ").append(device.getName());
        menu.append("\nType: ").append(device.getType());
        menu.append("\nElectricity Rate: ").append(device.getElectricityUsage()).append(" Watts/Hour");
        menu.append("\nTotal Usage Cost: $").append(
            currencyFormatter.format(
                billingService.calculateDeviceBill(device, system.getSimulation().getElectricityCost())
            )
        );
        menu.append("\nState: ").append(device.isOn() ? "ON" : "OFF");
        menu.append(device.getAdditionalMenuContent());
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        StringBuilder options = new StringBuilder("1. Turn ON\n2. Turn OFF\n");
        options.append(device.getAdditionalOptions());
        options.append("(0. Back to Dashboard)");
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
            case "4":
            case "5":
                boolean handled = device.handleDeviceCommand(command, controller);
                if (handled) {
                    controller.setCurrentMessage(device.getName() + " was updated");
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

    private void handleTurnOn() {
        if (!device.isOn()) {
            device.turnOn();
            controller.setCurrentMessage(device.getName() + " was turned on");
            controller.addLogMessage("[" + controller.dateTimeFormatter.format(LocalDateTime.now()) + "] " +
                    device.getName() + " was turned on\n");
        } else {
            controller.setCurrentMessage(device.getName() + " is already on");
        }
    }

    private void handleTurnOff() {
        if (device.isOn()) {
            device.turnOff();
            controller.setCurrentMessage(device.getName() + " was turned off");
            controller.addLogMessage("[" + controller.dateTimeFormatter.format(LocalDateTime.now()) + "] " +
                    device.getName() + " was turned off\n");
        } else {
            controller.setCurrentMessage(device.getName() + " is already off");
        }
    }
}
