/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome.view;

/**
 *
 * @author rlack
 */

import java.time.LocalDateTime;
import smarthome.model.Device;
import smarthome.model.SimulationSettings;

import java.util.Collection;
import smarthome.controller.CentralController;

public class SmartHomeCLIView {

    public void renderView(CentralController sm){
        String message = "Blexb: ";
        message += sm.getCurrentMessage();
        String menu = sm.getCurrentInterface().getMenuContents();
        String options = sm.getCurrentInterface().getOptionsContents();
        
        System.out.println("\n\n                  "+LocalDateTime.now().format(sm.getFormatter()));
        System.out.println(" ____                       _     _   _                      ");
        System.out.println("/ ___| _ __ ___   __ _ _ __| |_  | | | | ___  _ __ ___   ___ ");
        System.out.println("\\___ \\| '_ ` _ \\ / _` | '__| __| | |_| |/ _ \\| '_ ` _ \\ / _ \\");
        System.out.println(" ___) | | | | | | (_| | |  | |_  |  _  | (_) | | | | | |  __/");
        System.out.println("|____/|_| |_| |_|\\__,_|_|   \\__| |_| |_|\\___/|_| |_| |_|\\___|");
        int boxWidth = 62;
        String lineSeparator = "+" + "-".repeat(boxWidth - 2) + "+";

        StringBuilder box = new StringBuilder();
        box.append(lineSeparator).append("\n");

        // Message section
        if(message != null && !message.isEmpty()) {
            for (String line : message.split("\n")) {
                box.append(String.format("| %-58s |\n", line));
            }
            box.append(lineSeparator).append("\n");
        }

        // Menu section
        for (String line : menu.split("\n")) {
            box.append(String.format("| %-58s |\n", line));
        }
        box.append(lineSeparator).append("\n");

        // Options section
        for (String line : options.split("\n")) {
            box.append(String.format("| %-58s |\n", line));
        }
        box.append(lineSeparator);

        System.out.println(box.toString());
    }

    public void showInvalidOption(){
        System.out.println("Invalid option. Try again.");
    }
}