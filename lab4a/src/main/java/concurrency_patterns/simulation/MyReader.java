package concurrency_patterns.simulation;

import com.sun.org.slf4j.internal.LoggerFactory;
import concurrency_patterns.solutions.MyReadWriteLock;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyReader implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(MyReader.class.getName());

    private MyReadWriteLock safeLock;
    private String name;
    private long readingTime;

    public MyReader(String name, MyReadWriteLock lock, long readingTime) {
        this.name = name;
        this.safeLock = lock;
        this.readingTime = readingTime;
    }

    public MyReader(String name, MyReadWriteLock lock) {
        this(name, lock, 250L);
    }

    @Override
    public void run() {
        safeLock.lockReader();
        try {
            read();
        } catch (InterruptedException e) {
            LOGGER.log(Level.INFO, "InterruptedException when reading", e);
            Thread.currentThread().interrupt();
        } finally {
            safeLock.unlockReader();
        }
    }

    public void read() throws InterruptedException {
        LOGGER.log(Level.INFO, String.format("%s begin", name));
        Thread.sleep(readingTime);
        LOGGER.log(Level.INFO, String.format("%s finish after reading %dms", name, readingTime));
    }
}
