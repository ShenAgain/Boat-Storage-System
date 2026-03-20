package main;
import java.util.Scanner;

/**
 * Main class to run the Boat Storage System.
 * This class provides a console-based interface for managing boats and owners.
 */
public class BoatStorageSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BoatStorage storage = new BoatStorage();

        while (true) {
            System.out.println("\n1. Add Owner");
            System.out.println("2. Add Boat");
            System.out.println("3. Display Charges for Owner");
            System.out.println("4. Display Statistics");
            System.out.println("5. Display Owners (Sorted)");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Owner Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Owner Address: ");
                    String address = scanner.nextLine();
                    storage.addOwner(name, address);
                    break;

                case 2:
                    System.out.print("Enter Owner ID: ");
                    int ownerId = scanner.nextInt();
                    System.out.print("Enter Boat Type (1. SailBoat, 2. MotorBoat): ");
                    int boatType = scanner.nextInt();
                    System.out.print("Enter Height: ");
                    double height = scanner.nextDouble();
                    System.out.print("Enter Length: ");
                    double length = scanner.nextDouble();
                    System.out.print("Enter Width: ");
                    double width = scanner.nextDouble();
                    System.out.print("Enter Boat Value: ");
                    double boatValue = scanner.nextDouble();

                    if (boatType == 1) {
                        System.out.print("Enter Mast Height: ");
                        double mastHeight = scanner.nextDouble();
                        System.out.print("Enter Sail Area: ");
                        double sailArea = scanner.nextDouble();
                        storage.addBoatToOwner(ownerId,
                                new SailBoat(height, length, width,
                                        boatValue, mastHeight, sailArea));
                    } else if (boatType == 2) {
                        System.out.print("Enter Horse Power: ");
                        double horsePower = scanner.nextDouble();
                        storage.addBoatToOwner(ownerId,
                                new MotorBoat(height, length,
                                        width, boatValue, horsePower));
                    } else {
                        System.out.println("Invalid Boat Type!");
                    }
                    break;

                case 3:
                    System.out.print("Enter Owner ID: ");
                    int id = scanner.nextInt();
                    for (Owner owner : storage.getOwners()) {
                        if (owner.getIdNumber() == id) {
                            owner.displayCharges();
                            break;
                        }
                    }
                    break;

                case 4:
                    storage.displayStatistics();
                    break;

                case 5:
                    System.out.print("Sort by (1. Name, 2. Total Charge): ");
                    int sortChoice = scanner.nextInt();
                    storage.displayOwners(sortChoice == 1, sortChoice == 2);
                    break;

                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid Option!");
            }
        }
    }
}
