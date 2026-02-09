package jdc.pacman.entities;

import java.awt.*;

public class Dot extends Entity {

    private Player player;
    private boolean shouldVanish;

    public Dot(double x, double y, float width, float height, Player player) {
        super(x, y, width, height, 0, 0, 0);
        this.player = player;
        rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
        depth = 1;
    }

    @Override
    public void tick() {
        if (rect.intersects(player.getRect())) shouldVanish = true;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.white);
        g.drawRect(getX(), getY(), getWidth(), getHeight());
    }

    public boolean shouldVanish() {
        return shouldVanish;
    }
}
