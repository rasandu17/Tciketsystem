package org.example;

public class TicketPool {
    private int totalTickets;
    private int currentTickets = 0;  // Tracks the current number of tickets available in the pool
    private int maxCapacity;

    public TicketPool(int totalTickets, int maxCapacity) {
        this.totalTickets = totalTickets;
        this.maxCapacity = maxCapacity;
    }

    // Vendor adds tickets based on the release rate
    public synchronized void releaseTickets(int tickets) {
        // Ensure we don't release more tickets than remaining or exceed max capacity
        int ticketsToRelease = Math.min(tickets, totalTickets);
        ticketsToRelease = Math.min(ticketsToRelease, maxCapacity - currentTickets);

        if (ticketsToRelease > 0) {
            currentTickets += ticketsToRelease;
            totalTickets -= ticketsToRelease;
            System.out.println(ticketsToRelease + " tickets added to the pool. Current size: " + currentTickets);
            notifyAll();  // Notify all customers waiting for tickets
        }

        if (totalTickets == 0) {
            System.out.println("All tickets are released. Stopping the system.");
            notifyAll();  // Wake up any waiting threads
        }
    }

    // Customer attempting to buy tickets
    public synchronized void purchaseTickets(int customerId, int ticketsToPurchase) {
        // Wait while not enough tickets are available
        while (currentTickets < ticketsToPurchase && totalTickets > 0) {
            try {
                System.out.println("Customer " + customerId + " is waiting for ticket release...");
                wait();  // Wait until tickets are released
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Try to purchase a single ticket
        if (currentTickets > 0) {
            currentTickets--;
            System.out.println("Customer " + customerId + " purchased a ticket. Remaining tickets in pool: " + currentTickets);
        }

        // If no more tickets available, notify
        if (totalTickets == 0 && currentTickets == 0) {
            System.out.println("All tickets have been sold out!");
            notifyAll();
        }
    }

    // Check if tickets are exhausted
    public synchronized boolean areTicketsExhausted() {
        return totalTickets == 0 && currentTickets == 0;
    }
}