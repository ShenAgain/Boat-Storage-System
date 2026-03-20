package main;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import java.util.List;

/**
 * Panel for managing boats in the Boat Storage System.
 * Provides functionality to view, add, and update boat information.
 */
public class BoatsPanel extends JPanel {
    private BoatStorage storage;
    private JTable boatsTable;
    private DefaultTableModel tableModel;
    private JComboBox<Owner> ownerComboBox;
    private JComboBox<String> boatTypeComboBox;
    
    // Common boat fields
    private JTextField heightField;
    private JTextField lengthField;
    private JTextField widthField;
    private JTextField valueField;
    
    // Specific boat fields
    private JPanel sailboatPanel;
    private JTextField mastHeightField;
    private JTextField sailAreaField;
    
    private JPanel motorboatPanel;
    private JTextField horsePowerField;
    
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JComboBox<Owner> filterOwnerComboBox;
    private JButton showAllButton;

    public BoatsPanel(BoatStorage storage) {
        this.storage = storage;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        createComponents();
        setupLayout();
        setupListeners();
        refreshTable();
    }

    private void createComponents() {
        // Create table
        String[] columnNames = {"Owner", "Type", "Height", "Length", "Width", "Value", "Monthly Charge"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                return column >= 2 ? Double.class : String.class;
            }
        };
        boatsTable = new JTable(tableModel);
        boatsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Enable sorting
        boatsTable.setAutoCreateRowSorter(true);
        
        // Set column widths
        // Owner
        boatsTable.getColumnModel().getColumn(0).setPreferredWidth(150); 
        // Type
        boatsTable.getColumnModel().getColumn(1).setPreferredWidth(100); 
        // Height
        boatsTable.getColumnModel().getColumn(2).setPreferredWidth(70); 
        // Length 
        boatsTable.getColumnModel().getColumn(3).setPreferredWidth(70);  
        // Width
        boatsTable.getColumnModel().getColumn(4).setPreferredWidth(70);  
        // Value
        boatsTable.getColumnModel().getColumn(5).setPreferredWidth(100); 
        // Monthly Charge
        boatsTable.getColumnModel().getColumn(6).setPreferredWidth(100); 
        
