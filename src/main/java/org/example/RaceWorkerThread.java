package org.example;
import javax.swing.*;

class RaceWorkerThread extends Thread {
    private JSlider slider;
    private int value;

    RaceWorkerThread() {
        super();
    }

    RaceWorkerThread(int value) {
        super();
        this.value = value;
    }

    RaceWorkerThread(int value, JSlider slider) {
        super();
        this.value = value;
        this.slider = slider;
    }

    public void setSlider(JSlider slider) {
        this.slider = slider;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            int currentValue = slider.getValue();

            if (currentValue == value) {
                continue;
            }

            if (currentValue > value) {
                slider.setValue(currentValue - 1);
            } else {
                slider.setValue(currentValue + 1);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
