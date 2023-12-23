package org.example;

import javax.swing.*;
import java.awt.*;

import static org.example.Constants.*;

public class LifeWindow extends JFrame {
    private final GameField gameField = new GameField();
    private final Timer mainTimer;

    public LifeWindow() throws HeadlessException {
        setTitle("LIFE");
        setSize(WINDOW_WIDTH + 200, WINDOW_HEIGHT + 50);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(gameField);
        gameField.setLayout(null);

        JButton startStopButton = new JButton("Start/stop");
        startStopButton.setBounds(WINDOW_WIDTH + 10, 10, 90, 25);
        startStopButton.addActionListener(e -> {
            runOrStopTheGame();
        });

        JButton clearFieldButton = new JButton("Clear");
        clearFieldButton.setBounds(WINDOW_WIDTH + 10, 45, 90, 25);
        clearFieldButton.addActionListener(e -> {
            clearField();
        });

        JButton randomButton = new JButton("Random");
        randomButton.setBounds(WINDOW_WIDTH + 10, 80, 90, 25);
        randomButton.addActionListener(e -> {
            randomField();
        });

        gameField.add(startStopButton);
        gameField.add(clearFieldButton);
        gameField.add(randomButton);

        mainTimer = new Timer(GENERATION_DELAY, e -> {
            gameField.nextGeneration();
            setTitle("LIFE .::. Generation No." + gameField.getGenerationCount() +
                    " .::. Mid.Generation Time = " + String.format("%.2f", gameField.getMiddleGenerationTimeInMs()) + "ms." +
                    " .::. Mid.Paint Time = " + String.format("%.2f", gameField.getMiddlePaintTimeInMs()) + "ms.");
            gameField.repaint();
        });
    }

    public void runOrStopTheGame() {
        if (mainTimer.isRunning()){
            mainTimer.stop();
        }
        else {
            mainTimer.start();
        }
    }

    public void clearField() {
        gameField.clear();
        gameField.repaint();
    }

    public void randomField() {
        gameField.generateRandomField();
        gameField.repaint();
    }
}
