package distributed_computation.geovrgyan;

import javax.swing.*;
public class SliderConcurrentSimulator {
    private JSlider slider;
    private Thread thread1, thread2;
    private int thread1Priority, thread2Priority;

    public SliderConcurrentSimulator(JSlider s) {
        slider = s;
        thread1Priority = thread2Priority = Thread.NORM_PRIORITY;
    }

    public void increasePriority1() {
        if(thread1Priority < Thread.MAX_PRIORITY) {
            ++thread1Priority;
            System.out.println("Thread1 priority:" + thread1Priority);
            thread1.setPriority(thread1Priority);
        }
    }
    public void decreasePriority1() {
        if(thread1Priority > Thread.MIN_PRIORITY) {
            --thread1Priority;
            System.out.println("Thread1 priority:" + thread1Priority);
            thread1.setPriority(thread1Priority);
        }
    }
    public void increasePriority2() {
        if(thread2Priority < Thread.MAX_PRIORITY) {
            ++thread2Priority;
            System.out.println("Thread2 priority:" + thread2Priority);
            thread2.setPriority(thread2Priority);
        }
    }
    public void decreasePriority2() {
        if(thread2Priority > Thread.MIN_PRIORITY) {
            --thread2Priority;
            System.out.println("Thread2 priority:" + thread2Priority);
            thread2.setPriority(thread2Priority);
        }
    }

    public void start() {
        thread1 = new Thread(() -> {
            while(true) {
                synchronized (slider) {
                    if(slider.getValue() > slider.getMinimum())
                        slider.setValue(slider.getValue() - 1);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //Thread.yield();
            }
        });
        thread2 = new Thread(() -> {
            while(true) {
                synchronized (slider) {
                    if(slider.getValue() < slider.getMaximum())
                        slider.setValue(slider.getValue() + 1);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //Thread.yield();
            }
        });
        thread1.setPriority(thread1Priority);
        thread2.setPriority(thread2Priority);
        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.start();
        thread2.start();
    }
}
