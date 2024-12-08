package org.example;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;

// This class stores all the configuration settings for our ticket system
public class Configuration {
    // These variables store our configuration values
    private int totalTickets;      // Total number of tickets available
    private int releaseRate;       // How many tickets each vendor releases
    private int retrieveRate;      // How many tickets each customer can buy
    private int maxCapacity;       // Maximum tickets the system can hold

    // Constructor to create a new configuration
    public Configuration(int totalTickets, int releaseRate, int retrieveRate, int maxCapacity) {
        this.totalTickets = totalTickets;
        this.releaseRate = releaseRate;
        this.retrieveRate = retrieveRate;
        this.maxCapacity = maxCapacity;
    }

    // Methods to get our configuration values
    public int getTotalTickets() {
        return totalTickets;
    }

    public int getReleaseRate() {
        return releaseRate;
    }

    public int getRetrieveRate() {
        return retrieveRate;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    // Save our configuration to a JSON file
    public void saveToJson(String filename) {
        try {
            // Create a file writer
            FileWriter writer = new FileWriter(filename);

            // Create Gson object to convert our configuration to JSON
            Gson gson = new Gson();

            // Convert this configuration to JSON and write it to the file
            gson.toJson(this, writer);

            // Close the writer
            writer.close();

            // Print success message
            System.out.println("Configuration saved to file: " + filename);

        } catch (IOException e) {
            // If something goes wrong, print the error
            System.out.println("Error saving configuration: " + e.getMessage());
        }
    }

    // Save our configuration to a simple text file
    public void saveToText(String filename) {
        try {
            // Create a file writer
            FileWriter writer = new FileWriter(filename);

            // Write each setting on a new line
            writer.write("Total Number of Tickets: " + totalTickets + "\n");
            writer.write("Ticket Release Rate (per vendor): " + releaseRate + " tickets\n");
            writer.write("Time Between Ticket Releases: " + retrieveRate + " milliseconds\n");
            writer.write("Maximum Ticket Capacity: " + maxCapacity + " tickets\n");

            // Close the writer
            writer.close();

            // Print success message
            System.out.println("Configuration saved to file: " + filename);

        } catch (IOException e) {
            // If something goes wrong, print the error
            System.out.println("Error saving configuration: " + e.getMessage());
        }
    }

    // Load configuration from a JSON file
    public static Configuration loadFromJson(String filename) {
        try {
            // Create a file reader
            FileReader reader = new FileReader(filename);

            // Create Gson object to read JSON
            Gson gson = new Gson();

            // Read the JSON and convert it back to a Configuration object
            Configuration config = gson.fromJson(reader, Configuration.class);

            // Close the reader
            reader.close();

            // Print success message
            System.out.println("Configuration loaded from file: " + filename);

            return config;

        } catch (IOException e) {
            // If something goes wrong, print the error
            System.out.println("Error loading configuration: " + e.getMessage());
            return null;
        }
    }
}
