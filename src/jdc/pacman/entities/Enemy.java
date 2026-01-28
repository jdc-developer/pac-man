package jdc.pacman.entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    public Enemy(double x, double y, float width, float height, BufferedImage sprite) {
        super(x, y, width, height, 1.0f, sprite);
    }

    public void tick() {

        /*if (!isCollidingWithPlayer()) {
            if (path == null || path.isEmpty()) {
                Vector2i start = new Vector2i(getX() / 16, getY() / 16);
                Vector2i end = new Vector2i(Game.player.getX() / 16, Game.player.getY() / 16);
                path = AStar.findPath(Game.world, start, end);
            }
        } else if (new Random().nextInt(100) < 5) {
            //Sound.hurtSound.play();
            Game.player.life--;
            Player.isTakingDamage = true;
            System.out.println(Game.player.life);
        }

        followPath(path);*/

    }

    public void render(Graphics g) {

    }
}
