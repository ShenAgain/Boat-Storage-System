package main;

import java.io.*;
import java.util.*;

/**
 * Handles file operations for the Boat Storage System.
 * Provides functionality to save and load boat storage data.
 */
public class FileManager {
    private static final String FILE_HEADER = "BoatStorage_v1.0";
    
    /**
     * Saves the boat storage data to a file.
     * @param storage The BoatStorage object to save
     * @param file The file to save to
     * @throws IOException If there's an error writing to the file
     * @throws IllegalArgumentException If the objects are not serializable
     */
    public static void saveToFile(BoatStorage storage, File file) throws IOException {
        // Check if Owner class implements Serializable
        List<Owner> owners = storage.getOwners();
        if (!owners.isEmpty() && !(owners.get(0) instanceof Serializable)) {
            throw new IllegalArgumentException(
                "Owner class must implement java.io.Serializable");
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file))) {
            // Write file header
            oos.writeUTF(FILE_HEADER);
            
            // Write owners list
            oos.writeInt(owners.size());
            for (Owner owner : owners) {
                oos.writeObject(owner);
            }
        }
    }
    
    /**
     * Loads boat storage data from a file.
     * @param file The file to load from
     * @return A new BoatStorage object with the loaded data
     * @throws IOException If there's an error reading from the file
     * @throws ClassNotFoundException If there's an error deserializing objects
     * @throws IllegalArgumentException If the file format is invalid
     */
    public static BoatStorage loadFromFile(File file) throws IOException, 
            ClassNotFoundException {
        BoatStorage storage = new BoatStorage();
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            // Verify file header
            String header = ois.readUTF();
            if (!FILE_HEADER.equals(header)) {
                throw new IllegalArgumentException("Invalid file format");
            }
            
            // Read owners
            int ownerCount = ois.readInt();
            for (int i = 0; i < ownerCount; i++) {
                Owner owner = (Owner) ois.readObject();
                storage.addOwner(owner);
            }
        }
        
        return storage;
    }
} 