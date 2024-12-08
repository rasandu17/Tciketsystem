package org.example;

public class Customer implements Runnable {
    private TicketPool ticketPool;
    private int retrieveRate;  // Time delay between ticket purchases
    private int customerId;    // Unique ID for each customer

    public Customer(TicketPool ticketPool, int retrieveRate, int customerId) {
        this.ticketPool = ticketPool;
        this.retrieveRate = retrieveRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (ticketPool.areTicketsExhausted()) {
                System.out.println("Customer " + customerId + ": No more tickets available.");
                break;
            }

            try {
                ticketPool.purchaseTickets(customerId, 1);  // Purchase one ticket at a time
                Thread.sleep(retrieveRate);  // Wait for the specified time between ticket releases
            } catch (InterruptedException e) {
                System.out.println("Customer " + customerId + " stopped.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Customer " + customerId + " has stopped buying tickets.");
        System.out.println("All tickets have been released.");
    }

}


