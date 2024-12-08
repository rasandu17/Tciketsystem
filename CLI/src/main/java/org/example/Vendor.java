package org.example;

import java.util.concurrent.CountDownLatch;

public class Vendor implements Runnable {
    private TicketPool ticketPool;
    private int releaseRate;
    private CountDownLatch latch;

    public Vendor(TicketPool ticketPool, int releaseRate, CountDownLatch latch) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.latch = latch;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (ticketPool.areTicketsExhausted()) {
                latch.countDown();  // Signal that tickets are exhausted
                break;  // Stop the vendor if all tickets are released
            }

            try {
                ticketPool.releaseTickets(releaseRate);  // Add tickets based on the release rate
                Thread.sleep(2000);  // Vendor releases tickets every 2 seconds
            } catch (InterruptedException e) {
                System.out.println("Vendor interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Vendor has stopped due to all tickets being released.");
    }
}


