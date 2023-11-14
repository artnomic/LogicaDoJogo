package br.com.artnomic.lgj.spaceinvader;

import br.com.artnomic.lgj.base.Element;

public class Invader extends Element {

    enum Types {
        SMALL, MEDIUM, BIG, BOSS
    }

    private Types type;
    private boolean aberto;

    public Invader(Types t) {
        this.type = t;

        setWidth(20);
        setHeight(20);
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
