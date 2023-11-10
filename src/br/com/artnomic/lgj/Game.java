package br.com.artnomic.lgj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends JFrame {
    //TODO: Meu checkpoint é a imagem 1.4 - Movendo objetos pela tela

    private JPanel canva;
    private int px, py;
    private Point mouseClick = new Point();
    private int FPS = 1000 / 20;
    private boolean playing = true;

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

        canva = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                //Exemplo de como fazer cor:
                //Color nomeDaCor = new Color(R, G, B);

                //Limpar todos os desenhos
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, canva.getWidth(), canva.getHeight());

                int x = canva.getWidth() / 2 - 20 + px;
                int y = canva.getHeight() / 2 - 20 + py;

                g.setColor(Color.PINK);
                g.fillRect(x, y, 40, 40);
                g.drawString("Agora eu estou em " + x + " x " + y, 5, 10);
            }
        };

        canva.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        super.getContentPane().add(canva);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setVisible(true);
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
        if (controllArrowKey[0]) {
            py--;
        } else if (controllArrowKey[1]) {
            py++;
        }

        if (controllArrowKey[2]) {
            px--;
        } else if (controllArrowKey[3]) {
            px++;
        }
    }

//    private boolean colide(Element a, Element b) {
//
//    }

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
