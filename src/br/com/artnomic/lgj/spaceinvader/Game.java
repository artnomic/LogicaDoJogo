package br.com.artnomic.lgj.spaceinvader;

import br.com.artnomic.lgj.base.Element;
import br.com.artnomic.lgj.base.Util;
import br.com.artnomic.lgj.spaceinvader.Invader.Types;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final int FPS = 1000 / 20;

    private static final int SCREEN_HEIGHT = 680;

    private static final int SCREEN_WIDTH = 540;

    private JPanel canva;

    private Graphics2D g2d;

    private BufferedImage buffer;

    private boolean[] controllArrowKey = new boolean[5];

    public Game() {
        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                arrowKey(e.getKeyCode(), false);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                arrowKey(e.getKeyCode(), true);
            }
        });

        buffer = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        g2d = buffer.createGraphics();

        canva = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(buffer, 0, 0, null);
            }
        };

        getContentPane().add(canva);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setVisible(true);

    }

    private void arrowKey(int key, boolean press) {
        switch (key) {
            case KeyEvent.VK_ESCAPE: //ESC
                dispose();
                break;
            case KeyEvent.VK_W: //W
                controllArrowKey[0] = press;
                break;
            case KeyEvent.VK_UP: //Seta pra cima
                controllArrowKey[0] = press;
                break;
            case KeyEvent.VK_S: //S
                controllArrowKey[1] = press;
                break;
            case KeyEvent.VK_DOWN: //Seta pra baixa
                controllArrowKey[1] = press;
                break;
            case KeyEvent.VK_A: //A
                controllArrowKey[2] = press;
                break;
            case KeyEvent.VK_LEFT: //Seta para esquerda
                controllArrowKey[2] = press;
                break;
            case KeyEvent.VK_D: //D
                controllArrowKey[3] = press;
                break;
            case KeyEvent.VK_RIGHT: //Seta para direita
                controllArrowKey[3] = press;
                break;
            case KeyEvent.VK_SPACE: //Espaço
                controllArrowKey[4] = press;
                break;
        }
    }

    private int spacing = 15;
    private int baseline = 60;

    private int count, counterWait, totalEnemies, destroyed, points, dir;

    private int level = 1;

    private boolean moveEnemies, newLine;

    private Invader[][] invaders = new Invader[11][5];

    private Invader.Types[] typePerLine = {Types.SMALL, Types.MEDIUM, Types.MEDIUM, Types.BIG, Types.BIG};

    private Element tank;

    private Element tankShot;

    private Invader boss;

    private Element bossShot;

    private Element[] shots = new Shot[3];

    private Random rand = new Random();

    private void loadGame() {

        boss = new Invader(Invader.Types.BOSS);

        bossShot = new Shot(true);
        bossShot.setVelocity(20);
        bossShot.setHeight(15);

        for (int i = 0; i < invaders.length; i++) {
            for (int j = 0; j < invaders[i].length; j++) {
                Invader e = new Invader(typePerLine[j]);

                e.setActive(true);

                e.setPx(i * e.getWidth() + (i + 1) * spacing);
                e.setPy(i * e.getHeight() + j * spacing + baseline);

                invaders[i][j] = e;
            }
        }


    }

    private void startGame() {
        while (true) {

            if (count > counterWait) {
                moveEnemies = true;
                count = 0;
                counterWait = totalEnemies - destroyed - level * level;
            } else {
                count++;
            }

            if (tank.isActive()) {
                if (controllArrowKey[2]) {
                    tank.setPx(tank.getPx() - tank.getVelocity());
                } else if (controllArrowKey[3]) {
                    tank.setPx(tank.getPx() + tank.getVelocity());
                }
            }

            if (controllArrowKey[4] && !tankShot.isActive()) {
                tankShot.setPx(tank.getPx() +
                        tank.getWidth() / 2 - tankShot.getWidth() / 2);

                tankShot.setPy(tank.getPy() - tankShot.getHeight());
                tankShot.setActive(true);
            }

            if (boss.isActive()) {
                boss.incPx(tank.getVelocity() - 1);

                if (!bossShot.isActive() && Util.collideX(boss, tank)) {
                    addEnemyShot(boss, bossShot);
                }

                if (boss.getPx() > canva.getWidth()) {
                    boss.setActive(false);
                }
            }

            boolean collideEdges = false;

            for (int j = invaders[0].length - 1; j >= 0; j--) {
                for (int i = 0; i < invaders.length; i++) {
                    Invader inv = invaders[i][j];

                    if (!inv.isActive()) {
                        continue;
                    }

                    if (Util.collide(tankShot, inv)) {
                        inv.setActive(false);
                        tankShot.setActive(false);
                        destroyed++;
                        points = points + inv.getPrize() * level;
                        continue;
                    }

                    if(moveEnemies) {
                        inv.update();

                        if(newLine) {
                            inv.setPy(inv.getPy() + inv.getHeight() + spacing);
                        } else {
                            inv.incPx(spacing + dir);
                        }

                        if(!newLine && !collideEdges) {
                            int pxLeft = inv.getPx() - spacing;
                            int pxRight = inv.getPx() + inv.getWidth() + spacing;

                            if(pxLeft <= 0 || pxRight >= canva.getWidth()) {
                                collideEdges = true;
                            }
                        }

                        if(!shots[0].isActive() && inv.getPx() < tank.getPx()) {
                            addEnemyShot(inv, shots[0]);

                        } else if (!shots[1].isActive() &&
                                inv.getPx() > tank.getPx() &&
                                inv.getPx() < tank.getPx() + tank.getWidth()) {
                            addEnemyShot(inv, shots[1]);

                        } else if (!shots[2].isActive() &&
                                inv.getPx() > tank.getPx()) {
                            addEnemyShot(inv, shots[2]);
                        }

                        if(!boss.isActive() && rand.nextInt(500) == destroyed) {
                            boss.setPx(0);
                            boss.setActive(true);
                        }

                    }
                }
            }

            if(moveEnemies && newLine) {
                dir *= -1;
                newLine = false;
            } else if (moveEnemies && collideEdges) {
                newLine = true;
            }

            moveEnemies = false;


        }
    }

    public void addEnemyShot(Element enemy, Element shot) {
        shot.setActive(true);
        shot.setPx(enemy.getPx() +
                enemy.getWidth() / 2 - shot.getWidth() / 2);
        shot.setPy(enemy.getPy() + enemy.getHeight());
    }
    public static void main(String[] args) {
        Game game = new Game();
        game.loadGame();
        game.startGame();
    }
}
