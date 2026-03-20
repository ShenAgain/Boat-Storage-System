package main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Panel for displaying statistics about the Boat Storage System.
 * Shows summary information and graphical representations of the data.
 */
public class StatisticsPanel extends JPanel {
    private BoatStorage storage;
    private JPanel summaryPanel;
    private JPanel chartPanel;
    
    // Summary labels
    private JLabel totalOwnersLabel;
    private JLabel totalBoatsLabel;
    private JLabel totalSailboatsLabel;
    private JLabel totalMotorboatsLabel;
    private JLabel averageChargeLabel;
    private JLabel totalChargesLabel;
    private JLabel averageBoatsPerOwnerLabel;
    
    public StatisticsPanel(BoatStorage storage) {
        this.storage = storage;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        createComponents();
        setupLayout();
        refreshStatistics();
    }
    
    private void createComponents() {
        // Create summary labels
        totalOwnersLabel = new JLabel("Total Owners: 0");
        totalBoatsLabel = new JLabel("Total Boats: 0");
        totalSailboatsLabel = new JLabel("Total Sailboats: 0");
        totalMotorboatsLabel = new JLabel("Total Motorboats: 0");
        averageChargeLabel = new JLabel("Average Monthly Charge: $0.00");
        totalChargesLabel = new JLabel("Total Monthly Charges: $0.00");
        averageBoatsPerOwnerLabel = new JLabel("Average Boats per Owner: 0.00");
    }
    
    private void setupLayout() {
        // Summary Panel
        summaryPanel = new JPanel(new GridBagLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Summary Statistics",
            TitledBorder.LEFT, TitledBorder.TOP));
            
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        // Add summary components with spacing and styling
        Font boldFont = new Font(getFont().getFontName(), Font.BOLD, 14);
        
        // Owners section
        addSectionHeader(summaryPanel, "Owner Statistics", boldFont, gbc);
        summaryPanel.add(totalOwnersLabel, gbc);
        summaryPanel.add(averageBoatsPerOwnerLabel, gbc);
        addSeparator(summaryPanel, gbc);
        
        // Boats section
        addSectionHeader(summaryPanel, "Boat Statistics", boldFont, gbc);
        summaryPanel.add(totalBoatsLabel, gbc);
        summaryPanel.add(totalSailboatsLabel, gbc);
        summaryPanel.add(totalMotorboatsLabel, gbc);
        addSeparator(summaryPanel, gbc);
        
        // Financial section
        addSectionHeader(summaryPanel, "Financial Statistics", boldFont, gbc);
        summaryPanel.add(averageChargeLabel, gbc);
        summaryPanel.add(totalChargesLabel, gbc);
        
        // Chart Panel
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCharts((Graphics2D) g);
            }
        };
        chartPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Visual Statistics",
            TitledBorder.LEFT, TitledBorder.TOP));
        chartPanel.setPreferredSize(new Dimension(400, 300));
        
        // Add panels to main panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            summaryPanel, chartPanel);
        splitPane.setResizeWeight(0.4);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private void addSectionHeader(JPanel panel, String text, Font font, GridBagConstraints gbc) {
        JLabel header = new JLabel(text);
        header.setFont(font);
        panel.add(header, gbc);
    }
    
    private void addSeparator(JPanel panel, GridBagConstraints gbc) {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(200, 1));
        panel.add(separator, gbc);
    }
    
    private void drawCharts(Graphics2D g2d) {
        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
            RenderingHints.VALUE_ANTIALIAS_ON);
            
        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        
        // Draw pie chart for boat types
        drawBoatTypesPieChart(g2d, width / 2, height / 2);
    }
    
    private void drawBoatTypesPieChart(Graphics2D g2d, int centerX, int centerY) {
        int radius = Math.min(centerX, centerY) - 40;
        if (radius < 20) return;  // Too small to draw
        
        // Count boat types
        int sailboats = 0;
        int motorboats = 0;
        for (Owner owner : storage.getOwners()) {
            for (Boat boat : owner.getBoats()) {
                if (boat instanceof SailBoat) sailboats++;
                else if (boat instanceof MotorBoat) motorboats++;
            }
        }
        
        int total = sailboats + motorboats;
        if (total == 0) {
            // Draw "No Data" message
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font(getFont().getFontName(), Font.BOLD, 14));
            String msg = "No boats in system";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(msg, 
                centerX - fm.stringWidth(msg) / 2,
                centerY);
            return;
        }
        
        // Draw pie slices
        int startAngle = 0;
        
        // Sailboats slice
        if (sailboats > 0) {
            g2d.setColor(new Color(65, 105, 225));  // Royal Blue
            int arcAngle = (int) (360.0 * sailboats / total);
            g2d.fillArc(centerX - radius, centerY - radius,
                radius * 2, radius * 2,
                startAngle, arcAngle);
            drawLegendItem(g2d, "Sailboats (" + sailboats + ")",
                new Color(65, 105, 225),
                centerX + radius + 10,
                centerY - 20);
            startAngle += arcAngle;
        }
        
        // Motorboats slice
        if (motorboats > 0) {
            g2d.setColor(new Color(220, 20, 60));  // Crimson
            int arcAngle = (int) (360.0 * motorboats / total);
            g2d.fillArc(centerX - radius, centerY - radius,
                radius * 2, radius * 2,
                startAngle, arcAngle);
            drawLegendItem(g2d, "Motorboats (" + motorboats + ")",
                new Color(220, 20, 60),
                centerX + radius + 10,
                centerY + 20);
        }
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font(getFont().getFontName(), Font.BOLD, 14));
        String title = "Boat Type Distribution";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, 
            centerX - fm.stringWidth(title) / 2,
            centerY - radius - 10);
    }
    
    private void drawLegendItem(Graphics2D g2d, String text, Color color,
        int x, int y) {
        int boxSize = 12;
        g2d.setColor(color);
        g2d.fillRect(x, y - boxSize + 2, boxSize, boxSize);
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x + boxSize + 5, y);
    }
    
    public void refreshStatistics() {
        // Count totals
        int totalOwners = storage.getOwners().size();
        int totalBoats = 0;
        int sailboats = 0;
        int motorboats = 0;
        double totalCharges = 0.0;
        
        for (Owner owner : storage.getOwners()) {
            for (Boat boat : owner.getBoats()) {
                totalBoats++;
                if (boat instanceof SailBoat) sailboats++;
                else if (boat instanceof MotorBoat) motorboats++;
                totalCharges += boat.totalMonthlyCharge();
            }
        }
        
        // Calculate averages
        double averageCharge = totalBoats > 0 ? totalCharges / totalBoats : 0;
        double averageBoatsPerOwner = totalOwners > 0 ? 
            (double) totalBoats / totalOwners : 0;
        
        // Update labels
        totalOwnersLabel.setText(String.format("Total Owners: %d", totalOwners));
        totalBoatsLabel.setText(String.format("Total Boats: %d", totalBoats));
        totalSailboatsLabel.setText(String.format("Total Sailboats: %d", sailboats));
        totalMotorboatsLabel.setText(String.format("Total Motorboats: %d", motorboats));
        averageChargeLabel.setText(String.format("Average Monthly Charge: $%.2f", 
            averageCharge));
        totalChargesLabel.setText(String.format("Total Monthly Charges: $%.2f", 
            totalCharges));
        averageBoatsPerOwnerLabel.setText(String.format("Average Boats per Owner: %.2f",
            averageBoatsPerOwner));
        
        // Refresh chart
        chartPanel.repaint();
    }
} 