package br.com.artnomic.lgj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JFrame {
    //TODO: criar o readme e continuar o capitulo 2.

    private final int FPS = 1000 / 20;

    class Element {
        private int x, y, width, height;
        public float velocity;

        public Element(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private JPanel canva;
    private boolean playing = true;
    private boolean gameOver = false;

    private Element shot, player;
    private Element[] blocks;


    private int points;
    private int larg = 50;
    private int limitLine = 350;
    private java.util.Random r = new java.util.Random();

    private boolean[] controllArrowKey = new boolean[4];

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

        shot = new Element(0, 0, 1, 0);
        player = new Element(0, 0, larg, larg);
        player.velocity = 5;

        blocks = new Element[5];
        for (int i = 0; i < blocks.length; i++) {
            int space = i * larg + 10 * (i + 1);
            blocks[i] = new Element(space, 0, larg, larg);
            blocks[i].velocity = 1;
        }

        canva = new JPanel() {

            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                //Exemplo de como fazer cor:
                //Color nomeDaCor = new Color(R, G, B);

                //Limpar todos os desenhos
                g.setColor(Color.WHITE);
                g.fillRect(0, 0,
                        canva.getWidth(), canva.getHeight());

                g.setColor(Color.RED);
                g.fillRect(shot.x, shot.y,
                        shot.width, canva.getHeight());

                g.setColor(Color.GREEN);
                g.fillRect(player.x, player.y,
                        player.width, player.height);

                g.setColor(Color.BLUE);
                for (Element block : blocks) {
                    g.fillRect(block.x, block.y,
                            block.width, block.height);
                }

                g.setColor(Color.GRAY);
                g.drawLine(0, limitLine,
                        canva.getWidth(), limitLine);

                g.drawString("Pontos: " + points, 0, 10);

            }
        };

        getContentPane().add(canva);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(640, 480);
        setVisible(true);

        player.x = canva.getWidth() / 2 - player.width /2;
        player.y = canva.getHeight() - player.height;

        shot.height = canva.getHeight() - player.height;
    }

    public void start() {
        long nextAtualization = 0;

        while (playing) {
            if (System.currentTimeMillis() >= nextAtualization) {
                updateGame();
                canva.repaint();
                nextAtualization = System.currentTimeMillis() + FPS;
            }
        }
    }

    private void updateGame() {
        if(gameOver) {
            return;
        }

        if (controllArrowKey[2]) {
            player.x -= player.velocity;
        } else if (controllArrowKey[3]) {
            player.x += player.velocity;
        }

        if(player.x < 0) {
            player.x = canva.getWidth() - player.width;
        }

        if(player.x + player.width > canva.getWidth()) {
            player.x = 0;
        }

        shot.y = 0;
        shot.x = player.x + player.width / 2;

        for (Element block : blocks) {
            if (block.y > limitLine) {
                gameOver = true;
                break;
            }

            if (collide (block, shot) && block.y > 0) {
                block.y -= block.velocity * 2;
                shot.y = block.y;
            } else {
                int fate = r.nextInt(10);

                if(fate == 0) {
                    block.y += block.velocity + 1;
                } else if (fate == 5) {
                    block.y -= block.velocity;
                } else {
                    block.y += block.velocity;
                }
            }
            points = points + blocks.length;
        }
    }

    private boolean collide(Element a, Element b) {
        if (a.x + a.width >= b.x && a.x <= b.x + b.width) {
            return true;
        }

        return false;
    }

    private void arrowKey(int key, boolean press) {
        switch (key) {
            case KeyEvent.VK_ESCAPE: //ESC
                playing = false;
                dispose();
                break;
            case KeyEvent.VK_W: //Seta pra cima
                controllArrowKey[0] = press;
                break;
            case KeyEvent.VK_UP: //Seta pra cima
                controllArrowKey[0] = press;
                break;
            case KeyEvent.VK_S: //Seta pra baixa
                controllArrowKey[1] = press;
                break;
            case KeyEvent.VK_DOWN: //Seta pra baixa
                controllArrowKey[1] = press;
                break;
            case KeyEvent.VK_A: //Seta para esquerda
                controllArrowKey[2] = press;
                break;
            case KeyEvent.VK_LEFT: //Seta para esquerda
                controllArrowKey[2] = press;
                break;
            case KeyEvent.VK_D: //Seta para direita
                controllArrowKey[3] = press;
                break;
            case KeyEvent.VK_RIGHT: //Seta para direita
                controllArrowKey[3] = press;
                break;
        }
    }

    public static void main(String[] args) {
        Game animation = new Game();
        animation.start();
    }

}
