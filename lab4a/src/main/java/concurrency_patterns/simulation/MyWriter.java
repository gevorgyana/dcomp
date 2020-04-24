package concurrency_patterns.simulation;

import concurrency_patterns.solutions.MyReadWriteLock;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyWriter implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(MyWriter.class.getName());

    private MyReadWriteLock safeLock;
    private String name;
    private long writingTime;

    public MyWriter(String name, MyReadWriteLock lock) {
        this(name, lock, 250L);
    }

    public MyWriter(String name, MyReadWriteLock lock, long writingTime) {
        this.name = name;
        this.safeLock = lock;
        this.writingTime = writingTime;
    }
    @Override
    public void run() {
        safeLock.lockWriter();
        try {
            write();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "InterruptedException when writing", e);
            Thread.currentThread().interrupt();
        } finally {
            safeLock.unlockWriter();
        }
    }

    public void write() throws InterruptedException {
        LOGGER.log(Level.INFO, String.format("%s begin", name));
        Thread.sleep(writingTime);
        LOGGER.log(Level.INFO, String.format("%s finished after writing %dms", name, writingTime));
    }
}