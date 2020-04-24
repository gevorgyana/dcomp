
import java.util.ArrayList;
import java.util.List;

public class Vertex {
    public int id;
    public String name;
    public List<Vertex> adjacencyList = new ArrayList<>();
    public List<Integer> weights = new ArrayList<>();

    Vertex(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addEdge(Vertex v, int weight) {
        adjacencyList.add(v);
        weights.add(weight);

        v.adjacencyList.add(this);
        v.weights.add(weight);
    }

    public void deleteEdge(Vertex v) {
        if (adjacencyList.contains(v)) {
            weights.remove(adjacencyList.indexOf(v));
            adjacencyList.remove(v);

            v.weights.remove(v.adjacencyList.indexOf(this));
            v.adjacencyList.remove(this);
        }
    }

    public void changeWeight(Vertex v, int weight) {
        if (adjacencyList.contains(v)) {
            weights.set(adjacencyList.indexOf(v), weight);
            v.weights.set(v.adjacencyList.indexOf(this), weight);
        }

    }

    public void findPath(Vertex destination) {
        List<Vertex> path = new ArrayList<>();
        int price = findPath(destination, path, 0);
        if (price == -1) {
            System.out.println("Path not found");
            return;
        }
        System.out.println("Found path from city " + this.name + " to city " + destination.name);
        System.out.println("Path fare: " + price);
        if (path.size() > 1) {
            System.out.println("Required changes: ");
            for (int i = 1; i < path.size(); i++) {
                System.out.println(path.get(i).name);
            }
        }
    }

    private int findPath(Vertex destination, List<Vertex> route, int price) {
        for (int i = 0; i < adjacencyList.size(); i++) {
            if (adjacencyList.get(i) == destination) {
                return price + weights.get(i);
            }
        }
        for (int i = 0; i < adjacencyList.size(); i++) {
            boolean used = false;
            for (int j = 0; j < route.size(); j++) {
                if (route.get(j) == adjacencyList.get(i)) used = true;
            }
            if (used) continue;
            route.add(this);
            int result = adjacencyList.get(i).findPath(destination, route, price + weights.get(i));
            if (result != -1) return result;
            route.remove(this);
        }
        return -1;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Title: ").append(name).append('\n');
        if (adjacencyList.size() == 0) {
            sb.append("No connections with other cities\n\n");
            return sb.toString();
        }
        sb.append("Connections with other cities: \n-------------------\n");
        for (int i = 0; i < adjacencyList.size(); i++) {
            sb.append(adjacencyList.get(i).name).append(": ").append(weights.get(i)).append('\n');
        }
        sb.append("-------------------\n");

        return sb.toString();
    }
}
