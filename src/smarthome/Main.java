/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smarthome;

/**
 *
 * @author rlack
 */


import smarthome.controller.CentralController;
import smarthome.model.SmartHomeSystem;
import smarthome.view.SmartHomeCLIView;
import smarthome.persistence.SaveLoadService;

public class Main {

    public static void main(String[] args) {

        // Load previous system state or create new
        SmartHomeSystem system = SaveLoadService.loadSystem();

        SmartHomeCLIView view = new SmartHomeCLIView();
        CentralController controller = new CentralController(system, view);

        // Hook to save system on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SaveLoadService.saveSystem(system);
        }));

        controller.start();
    }
}