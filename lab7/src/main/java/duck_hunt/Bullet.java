package duck_hunt;

import javax.swing.*;
import java.awt.*;


public class Bullet extends Thread {
  private int x;
  private int y;

  private static final int dy = 10;
  private final int sizeX = 70;
  private final int sizeY = 60;

  private GamePanel panel;
  private JLabel bulletLabel;
  private Hunter hunter;

  Bullet(GamePanel panel, Hunter hunter, int x, int y) {
    this.panel = panel;
    this.hunter = hunter;
    this.x = x;
    this.y = y;
    this.bulletLabel = new JLabel(new ImageIcon(getClass().getResource("/bullet.png")));
    bulletLabel.setSize(new Dimension(sizeX, sizeY));
    bulletLabel.setLocation(x - sizeX / 2, y - sizeY / 2);
  }

  @Override
  public void run() {
    hunter.addBullet(1);
    panel.add(bulletLabel);

    while (!isInterrupted()) {
      if (y < 0) break;
      y -= dy;

      bulletLabel.setLocation(x - sizeX / 2, y - sizeY / 2);

      for (Duck duck : panel.ducks) {
        synchronized (duck) {
          if (duck.x < x && x < duck.x + duck.sizeX && duck.y < y && y < duck.y + duck.sizeY) {
            duck.interrupt();
            this.interrupt();
            break;
          }
        }
      }

      try {
        sleep(10);
      } catch (InterruptedException e) {
        break;
      }
    }

    panel.remove(bulletLabel);
    panel.repaint();
    hunter.addBullet(-1);
  }
}
