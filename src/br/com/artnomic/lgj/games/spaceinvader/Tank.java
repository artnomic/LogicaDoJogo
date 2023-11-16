package br.com.artnomic.lgj.games.spaceinvader;

import br.com.artnomic.lgj.base.Element;
import br.com.artnomic.lgj.base.Colors;

import java.awt.*;

public class Tank extends Element {

    Colors colors = new Colors();

    private final int barrel = 8;
    private final int hatch = 10;

    public Tank() {
        setWidth(30);
        setHeight(15);
    }

    @Override
    public void update() {}

    @Override
    public void draw(Graphics2D g) {
        g.setColor(colors.getGreenMalachite());
        g.fillRect(getPx() +getWidth() / 2 - barrel /2,
                getPy() - barrel, barrel, barrel);

        g.fillRect(getPx(), getPy(), getWidth(), getHeight());
        g.setColor(colors.getYellowGoldenFizz());
        g.fillOval(getPx() + getWidth() / 2 - hatch /2,
                getPy() + getHeight() / 2 - hatch / 2 ,
                hatch, hatch);
    }
}
