package smarthome.view;

import java.util.List;
import java.util.Map;

public interface ISmartHomeView {
    void displayMessage(String message);
    void displayMenu(String title, String content, String options);
    void displayError(String error);
    String getInput(String prompt);
    void clearScreen();
}
