package ds.geovrgyan.lab1;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CritSectionSimulator {
    private AtomicInteger simpleSem = new AtomicInteger(0);
    private Thread thread1, thread2;
    private JSlider slider;

    public CritSectionSimulator(JSlider slider){
        thread1 = new Thread();
        thread2 = new Thread();
        this.slider = slider;
    }

    public void runThread1(){
        if(simpleSem.compareAndSet(0,1)){
            thread1 = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    slider.setValue(slider.getValue() - 1);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        break;
                    }
                }
                simpleSem.set(0);
            });
            thread1.setPriority(Thread.MIN_PRIORITY);
            thread1.start();
        }
        else{
            JOptionPane.showMessageDialog(null, "Critical section is occupied");
        }
    }
    public void stopThread1(){
        if(thread1.isAlive())
            thread1.interrupt();
    }

    public void runThread2(){
        if(simpleSem.compareAndSet(0,1)){
            thread2 = new Thread(() -> {
                while(!Thread.currentThread().isInterrupted()) {
                    slider.setValue(slider.getValue() + 1);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        break;
                    }
                }
                simpleSem.set(0);
            });
            thread2.setPriority(Thread.MAX_PRIORITY);
            thread2.start();
        }
        else {
            JOptionPane.showMessageDialog(null, "Critical section is occupied");
        }

    }
    public void stopThread2(){
        if(thread2.isAlive()){
            thread2.interrupt();
        }
    }
}
