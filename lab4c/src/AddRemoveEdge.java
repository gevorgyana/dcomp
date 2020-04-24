
import java.util.Random;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class AddRemoveEdge implements Runnable {
    @Override
    public void run() {
        Random random = new Random();
        int src;
        int dest;

        while (!Thread.currentThread().isInterrupted()) {
            Main.rwl.writeLock().lock();
            System.out.println("Started adding a new edge");
            src = random.nextInt(Main.cities.size());
            do {
                dest = random.nextInt(Main.cities.size());
            } while (dest == src);

            Main.cities.get(src).addEdge(Main.cities.get(dest), random.nextInt(50));
            System.out.println("Finished eding new edge");
            Main.rwl.writeLock().unlock();
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                currentThread().interrupt();
                ex.printStackTrace();
            }

            Main.rwl.writeLock().lock();
            System.out.println("Started deleting existing edge");
            src = random.nextInt(Main.cities.size());
            do {
                dest = random.nextInt(Main.cities.size());
            } while (dest == src);

            Main.cities.get(src).deleteEdge(Main.cities.get(dest));
            System.out.println("Finished deleting existing edge");
            Main.rwl.writeLock().unlock();
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                currentThread().interrupt();
                ex.printStackTrace();
            }
        }
    }
}
