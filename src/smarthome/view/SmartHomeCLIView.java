package smarthome.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SmartHomeCLIView implements ISmartHomeView {
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void displayMessage(String message) {
        if (message != null && !message.isEmpty()) {
            System.out.println("Blexb: " + message);
        }
    }

    @Override
    public void displayMenu(String title, String content, String options) {
        System.out.println("\n\n                  " + LocalDateTime.now().format(dateTimeFormatter));
        System.out.println(" ____                       _     _   _                      ");
        System.out.println("/ ___| _ __ ___   __ _ _ __| |_  | | | | ___  _ __ ___   ___ ");
        System.out.println("\\___ \\| '_ ` _ \\ / _` | '__| __| | |_| |/ _ \\| '_ ` _ \\ / _ \\");
        System.out.println(" ___) | | | | | | (_| | |  | |_  |  _  | (_) | | | | | |  __/");
        System.out.println("|____/|_| |_| |_|\\__,_|_|   \\__| |_| |_|\\___/|_| |_| |_|\\___|");
        
        int boxWidth = 62;
        String lineSeparator = "+" + "-".repeat(boxWidth - 2) + "+";

        System.out.println(lineSeparator);
        
        if (title != null && !title.isEmpty()) {
            System.out.printf("| %-58s |\n", title);
            System.out.println(lineSeparator);
        }

        if (content != null && !content.isEmpty()) {
            for (String line : content.split("\n")) {
                System.out.printf("| %-58s |\n", line);
            }
            System.out.println(lineSeparator);
        }

        if (options != null && !options.isEmpty()) {
            for (String line : options.split("\n")) {
                System.out.printf("| %-58s |\n", line);
            }
            System.out.println(lineSeparator);
        }
    }

    @Override
    public void displayError(String error) {
        System.err.println("ERROR: " + error);
    }

    @Override
    public String getInput(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        return scanner.nextLine();
    }

    @Override
    public void clearScreen() {
        // Simple console "clear"
        for (int i = 0; i < 50; i++) System.out.println();
    }
}
