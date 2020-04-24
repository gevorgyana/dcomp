
import java.util.Random;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class PathSearch implements Runnable {
    @Override
    public void run() {
        Random random = new Random();
        int src;
        int dst;

        while (!currentThread().isInterrupted()) {
            Main.rwl.readLock().lock();
            System.out.println("Path search started...");
            src = random.nextInt(Main.cities.size());
            do{
                dst = random.nextInt(Main.cities.size());
            } while(src == dst);
            Main.cities.get(src).findPath(Main.cities.get(dst));
            System.out.println("Path search finished\n");
            Main.rwl.readLock().unlock();
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                currentThread().interrupt();
                ex.printStackTrace();
            }
        }
    }
}
