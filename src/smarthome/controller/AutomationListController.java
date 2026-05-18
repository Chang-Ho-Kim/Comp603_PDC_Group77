package smarthome.controller;

import java.util.ArrayList;
import smarthome.model.Device;
import smarthome.model.*;
import smarthome.view.SmartHomeGUIView;

public class AutomationListController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeGUIView view;

    private enum automationType {ALL, POWERSAVER, SENSOR, SCHEDULER};
    private automationType dType;

    private ArrayList<Device> deviceList;

    public AutomationListController(CentralController controller,
                                    SmartHomeSystem system,
                                    SmartHomeGUIView view) {

        this.controller = controller;
        this.system = system;
        this.view = view;
        dType = automationType.ALL;
    }

    @Override
    public String getMenuContents() {

        deviceList = new ArrayList<>();

        StringBuilder menu =
                new StringBuilder(
                        "=== AUTOMATION SETTINGS ===\n\nCurrent View: "
                );

        switch(dType) {

            case SCHEDULER:
                menu.append("Scheduled Devices\n\n");
                break;

            case SENSOR:
                menu.append("Sensor Devices\n\n");
                break;

            case POWERSAVER:
                menu.append("Power Saver Devices\n\n");
                break;

            default:
                menu.append("All Automatable Devices\n\n");
        }

        int i = 1;

        if(dType == automationType.ALL){

            for (Device d : system.getAllDevices()) {

                if((d instanceof ISensorable)
                        || (d instanceof ScheduledDevice)|| (d instanceof PowerSaverDevice)) {

                    deviceList.add(i - 1, d);

                    menu.append(i)
                            .append(". ")
                            .append(d.getName())
                            .append(" [")
                            .append(d.isOn() ? "ON" : "OFF")
                            .append("]");

                    // show automation status
                    if(d instanceof SensorDevice sd){
                        menu.append(" | Sensor Mode: [")
                                .append(sd.isAutoOn() ? "ON" : "OFF")
                                .append("]");
                    }
                    else if(d instanceof ScheduledDevice sdd){
                        menu.append(" | Schedule Mode: [")
                                .append(sdd.isAutoOn() ? "ON" : "OFF")
                                .append("]");
                    }
                    else if(d instanceof PowerSaverDevice psd){
                        menu.append(" | Power Saver Mode: [")
                                .append(psd.isAutoOn() ? "ON" : "OFF")
                                .append("]");
                    }
                    menu.append("\n");

                    i++;
                }
            }
        }

        else if(dType == automationType.SCHEDULER){

            for (Device d : system.getAllDevices()) {

                if(d instanceof ScheduledDevice sd){

                    deviceList.add(i - 1, sd);

                    menu.append(i)
                            .append(". ")
                            .append(sd.getName())
                            .append(" | Schedule Mode: [")
                            .append(sd.isAutoOn() ? "ON" : "OFF")
                            .append("]")
                            .append(" [")
                            .append(sd.getStart())
                            .append(" - ")
                            .append(sd.getEnd())
                            .append("]\n");

                    i++;
                }
            }
        }

        else if(dType == automationType.SENSOR){

            for (Device d : system.getAllDevices()) {

                if(d instanceof SensorDevice sd){

                    deviceList.add(i - 1, sd);

                    menu.append(i)
                            .append(". ")
                            .append(sd.getName())
                            .append(" | Sensor Mode: [")
                            .append(sd.isAutoOn() ? "ON" : "OFF")
                            .append("]")
                            .append(" [Range: ")
                            .append(sd.getLower())
                            .append("-")
                            .append(sd.getUpper())
                            .append("]\n");

                    i++;
                }
            }
        }

        else if(dType == automationType.POWERSAVER){

            for (Device d : system.getAllDevices()) {

                if(d instanceof PowerSaverDevice psd){

                    deviceList.add(i - 1, psd);

                    menu.append(i)
                            .append(". ")
                            .append(psd.getName())
                            .append(" | Power Saver Mode: [")
                            .append(psd.isAutoOn() ? "ON" : "OFF")
                            .append("]\n");

                    i++;
                }
            }
        }

        if (i == 1) {
            menu.append("No devices of this type.");
        }

        return menu.toString();
    }

    @Override
    public String getOptionsContents() {

        return
                "1. Scheduled Devices\n" +
                "2. Sensor Devices\n" +
                "3. Power Saver Devices\n" +
                "4. All Automatable Devices\n\n" +

                "5. Scheduler Auto ON\n" +
                "6. Scheduler Auto OFF\n\n" +

                "7. Sensor Auto ON\n" +
                "8. Sensor Auto OFF\n\n" +

                "9. ALL (except PS) Auto ON\n" +
                "10. ALL (except PS) Auto OFF\n\n" +

                "0. Back to Dashboard";
    }

    // helper methods

    private void setSchedulerAutomation(boolean enabled){

        for(Device d : system.getAllDevices()){

            if(d instanceof ScheduledDevice sd){

                sd.setScheduleOn(enabled);

                // logging
                // system.addLog(sd.getName() +
                //      " scheduler automation "
                //      + (enabled ? "ENABLED" : "DISABLED"));
            }
        }
    }

    private void setSensorAutomation(boolean enabled){

        for(Device d : system.getAllDevices()){

            if(d instanceof SensorDevice sd){

                sd.setSensorOn(enabled);

                // logging
                // system.addLog(sd.getName() +
                //      " sensor automation "
                //      + (enabled ? "ENABLED" : "DISABLED"));
            }
        }
    }

    @Override
    public void handleCommand(String command) {

        switch(command){

            case "1":
                dType = automationType.SCHEDULER;
                break;

            case "2":
                dType = automationType.SENSOR;
                break;

            case "3":
                dType = automationType.POWERSAVER;
                break;

            case "4":
                dType = automationType.ALL;
                break;

            // scheduler

            case "5":
                setSchedulerAutomation(true);
                break;

            case "6":
                setSchedulerAutomation(false);
                break;

            // sensor

            case "7":
                setSensorAutomation(true);
                break;

            case "8":
                setSensorAutomation(false);
                break;

            // all

            case "9":

                setSchedulerAutomation(true);
                setSensorAutomation(true);

                break;

            case "10":

                setSchedulerAutomation(false);
                setSensorAutomation(false);

                break;

            case "0":
                controller.showDashboard();
                return;

            default:
                view.showInvalidOption();
        }
    }
}