
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    static ArrayList<Vertex> cities = new ArrayList<>();
    static ArrayList<Vertex> availableCities = new ArrayList<>();

    static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);

    public static void main(String[] args) throws InterruptedException {
        String[] city_names = {"Київ", "Львів", "Чернігів", "Житомир", "Івано-Франківськ", "Херсон", "Полтава"
                , "Черкаси", "Чернівці", "Тернопіль", "Запоріжжя", "Вінниця", "Миколаїв", "Дніпро", "Луцьк"
                , "Ужгород", "Суми", "Кропивницький", "Донецьк", "Луганськ", "Одеса", "Рівне", "Харків"
                , "Хмельницький", "Севастополь", "Сімферополь"};
        String[] available_cities = {};
        for(int i = 0; i < 13; ++i)
            cities.add(new Vertex(i, city_names[i]));
        for(int i = 13; i < 26; ++i)
            availableCities.add(new Vertex(i, city_names[i]));

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int origin = random.nextInt(cities.size());
            int destination = random.nextInt(cities.size());
            while (destination == origin)
                destination = random.nextInt(cities.size());
            cities.get(origin).addEdge(cities.get(destination), random.nextInt(50));
        }
        for (Vertex vertex : cities) {
            System.out.println(vertex);
        }

        Thread priceChanger = new Thread(new ChangeWeight());
        Thread connectionsChanger = new Thread(new AddRemoveEdge());
        Thread citiesChanger = new Thread(new AddRemoveVertex());
        Thread PathSearch1 = new Thread(new PathSearch());
        Thread PathSearch2 = new Thread(new PathSearch());
        Thread PathSearch3 = new Thread(new PathSearch());

        priceChanger.start();
        connectionsChanger.start();
        citiesChanger.start();
        PathSearch1.start();
        PathSearch2.start();
        PathSearch3.start();

        Thread.currentThread().sleep(10000);
        priceChanger.interrupt();
        connectionsChanger.interrupt();
        citiesChanger.interrupt();
        PathSearch1.interrupt();
        PathSearch2.interrupt();
        PathSearch3.interrupt();
    }
}
