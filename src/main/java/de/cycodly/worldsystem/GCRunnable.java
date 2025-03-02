package de.cycodly.worldsystem;

public class GCRunnable implements Runnable {

    @Override
    public void run() {
        new Thread(() -> System.gc()).start();
    }
}
