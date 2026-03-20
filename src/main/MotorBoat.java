package main;

/**
 * Class representing a MotorBoat, which is a type of Boat.
 * This class extends the Boat class and adds attributes specific to motorboats.
 */
public class MotorBoat extends Boat {
    private double horsePower;
    private static double fireRate = 0.10;

    /**
     * Constructor for MotorBoat.
     *
     * @param height     the height of the motorboat in meters
     * @param length     the length of the motorboat in meters
     * @param width      the width of the motorboat in meters
     * @param boatValue  the value of the motorboat in dollars
     * @param horsePower the horsepower of the motorboat
     */
    public MotorBoat(double height, double length, double width,
                     double boatValue, double horsePower) {
        super(height, length, width, boatValue);
        this.horsePower = horsePower;
    }

    // getter & setter

    public double getHorsePower() {
        return horsePower;
    }

    /**
     * Sets the horsepower of the motorboat.
     *
     * @param horsePower the horsepower (must be positive)
     */
    public void setHorsePower(double horsePower) {
        this.horsePower = horsePower;
    }

    public static double getFireRate() {
        return fireRate;
    }

    /**
     * Sets the fire rate (percentage of horsepower used for fire levy charge).
     *
     * @param fireRate the fire rate (must be positive)
     */
    public static void setFireRate(double fireRate) {
        MotorBoat.fireRate = fireRate;
    }

    /**
     * Calculates the fire levy charge (10% of horsepower).
     *
     * @return the fire levy charge in dollars
     */    public double fireLevyCharge() {
         return horsePower * fireRate;
    }


    /**
     * Calculates the total monthly charge for the motorboat.
     * Total charge = storage charge + insurance levy + fire levy charge.
     *
     * @return the total monthly charge in dollars
     */
    @Override
    public double totalMonthlyCharge() {
        return storageCharge() + insuranceLevy() + fireLevyCharge();
    }
}
