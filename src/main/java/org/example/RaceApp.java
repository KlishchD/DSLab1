package org.example;

import javax.swing.*;

public class RaceApp implements App {
    private final RaceWorkerThread thread1;
    private final RaceWorkerThread thread2;

    public RaceApp(int value1, int value2) {
        thread1 = new RaceWorkerThread(value1);
        thread2 = new RaceWorkerThread(value2);
    }

    @Override
    public void start() {
        createAppWindow();
    }

    private void run() {
        if (!thread1.isAlive()) {
            thread1.start();
        }
        if (!thread2.isAlive()) {
            thread2.start();
        }
    }

    private JPanel createSliderPanel() {
        JPanel sliderPanel = new JPanel();
        JSlider slider = new JSlider(0, 100, 50);

        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);

        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(10);

        sliderPanel.add(slider);

        thread1.setSlider(slider);
        thread2.setSlider(slider);

        return sliderPanel;
    }

    private JPanel createPrioritiesPanel() {
        JPanel threadsPriorityPanel = new JPanel();

        threadsPriorityPanel.setLayout(new BoxLayout(threadsPriorityPanel, BoxLayout.X_AXIS));

        JSpinner prioritySpinner1 = new JSpinner(new SpinnerNumberModel(1, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1));
        prioritySpinner1.addChangeListener(e -> thread1.setPriority((int) prioritySpinner1.getValue()));

        JSpinner prioritySpinner2 = new JSpinner(new SpinnerNumberModel(1, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1));
        prioritySpinner2.addChangeListener(e -> thread2.setPriority((int) prioritySpinner2.getValue()));

        threadsPriorityPanel.add(prioritySpinner1);
        threadsPriorityPanel.add(prioritySpinner2);

        return threadsPriorityPanel;
    }

    private JPanel createControlsPanel() {
        JPanel controlsPanel = new JPanel();

        JButton button = new JButton();
        button.setText("Start");

        button.addActionListener(e -> run());

        controlsPanel.add(button);

        return controlsPanel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createSliderPanel());
        panel.add(createPrioritiesPanel());
        panel.add(createControlsPanel());

        return panel;
    }

    private void createAppWindow() {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.add(createMainPanel());

        frame.pack();
    }
}
