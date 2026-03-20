package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


/**
 * Main GUI class for the Boat Storage System.
 */
public class BoatStorageGUI extends JFrame {
    private BoatStorage storage;
    private JPanel mainPanel;
    private JLabel statusBar;

    public BoatStorageGUI() {
        storage = new BoatStorage();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Boat Storage System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 500));

        // Create main panel with vertical BoxLayout
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.
            createEmptyBorder(10, 10, 10, 10));

        // Create buttons
        addButton("Add Owner/Boat", e -> showAddDialog());
        addButton("List All", e -> showListDialog());
        addButton("Delete Owner/Boat", e -> showDeleteDialog());
        addButton("View Statistics", e -> showStatisticsDialog());
        addButton("Manage Storage", e -> showManageDialog());
        addButton("Save", e -> saveDatabase());
        addButton("Load", e -> loadDatabase());

        // Create status bar
        statusBar = new JLabel(" Ready");
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        // Layout
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        // Center on screen
        setLocationRelativeTo(null);
    }

    private void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 50));
        button.setPreferredSize(new Dimension(300, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(button);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        button.addActionListener(listener);
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Add Owner/Boat", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(
            10, 10, 10, 10));
        
        JButton addOwnerBtn = new JButton("Add Owner");
        JButton addBoatBtn = new JButton("Add Boat");
        
        addOwnerBtn.addActionListener(e -> {
            dialog.dispose();
            showAddOwnerDialog();
        });
        
        addBoatBtn.addActionListener(e -> {
            dialog.dispose();
            showAddBoatDialog();
        });
        
        panel.add(addOwnerBtn);
        panel.add(addBoatBtn);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddOwnerDialog() {
        JDialog dialog = new JDialog(this, "Add Owner", true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            if (!name.isEmpty() && !address.isEmpty()) {
                storage.addOwner(name, address);
                updateStatus("Owner added successfully!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Please fill in all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel(""));
        panel.add(addButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddBoatDialog() {
        JDialog dialog = new JDialog(this, "Add Boat", true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> ownerCombo = new JComboBox<>();
        JComboBox<String> boatTypeCombo = new JComboBox<>(new String[]{"Sail Boat", "Motor Boat"});
        JTextField heightField = new JTextField(10);
        JTextField lengthField = new JTextField(10);
        JTextField widthField = new JTextField(10);
        JTextField valueField = new JTextField(10);
        
        // Additional fields for specific boat types
        JTextField mastHeightField = new JTextField(10);
        JTextField sailAreaField = new JTextField(10);
        JTextField horsePowerField = new JTextField(10);

        // Populate owner combo box
        for (Owner owner : storage.getOwners()) {
            ownerCombo.addItem(owner.getIdNumber() + " - " + owner.getName());
        }

        panel.add(new JLabel("Owner:"));
        panel.add(ownerCombo);
        panel.add(new JLabel("Boat Type:"));
        panel.add(boatTypeCombo);
        panel.add(new JLabel("Height (m):"));
        panel.add(heightField);
        panel.add(new JLabel("Length (m):"));
        panel.add(lengthField);
        panel.add(new JLabel("Width (m):"));
        panel.add(widthField);
        panel.add(new JLabel("Value ($):"));
        panel.add(valueField);

        // Additional fields panels
        JPanel sailBoatPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        sailBoatPanel.add(new JLabel("Mast Height (m):"));
        sailBoatPanel.add(mastHeightField);
        sailBoatPanel.add(new JLabel("Sail Area (m²):"));
        sailBoatPanel.add(sailAreaField);

        JPanel motorBoatPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        motorBoatPanel.add(new JLabel("Horse Power:"));
        motorBoatPanel.add(horsePowerField);

        // Initially show sail boat fields
        panel.add(sailBoatPanel);
        panel.add(motorBoatPanel);  // Add motorBoatPanel to main panel
        motorBoatPanel.setVisible(false);  // Initially hide motor boat fields

        // Switch between boat type specific fields
        boatTypeCombo.addActionListener(e -> {
            boolean isSailBoat = boatTypeCombo.getSelectedItem().equals("Sail Boat");
            sailBoatPanel.setVisible(isSailBoat);
            motorBoatPanel.setVisible(!isSailBoat);
            dialog.pack();
        });

        JButton addButton = new JButton("Add Boat");
        addButton.addActionListener(e -> {
            try {
                double height = Double.parseDouble(heightField.getText());
                double length = Double.parseDouble(lengthField.getText());
                double width = Double.parseDouble(widthField.getText());
                double value = Double.parseDouble(valueField.getText());

                String selectedOwner = (String) ownerCombo.getSelectedItem();
                int ownerId = Integer.parseInt(selectedOwner.split(" - ")[0]);

                Boat boat;
                if (boatTypeCombo.getSelectedItem().equals("Sail Boat")) {
                    double mastHeight = Double.parseDouble(mastHeightField.getText());
                    double sailArea = Double.parseDouble(sailAreaField.getText());
                    boat = new SailBoat(height, length, width, value, mastHeight, sailArea);
                } else {
                    double horsePower = Double.parseDouble(horsePowerField.getText());
                    boat = new MotorBoat(height, length, width, value, horsePower);
                }

                storage.addBoatToOwner(ownerId, boat);
                updateStatus("Boat added successfully!");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter valid numbers for all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel(""));
        panel.add(addButton);

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showListDialog() {
        JDialog dialog = new JDialog(this, "List All", true);
        dialog.setLayout(new BorderLayout());
        
        // Create and add BoatsPanel
        BoatsPanel boatsPanel = new BoatsPanel(storage);
        dialog.add(boatsPanel, BorderLayout.CENTER);
        
        // Set dialog size and position
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        
        // Refresh the owners list in the boats panel
        boatsPanel.refreshOwnersList();
        
        dialog.setVisible(true);
    }

    private void showDeleteDialog() {
        JDialog dialog = new JDialog(this, "Delete Owner/Boat", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JComboBox<String> ownerCombo = new JComboBox<>();
        for (Owner owner : storage.getOwners()) {
            ownerCombo.addItem(owner.getIdNumber() + " - " + owner.getName());
        }
        
        JButton deleteOwnerBtn = new JButton("Delete Selected Owner");
        deleteOwnerBtn.addActionListener(e -> {
            if (ownerCombo.getSelectedItem() != null) {
                String selectedOwner = (String) ownerCombo.getSelectedItem();
                int ownerId = Integer.parseInt(selectedOwner.split(" - ")[0]);
                if (storage.deleteOwner(ownerId)) {
                    updateStatus("Owner deleted successfully!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Error deleting owner",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        panel.add(new JLabel("Select Owner:"));
        panel.add(ownerCombo);
        panel.add(deleteOwnerBtn);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showStatisticsDialog() {
        JDialog dialog = new JDialog(this, "Statistics", true);
        dialog.setLayout(new BorderLayout());
        
        // Create and add StatisticsPanel
        StatisticsPanel statsPanel = new StatisticsPanel(storage);
        dialog.add(statsPanel, BorderLayout.CENTER);
        
        // Set dialog size and position
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showManageDialog() {
        JDialog dialog = new JDialog(this, "Manage Storage", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField chargeRateField = new JTextField(String.valueOf(Boat.getChargeRate()));
        JButton updateButton = new JButton("Update Charge Rate");
        
        updateButton.addActionListener(e -> {
            try {
                double newRate = Double.parseDouble(chargeRateField.getText());
                Boat.setChargeRate(newRate);
                updateStatus("Charge rate updated successfully!");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Please enter a valid number",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(new JLabel("Storage Charge Rate ($ per cubic meter):"));
        panel.add(chargeRateField);
        panel.add(updateButton);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void saveDatabase() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                FileManager.saveToFile(storage, selectedFile);
                updateStatus("Database saved successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error saving database: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadDatabase() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                storage = FileManager.loadFromFile(selectedFile);
                updateStatus("Database loaded successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error loading database: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateStatus(String message) {
        statusBar.setText(" " + message);
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            BoatStorageGUI gui = new BoatStorageGUI();
            gui.setVisible(true);
        });
    }
} 