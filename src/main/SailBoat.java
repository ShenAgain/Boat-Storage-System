package main;

/**
 * Class representing a SailBoat, which is a type of Boat.
 * This class extends the Boat class and adds attributes specific to sailboats.
 */
public class SailBoat extends Boat {
    private double mastHeight; // in meters
    private double sailArea; // in square meters
    private static double sailRate = 0.10; // 10% of sail area

    /**
     * Constructor for SailBoat.
     *
     * @param height     the height of the sailboat in meters
     * @param length     the length of the sailboat in meters
     * @param width      the width of the sailboat in meters
     * @param boatValue  the value of the sailboat in dollars
     * @param mastHeight the mast height of the sailboat in meters
     * @param sailArea   the sail area of the sailboat in square meters
     */
    public SailBoat(double height, double length, double width,
                    double boatValue, double mastHeight,
                    double sailArea) {
        super(height, length, width, boatValue);
        this.mastHeight = mastHeight;
        this.sailArea = sailArea;
    }

    // getter & setter
    public double getMastHeight() {
        return mastHeight;
    }

    /**
     * Sets the mast height of the sailboat.
     *
     * @param mastHeight the mast height in meters (must be positive)
     */
    public void setMastHeight(double mastHeight) {
        this.mastHeight = mastHeight;
    }

    public double getSailArea() {
        return sailArea;
    }

    /**
     * Sets the sail area of the sailboat.
     *
     * @param sailArea the sail area in square meters (must be positive)
     */
    public void setSailArea(double sailArea) {
        this.sailArea = sailArea;
    }

    public static double getSailRate() {
        return sailRate;
    }

    /**
     * Sets the sail rate (percentage of sail area used for drying charge).
     *
     * @param sailRate the sail rate (must be positive)
     */
    public static void setSailRate(double sailRate) {
        SailBoat.sailRate = sailRate;
    }

    /**
     * Calculates the sail drying charge (10% of sail area).
     *
     * @return the sail drying charge in dollars
     */    public double sailDryingCharge() {
        return sailArea * sailRate;
    }

    /**
     * Calculates the total monthly charge for the sailboat.
     * Total charge = storage charge + insurance levy + sail drying charge.
     *
     * @return the total monthly charge in dollars
     */
    @Override
    public double totalMonthlyCharge() {
        return storageCharge()
                + insuranceLevy() + sailDryingCharge();
    }
}
