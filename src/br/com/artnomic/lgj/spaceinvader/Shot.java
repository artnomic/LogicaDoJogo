package br.com.artnomic.lgj.spaceinvader;

import br.com.artnomic.lgj.base.Element;

import java.awt.*;

public class Shot extends Element {
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

        g.setColor(enemy ? Color.RED : Color.WHITE);

        g.fillRect(getPx(), getPy(), getWidth(), getHeight());
    }
}
