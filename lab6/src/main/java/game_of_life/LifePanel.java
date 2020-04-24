package game_of_life;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.swing.*;

public class LifePanel extends JPanel {
    private class MyMouseAdapter extends MouseAdapter{
        private boolean pressedLeft = false;
        private boolean pressedRight = false;
        private boolean pressedWheel = false;

        @Override
        public void mouseDragged(MouseEvent e) {
            setCell(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                pressedLeft = true;
                pressedRight = false;
                pressedWheel = false;
                setCell(e);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                pressedLeft = false;
                pressedRight = true;
                pressedWheel = false;
                setCell(e);
            } else if (e.getButton() == MouseEvent.BUTTON2) {
                pressedLeft = false;
                pressedRight = false;
                pressedWheel = true;
                setCell(e);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                pressedLeft = false;
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                pressedRight = false;
            } else if (e.getButton() == MouseEvent.BUTTON2) {
                pressedWheel = false;
            }
        }

        private void setCell(MouseEvent e) {
            if (gameModel != null) {
                locker.writeLock().lock();
                int row = e.getY() / (cellSize + cellGap);
                int col = e.getX() / (cellSize + cellGap);
                if (0 <= row && row < gameModel.getHeight() && 0 <= col && col < gameModel.getWidth()) {
                    if (pressedLeft) {
                        gameModel.setCell(row, col, (byte) 1);
                        repaint();
                    }
                    if (pressedRight) {
                        gameModel.setCell(row, col, (byte) 2);
                        repaint();
                    }
                    if (pressedWheel) {
                        gameModel.setCell(row, col, (byte) 0);
                        repaint();
                    }
                }
                locker.writeLock().unlock();
            }
        }
    }
    private int updateDelay = 1000;
    private ReentrantReadWriteLock locker = null;
    private Checker checker = null;
    private Thread[] workers = null;
    private LifeModel gameModel = null;
    private int cellSize = 8;
    private int cellGap = 1;
    private final int workersAmount = 2;
    private static final Color c0 = new Color(0xFFFFFF);
    private static final Color c1 = new Color(0x1918FF);
    private static final Color c2 = new Color(0xFF2F0F);

    LifePanel() {
        setBackground(Color.BLACK);
        MouseAdapter ma = new MyMouseAdapter();
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    LifeModel getLifeModel() {
        return gameModel;
    }

    void initialize(int width, int height, int newCellSize) {
        cellSize = newCellSize;
        locker = new ReentrantReadWriteLock();
        gameModel = new LifeModel(width, height);
    }

    void setUpdateDelay(int newUpdateDelay) {
        updateDelay = 10 + newUpdateDelay * 50;
        if (checker != null) {
            synchronized (checker) {
                checker.timeSleep = updateDelay;
            }
        }
    }

    void startSimulation() {
        if (workers == null) {
            checker = new Checker(this, gameModel, locker);
            checker.timeSleep = updateDelay;
            CyclicBarrier barrier = new CyclicBarrier(workersAmount, checker);

            workers = new Worker[workersAmount];
            for(int i = 0; i < workersAmount; ++i)
                workers[i] = new Worker(gameModel, barrier, locker, (byte)(i+1));

            for (int i = 0; i < 2; i++)
                workers[i].start();
        }
    }

    void stopSimulation(JButton button) {
        button.setEnabled(false);
        if (workers != null){
            for (int i = 0; i < workersAmount; i++) {
                workers[i].interrupt();
            }
        }
        workers = null;
        button.setEnabled(true);
    }

    @Override
    public Dimension getPreferredSize() {
        if (gameModel != null) {
            Insets b = getInsets();
            return new Dimension(
                    (cellSize + cellGap) * gameModel.getWidth() + cellGap + b.left + b.right,
                    (cellSize + cellGap) * gameModel.getHeight() + cellGap + b.top + b.bottom);
        } else
            return new Dimension(100, 100);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (gameModel != null) {
            locker.readLock().lock();
            super.paintComponent(g);
            Insets b = getInsets();
            for (int x = 0; x < gameModel.getHeight(); x++) {
                for (int y = 0; y < gameModel.getWidth(); y++) {
                    byte c = gameModel.getCell(x, y);
                    Color tmp = c0;
                    if (c == 1) tmp = c1;
                    if (c == 2) tmp = c2;
                    g.setColor(tmp);
                    g.fillRect(
                            b.left + cellGap + y * (cellSize + cellGap),
                            b.top + cellGap + x * (cellSize + cellGap),
                            cellSize,
                            cellSize);
                }
            }
            locker.readLock().unlock();
        }
    }

    void download(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        ArrayList<Integer> points = new ArrayList<>();

        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                points.add(scanner.nextInt());
                points.add(scanner.nextInt());
                points.add(scanner.nextInt());
            }
            scanner.close();
        }

        locker.writeLock().lock();
        gameModel.clear();

        for (int i = 0; i < points.size(); i += 3) {
            gameModel.setCell(
                    points.get(i), points.get(i + 1),
                    Byte.parseByte(Integer.toString(points.get(i + 2)))
            );
        }
        locker.writeLock().unlock();
        repaint();
    }

    void save(String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        StringBuilder result = new StringBuilder();
        locker.readLock().lock();
        for (int i = 0; i < gameModel.getHeight(); i++)
            for (int j = 0; j < gameModel.getWidth(); j++) {
                byte type = gameModel.getCell(i, j);
                if (type != 0)
                    result.append(String.format("%d %d %d\n", i, j, type));
            }
        locker.readLock().unlock();
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created)
                    throw new FileNotFoundException("Can't create new file");
            }

            try (PrintWriter out = new PrintWriter(file.getAbsoluteFile())) {
                out.print(result);
            }
        } catch (IOException e) {
            throw new FileNotFoundException("Can't write to file");
        }
    }
}

