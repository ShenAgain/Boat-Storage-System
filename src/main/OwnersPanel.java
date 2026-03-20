package main;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Panel for managing boat owners in the Boat Storage System.
 * Provides functionality to view, add, and update owner information.
 */
public class OwnersPanel extends JPanel {
    private BoatStorage storage;
    private JTable ownersTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextArea addressArea;
    private JLabel idLabel;
    private JButton addButton;
    private JButton updateButton;
    private JButton viewChargesButton;
    private JComboBox<String> sortComboBox;

    public OwnersPanel(BoatStorage storage) {
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
        String[] columnNames = {"ID", "Name", "Address", "Number of Boats", "Total Charges ($)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                // Return Double class for the Total Charges column for proper number formatting
                return column == 4 ? Double.class : Object.class;
            }
        };
        ownersTable = new JTable(tableModel);
        ownersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set preferred column widths
        ownersTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        ownersTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Name
        ownersTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Address
        ownersTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Number of Boats
        ownersTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Total Charges
        
        // Set number format for Total Charges column
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
        ownersTable.getColumnModel().getColumn(4).setCellRenderer(numberRenderer);
        
        // Create form components
        idLabel = new JLabel("ID: ");
        nameField = new JTextField(20);
        addressArea = new JTextArea(4, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        
        // Create buttons
        addButton = new JButton("Add New Owner");
        updateButton = new JButton("Update Owner");
        viewChargesButton = new JButton("View Charges");
        updateButton.setEnabled(false);
        viewChargesButton.setEnabled(false);
        
        // Create sort combo box
        sortComboBox = new JComboBox<>(new String[]{"Sort by Name", "Sort by Total Charge"});
    }

    private void setupLayout() {
        // Left panel - Table and sort controls
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Owners List"));
        
        // Add sort controls
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.add(new JLabel("Sort:"));
        sortPanel.add(sortComboBox);
        leftPanel.add(sortPanel, BorderLayout.NORTH);
        
        // Add table with scroll pane
        JScrollPane scrollPane = new JScrollPane(ownersTable);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Right panel - Owner details form
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Owner Details"));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add form components
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(idLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(addressArea), gbc);
        
        rightPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(viewChargesButton);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add panels to main panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
    }

