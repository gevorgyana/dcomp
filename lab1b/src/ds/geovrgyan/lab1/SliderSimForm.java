package ds.geovrgyan.lab1;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SliderSimForm extends JFrame{
    private JSlider slider;
    private JPanel panel;
    private JButton start1Button;
    private JButton start2Button;
    private JButton stop1Button;
    private JButton stop2Button;

    public SliderSimForm(){
        super("LabSP1.2");
        setContentPane(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(400, 400));
        setResizable(false);
        setVisible(true);
        pack();

        CritSectionSimulator simulator = new CritSectionSimulator(slider);
        start1Button.addActionListener(e -> simulator.runThread1());
        stop1Button.addActionListener(e -> simulator.stopThread1());
        start2Button.addActionListener(e -> simulator.runThread2());
        stop2Button.addActionListener(e -> simulator.stopThread2());
    }
}
