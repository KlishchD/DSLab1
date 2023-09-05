package org.example;
import javax.swing.*;

class ControlledAppUI {
    private JSlider slider;

    private JSpinner prioritySpinner1;
    private JSpinner prioritySpinner2;

    private JButton startButton1;
    private JButton startButton2;

    private JButton stopButton1;
    private JButton stopButton2;

    private JLabel semaphoreStateText;

    public JSpinner getPrioritySpinner1() {
        return prioritySpinner1;
    }

    public JSpinner getPrioritySpinner2() {
        return prioritySpinner2;
    }

    public JButton getStartButton1() {
        return startButton1;
    }

    public JButton getStartButton2() {
        return startButton2;
    }

    public JButton getStopButton1() {
        return stopButton1;
    }

    public JButton getStopButton2() {
        return stopButton2;
    }

    public JSlider getSlider() {
        return slider;
    }

    public void updateSemaphoreStateText(boolean isTaken) {
        if (isTaken) {
            semaphoreStateText.setText("Semaphore is not available");
        }
        else {
            semaphoreStateText.setText("Semaphore is available");
        }
    }

    public void create() {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.add(createMainPanel());

        frame.pack();
    }

    public void setFirstControlsEnabled(boolean enabled) {
        startButton1.setEnabled(enabled);
        prioritySpinner1.setEnabled(enabled);
        stopButton2.setEnabled(!enabled);
    }

    public void setSecondControlsEnabled(boolean enabled) {
        startButton2.setEnabled(enabled);
        prioritySpinner2.setEnabled(enabled);
        stopButton1.setEnabled(!enabled);
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

        prioritySpinner1 = new JSpinner(new SpinnerNumberModel(1, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1));

        prioritySpinner2 = new JSpinner(new SpinnerNumberModel(1, Thread.MIN_PRIORITY, Thread.MAX_PRIORITY, 1));

        threadsPriorityPanel.add(prioritySpinner1);
        threadsPriorityPanel.add(prioritySpinner2);

        return threadsPriorityPanel;
    }

    private JPanel createControlsPanel() {
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));

        JPanel startButtonsPanel = new JPanel();

        startButton1 = new JButton("Start1");
        startButton2 = new JButton("Start2");

        startButtonsPanel.add(startButton1);
        startButtonsPanel.add(startButton2);

        JPanel stopButtonsPanel = new JPanel();

        stopButton1 = new JButton("Stop1");
        stopButton1.setEnabled(false);

        stopButton2 = new JButton("Stop2");
        stopButton2.setEnabled(false);

        stopButtonsPanel.add(stopButton1);
        stopButtonsPanel.add(stopButton2);

        JPanel semaphoreStatePanel = new JPanel();

        semaphoreStateText = new JLabel();

        updateSemaphoreStateText(false);

        semaphoreStatePanel.add(semaphoreStateText);

        controlsPanel.add(startButtonsPanel);
        controlsPanel.add(stopButtonsPanel);
        controlsPanel.add(semaphoreStatePanel);

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
}

public class ControlledApp implements App {
    private final ControlledAppUI UI;

    private int value1;
    private RaceWorkerThread thread1;

    private int value2;
    private RaceWorkerThread thread2;

    private final int SEMAPHORE_IS_AVAILABLE = 0;
    private final int SEMAPHORE_IS_NOT_AVAILABLE = 1;

    private Integer Semaphore = SEMAPHORE_IS_AVAILABLE;

    public ControlledApp(int value1, int value2) {
        this.value1 = value1;
        this.value2 = value2;
        UI = new ControlledAppUI();
    }

    @Override
    public void start() {
        UI.create();

        UI.getPrioritySpinner1().addChangeListener(e -> {
            if (thread1 != null) {
                thread1.setPriority((int) UI.getPrioritySpinner1().getValue());
            }
        });
        UI.getPrioritySpinner2().addChangeListener(e -> {
            if (thread2 != null) {
                thread2.setPriority((int) UI.getPrioritySpinner2().getValue());
            }
        });

        UI.getStartButton1().addActionListener(e -> runFirstThread());
        UI.getStartButton2().addActionListener(e -> runSecondThread());

        UI.getStopButton1().addActionListener(e -> stopFirstThread());
        UI.getStopButton2().addActionListener(e -> stopSecondThread());
    }

    private void stopSecondThread() {
        if (Semaphore == SEMAPHORE_IS_NOT_AVAILABLE && thread2.isAlive()) {
            Semaphore = SEMAPHORE_IS_AVAILABLE;

            thread2.interrupt();
            thread2 = null;

            UI.setFirstControlsEnabled(true);
            UI.updateSemaphoreStateText(Semaphore == SEMAPHORE_IS_NOT_AVAILABLE);
        }
    }

    private void stopFirstThread() {
        if (Semaphore == SEMAPHORE_IS_NOT_AVAILABLE && thread1.isAlive()) {
            Semaphore = SEMAPHORE_IS_AVAILABLE;

            thread1.interrupt();
            thread1 = null;

            UI.setSecondControlsEnabled(true);
            UI.updateSemaphoreStateText(Semaphore == SEMAPHORE_IS_NOT_AVAILABLE);
        }
    }

    private void runSecondThread() {
        if (Semaphore == SEMAPHORE_IS_AVAILABLE) {
            Semaphore = SEMAPHORE_IS_NOT_AVAILABLE;

            thread2 = new RaceWorkerThread(value2, UI.getSlider());
            thread2.setPriority((int) UI.getPrioritySpinner2().getValue());
            thread2.start();

            UI.setFirstControlsEnabled(false);
            UI.updateSemaphoreStateText(Semaphore == SEMAPHORE_IS_NOT_AVAILABLE);
        }
    }

    private void runFirstThread() {
        if (Semaphore == SEMAPHORE_IS_AVAILABLE) {
            Semaphore = SEMAPHORE_IS_NOT_AVAILABLE;

            thread1 = new RaceWorkerThread(value1, UI.getSlider());
            thread1.setPriority((int) UI.getPrioritySpinner1().getValue());
            thread1.start();

            UI.setSecondControlsEnabled(false);
            UI.updateSemaphoreStateText(Semaphore == SEMAPHORE_IS_NOT_AVAILABLE);
        }
    }
}
