package br.com.artnomic.lgj.games.spaceinvader;

import br.com.artnomic.lgj.base.Colors;
import br.com.artnomic.lgj.base.Element;

import java.awt.*;

public class Shot extends Element {

    Colors colors = new Colors();
    private boolean enemy;

    public Shot() {
        setWidth(5);
        setHeight(5);
    }
    public Shot(boolean enemy) {
        this();
        this.enemy = enemy;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g) {
        if (!isActive())
            return;

        g.setColor(enemy ? colors.getRedRose() : Color.WHITE);
        g.fillRect(getPx(), getPy(), getWidth(), getHeight());
    }
}
