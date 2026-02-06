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

        if (hasHit) {
            g.setColor(Color.blue);
            g.drawRect((int)rectangle.getX(), (int)rectangle.getY(), (int)rectangle.getWidth(), (int)rectangle.getHeight());
        } else {
            g.setColor(WALL_COLOR);
            g.drawRect(x, y, 1, 1);
        }

        /*g.setColor(WALL_COLOR);
        g.drawRect(x, y, 1, 1);*/
    }
}
