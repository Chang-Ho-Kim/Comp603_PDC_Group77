package smarthome.view;

import javax.swing.*;
import java.awt.*;

/**
 * SmartHomeGUIView - GUI implementation of View interface.
 * Displays ViewData using Swing components.
 */
public class SmartHomeGUIViewAIRough extends JFrame implements View {

    private JLabel dateTimeLabel;
    private JTextArea displayArea;
    private JLabel statusLabel;

    public SmartHomeGUIViewAIRough() {
        setTitle("Smart Home");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top: Date/Time
        dateTimeLabel = new JLabel("", SwingConstants.CENTER);
        dateTimeLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        add(dateTimeLabel, BorderLayout.NORTH);

        // Center: Main content (message + menu + options)
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setEditable(false);
        displayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: Status
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void renderView(ViewData data) {
        StringBuilder content = new StringBuilder();

        String message = data.getMessage();
        String menu = data.getMenuContents();
        String options = data.getOptionsContents();
        String formattedDateTime = data.getFormattedDateTime();

        // Update datetime
        dateTimeLabel.setText(formattedDateTime);

        // Message section
        if (message != null && !message.isEmpty()) {
            content.append("Blexb: ").append(message).append("\n\n");
        }

        // Menu section
        content.append(menu).append("\n\n");

        // Options section
        content.append(options);

        displayArea.setText(content.toString());
        displayArea.setCaretPosition(0); // scroll to top

        // Clear status message on valid render
        statusLabel.setText(" ");
    }

    @Override
    public void showInvalidOption() {
        statusLabel.setText("Invalid option. Try again.");
    }
}