    private void setupListeners() {
        // Table selection listener
        ownersTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = ownersTable.getSelectedRow();
                if (selectedRow != -1) {
                    updateButton.setEnabled(true);
                    viewChargesButton.setEnabled(true);
                    displayOwnerDetails(selectedRow);
                } else {
                    clearForm();
                }
            }
        });

        // Add button listener
        addButton.addActionListener(e -> addOwner());

        // Update button listener
        updateButton.addActionListener(e -> updateOwner());

        // View charges button listener
        viewChargesButton.addActionListener(e -> viewCharges());

        // Sort combo box listener
        sortComboBox.addActionListener(e -> sortOwners());
    }

    private void displayOwnerDetails(int row) {
        idLabel.setText("ID: " + tableModel.getValueAt(row, 0));
        nameField.setText((String)tableModel.getValueAt(row, 1));
        addressArea.setText((String)tableModel.getValueAt(row, 2));
    }

    private void clearForm() {
        idLabel.setText("ID: ");
        nameField.setText("");
        addressArea.setText("");
        updateButton.setEnabled(false);
        viewChargesButton.setEnabled(false);
    }

    private void addOwner() {
        String name = nameField.getText().trim();
        String address = addressArea.getText().trim();
        
        if (name.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        storage.addOwner(name, address);
        clearForm();
        refreshTable();
    }

    private void updateOwner() {
        int selectedRow = ownersTable.getSelectedRow();
        if (selectedRow == -1) return;

        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String name = nameField.getText().trim();
        String address = addressArea.getText().trim();

        if (name.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update owner in storage
        for (Owner owner : storage.getOwners()) {
            if (owner.getIdNumber() == id) {
                owner.setName(name);
                owner.setAddress(address);
                break;
            }
        }

        refreshTable();
    }

    private void viewCharges() {
        int selectedRow = ownersTable.getSelectedRow();
        if (selectedRow == -1) return;

        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        
        for (Owner owner : storage.getOwners()) {
            if (owner.getIdNumber() == id) {
                // Create and show charges dialog
                JDialog chargesDialog = new JDialog();
                chargesDialog.setTitle("Owner Charges");
                chargesDialog.setModal(true);
                chargesDialog.setLayout(new BorderLayout(10, 10));
                
                // Create text area for charges
                JTextArea chargesArea = new JTextArea(10, 40);
                chargesArea.setEditable(false);
                chargesArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                
                // Build charges text
                StringBuilder chargesText = new StringBuilder();
                chargesText.append("Owner: ").append(owner.getName()).append("\n\n");
                chargesText.append("=== Boat Charges ===\n\n");
                
                for (Boat boat : owner.getBoats()) {
                    chargesText.append("Boat Type: ").append(boat.getClass().getSimpleName()).append("\n");
                    chargesText.append("Storage Charge: $").append(String.format("%.2f", boat.storageCharge())).append("\n");
                    if (boat instanceof SailBoat) {
                        chargesText.append("Sail Drying Charge: $")
                                 .append(String.format("%.2f", ((SailBoat) boat).sailDryingCharge()))
                                 .append("\n");
                    } else if (boat instanceof MotorBoat) {
                        chargesText.append("Fire Levy Charge: $")
                                 .append(String.format("%.2f", ((MotorBoat) boat).fireLevyCharge()))
                                 .append("\n");
                    }
                    chargesText.append("Insurance Levy: $")
                              .append(String.format("%.2f", boat.insuranceLevy()))
                              .append("\n");
                    chargesText.append("Total Monthly Charge: $")
                              .append(String.format("%.2f", boat.totalMonthlyCharge()))
                              .append("\n\n");
                }
                
                chargesText.append("=== Total Charges ===\n");
                chargesText.append("Total Monthly Charges: $")
                          .append(String.format("%.2f", owner.totalOwnerCharge()));
                
                chargesArea.setText(chargesText.toString());
                
                chargesDialog.add(new JScrollPane(chargesArea), BorderLayout.CENTER);
                
                // Add close button
                JButton closeButton = new JButton("Close");
                closeButton.addActionListener(e -> chargesDialog.dispose());
                JPanel buttonPanel = new JPanel();
                buttonPanel.add(closeButton);
                chargesDialog.add(buttonPanel, BorderLayout.SOUTH);
                
                // Show dialog
                chargesDialog.pack();
                chargesDialog.setLocationRelativeTo(this);
                chargesDialog.setVisible(true);
                break;
            }
        }
    }

    private void sortOwners() {
        String sortOption = (String) sortComboBox.getSelectedItem();
        List<Owner> owners = new ArrayList<>(storage.getOwners());

        if ("Sort by Name".equals(sortOption)) {
            // Sort by name
            Collections.sort(owners, (o1, o2) -> 
                o1.getName().compareToIgnoreCase(o2.getName()));
        } else {
            // Sort by total charge
            Collections.sort(owners, (o1, o2) -> 
                Double.compare(o2.totalOwnerCharge(), o1.totalOwnerCharge()));
        }

        // Update the table with sorted data
        tableModel.setRowCount(0);
        for (Owner owner : owners) {
            tableModel.addRow(new Object[]{
                owner.getIdNumber(),
                owner.getName(),
                owner.getAddress(),
                owner.getBoats().size(),
                owner.totalOwnerCharge()
            });
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Owner owner : storage.getOwners()) {
            tableModel.addRow(new Object[]{
                owner.getIdNumber(),
                owner.getName(),
                owner.getAddress(),
                owner.getBoats().size(),
                owner.totalOwnerCharge()
            });
        }
    }
} 