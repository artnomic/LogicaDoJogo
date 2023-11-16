package br.com.artnomic.lgj.games.spaceinvader;

import br.com.artnomic.lgj.base.Colors;
import br.com.artnomic.lgj.base.Element;

import java.awt.*;

public class Invader extends Element {

    Colors colors = new Colors();

    enum Types {
        SMALL, MEDIUM, BIG, BOSS
    }

    private Types type;
    private boolean open;

    public Invader(Types t) {
        this.type = t;

        setWidth(20);
        setHeight(20);
    }

    @Override
    public void update() {
        open = !open;
    }

    @Override
    public void draw(Graphics2D g) {
        if (!isActive()) {
            return;
        }

        int wid = getWidth();

        if (type == Types.SMALL) {
            wid -= 2;
            g.setColor(colors.getBlue());

            if (open) {
                g.fillOval(getPx(), getPy(), wid, getHeight());

                g.fillRect(getPx() - 5, getPy() - 5, 5, 5);
                g.fillRect(getPx() + wid, getPy() - 5, 5, 5);

                g.fillRect(getPx() - 5, getPy() + getWidth(), 5, 5);
                g.fillRect(getPx() + wid, getPy() + wid, 5, 5);
            } else {
                g.fillRect(getPx(), getPy(), wid, getHeight());
            }
        } else if (type == Types.MEDIUM) {
            g.setColor(colors.getOrangeNeonCarrot());
            if (open) {
                g.drawRect(getPx(), getPy(), wid, getHeight());
            } else {
                g.fillRect(getPx(), getPy(), wid, getHeight());
            }
        } else if (type == Types.BIG) {
            wid += 4;
            if (open) {
                g.setColor(colors.getGrayThunder());
                g.fillRect(getPx(), getPy(), getHeight(), wid);
            } else {
                g.setColor(colors.getGrayCoralReef());
                g.fillRect(getPx(), getPy(), wid, getHeight());
            }
        } else {
            wid = wid + 10;

            g.setColor(colors.getRedRose());
            g.fillOval(getPx(), getPy(), wid, getHeight());

            if (open) {
                g.setColor(Color.WHITE);
                g.fillRect(getPx() + 7, getPy() + getHeight() / 2 -2, 4, 4);
                g.fillRect(getPx() + 13, getPy() + getHeight() / 2 -2, 4, 4);
                g.fillRect(getPx() + 19, getPy() + getHeight() / 2 -2, 4, 4);
            }
        }
    }

    public int getPrize() {
        switch (type) {
            case SMALL:
                return 300;
            case MEDIUM:
                return 200;
            case BIG:
                return 100;
            default:
                return 1000;
        }
    }
}
