package org.example;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Worker implements Runnable {
    private final JSlider slider;
    private final int value;
    Worker(int value, JSlider slider) {
        this.value = value;
        this.slider = slider;
    }

    @Override
    public void run() {
        while (true) {
            slider.setValue(value);
        }
    }
}


class App {
    private JFrame frame;
    private JSlider slider;
    private JSpinner prioritySpinner1;
    private JSpinner prioritySpinner2;

    private Thread thread1;
    private Thread thread2;

    public App() {
    }

    public void createWindow() {
        createAppWindow();
    }

    private void run() {
        if (thread1 != null) {
            thread1.interrupt();
        }

        thread1 = new Thread(new Worker(10, slider));

        if (thread2 != null) {
            thread2.interrupt();
        }

        thread2 = new Thread(new Worker(90, slider));

        thread1.start();
        thread2.start();

        thread1.setPriority((int)prioritySpinner1.getValue());
        thread2.setPriority((int)prioritySpinner2.getValue());

    }

    private JPanel createSliderPanel() {
        JPanel sliderPanel = new JPanel();
        slider = new JSlider(0, 100, 50);

        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);

        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(10);

        sliderPanel.add(slider);

        return sliderPanel;
    }

    private JPanel createPrioritiesPanel() {
        JPanel threadsPriorityPanel = new JPanel();

        threadsPriorityPanel.setLayout(new BoxLayout(threadsPriorityPanel, BoxLayout.X_AXIS));

        prioritySpinner1 = new JSpinner();
        prioritySpinner1.setValue(1);
        prioritySpinner1.addChangeListener(e -> {
            if (thread1 != null) {
                thread1.setPriority(((int) prioritySpinner1.getValue()));
            }
        });

        prioritySpinner2 = new JSpinner();
        prioritySpinner2.setValue(1);
        prioritySpinner2.addChangeListener(e -> {
            if (thread2 != null) {
                thread2.setPriority(((int) prioritySpinner2.getValue()));
            }
        });

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
        frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.add(createMainPanel());

        frame.pack();
    }
}

public class Main {
    public static void main(String[] args) {
        App window = new App();
        window.createWindow();
    }
}