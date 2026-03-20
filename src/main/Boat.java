package main;

import java.io.Serializable;

public abstract class Boat implements Serializable {
    private static final long serialVersionUID = 1L;
    private double height; // in meters
    private double length; // in meters
    private double width; // in meters
    private double boatValue; // in dollars
    private static double chargeRate = 10.0; //Charge rate per cm

    /**
     * Constructor for Boat.
     *
     * @param height    the height of the boat in meters
     * @param length    the length of the boat in meters
     * @param width     the width of the boat in meters
     * @param boatValue the value of the boat in dollars
     */
    public Boat(double height, double length,
                double width, double boatValue) {
        this.height = height;
        this.length = length;
        this.width = width;
        this.boatValue = boatValue;
    }

    // getters and Setters
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of the boat.
     *
     * @param height the height in meters (must be positive)
     */
    public void setHeight(double height) {
        this.height = height;
    }


    public double getLength() {
        return length;
    }

    /**
     * Sets the length of the boat.
     *
     * @param length the length in meters (must be positive)
     */
    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    /**
     * Sets the width of the boat.
     *
     * @param width the width in meters (must be positive)
     */
    public void setWidth(double width) {
        this.width = width;
    }

    public double getBoatValue() {
        return boatValue;
    }

    /**
     * Sets the value of the boat.
     *
     * @param boatValue the value in dollars (must be positive)
     */
    public void setBoatValue(double boatValue) {
        this.boatValue = boatValue;
    }

    public static double getChargeRate() {
        return chargeRate;
    }

    /**
     * Sets the charge rate per cubic meter.
     *
     * @param chargeRate the charge rate in dollars per cubic meter (must be positive)
     */
    public static void setChargeRate(double chargeRate) {
        Boat.chargeRate = chargeRate;
    }

    /**
     * Abstract method to calculate the total monthly charge for the boat.
     *
     * @return the total monthly charge in dollars
     */
    public abstract double totalMonthlyCharge();

    /**
     * Calculates the storage charge based on the boat's volume.
     * Volume = height * length * width (in cubic meters).
     * Storage charge = volume * charge rate.
     *
     * @return the storage charge in dollars
     */    public double storageCharge() {
        return height * length * width * chargeRate;
    }

    /**
     * Calculates the insurance levy (0.05% of the boat's value).
     *
     * @return the insurance levy in dollars
     */    public double insuranceLevy() {
        return boatValue * 0.0005;
    }
}
