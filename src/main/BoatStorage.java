package main;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Class representing a BoatStorage system.
 * This class manages the collection of boats and owners.
 */
public class BoatStorage {
    private List<Boat> boats; // List of all boats in storage
    private List<Owner> owners; // List of all owners
    private int ownerIDCounter; // Counter for generating unique owner IDs


    /**
     * Constructor for BoatStorage.
     */
    public BoatStorage() {
        this.boats = new ArrayList<>();
        this.owners = new ArrayList<>();
        this.ownerIDCounter = 1; // Start owner IDs from 1
    }

    // getter & setters
    public List<Boat> getBoats() {
        return boats;
    }

    /**
     * Sets the list of boats in storage.
     *
     * @param boats the list of boats
     */
    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    /**
     * Sets the list of owners.
     *
     * @param owners the list of owners
     */
    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public int getOwnerIDCounter() {
        return ownerIDCounter;
    }

    /**
     * Sets the owner ID counter.
     *
     * @param ownerIDCounter the owner ID counter (must be positive)
     */
    public void setOwnerIDCounter(int ownerIDCounter) {
        this.ownerIDCounter = ownerIDCounter;
    }

    /**
     * Adds an existing Owner object to the storage.
     * @param owner The Owner object to add
     */
    public void addOwner(Owner owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        owners.add(owner);
    }

    /**
     * Deletes an owner and their boats from storage.
     * @param ownerID The ID of the owner to delete
     * @return true if owner was found and deleted, false otherwise
     */
    public boolean deleteOwner(int ownerID) {
        for (Owner owner : owners) {
            if (owner.getIdNumber() == ownerID) {
                // Remove all boats owned by this owner
                boats.removeAll(owner.getBoats());
                // Remove the owner
                owners.remove(owner);
                return true;
            }
        }
        return false;
    }

    /**
     * Creates and adds a new owner to the storage.
     * @param name The name of the owner
     * @param address The address of the owner
     */
    public void addOwner(String name, String address) {
        Owner owner = new Owner(ownerIDCounter++,
                name, address);
        owners.add(owner);
    }

    /**
     * Adds a boat to a specific owner.
     *
     * @param ownerID the ID of the owner
     * @param boat    the boat to add
     */
    public void addBoatToOwner(int ownerID, Boat boat) {
        for (Owner owner : owners) {
            if (owner.getIdNumber() == ownerID) {
                owner.addBoat(boat);
                boats.add(boat);
                return;
            }
        }
        System.out.println("Owner not found!");
    }

    // display statistics for all boats
    public void displayStatistics() {
        int sailBoatCount = 0;
        int motorBoatCount = 0;
        double totalStorageCharge = 0;
        double totalSailDryingCharge = 0;
        double totalFireLevyCharge = 0;
        double totalInsuranceLevy = 0;

        for (Boat boat : boats) {
            totalStorageCharge += boat.storageCharge();
            totalInsuranceLevy += boat.insuranceLevy();
            if (boat instanceof SailBoat) {
                sailBoatCount++;
                totalSailDryingCharge +=
                        ((SailBoat) boat).sailDryingCharge();
            } else if (boat instanceof MotorBoat) {
                motorBoatCount++;
                totalFireLevyCharge +=
                        ((MotorBoat) boat).fireLevyCharge();
            }
        }

        System.out.println("\n--- Boat Statistics ---");
        System.out.printf("Total Sail Boats: %d%n", sailBoatCount);
        System.out.printf("Total Motor Boats: %d%n", motorBoatCount);
        System.out.printf("Total Storage Charges: $%.2f%n", totalStorageCharge);
        System.out.printf("Total Sail Drying Charges: $%.2f%n", totalSailDryingCharge);
        System.out.printf("Total Fire Levy Charges: $%.2f%n", totalFireLevyCharge);
        System.out.printf("Total Insurance Levies: $%.2f%n", totalInsuranceLevy);
        System.out.println("-----------------------\n");
    }

    /**
     * Displays all owners, sorted by name or total charge.
     *
     * @param sortByName   if true, sort by owner name
     * @param sortByCharge if true, sort by total monthly charge
     */
    public void displayOwners(boolean sortByName,
                              boolean sortByCharge) {
        List<Owner> sortedOwners = new ArrayList<>(owners);
        if (sortByName) {
            sortedOwners.sort(Comparator.comparing(Owner::getName));
        } else if (sortByCharge) {
            sortedOwners.sort(Comparator.comparingDouble
                    (Owner::totalOwnerCharge));
        }

        for (Owner owner : sortedOwners) {
            System.out.println("Owner ID: " + owner.getIdNumber()
            + ", Name: " + owner.getName() + ", Total Charge: $"
            + owner.totalOwnerCharge());
        }
    }
}
