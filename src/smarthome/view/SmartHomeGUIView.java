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
    private JPanel deviceButtonPanel;
    private List<JButton> currentButtons;
    private Consumer<String> commandHandler;
    private String lastOptionsText = null;

    // ➕ ADDED: Titles
    private JLabel dashboardTitle;
    private JLabel devicesTitle;
    
    private JLabel gifLabel;
    private JLabel gifLabel1;
    private ImageIcon gif;
    private ImageIcon staticimg;
    
    // 🎨 Base Theme
    private final Color BG = new Color(10, 10, 20);
    private final Color PANEL = new Color(18, 18, 35);
    private final Color BLUE = new Color(0, 255, 255);
    private final Color PURPLE = new Color(180, 0, 255);
    private final Color PINK = new Color(255, 0, 150);

    public SmartHomeGUIView() {

        setTitle("Smart Home Dashboard");
        setSize(1100, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        currentButtons = new ArrayList<>();

        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(BG);

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

        // ===== CENTER DISPLAY (WIDER) =====
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFocusable(false);
        displayArea.setHighlighter(null);
        displayArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setBackground(new Color(5, 5, 15));
        displayArea.setForeground(BLUE);
        displayArea.setCaretColor(BLUE);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane centerPane = new JScrollPane(displayArea);
        centerPane.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        dashboardTitle = new JLabel("DASHBOARD", SwingConstants.CENTER);
        dashboardTitle.setForeground(new Color(255, 40, 80));
        dashboardTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dashboardTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(BG);
        centerContainer.add(dashboardTitle, BorderLayout.NORTH);
        centerContainer.add(centerPane, BorderLayout.CENTER);

        
        
        centerContainer.setPreferredSize(new Dimension(650, 0));

        add(centerContainer, BorderLayout.CENTER);

        // ===== LEFT PANEL (WEST) =====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BG);
        leftPanel.setPreferredSize(new Dimension(225, 0));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel controlTitle = new JLabel("CONTROLS", SwingConstants.CENTER);
        controlTitle.setForeground(new Color(255, 40, 80));
        controlTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        controlTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        leftPanel.add(controlTitle, BorderLayout.NORTH);

        buttonPanel = new JPanel(new GridLayout(0, 1, 12, 12));
        buttonPanel.setBackground(BG);

        JScrollPane buttonScroll = new JScrollPane(buttonPanel);
        buttonScroll.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        buttonScroll.getVerticalScrollBar().setUnitIncrement(12);

        leftPanel.add(buttonScroll, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // ===== EAST PANEL =====
        deviceButtonPanel = new JPanel(new GridLayout(0, 1, 12, 12));
        deviceButtonPanel.setBackground(BG);

        JScrollPane deviceScroll = new JScrollPane(deviceButtonPanel);
        deviceScroll.setPreferredSize(new Dimension(225, 0));
        deviceScroll.setBorder(BorderFactory.createLineBorder(Color.WHITE,2));

        devicesTitle = new JLabel("DEVICES", SwingConstants.CENTER);
        devicesTitle.setForeground(new Color(255, 40, 80));
        devicesTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        devicesTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.setBackground(BG);

        // 🔧 FIX: enforce identical structure/padding symmetry with west
        eastPanel.setPreferredSize(new Dimension(225, 0));
        eastPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        eastPanel.add(devicesTitle, BorderLayout.NORTH);
        eastPanel.add(deviceScroll, BorderLayout.CENTER);
        
        gifLabel1 = new JLabel();

        ImageIcon gif1 = new ImageIcon("src/smarthome/resources/coffee.gif");
        Image scaled1 = gif1.getImage().getScaledInstance(250, 480, Image.SCALE_DEFAULT);

        gifLabel1.setIcon(new ImageIcon(scaled1));
        gifLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        eastPanel.add(gifLabel1, BorderLayout.SOUTH);

        add(eastPanel, BorderLayout.EAST);

        // ===== SOUTH PANEL =====
        statusLabel = new JLabel("System Ready");
        statusLabel.setForeground(PINK);
        statusLabel.setBackground(PANEL);
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dateTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gifLabel = new JLabel();

        // Load GIF (put file in project resources or use absolute path)
        gif = new ImageIcon("src/smarthome/resources/waves.gif");
        Image scaled = gif.getImage().getScaledInstance(500, 100, Image.SCALE_DEFAULT);
        
        staticimg = new ImageIcon("src/smarthome/resources/waves.png");
        Image scaledstatic = staticimg.getImage().getScaledInstance(500, 100, Image.SCALE_DEFAULT);
       
        gif = new ImageIcon(scaled);
        staticimg = new ImageIcon(scaledstatic);

        gifLabel.setIcon(staticimg);
        gifLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gifLabel.setVerticalAlignment(SwingConstants.CENTER);

        
        JPanel southLabels = new JPanel(new BorderLayout());
        southLabels.setBackground(BG);

        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        southLabels.add(statusLabel, BorderLayout.WEST);

        dateTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        southLabels.add(dateTimeLabel, BorderLayout.EAST);
        southLabels.add(gifLabel, BorderLayout.CENTER);
        
        JPanel musicPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        musicPanel.setBackground(BG);
        JButton musicBtn = new JButton("Play / Pause Music");
        musicPanel.add(musicBtn);
        southLabels.add(musicPanel, BorderLayout.SOUTH);
        
        musicBtn.addActionListener(e -> {
            if (commandHandler != null) {
                commandHandler.accept("TOGGLE_MUSIC");
            }
        });
        
        add(southLabels, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void renderView(ViewData data) {

        dateTimeLabel.setText(data.getFormattedDateTime());

        StringBuilder content = new StringBuilder();

        if (data.getMessage() != null && !data.getMessage().isEmpty()) {
            content.append("blexb: ").append(data.getMessage()).append("\n\n");
            statusLabel.setText(data.getMessage());
        }

        if (data.getMenuContents() != null) {
            content.append("═══════════════════════════════\n");
            content.append(data.getMenuContents()).append("\n");
            content.append("═══════════════════════════════");
        }

        displayArea.setText(content.toString());
        displayArea.setCaretPosition(0);
        
        
        
        String options = data.getOptionsContents();

        if (options != null && !options.equals(lastOptionsText)) {
            generateButtonsFromOptions(options);
            lastOptionsText = options;
        }
    }

        private void generateButtonsFromOptions(String optionsText) {

        buttonPanel.removeAll();
        deviceButtonPanel.removeAll();
        currentButtons.clear();

        if (optionsText == null || optionsText.isEmpty()) {
            devicesTitle.setText("");
            gifLabel.setVisible(true); // show matrix when empty
            return;
        }

        String[] lines = optionsText.split("\n");

        for (String line : lines) {

            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\.", 2);
            String command = parts[0].trim();
            String label = parts.length > 1 ? parts[1].trim() : command;

            if (label.contains("Enter") || label.contains("Type")) continue;

            JButton button = createButton(command, label);

            if (isSystemButton(label)) {
                buttonPanel.add(button);
            } else {
                deviceButtonPanel.add(button);
            }

            currentButtons.add(button);
        }

        if (deviceButtonPanel.getComponentCount() == 0) {
            devicesTitle.setText("");
        } else {
            devicesTitle.setText("DEVICES");
        }

        // 🔥 MATRIX GIF TOGGLE LOGIC
        boolean hasButtons = deviceButtonPanel.getComponentCount() > 0;
        gifLabel1.setVisible(!hasButtons);

        SwingUtilities.invokeLater(() -> {
            buttonPanel.revalidate();
            buttonPanel.repaint();

            deviceButtonPanel.revalidate();
            deviceButtonPanel.repaint();

            gifLabel1.revalidate();
            gifLabel1.repaint();
        });
    }
    // ===== EVERYTHING BELOW UNCHANGED =====

    private boolean isSystemButton(String label) {
        if (label.equals("Heater")) return true;
        if (label.equals("Air Conditioner")) return true;
        if (label.equals("Light")) return true;
        if (label.equals("Door")) return true;
        if (label.equals("Television")) return true;
        if (label.equals("Music Player")) return true;
        if (label.equals("Robot Cleaner")) return true;
        if (label.equals("Alarm Clock")) return true;
        if (label.equals("Turn ON")) return true;
        if (label.equals("Turn OFF")) return true;

        String l = label.toLowerCase().trim();

        if (l.startsWith("remove")) return true;
        if (l.contains("increase")) return true;
        if (l.contains("decrease")) return true;
        if (l.contains("set custom")) return true;
        if (l.contains("devices")) return true;
        if (l.matches(".*\\blog\\b.*")) return true;
        if (l.contains("set ")) return true;

        return l.equals("turn off all")
                || l.equals("turn on all")
                || l.equals("add device")
                || l.equals("remove device")
                || l.equals("view automation")
                || l.equals("simulation settings")
                || l.equals("view log")
                || l.equals("quit")
                || l.equals("back to dashboard")
                || l.equals("cancel");
    }

    private JButton createButton(String command, String label) {

        JButton button = new JButton(label);

        Color accent = getButtonColor(command);

        Color baseBg = new Color(20, 22, 32);
        Color hoverBg = new Color(30, 34, 50);
        Color pressBg = new Color(15, 18, 28);

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);

        button.setBackground(baseBg);

        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        button.setPreferredSize(new Dimension(140, 44));

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accent, 2, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBg);
                button.setForeground(accent.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseBg);
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(pressBg);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(hoverBg);
            }
        });

        button.addActionListener(e -> {
            if (commandHandler != null) {
                commandHandler.accept(command);
            }
        });

        return button;
    }

    private Color getButtonColor(String command) {

        if (command.equalsIgnoreCase("q") || command.equalsIgnoreCase("0"))
            return new Color(239, 68, 68);

        if (command.equalsIgnoreCase("w"))
            return new Color(34, 197, 94);

        if (command.equalsIgnoreCase("e"))
            return new Color(59, 130, 246);

        if (command.equalsIgnoreCase("a"))
            return new Color(255, 75, 75);

        if (command.equalsIgnoreCase("r"))
            return new Color(57, 255, 20);

        if (command.equalsIgnoreCase("f"))
            return new Color(0, 255, 255);

        if (command.equalsIgnoreCase("s"))
            return new Color(255, 255, 0);

        if (command.equalsIgnoreCase("l"))
            return new Color(0, 128, 255);

        if (command.equalsIgnoreCase("1"))
            return new Color(245, 158, 11);

        if (command.equalsIgnoreCase("2"))
            return new Color(236, 72, 153);

        if (command.equalsIgnoreCase("3"))
            return new Color(168, 85, 247);

        if (command.equalsIgnoreCase("4"))
            return new Color(14, 165, 233);

        if (command.equalsIgnoreCase("5"))
            return new Color(250, 204, 21);

        return new Color(139, 92, 246);
    }

    @Override
    public void showInvalidOption() {
        statusLabel.setText("Invalid option");
    }

    public void setCommandHandler(Consumer<String> handler) {
        this.commandHandler = handler;
    }

    public String showDeviceNameDialog() {
        return JOptionPane.showInputDialog(this,"Enter device name:","Add Device",JOptionPane.QUESTION_MESSAGE);
    }

    public String showTimeDialog() {
        return JOptionPane.showInputDialog(this,"Enter time (HH:mm:ss):","Set Time",JOptionPane.QUESTION_MESSAGE);
    }

    public Integer showTemperatureDialog() {
        String input = JOptionPane.showInputDialog(this,"Enter temperature:","Set Temperature",JOptionPane.QUESTION_MESSAGE);

        if (input == null || input.trim().isEmpty()) return null;

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid temperature value", "Input Error");
            return null;
        }
    }

    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this,message,title,JOptionPane.ERROR_MESSAGE);
    }

    public boolean showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this,message,title,
                JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)
                == JOptionPane.YES_OPTION;
    }
    
    public void setMusicPlaying(boolean playing) {
    if (playing) {
        gifLabel.setIcon(gif);
    } else {
        gifLabel.setIcon(staticimg);
    }
}
}