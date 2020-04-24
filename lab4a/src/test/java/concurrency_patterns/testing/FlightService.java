package concurrency_patterns.testing;

import concurrency_patterns.solutions.MyReadWriteLock;

import java.util.Date;
import java.util.Map;

public class FlightService {
    private Map<String, Integer> schedules;
    public FlightService(MyReadWriteLock lock){
        fillMapWithInitialSchedules();
    }
    public void updateFlight(String flight, Integer value){
        schedules.put(flight, value);
    }
    public Integer getFlightInfo(String flight){
        return schedules.get(flight);
    }
    public void fillMapWithInitialSchedules() {
        this.schedules.put("Flight-1", 1);
        this.schedules.put("Flight-2", 4);
        this.schedules.put("Flight-3", 6);
        this.schedules.put("Flight-4", 9);
        this.schedules.put("Flight-5", 10);
        this.schedules.put("Flight-6", 13);
        this.schedules.put("Flight-7", 15);
        this.schedules.put("Flight-8", 17);
        this.schedules.put("Flight-9", 20);
        this.schedules.put("Flight-10", 23);
    }
    private static class MyWriter implements Runnable{
        private MyReadWriteLock safeLock;
        private String flight;
        private FlightService service;
        private static int j = 100;

        public MyWriter(MyReadWriteLock safeLock, String flight, FlightService service) {
            this.safeLock = safeLock;
            this.flight = flight;
        }

        @Override
        public void run() {
            safeLock.lockWriter();
            service.updateFlight(flight, j++);
            safeLock.unlockWriter();
        }
    }
    private static class MyReader implements Runnable {
        private MyReadWriteLock safeLock;
        private String flight;

        public MyReader(MyReadWriteLock safeLock, String flight) {
            this.safeLock = safeLock;
            this.flight = flight;
        }

        @Override
        public void run() {
            safeLock.lockReader();
            safeLock.unlockReader();
        }
    }
}
