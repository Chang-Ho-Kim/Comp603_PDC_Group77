package smarthome.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import smarthome.model.AutomationContext;
import smarthome.model.Device;
import smarthome.model.SmartHomeSystem;

public class AutomationService {
    private final SmartHomeSystem system;
    private final CentralController controller;

    public AutomationService(SmartHomeSystem system, CentralController controller) {
        this.system = system;
        this.controller = controller;
    }

    public void startAutomation() {
        Thread automationThread = new Thread(() -> {
            while (true) {
                try {
                    synchronized(system) {
                        checkAutomation();
                    }
                    Thread.sleep(1000); // 1 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        automationThread.setDaemon(true);
        automationThread.start();
    }

    private void checkAutomation() {
        int temp = system.getSimulation().getTemperature();
        LocalTime now = LocalTime.now();
        system.calculateTotalElectricityUsage(); // Ensure power status is up to date
        boolean isPowerSaving = system.isPowerSavingMode();
        
        AutomationContext context = new AutomationContext(now, temp, isPowerSaving);

        for (Device d : system.getAllDevices()) {
            if (d.isAutoOn()) {
                boolean wasOn = d.isOn();
                d.checkAutomation(context);
                if (wasOn != d.isOn()) {
                    String log = "[" + LocalDateTime.now().format(controller.getDateTimeFormatter()) + "] " +
                            "Automation action: " + d.getName() + " is now " + (d.isOn() ? "ON" : "OFF") + "\n";
                    system.addMessage(log);
                    controller.setCurrentMessage("Automation action: " + d.getName());
                }
            }
        }
    }
}
