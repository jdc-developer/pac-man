package jdc.pacman.map;


import java.awt.*;

public class Tile {

    protected int x, y;
    protected Rectangle rectangle;
    protected boolean hasHit;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        rectangle = new Rectangle(x, y, 1, 1);
    }

    public void render(Graphics g) {}

    public Rectangle getRectangle() {
        return rectangle;
    }

    public boolean isHasHit() {
        return hasHit;
    }

    public void setHasHit(boolean hasHit) {
        this.hasHit = hasHit;
    }
}
