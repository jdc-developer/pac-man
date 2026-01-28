package jdc.pacman.map;

import java.awt.*;

public class WallTile extends Tile {

    private static final Color WALL_COLOR = new Color(61, 61, 181);

    public WallTile(int x, int y) {
        super(x, y);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        g.setColor(WALL_COLOR);
        g.drawRect(x, y, 1, 1);
    }
}
