package org.example;

import java.io.File;
import org.example.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Configuration config = null;

        // Main program loop
        while (true) {
            try {
                // Show main menu
                System.out.println("\nTicket System Main Menu");
                System.out.println("1. Set up configuration");
                System.out.println("2. Start system");
                System.out.println("3. Exit system");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();

                // Exit if user chooses 3
                if (choice == 3) {
                    System.out.println("Exit system...");
                    break;
                }

                // Handle menu options
                switch (choice) {
                    case 1:
                        // Configuration menu
                        while (true) {
                            try {
                                System.out.println("\nConfiguration Menu");
                                System.out.println("1. Load configuration from file");
                                System.out.println("2. Enter manual configuration");
                                System.out.print("Enter your choice: ");

                                int configChoice = sc.nextInt();

                                if (configChoice == 1) {
                                    // Try to load from file
                                    File file = new File("config.json");
                                    if (!file.exists()) {
                                        System.out.println("Error: config.json file not found!");
                                        continue;
                                    }

                                    config = Configuration.loadFromJson("config.json");
                                    if (config != null) {
                                        System.out.println("Configuration loaded successfully!");
                                        break;
                                    } else {
                                        System.out.println("Error: Failed to load configuration!");
                                        continue;
                                    }

                                } else if (configChoice == 2) {
                                    // Manual configuration
                                    int totalTickets = 0, releaseRate = 0, retrieveRate = 0, maxCapacity = 0;

                                    // Get total tickets with error checking
                                    while (true) {
                                        try {
                                            System.out.print("Enter total number of tickets (positive number): ");
                                            totalTickets = sc.nextInt();
                                            if (totalTickets <= 0) {
                                                System.out.println("Error: Total tickets must be positive!");
                                                continue;
                                            }
                                            break;
                                        } catch (Exception e) {
                                            System.out.println("Error: Please enter a valid number!");
                                            sc.nextLine(); // Clear the invalid input
                                        }
                                    }

                                    // Get release rate with error checking
                                    while (true) {
                                        try {
                                            System.out.print("Enter ticket release rate (positive number): ");
                                            releaseRate = sc.nextInt();
                                            if (releaseRate <= 0) {
                                                System.out.println("Error: Release rate must be positive!");
                                                continue;
                                            }
                                            break;
                                        } catch (Exception e) {
                                            System.out.println("Error: Please enter a valid number!");
                                            sc.nextLine(); // Clear the invalid input
                                        }
                                    }

                                    // Get retrieval rate with error checking
                                    while (true) {
                                        try {
                                            System.out.print("Enter time between ticket releases in milliseconds (positive number): ");
                                            retrieveRate = sc.nextInt();
                                            if (retrieveRate <= 0) {
                                                System.out.println("Error: Time between releases must be positive!");
                                                continue;
                                            }
                                            break;
                                        } catch (Exception e) {
                                            System.out.println("Error: Please enter a valid number!");
                                            sc.nextLine(); // Clear the invalid input
                                        }
                                    }

                                    // Get max capacity with error checking
                                    while (true) {
                                        try {
                                            System.out.print("Enter maximum ticket capacity (positive number): ");
                                            maxCapacity = sc.nextInt();
                                            if (maxCapacity <= 0) {
                                                System.out.println("Error: Maximum capacity must be positive!");
                                                continue;
                                            }
                                            break;
                                        } catch (Exception e) {
                                            System.out.println("Error: Please enter a valid number!");
                                            sc.nextLine(); // Clear the invalid input
                                        }
                                    }

                                    // Create and save configuration
                                    config = new Configuration(totalTickets, releaseRate, retrieveRate, maxCapacity);
                                    config.saveToJson("config.json");
                                    config.saveToText("config.txt");
                                    System.out.println("Configuration saved successfully!");
                                    break;

                                } else {
                                    System.out.println("Error: Please enter 1 or 2!");
                                }

                            } catch (Exception e) {
                                System.out.println("Error: Invalid input! Please try again.");
                                sc.nextLine(); // Clear the invalid input
                            }
                        }
                        break;

                    case 2:
                        // Check if configuration exists
                        if (config == null) {
                            System.out.println("Error: Please set up configuration first!");
                            continue;
                        }

                        try {
                            // Create ticket pool
                            TicketPool ticketPool = new TicketPool(config.getTotalTickets(), config.getMaxCapacity());
                            CountDownLatch latch = new CountDownLatch(1);

                            // Create vendor threads
                            System.out.println("\nStarting vendor threads...");
                            Thread vendor1 = new Thread(new Vendor(ticketPool, config.getReleaseRate(), latch));
                            Thread vendor2 = new Thread(new Vendor(ticketPool, config.getReleaseRate(), latch));
                            vendor1.start();
                            vendor2.start();

                            // Create customer threads
                            System.out.println("Starting customer threads...");
                            List<Thread> customerThreads = new ArrayList<>();
                            for (int i = 0; i < 5; i++) {
                                Thread customer = new Thread(new Customer(ticketPool, config.getRetrieveRate(), i + 1));
                                customerThreads.add(customer);
                                customer.start();
                            }

                            System.out.println("\nSystem is running! Press Enter to stop...");
                            sc.nextLine(); // Consume leftover newline
                            sc.nextLine(); // Wait for Enter key

                            // Stop all threads
                            System.out.println("\nStopping all threads...");
                            vendor1.interrupt();
                            vendor2.interrupt();
                            for (Thread customer : customerThreads) {
                                customer.interrupt();
                            }

                            // Wait for all threads to finish
                            try {
                                vendor1.join();
                                vendor2.join();
                                for (Thread customer : customerThreads) {
                                    customer.join();
                                }
                            } catch (InterruptedException e) {
                                System.out.println("Error while waiting for threads to finish: " + e.getMessage());
                            }

                            System.out.println("System stopped successfully!");
                            System.out.println("\nReturning to main menu...\n");

                        } catch (Exception e) {
                            System.out.println("Error running the system: " + e.getMessage());
                        }
                        break;

                    default:
                        System.out.println("Error: Please enter a number between 1 and 3!");
                        break;
                }

            } catch (Exception e) {
                System.out.println("Error: Invalid input! Please try again.");
                sc.nextLine(); // Clear the invalid input
            }
        }

        sc.close();
    }
}