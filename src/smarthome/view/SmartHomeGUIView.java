package smarthome.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SmartHomeGUIView extends JFrame implements View {

    private JLabel dateTimeLabel;
    private JTextArea displayArea;
    private JLabel statusLabel;
    private JPanel buttonPanel;
    private List<JButton> currentButtons;
    private Consumer<String> commandHandler;

    // 🎨 Neon Colors
    private final Color BG = new Color(10, 10, 20);
    private final Color PANEL = new Color(18, 18, 35);
    private final Color BLUE = new Color(0, 255, 255);
    private final Color PURPLE = new Color(180, 0, 255);
    private final Color PINK = new Color(255, 0, 150);

    public SmartHomeGUIView() {

        setTitle("Smart Home Dashboard");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        currentButtons = new ArrayList<>();

        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(BG);

        // outer spacing so UI isn't glued to edges
        ((JComponent) getContentPane())
                .setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // ===== TOP BAR =====
        dateTimeLabel = new JLabel("Loading...", SwingConstants.CENTER);
        dateTimeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dateTimeLabel.setForeground(BLUE);
        dateTimeLabel.setOpaque(true);
        dateTimeLabel.setBackground(PANEL);
        dateTimeLabel.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        add(dateTimeLabel, BorderLayout.NORTH);

        // ===== CENTER DISPLAY =====
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setBackground(new Color(5, 5, 15));
        displayArea.setForeground(BLUE);
        displayArea.setCaretColor(BLUE);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane centerPane = new JScrollPane(displayArea);
        centerPane.setBorder(BorderFactory.createLineBorder(PURPLE));
        add(centerPane, BorderLayout.CENTER);

        // ===== LEFT CONTROL PANEL =====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BG);

        // 👇 slightly wider than before
        leftPanel.setPreferredSize(new Dimension(400, 0));

        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel controlTitle = new JLabel("CONTROLS");
        controlTitle.setForeground(PURPLE);
        controlTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        controlTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        leftPanel.add(controlTitle, BorderLayout.NORTH);

        buttonPanel = new JPanel(new GridLayout(0, 2, 12, 12));
        buttonPanel.setBackground(BG);

        JScrollPane buttonScroll = new JScrollPane(buttonPanel);
        buttonScroll.setBorder(BorderFactory.createLineBorder(PURPLE));
        buttonScroll.getVerticalScrollBar().setUnitIncrement(12);

        leftPanel.add(buttonScroll, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // ===== STATUS BAR =====
        statusLabel = new JLabel("System Ready");
        statusLabel.setForeground(PINK);
        statusLabel.setBackground(PANEL);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void renderView(ViewData data) {

        dateTimeLabel.setText("📅 " + data.getFormattedDateTime());

        StringBuilder content = new StringBuilder();

        if (data.getMessage() != null && !data.getMessage().isEmpty()) {
            content.append(">> ").append(data.getMessage()).append("\n\n");
            statusLabel.setText("✓ " + data.getMessage());
        }

        if (data.getMenuContents() != null) {
            content.append("═══════════════════════════════\n");
            content.append(data.getMenuContents()).append("\n");
            content.append("═══════════════════════════════");
        }

        displayArea.setText(content.toString());
        displayArea.setCaretPosition(0);

        generateButtonsFromOptions(data.getOptionsContents());
    }

    private void generateButtonsFromOptions(String optionsText) {

        buttonPanel.removeAll();
        currentButtons.clear();

        if (optionsText == null || optionsText.isEmpty()) return;

        String[] lines = optionsText.split("\n");

        for (String line : lines) {

            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\.", 2);
            String command = parts[0].trim();
            String label = parts.length > 1 ? parts[1].trim() : command;

            if (label.contains("Enter") || label.contains("Type")) continue;

            createButton(command, label);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void createButton(String command, String label) {

        JButton button = new JButton(label) {

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getForeground());
                for (int i = 6; i > 0; i--) {
                    g2.setComposite(AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER, 0.05f));
                    g2.fillRoundRect(i, i, getWidth() - i * 2, getHeight() - i * 2, 20, 20);
                }

                g2.setComposite(AlphaComposite.SrcOver);
                g2.setColor(new Color(20, 20, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(getForeground());
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        // 🔥 FORCE ALL BUTTONS SAME SIZE
        button.setPreferredSize(new Dimension(140, 45));
        button.setMinimumSize(new Dimension(140, 45));

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(getButtonColor(command));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(button.getForeground().brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setForeground(getButtonColor(command));
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

    private Color getButtonColor(String command) {
        if (command.equalsIgnoreCase("q") || command.equalsIgnoreCase("0"))
            return new Color(255, 50, 50);
        if (command.equalsIgnoreCase("w"))
            return new Color(0, 255, 120);
        if (command.equalsIgnoreCase("e"))
            return new Color(255, 140, 0);
        if (command.equalsIgnoreCase("1"))
            return new Color(0, 200, 255);

        return new Color(200, 0, 255);
    }

    @Override
    public void showInvalidOption() {
        statusLabel.setText("❌ Invalid option");
    }

    public void setCommandHandler(Consumer<String> handler) {
        this.commandHandler = handler;
    }
    // =========================
// 🔧 INPUT DIALOG FUNCTIONS
// =========================

    public String showDeviceNameDialog() {
        return JOptionPane.showInputDialog(
                this,
                "Enter device name:",
                "Add Device",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    public String showTimeDialog() {
        return JOptionPane.showInputDialog(
                this,
                "Enter time (HH:mm:ss):",
                "Set Time",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    public int showTemperatureDialog() {
        String input = JOptionPane.showInputDialog(
                this,
                "Enter temperature:",
                "Set Temperature",
                JOptionPane.QUESTION_MESSAGE
        );

        if (input == null || input.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid temperature value", "Input Error");
            return 0;
        }
    }

    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }
    
    public boolean showConfirmDialog(String message, String title) {
        int result = JOptionPane.showConfirmDialog(
                this,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return result == JOptionPane.YES_OPTION;
    }
    
}