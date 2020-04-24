package distributed_computation.geovrgyan;

import javax.swing.*;

public class SliderTestForm extends JFrame {
    private JPanel mainPanel;
    private JButton increasePriorityButton1;
    private JButton decreasePriorityButton1;
    private JButton increasePriorityButton2;
    private JButton decreasePriorityButton2;
    private JSlider slider;

    public SliderTestForm() {
        super("LabSP1.1");
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(500, 300));
        setResizable(false);
        setVisible(true);
        pack();

        SliderConcurrentSimulator simulator = new SliderConcurrentSimulator(slider);
        simulator.start();
        increasePriorityButton1.addActionListener(e -> simulator.increasePriority1());
        decreasePriorityButton1.addActionListener(e -> simulator.decreasePriority1());
        increasePriorityButton2.addActionListener(e -> simulator.increasePriority2());
        decreasePriorityButton2.addActionListener(e -> simulator.decreasePriority2());
    }
}
