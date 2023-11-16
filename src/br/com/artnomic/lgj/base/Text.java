package br.com.artnomic.lgj.base;

import java.awt.*;

public class Text extends Element {
    private Font font;

    public Text() {
        font = new Font("Tahoma", Font.PLAIN, 16);
    }

    public Text(Font font) {
        this.font = font;
    }

    public void draw(Graphics2D g, String text) {
        draw(g, text, getPx(), getPy());
    }

    public void draw(Graphics2D g, String text,
                     int px, int py) {
        if(getColor() != null) {
            g.setColor(getColor());
        }

        g.setFont(font);
        g.drawString(text, px, py);
    }
}
