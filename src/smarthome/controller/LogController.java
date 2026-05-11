package smarthome.controller;

import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;
import smarthome.service.DependencyContainer;
import smarthome.service.IBillingService;
import smarthome.view.SmartHomeGUIView;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class LogController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeGUIView view;
    private SmartHomeSystem system;

    private IBillingService billingService;
    private DecimalFormat currencyFormatter = new DecimalFormat("0.000000000");

    public LogController(CentralController controller,
                         SmartHomeSystem system,
                         SmartHomeGUIView view) {

        this.controller = controller;
        this.view = view;
        this.system = system;

        this.billingService =
                DependencyContainer.getInstance().getBillingService();
    }

    @Override
    public String getMenuContents() {

        String logs = controller.getLoggingService()
                .getMessages()
                .toString();

        if (logs.isEmpty()) {
            return "=== APPLICATION LOG ===\n\nNo logs yet.";
        }

        return "=== APPLICATION LOG ===\n\n" + logs;
    }

    @Override
    public String getOptionsContents() {
        return "1. Delete Log\n" +
               "2. Export Log\n" +
               "3. Export Device Summary\n" +
               "0. Back to Dashboard";
    }

    @Override
    public void handleCommand(String command) {

        switch (command) {

            case "0":
                controller.showDashboard();
                return;

            case "1":
                if (view.showConfirmDialog("Delete all logs?", "Confirm")) {
                    controller.getLoggingService().clearMessages();
                    controller.setCurrentMessage("Log deleted");
                } else {
                    controller.setCurrentMessage("Deletion cancelled");
                }
                return;

            case "2":
                exportLogWithFileChooser();
                return;

            case "3":
                exportDeviceSummaryWithFileChooser();
                return;

            default:
                view.showInvalidOption();
        }
    }

    // =========================
    // 📁 EXPORT LOG
    // =========================
    private void exportLogWithFileChooser() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Log File");
        fileChooser.setSelectedFile(new File("Log.txt"));

        int result = fileChooser.showSaveDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(file)) {

                writer.write(
                        controller.getLoggingService()
                                .getMessages()
                                .toString()
                );

                controller.setCurrentMessage(
                        "Log exported to " + file.getName()
                );

            } catch (IOException e) {

                controller.setCurrentMessage(
                        "Export failed: " + e.getMessage()
                );
            }

        } else {

            controller.setCurrentMessage("Export cancelled");
        }
    }

    // =========================
    // 📁 EXPORT DEVICE SUMMARY
    // =========================
    private void exportDeviceSummaryWithFileChooser() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Device Summary");
        fileChooser.setSelectedFile(new File("DeviceSummary.txt"));

        int result = fileChooser.showSaveDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(file)) {

                StringBuilder summary = new StringBuilder();

                summary.append("=== SMART HOME DEVICE SUMMARY ===\n\n");

                for (Device device : system.getAllDevices()) {

                    summary.append("========================================\n");
                    summary.append("Name: ").append(device.getName()).append("\n");
                    summary.append("Type: ").append(device.getType()).append("\n");
                    summary.append("Electricity Rate: ")
                            .append(device.getElectricityUsage())
                            .append(" Watts/Hour\n");

                    summary.append("Total Usage Cost: $")
                            .append(
                                    currencyFormatter.format(
                                            billingService.calculateDeviceBill(
                                                    device,
                                                    system.getSimulation()
                                                           .getElectricityCost()
                                            )
                                    )
                            )
                            .append("\n");

                    summary.append("State: ")
                            .append(device.isOn() ? "ON" : "OFF")
                            .append("\n");

                    String additional = device.getAdditionalMenuContent();

                    if (additional != null && !additional.trim().isEmpty()) {
                        summary.append(additional);

                        if (!additional.endsWith("\n")) {
                            summary.append("\n");
                        }
                    }

                    summary.append("========================================\n\n");
                }

                writer.write(summary.toString());

                controller.setCurrentMessage(
                        "Device summary exported to " + file.getName()
                );

            } catch (IOException e) {

                controller.setCurrentMessage(
                        "Export failed: " + e.getMessage()
                );
            }

        } else {

            controller.setCurrentMessage("Export cancelled");
        }
    }
}