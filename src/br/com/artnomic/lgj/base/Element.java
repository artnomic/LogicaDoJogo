package br.com.artnomic.lgj.base;

import java.awt.*;

public class Element {
    private int px, py, width, height, velocity;
    private boolean active;
    private Color color;

    public Element(int px, int py, int width, int height) {
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;
    }

    public void Update() {

    }

    public void Draw(Graphics2D g) {
        g.drawRect(px, py, width, height);
    }

    public void incPx(int x) {
        px += x;
    }

    public void incPy(int y) {
        py += y;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getPy() {
        return py;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void update() {

    }

    public void draw(Graphics2D g) {
        g.drawRect(px, py, width, height);
    }
}