package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.Constants.*;

public class LifeWindow extends JFrame {
    private final GameField gameField = new GameField();
    private final Timer mainTimer;
    private final AtomicInteger generation = new AtomicInteger(0);
//    private AtomicBoolean gameStarted = new AtomicBoolean(false;)

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
        randomButton.setBounds(WINDOW_WIDTH + 10, 85, 90, 25);
        randomButton.addActionListener(e -> {
            randomField();
        });

        gameField.add(startStopButton);
        gameField.add(clearFieldButton);
        gameField.add(randomButton);

        mainTimer = new Timer(GENERATION_DELAY, e -> {
            gameField.nextGeneration();
            setTitle("LIFE .::. Generation: " + generation.incrementAndGet());
            gameField.repaint();
        });
    }

    public void runOrStopTheGame() {
        if (mainTimer.isRunning()){
            mainTimer.stop();
        }
        else {
            generation.set(0);
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
