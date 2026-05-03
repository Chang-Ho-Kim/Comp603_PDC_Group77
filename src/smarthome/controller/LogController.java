package smarthome.controller;

import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeGUIView;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeGUIView view;

    public LogController(CentralController controller, SmartHomeSystem system, SmartHomeGUIView view){
        this.controller = controller;
        this.view = view;
    }

    @Override
    public String getMenuContents(){
        String logs = controller.getLoggingService().getMessages().toString();
        if (logs.isEmpty()) {
            return "📋 === APPLICATION LOG ===\n\nNo logs yet.";
        }
        return "📋 === APPLICATION LOG ===\n\n" + logs;
    }

    @Override
    public String getOptionsContents() {
        return "1. Delete Log\n" +
                "2. Export Log\n" +
                "0. Back to Dashboard";
    }

    @Override
    public void handleCommand(String command){

        switch(command){
            case "0":
                controller.showDashboard();
                return;

            case "1":
                if(view.showConfirmDialog("Delete all logs?", "Confirm")) {
                    controller.getLoggingService().clearMessages();
                    controller.setCurrentMessage("✅ Log deleted");
                } else {
                    controller.setCurrentMessage("❌ Deletion cancelled");
                }
                return;

            case "2":
                exportLogWithFileChooser();
                return;

            default:
                view.showInvalidOption();
        }
    }

    // =========================
    // 📁 EXPORT LOG (FILE EXPLORER STYLE)
    // =========================
    private void exportLogWithFileChooser() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Log File");

        // default filename
        fileChooser.setSelectedFile(new File("Log.txt"));

        int result = fileChooser.showSaveDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // auto-add .txt if user didn't include extension
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(controller.getLoggingService().getMessages().toString());
                controller.setCurrentMessage("✅ Log exported to " + file.getName());
            } catch (IOException e) {
                controller.setCurrentMessage("❌ Export failed: " + e.getMessage());
            }
        } else {
            controller.setCurrentMessage("❌ Export cancelled");
        }
    }
}