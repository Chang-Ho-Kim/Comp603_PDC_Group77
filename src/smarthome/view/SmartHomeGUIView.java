package smarthome.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * SmartHomeGUIView - Comprehensive Swing GUI implementation
 * Provides button-driven interface for all Smart Home operations
 * Replaces console input with interactive GUI components
 */
public class SmartHomeGUIView extends JFrame implements View {

    private JLabel dateTimeLabel;
    private JTextArea displayArea;
    private JLabel statusLabel;
    private JPanel buttonPanel;
    private List<JButton> currentButtons;
    private Consumer<String> commandHandler;

    public SmartHomeGUIView() {
        setTitle("Smart Home Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        currentButtons = new ArrayList<>();

        // Top Panel: DateTime
        dateTimeLabel = new JLabel("Loading...", SwingConstants.CENTER);
        dateTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dateTimeLabel.setBackground(new Color(33, 150, 243));
        dateTimeLabel.setForeground(Color.WHITE);
        dateTimeLabel.setOpaque(true);
        dateTimeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(dateTimeLabel, BorderLayout.NORTH);

        // Center Panel: Display Area
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(245, 245, 245));
        displayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Status Bar
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(76, 175, 80));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        statusLabel.setBackground(new Color(240, 248, 245));
        statusLabel.setOpaque(true);
        add(statusLabel, BorderLayout.SOUTH);

        // Button Panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        buttonPanel.setBackground(new Color(250, 250, 250));

        JScrollPane buttonScroll = new JScrollPane(buttonPanel);
        buttonScroll.setPreferredSize(new Dimension(1000, 80));
        add(buttonScroll, BorderLayout.EAST);

        setVisible(true);
    }

    @Override
    public void renderView(ViewData data) {
        // Update datetime
        dateTimeLabel.setText("📅 " + data.getFormattedDateTime());

        // Build display content
        StringBuilder content = new StringBuilder();

        String message = data.getMessage();
        if (message != null && !message.isEmpty()) {
            content.append("ℹ️  Message: ").append(message).append("\n\n");
            statusLabel.setText("✓ " + message);
            statusLabel.setForeground(new Color(76, 175, 80));
        }

        String menu = data.getMenuContents();
        if (menu != null && !menu.isEmpty()) {
            content.append("═══════════════════════════════════════\n");
            content.append(menu).append("\n");
            content.append("═══════════════════════════════════════");
        }

        displayArea.setText(content.toString());
        displayArea.setCaretPosition(0);

        // Generate buttons from options
        generateButtonsFromOptions(data.getOptionsContents());
    }

    /**
     * Parse options string and generate clickable buttons
     */
    private void generateButtonsFromOptions(String optionsText) {
        // Clear old buttons
        buttonPanel.removeAll();
        currentButtons.clear();

        if (optionsText == null || optionsText.isEmpty()) {
            buttonPanel.revalidate();
            buttonPanel.repaint();
            return;
        }

        String[] lines = optionsText.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Extract command code and label
            String[] parts = line.split("\\.", 2);
            if (parts.length < 1) continue;

            String command = parts[0].trim();
            String label = parts.length > 1 ? parts[1].trim() : command;

            // Skip complex option instructions
            if (label.contains("device") && label.contains("index")) continue;
            if (label.contains("Enter") && label.contains("Refresh")) continue;
            if (label.contains("Type") || label.contains("name")) continue;

            createButton(command, label);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    /**
     * Create a single button for an option
     */
    private void createButton(String command, String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setFocusPainted(false);
        button.setBackground(getButtonColor(command));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(100, 35));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(getButtonColor(command));
            }
        });

        button.addActionListener(e -> {
            if (commandHandler != null) {
                commandHandler.accept(command);
            }
        });

        buttonPanel.add(button);
        currentButtons.add(button);
    }

    /**
     * Get color based on command type
     */
    private Color getButtonColor(String command) {
        if (command.equalsIgnoreCase("q") || command.equalsIgnoreCase("0")) {
            return new Color(244, 67, 54); // Red for exit/back
        } else if (command.equalsIgnoreCase("w")) {
            return new Color(76, 175, 80); // Green for turn on
        } else if (command.equalsIgnoreCase("e")) {
            return new Color(255, 152, 0); // Orange for turn off
        } else if (command.equalsIgnoreCase("1")) {
            return new Color(33, 150, 243); // Blue for primary options
        }
        return new Color(156, 39, 176); // Purple for other options
    }

    @Override
    public void showInvalidOption() {
        statusLabel.setText("❌ Invalid option. Try again.");
        statusLabel.setForeground(new Color(244, 67, 54));
    }

    /**
     * Show input dialog for device name
     */
    public String showDeviceNameDialog() {
        String name = JOptionPane.showInputDialog(this, "Enter device name:", "Add Device", JOptionPane.QUESTION_MESSAGE);
        return name != null ? name : "";
    }

    /**
     * Show input dialog for temperature
     */
    public int showTemperatureDialog() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter temperature (integer):", "Set Temperature", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return -1;
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Show input dialog for time (HH:mm:ss format)
     */
    public String showTimeDialog() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter time (HH:mm:ss):", "Set Time", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return null;
            if (input.matches("\\d{2}:\\d{2}:\\d{2}")) {
                return input;
            }
            JOptionPane.showMessageDialog(this, "Invalid format. Please use HH:mm:ss.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Show confirmation dialog
     */
    public boolean showConfirmDialog(String message, String title) {
        int result = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Show info message
     */
    public void showInfoMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show error message
     */
    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Set the command handler callback
     */
    public void setCommandHandler(Consumer<String> handler) {
        this.commandHandler = handler;
    }
}
