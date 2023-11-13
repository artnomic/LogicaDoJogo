package br.com.artnomic.lgj.base;

public class Element {
    private int x, y, width, height;
    public float velocity;

    public Element(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}