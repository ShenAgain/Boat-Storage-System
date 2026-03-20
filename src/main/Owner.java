package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an Owner of boats.
 * This class manages the owner's details and their collection of boats.
 */
public class Owner implements Serializable {
    private static final long serialVersionUID = 1L;
    private int idNumber; // Unique identifier for the owner
    private String name; // Name of the owner
    private String address; // Address of the owner
    private List<Boat> boats; // List of boats owned by this owner

    /**
     * Constructor for Owner.
     *
     * @param idNumber the unique identifier for the owner
     * @param name     the name of the owner
     * @param address  the address of the owner
     */
    public Owner(int idNumber, String name, String address) {
        this.idNumber = idNumber;
        this.name = name;
        this.address = address;
        this.boats = new ArrayList<>(); // Initialize the list of boats
    }

    // getter & setter

    public int getIdNumber() {
        return idNumber;
    }

    /**
     * Sets the unique identifier for the owner.
     *
     * @param idNumber the unique identifier (must be positive)
     */
    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the name of the owner.
     *
     * @param name the name of the owner
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the owner.
     *
     * @param address the address of the owner
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public List<Boat> getBoats() {
        return boats;
    }

    /**
     * Sets the list of boats owned by the owner.
     *
     * @param boats the list of boats
     */
    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }

    /**
     * Adds a boat to the owner's collection.
     *
     * @param boat the boat to add
     */
    public void addBoat(Boat boat) {
        boats.add(boat);
    }

    /**
     * Calculates the total charges for all boats owned by this owner.
     *
     * @return the total monthly charge in dollars
     */
    public double totalOwnerCharge() {
        double total = 0;
        for (Boat boat : boats) {
            total += boat.totalMonthlyCharge();
        }
        return total;
    }

    /**
     * Displays the charges for all boats owned by this owner.
     */
    public void displayCharges() {
        System.out.println("Charges for Owner ID: " + idNumber
                + "("+ name + ")");
        for (Boat boat : boats) {
            System.out.println("Boat Type: "
                    + boat.getClass().getSimpleName());
            System.out.println("Storage Charge: $" +
                    boat.storageCharge());
            if (boat instanceof SailBoat) {
                System.out.println("Sail Drying Charge: $" +
                        ((SailBoat) boat).sailDryingCharge());
            } else if (boat instanceof MotorBoat) {
                System.out.println("Fire Levy Charge: $" +
                        ((MotorBoat) boat).fireLevyCharge());
            }
            System.out.println("Insurance Levy: $" +
                    boat.insuranceLevy());
            System.out.println("Total Monthly Charge: $" +
                    boat.totalMonthlyCharge());
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return name + " (ID: " + idNumber + ")";
    }
}
