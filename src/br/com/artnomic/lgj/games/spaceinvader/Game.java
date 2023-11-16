package br.com.artnomic.lgj.games.spaceinvader;

import br.com.artnomic.lgj.base.Colors;
import br.com.artnomic.lgj.base.Element;
import br.com.artnomic.lgj.base.Text;
import br.com.artnomic.lgj.base.Util;
import br.com.artnomic.lgj.games.spaceinvader.Invader.Types;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game extends JFrame {

    Colors colors = new Colors();

    private static final long serialVersionUID = 1L;

    private static final int FPS = 1000 / 20;

    private static final int SCREEN_HEIGHT = 680;

    private static final int SCREEN_WIDTH = 540;

    private JPanel screen;

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

        screen = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(buffer, 0, 0, null);
            }
        };

        getContentPane().add(screen);

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

    //Elementos do Jogo
    private int lives = 3;

    //Variaveis para o controle do Jogo
    private int spacing = 15;
    private int baseline = 60;

    private Text text = new Text();

    private Element life = new Tank();


    private int count, counterWait, totalEnemies, destroyed, points, dir;

    private Element[] level;

    private int lvl = 1;

    private boolean moveEnemies, newLine;

    private Invader[][] invaders = new Invader[11][5];

    private Invader.Types[] typePerLine = {Types.SMALL, Types.MEDIUM, Types.MEDIUM, Types.BIG, Types.BIG};

    private Element tank, tankShot, bossShot;

    private Invader boss;

    private Element[] shots = new Shot[3];

    private Random rand = new Random();

    private void loadGame() {

        int total = 0;
        int _WID = 10;

        for (int i = 0; i < Level.levels.length; i++) {
            char[][] n = Level.levels[i];

            for (int line = 0; line < n.length; line++) {
                for (int column = 0; column < n[0].length; column++) {
                    if (n[line][column] == '0') {
                        total++;
                    }
                }
            }
        }

        level = new Element[total];

        for (int i = 0; i < Level.levels.length; i++) {
            char[][] n = Level.levels[i];

            for (int line = 0; line < n.length; line++) {
                for (int column = 0; column < n[0].length; column++) {
                    if (n[line][column] != ' ') {

                        Element e = new Element();
                        e.setActive(true);
                        e.setColor(colors.getGrayCoralReef());

                        e.setPx(_WID * column + 30 + (200 * i));
                        e.setPy(_WID * line + SCREEN_HEIGHT - 300);

                        e.setHeight(_WID);
                        e.setWidth(_WID);

                        level[--total] = e;
                    }
                }
            }

        }

        tank = new Tank();
        tank.setVelocity(3);
        tank.setActive(true);
        tank.setPx(screen.getWidth() / 2 - tank.getWidth() / 2);
        tank.setPy(screen.getHeight() - tank.getHeight() - baseline);

        tankShot = new Shot();
        tankShot.setVelocity(-15);

        boss = new Invader(Invader.Types.BOSS);

        bossShot = new Shot(true);
        bossShot.setVelocity(20);
        bossShot.setHeight(15);

        for (int i = 0; i < shots.length; i++) {
            shots[i] = new Shot(true);
        }

        for (int i = 0; i < invaders.length; i++) {
            for (int j = 0; j < invaders[i].length; j++) {
                Invader e = new Invader(typePerLine[j]);

                e.setActive(true);

                e.setPx(i * e.getWidth() + (i + 1) * spacing);
                e.setPy(i * e.getHeight() + j * spacing + baseline);

                invaders[i][j] = e;
            }
        }

        dir = 1;

        totalEnemies = invaders.length * invaders[0].length;

        counterWait = totalEnemies / lvl;
    }

    private void startGame() {
        long nxtAtualization = 0;

        while (true) {
            if (System.currentTimeMillis() >= nxtAtualization) {
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

                if (destroyed == totalEnemies) {
                    destroyed = 0;
                    lvl++;
                    loadGame();
                    continue;
                }

                if (count > counterWait) {
                    moveEnemies = true;
                    count = 0;
                    counterWait = totalEnemies - destroyed - lvl * lvl;
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

                    if (boss.getPx() > screen.getWidth()) {
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
                            points = points + inv.getPrize() * lvl;
                            continue;
                        }

                        if (moveEnemies) {
                            inv.update();

                            if (newLine) {
                                inv.setPy(inv.getPy() + inv.getHeight() + spacing);
                            } else {
                                inv.incPx(spacing + dir);
                            }

                            if (!newLine && !collideEdges) {
                                int pxLeft = inv.getPx() - spacing;
                                int pxRight = inv.getPx() + inv.getWidth() + spacing;

                                if (pxLeft <= 0 || pxRight >= screen.getWidth()) {
                                    collideEdges = true;
                                }
                            }

                            if (!shots[0].isActive() && inv.getPx() < tank.getPx()) {
                                addEnemyShot(inv, shots[0]);

                            } else if (!shots[1].isActive() &&
                                    inv.getPx() > tank.getPx() &&
                                    inv.getPx() < tank.getPx() + tank.getWidth()) {
                                addEnemyShot(inv, shots[1]);

                            } else if (!shots[2].isActive() &&
                                    inv.getPx() > tank.getPx()) {
                                addEnemyShot(inv, shots[2]);
                            }

                            if (!boss.isActive() && rand.nextInt(500) == destroyed) {
                                boss.setPx(0);
                                boss.setActive(true);
                            }
                        }
                    }
                }

                if (moveEnemies && newLine) {
                    dir *= -1;
                    newLine = false;

                } else if (moveEnemies && collideEdges) {
                    newLine = true;
                }

                moveEnemies = false;

                if (tankShot.isActive()) {
                    tankShot.incPy(tankShot.getVelocity());

                    if (Util.collide(tankShot, boss)) {
                        points = points + boss.getPrize() * lvl;
                        boss.setActive(false);
                        tankShot.setActive(false);

                    } else if (tankShot.getPy() < 0) {
                        tankShot.setActive(false);
                    }

                    tankShot.draw(g2d);
                }

                if (bossShot.isActive()) {
                    bossShot.incPy(bossShot.getVelocity());

                    if (Util.collide(bossShot, tank)) {
                        lives--;
                        bossShot.setActive(false);
                    } else if (bossShot.getPy() > screen.getHeight() - baseline - bossShot.getHeight()) {
                        bossShot.setActive(false);
                    } else {
                        bossShot.draw(g2d);
                    }
                }

                for (int i = 0; i < shots.length; i++) {
                    if(shots[i].isActive()) {
                        shots[i].incPy(+10);

                        if(Util.collide(shots[i], tank)) {
                            lives--;
                            shots[i].setActive(false);
                        } else if (shots[i].getPy() > screen.getHeight() - baseline - shots[i].getHeight()) {
                            shots[i].setActive(false);
                        }

                        shots[i].draw(g2d);
                    }
                }

                // Desenhe aqui para as naves ficarem acima dos tiros
                for (int i = 0; i < invaders.length; i++) {
                    for (int j = 0; j < invaders[i].length; j++) {
                        Invader e = invaders[i][j];
                        e.draw(g2d);
                    }
                }

                for (Element e : level) {
                    if (!e.isActive())
                        continue;

                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i].isActive() && Util.collide(shots[i], e)) {
                            e.setActive(false);
                            shots[i].setActive(false);
                        }
                    }

                    g2d.setColor(e.getColor());
                    e.draw(g2d);
                }

                tank.update();
                tank.draw(g2d);

                boss.update();
                boss.draw(g2d);

                g2d.setColor(Color.WHITE);

                text.draw(g2d, String.valueOf(points), 10, 20);
                text.draw(g2d, "Level " + lvl, screen.getWidth() - 100, 20);
                text.draw(g2d, String.valueOf(lives), 10, screen.getHeight() - 10);

                // Linha base
                g2d.setColor(Color.GREEN);
                g2d.drawLine(0, screen.getHeight() - baseline, screen.getWidth(), screen.getHeight() - baseline);

                for (int i = 1; i < lives; i++) {
                    life.setPx(i * life.getWidth() + i * spacing);
                    life.setPy(screen.getHeight() - life.getHeight());

                    life.draw(g2d);
                }

                if(lives == 0) return;

                screen.repaint();

                nxtAtualization = System.currentTimeMillis() + FPS;

            }
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
