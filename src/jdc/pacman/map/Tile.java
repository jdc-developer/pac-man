package jdc.pacman.map;


import java.awt.*;

public class Tile {

    protected int x, y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        //g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
    }
}
