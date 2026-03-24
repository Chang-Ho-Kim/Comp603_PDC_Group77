package smarthome.view;

/**
 * SmartHomeCLIView - CLI implementation of View interface.
 * Receives ViewData (DTOs) and renders to console.
 * No dependency on Controller or business logic.
 */
public class SmartHomeCLIView implements View {

    @Override
    public void renderView(ViewData data) {
        String message = "Blexb: ";
        message += data.getMessage();
        String menu = data.getMenuContents();
        String options = data.getOptionsContents();
        String formattedDateTime = data.getFormattedDateTime();
        
        System.out.println("\n\n                  " + formattedDateTime);
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

    @Override
    public void showInvalidOption() {
        System.out.println("Invalid option. Try again.");
    }
}
