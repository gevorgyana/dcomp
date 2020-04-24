
import java.util.Random;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class AddRemoveVertex implements Runnable {

    @Override
    public void run() {
        Random random = new Random();
        while (!Thread.currentThread().isInterrupted()) {
            Main.rwl.writeLock().lock();
            System.out.println("Deleting a city");
            Vertex toDelete = Main.cities.get(random.nextInt(Main.cities.size()));
            Main.availableCities.add(toDelete);
            Main.cities.remove(toDelete);
            for (int i = 0; i < Main.cities.size(); i++)
                Main.cities.get(i).deleteEdge(toDelete);
            System.out.println("Deleting finished");
            Main.rwl.writeLock().unlock();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
                e.printStackTrace();
            }

            Main.rwl.writeLock().lock();
            System.out.println("Adding new city");
            Vertex toAdd = Main.availableCities.get(random.nextInt(Main.availableCities.size()));
            Main.cities.add(toAdd);
            Main.availableCities.remove(toAdd);
            System.out.println("Adding finished");
            Main.rwl.writeLock().unlock();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

}
