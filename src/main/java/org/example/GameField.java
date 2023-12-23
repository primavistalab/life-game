package org.example;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import static org.example.Constants.*;

@Slf4j
public class GameField extends JPanel {

    private final boolean[][] currentGeneration = new boolean[COLUMNS][ROWS];
    private final boolean[][] nextGenerationField = new boolean[COLUMNS][ROWS];

    private int paintCount = 0;
    private double totalPaintTime = 0;

    private int generationCount = 0;
    private double totalGenerationTime = 0;

    public GameField() {
        clear();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                final int col = me.getX() / CELL_SIZE;
                final int row = me.getY() / CELL_SIZE;
                if (0 <= col && col < COLUMNS && 0 <= row && row < ROWS) {
                    currentGeneration[col][row] = !currentGeneration[col][row];
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final var paintStarted = System.nanoTime();
        paintCount++;

        super.paintComponent(g);
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                drawCell(g, col, row, currentGeneration[col][row]);
            }
        }

        final long paintTime = System.nanoTime() - paintStarted;
        totalPaintTime += paintTime * 0.000001;
    }

    private void drawCell(final Graphics g, int col, int row, boolean isLife) {
        g.setColor(Color.BLACK);
        g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        if (isLife) g.setColor(Color.GREEN);
        else g.setColor(Color.WHITE);
        g.fillRect(col * CELL_SIZE + 1, row * CELL_SIZE + 1, CELL_SIZE - 1, CELL_SIZE - 1);
    }

    public void nextGeneration() {
        final var generationStarted = System.nanoTime();
        generationCount++;

        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                final var aliveCellAround = aliveCellsAroundThis(col, row);
                // в пустой (мёртвой) клетке, с которой соседствуют три живые клетки, зарождается жизнь;
                if (!currentGeneration[col][row] && aliveCellAround == 3) nextGenerationField[col][row] = true;
                    // если у живой клетки есть две или три живые соседки, то эта клетка продолжает жить;
                else if (currentGeneration[col][row] && 2 <= aliveCellAround && aliveCellAround <= 3) nextGenerationField[col][row] = true;
                    // в противном случае (если живых соседей меньше двух или больше трёх) клетка умирает («от одиночества» или «от перенаселённости»).
                else nextGenerationField[col][row] = false;
            }
        }

        for (int col = 0; col < COLUMNS; col++) {
            System.arraycopy(nextGenerationField[col], 0, currentGeneration[col], 0, ROWS);
        }

        final long generationTime = System.nanoTime() - generationStarted;
        totalGenerationTime += generationTime * 0.000001;
    }

    private int aliveCellsAroundThis(int col, int row) {
        int alive = 0;
        for (int x = col - 1; x <= col + 1; x++) {
            for (int y = row - 1; y <= row + 1; y++) {
                if (x == col && y == row) continue;
                if (x < 0 || y < 0 || COLUMNS < x + 1 || ROWS < y + 1) continue;
                if (currentGeneration[x][y]) alive++;
            }
        }
        return alive;
    }

    public void clear() {
        paintCount = 0;
        totalPaintTime = 0;
        generationCount = 0;
        totalGenerationTime = 0;

        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                currentGeneration[col][row] = false;
                nextGenerationField[col][row] = false;
            }
        }
    }

    public void generateRandomField() {
        final var random = new Random();
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS; row++) {
                final var r = random.nextBoolean();
                currentGeneration[col][row] = r;
                nextGenerationField[col][row] = r;
            }
        }
    }

    public double getMiddlePaintTimeInMs(){
        if (paintCount == 0) return 0.0;
        else return totalPaintTime / paintCount;
    }

    public double getMiddleGenerationTimeInMs() {
        if (generationCount == 0) return 0.0;
        else return totalGenerationTime / generationCount;
    }

    public int getGenerationCount() {
        return generationCount;
    }
}
