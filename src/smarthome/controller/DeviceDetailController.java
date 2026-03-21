/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.controller;

/**
 *
 * @author rlack
 */

import java.text.DecimalFormat;
import java.time.LocalTime;
import smarthome.model.Device;
import smarthome.model.ScheduledDevice;
import smarthome.model.SensorDevice;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeCLIView;

public class DeviceDetailController implements IInterfaceController {

    private CentralController controller;
    private SmartHomeSystem system;
    private SmartHomeCLIView view;
    private Device device;

    public DeviceDetailController(CentralController controller, SmartHomeSystem system, SmartHomeCLIView view){
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
        "\nTotal Usage Cost: " + df.format(device.calculateCost(system.getECost())) +  
        "\nState: " + (device.isOn() ? "ON" : "OFF"));
        
        if(device instanceof ScheduledDevice sd){
            if(sd.isScheduleOn()){
                menu.append("\nSchedule Mode: On" );
                menu.append("\nSchedule: " + sd.getStart() + " - " + sd.getEnd());
            }
            else{
                menu.append("\nSchedule: Off" );
            }
        }
        if(device instanceof SensorDevice s){
            if(s.isSensorOn()){
                menu.append("\nSensor Mode: On" );
                menu.append("\nSensor range: " + s.getLower() + " - " + s.getUpper());
            }
            else{
                menu.append("\nSensor: Off" );
            }
        }
        
        return menu.toString();
    }

    @Override
    public String getOptionsContents() {
        StringBuilder options = new StringBuilder( 
        "1. Turn ON\n"+
        "2. Turn OFF\n");
        if(device instanceof ScheduledDevice){
            options.append("3. Set scheduled start time\n" + "4. Set scheduled end time \n"+"5. Set Schedule Mode On/Off\n");
        }
        if(device instanceof SensorDevice){
            options.append("3. Set sensor lower threshold\n" + "4. Set sensor upper threshold \n" +"5. Set sensor Mode On/Off\n");
        }
        
        options.append("                                   (0. Back to Dashboard)");
        
        return options.toString();
    }

    @Override
    public void handleCommand(String command){
        switch(command){
            case "1": if(!device.isOn()){
                        device.turnOn(); 
                        controller.setCurrentMessage(device.getName() + " was turned on" ); 
                        system.addMessage(device.getName() + " was turned on\n"); 
                        break;}
                      else{
                        controller.setCurrentMessage(device.getName() + " is already on" );break;
                      }
            case "2": if(device.isOn()){
                        device.turnOff(); 
                        controller.setCurrentMessage(device.getName() + " was turned off" );
                        system.addMessage(device.getName() + " was turned off\n"); 
                        break;}
                      else{
                        controller.setCurrentMessage(device.getName() + " is already off" );break;
                      }
             case "3": if(device instanceof ScheduledDevice sd){
                            LocalTime original = sd.getStart();
                            sd.setStart(controller.setTime());
                            if(!sd.getEnd().equals(LocalTime.of(0, 0, 0)) && (sd.getStart().isAfter(sd.getEnd()))){
                                view.showInvalidOption();
                                controller.setCurrentMessage("Start time must be before start time!");
                                sd.setStart(original);
                                break;
                            }
                            else{
                                controller.setCurrentMessage(device.getName() + "'s start time was set" );
                                //system.addMessage(device.getName() + "'s start time was set\n"); 
                                break;
                            }
                       }
                       else if(device instanceof SensorDevice s){
                            int originalT = s.getLower();
                            s.setLower(controller.setTemp());
                            if((s.getUpper()!=0) && (s.getLower()> (s.getUpper()))){
                                view.showInvalidOption();
                                controller.setCurrentMessage("Lower threshold must be less than upper");
                                s.setLower(originalT);
                                break;     
                            }
                            else{
                                controller.setCurrentMessage(device.getName() + "'s lower threshold was set" );
                                //system.addMessage(device.getName() + "'s lower threshold was set\n"); 
                                break;
                            }
                        
                       }
                       //add if else statements for each type
                      else{
                        view.showInvalidOption();
                      }
             case "4": if(device instanceof ScheduledDevice sd){
                            LocalTime original = sd.getEnd();
                            sd.setEnd(controller.setTime());
                            if(sd.getEnd().isBefore(sd.getStart())){
                                view.showInvalidOption();
                                controller.setCurrentMessage("End time must be after start time!");
                                sd.setEnd(original);
                                break;
                            }
                            else{
                                controller.setCurrentMessage(device.getName() + "'s end time was set" );
                                //system.addMessage(device.getName() + "'s end time was set\n"); 
                                break;
                            }
                       }
                       else if(device instanceof SensorDevice s){
                            int originalT = s.getUpper();
                            s.setUpper(controller.setTemp());
                            if((s.getUpper() < (s.getLower()))){
                                view.showInvalidOption();
                                controller.setCurrentMessage("Upper threshold must be higher than lower");
                                s.setUpper(originalT);
                                break;     
                            }
                            else{
                                controller.setCurrentMessage(device.getName() + "'s upper threshold was set" );
                                //system.addMessage(device.getName() + "'s upper threshold was set\n"); 
                                break;
                            }
                        
                       }
             
                       //add if else statements for each type
                      else{
                        view.showInvalidOption();
                      }  
             case "5": if(device instanceof ScheduledDevice sd){
                            if(sd.isScheduleOn()){
                                sd.setScheduleOn(false);
                             }else{
                                sd.setScheduleOn(true);
                            }
                            break;
                        }
                        else if(device instanceof SensorDevice s){
                            if(s.isSensorOn()){
                                s.setSensorOn(false);
                               }
                            else{
                                s.setSensorOn(true);
                              }
                            break;
                        }
             /*
             case "5": if(device instanceof ScheduledDevice sd){
                        sd.setStart(LocalTime.of(0, 0, 0));
                        sd.setEnd(LocalTime.of(0, 0, 0));
                        controller.setCurrentMessage(device.getName() + " was descheduled" );
                        system.addMessage(device.getName() + " was descheduled\n"); 
                        break;}
                      //add if else statements for each type
                      else{
                        view.showInvalidOption();
                      }
            */
            case "0": controller.showDashboard(); return;
            default: view.showInvalidOption();
        }
    }

    
    
}