        // Set number format for numeric columns
        TableCellRenderer numberRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                if (value instanceof Double) {
                    setText(String.format("%.2f", value));
                }
                setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        };
        
        for (int i = 2; i <= 6; i++) { // Apply to numeric columns
            boatsTable.getColumnModel().getColumn(i).setCellRenderer(numberRenderer);
        }
        
        // Create form components
        ownerComboBox = new JComboBox<>();
        boatTypeComboBox = new JComboBox<>(new String[]{"Sailboat", "Motorboat"});
        
        heightField = new JTextField(10);
        lengthField = new JTextField(10);
        widthField = new JTextField(10);
        valueField = new JTextField(10);
        
        mastHeightField = new JTextField(10);
        sailAreaField = new JTextField(10);
        horsePowerField = new JTextField(10);
        
        // Create specific boat type panels
        sailboatPanel = new JPanel(new GridBagLayout());
        sailboatPanel.setBorder(BorderFactory.createTitledBorder("Sailboat Specific"));
        motorboatPanel = new JPanel(new GridBagLayout());
        motorboatPanel.setBorder(BorderFactory.createTitledBorder("Motorboat Specific"));
        
        // Create buttons
        addButton = new JButton("Add Boat");
        updateButton = new JButton("Update Boat");
        deleteButton = new JButton("Delete Boat");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        // Create filter components
        filterOwnerComboBox = new JComboBox<>();
        filterOwnerComboBox.addItem(null);  // Add null option for "All Owners"
        showAllButton = new JButton("Show All Boats");
    }

    private void setupLayout() {
        // Main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.6);
        
        // Left panel - Table
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Boats List"));

        // Add filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Owner:"));
        filterPanel.add(filterOwnerComboBox);
        filterPanel.add(showAllButton);
        leftPanel.add(filterPanel, BorderLayout.NORTH);
        
        leftPanel.add(new JScrollPane(boatsTable), BorderLayout.CENTER);
        
        // Add Delete Boat button under the table
        JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tableButtonPanel.add(deleteButton);
        leftPanel.add(tableButtonPanel, BorderLayout.SOUTH);
        
        // Right panel - Form
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Boat Details"));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add form components
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Owner:"), gbc);
        gbc.gridx = 1;
        formPanel.add(ownerComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Boat Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(boatTypeComboBox, gbc);
        
        // Common boat details
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Height:"), gbc);
        gbc.gridx = 1;
        formPanel.add(heightField, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Length:"), gbc);
        gbc.gridx = 1;
        formPanel.add(lengthField, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Width:"), gbc);
        gbc.gridx = 1;
        formPanel.add(widthField, gbc);
        
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Value:"), gbc);
        gbc.gridx = 1;
        formPanel.add(valueField, gbc);
        
        // Sailboat specific panel
        gbc.gridx = 0; gbc.gridy = 0;
        sailboatPanel.add(new JLabel("Mast Height:"), gbc);
        gbc.gridx = 1;
        sailboatPanel.add(mastHeightField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        sailboatPanel.add(new JLabel("Sail Area:"), gbc);
        gbc.gridx = 1;
        sailboatPanel.add(sailAreaField, gbc);
        
        // Motorboat specific panel
        gbc.gridx = 0; gbc.gridy = 0;
        motorboatPanel.add(new JLabel("Horse Power:"), gbc);
        gbc.gridx = 1;
        motorboatPanel.add(horsePowerField, gbc);
        
        // Add specific panels to card layout
        JPanel specificPanel = new JPanel(new CardLayout());
        specificPanel.add(sailboatPanel, "Sailboat");
        specificPanel.add(motorboatPanel, "Motorboat");
        
        // Add all panels to form
        rightPanel.add(formPanel, BorderLayout.NORTH);
        rightPanel.add(specificPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add panels to split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        
        // Add split pane to main panel
        add(splitPane, BorderLayout.CENTER);
    }

    private void setupListeners() {
        // Boat type combo box listener
        boatTypeComboBox.addActionListener(e -> {
            String selectedType = (String) boatTypeComboBox.getSelectedItem();
            CardLayout cl = (CardLayout) ((JPanel) sailboatPanel.getParent()).getLayout();
            cl.show(sailboatPanel.getParent(), selectedType);
        });
        
        // Table selection listener
        boatsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = boatsTable.getSelectedRow();
                if (selectedRow != -1) {
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    displayBoatDetails(selectedRow);
                } else {
                    clearForm();
                }
            }
        });
        
        // Add button listener
        addButton.addActionListener(e -> addBoat());
        
        // Update button listener
        updateButton.addActionListener(e -> updateBoat());
        
        // Delete button listener
        deleteButton.addActionListener(e -> deleteBoat());

        // Filter owner combo box listener
        filterOwnerComboBox.addActionListener(e -> {
            Owner selectedOwner = (Owner) filterOwnerComboBox.getSelectedItem();
            filterTableByOwner(selectedOwner);
        });

        // Show all button listener
        showAllButton.addActionListener(e -> {
            filterOwnerComboBox.setSelectedItem(null);
            refreshTable();
        });
    }

    private void displayBoatDetails(int row) {
        String ownerName = (String) tableModel.getValueAt(row, 0);
        String boatType = (String) tableModel.getValueAt(row, 1);
        double height = (Double) tableModel.getValueAt(row, 2);
        double length = (Double) tableModel.getValueAt(row, 3);
        double width = (Double) tableModel.getValueAt(row, 4);
        double value = (Double) tableModel.getValueAt(row, 5);

        // Set common fields
        heightField.setText(String.format("%.2f", height));
        lengthField.setText(String.format("%.2f", length));
        widthField.setText(String.format("%.2f", width));
        valueField.setText(String.format("%.2f", value));

        // Set boat type and show appropriate panel
        boatTypeComboBox.setSelectedItem(boatType);

        // Find the owner and boat
        for (Owner owner : storage.getOwners()) {
            if (owner.getName().equals(ownerName)) {
                ownerComboBox.setSelectedItem(owner);
                for (Boat boat : owner.getBoats()) {
                    if (boat.getHeight() == height && boat.getLength() == length 
                        && boat.getWidth() == width && boat.getBoatValue() == value) {
                        // Set specific fields based on boat type
                        if (boat instanceof SailBoat) {
                            SailBoat sailBoat = (SailBoat) boat;
                            mastHeightField.setText(String.format("%.2f", sailBoat.getMastHeight()));
                            sailAreaField.setText(String.format("%.2f", sailBoat.getSailArea()));
                        } else if (boat instanceof MotorBoat) {
                            MotorBoat motorBoat = (MotorBoat) boat;
                            horsePowerField.setText(String.format("%.2f", motorBoat.getHorsePower()));
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    private void clearForm() {
        heightField.setText("");
        lengthField.setText("");
        widthField.setText("");
        valueField.setText("");
        mastHeightField.setText("");
        sailAreaField.setText("");
        horsePowerField.setText("");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void validateInput(double value, String fieldName) throws IllegalArgumentException {
        if (value <= 0) {
            throw new IllegalArgumentException(fieldName + " must be greater than 0");
        }
    }

    private void addBoat() {
        Owner owner = (Owner) ownerComboBox.getSelectedItem();
        if (owner == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an owner",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Parse values
            double height = Double.parseDouble(heightField.getText().trim());
            double length = Double.parseDouble(lengthField.getText().trim());
            double width = Double.parseDouble(widthField.getText().trim());
            double value = Double.parseDouble(valueField.getText().trim());

            // Validate values
            validateInput(height, "Height");
            validateInput(length, "Length");
            validateInput(width, "Width");
            validateInput(value, "Value");

            Boat newBoat = null;
            String boatType = (String) boatTypeComboBox.getSelectedItem();
            
            if ("Sailboat".equals(boatType)) {
                double mastHeight = Double.parseDouble(mastHeightField.getText().trim());
                double sailArea = Double.parseDouble(sailAreaField.getText().trim());
                validateInput(mastHeight, "Mast height");
                validateInput(sailArea, "Sail area");
                newBoat = new SailBoat(height, length, width, value, mastHeight, sailArea);
            } else {
                double horsePower = Double.parseDouble(horsePowerField.getText().trim());
                validateInput(horsePower, "Horse power");
                newBoat = new MotorBoat(height, length, width, value, horsePower);
            }

            owner.addBoat(newBoat);
            clearForm();
            refreshTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for all measurements",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBoat() {
        int selectedRow = boatsTable.getSelectedRow();
        if (selectedRow == -1) return;

        // Convert row index from view to model since table is sortable
        selectedRow = boatsTable.convertRowIndexToModel(selectedRow);

        Owner owner = (Owner) ownerComboBox.getSelectedItem();
        if (owner == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an owner",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Parse values
            double height = Double.parseDouble(heightField.getText().trim());
            double length = Double.parseDouble(lengthField.getText().trim());
            double width = Double.parseDouble(widthField.getText().trim());
            double value = Double.parseDouble(valueField.getText().trim());

            // Validate values
            validateInput(height, "Height");
            validateInput(length, "Length");
            validateInput(width, "Width");
            validateInput(value, "Value");

            // Find the original owner and boat
            String originalOwnerName = (String) tableModel.getValueAt(selectedRow, 0);
            Owner originalOwner = null;
            Boat boatToUpdate = null;
            
            for (Owner o : storage.getOwners()) {
                if (o.getName().equals(originalOwnerName)) {
                    originalOwner = o;
                    double originalHeight = (Double) tableModel.getValueAt(selectedRow, 2);
                    double originalLength = (Double) tableModel.getValueAt(selectedRow, 3);
                    double originalWidth = (Double) tableModel.getValueAt(selectedRow, 4);
                    double originalValue = (Double) tableModel.getValueAt(selectedRow, 5);
                    
                    for (Boat b : o.getBoats()) {
                        if (b.getHeight() == originalHeight && b.getLength() == originalLength 
                            && b.getWidth() == originalWidth && b.getBoatValue() == originalValue) {
                            boatToUpdate = b;
                            break;
                        }
                    }
                    break;
                }
            }

            if (originalOwner == null || boatToUpdate == null) {
                JOptionPane.showMessageDialog(this,
                    "Error finding boat to update",
                    "Update Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create new boat with updated values
            Boat updatedBoat = null;
            String boatType = (String) boatTypeComboBox.getSelectedItem();
            
            if ("Sailboat".equals(boatType)) {
                double mastHeight = Double.parseDouble(mastHeightField.getText().trim());
                double sailArea = Double.parseDouble(sailAreaField.getText().trim());
                validateInput(mastHeight, "Mast height");
                validateInput(sailArea, "Sail area");
                updatedBoat = new SailBoat(height, length, width, value, mastHeight, sailArea);
            } else {
                double horsePower = Double.parseDouble(horsePowerField.getText().trim());
                validateInput(horsePower, "Horse power");
                updatedBoat = new MotorBoat(height, length, width, value, horsePower);
            }

            // Remove boat from original owner
            originalOwner.getBoats().remove(boatToUpdate);
            
            // Add to new owner (might be the same owner)
            owner.addBoat(updatedBoat);

            clearForm();
            refreshTable();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for all measurements",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBoat() {
        int selectedRow = boatsTable.getSelectedRow();
        if (selectedRow == -1) return;

        // Convert row index from view to model since table is sortable
        selectedRow = boatsTable.convertRowIndexToModel(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this boat?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            String ownerName = (String) tableModel.getValueAt(selectedRow, 0);
            double height = (Double) tableModel.getValueAt(selectedRow, 2);
            double length = (Double) tableModel.getValueAt(selectedRow, 3);
            double width = (Double) tableModel.getValueAt(selectedRow, 4);
            double value = (Double) tableModel.getValueAt(selectedRow, 5);

            // Find the owner and boat
            for (Owner owner : storage.getOwners()) {
                if (owner.getName().equals(ownerName)) {
                    owner.getBoats().removeIf(boat -> 
                        boat.getHeight() == height &&
                        boat.getLength() == length &&
                        boat.getWidth() == width &&
                        boat.getBoatValue() == value
                    );
                    break;
                }
            }

            clearForm();
            refreshTable();
        }
    }

    private void filterTableByOwner(Owner owner) {
        if (owner == null) {
            refreshTable();
            return;
        }

        tableModel.setRowCount(0);
        for (Boat boat : owner.getBoats()) {
            tableModel.addRow(new Object[]{
                owner.getName(),
                boat.getClass().getSimpleName(),
                boat.getHeight(),
                boat.getLength(),
                boat.getWidth(),
                boat.getBoatValue(),
                boat.totalMonthlyCharge()
            });
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Owner owner : storage.getOwners()) {
            for (Boat boat : owner.getBoats()) {
                tableModel.addRow(new Object[]{
                    owner.getName(),
                    boat.getClass().getSimpleName(),
                    boat.getHeight(),
                    boat.getLength(),
                    boat.getWidth(),
                    boat.getBoatValue(),
                    boat.totalMonthlyCharge()
                });
            }
        }
    }

    public void refreshOwnersList() {
        // Update both combo boxes
        ownerComboBox.removeAllItems();
        filterOwnerComboBox.removeAllItems();
        
        filterOwnerComboBox.addItem(null);  // Add null option for "All Owners"
        for (Owner owner : storage.getOwners()) {
            ownerComboBox.addItem(owner);
            filterOwnerComboBox.addItem(owner);
        }
    }
} 