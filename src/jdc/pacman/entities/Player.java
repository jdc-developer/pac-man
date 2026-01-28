package jdc.pacman.entities;

import jdc.pacman.PacmanGame;
import jdc.pacman.map.Map;
import jdc.zelda.world.Camera;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public static final float PLAYER_SPRITE_WIDTH = 29f;
    public static final float PLAYER_SPRITE_HEIGHT = 43f;

    public static final float PLAYER_WIDTH = 29f / 2.5f;
    public static final float PLAYER_HEIGHT = 43f / 2.5f;

    private BufferedImage[] animation = new BufferedImage[3];

    private int frames = 0, maxFrames = 5, animationIndex = 0, maxAnimationIndex = 2;

    public Player(int x, int y) {
        super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT, 1.0f, PacmanGame.spritesheet
                .getSprite(0, 0, PLAYER_SPRITE_WIDTH, PLAYER_SPRITE_HEIGHT));

        for (int i = 0; i < 3; i++) {
            animation[i] = PacmanGame.spritesheet.getSprite(758 , 37 + (i*PLAYER_SPRITE_HEIGHT), PLAYER_SPRITE_WIDTH, PLAYER_SPRITE_HEIGHT);
        }

        direction = Direction.RIGHT;
    }

    public void tick() {
        if (direction.equals(Direction.RIGHT) && Map.isFree((getX() + getSpeed()), getY())) {
            x += speed;
        }
        if (direction.equals(Direction.LEFT) && Map.isFree((getX() - getSpeed()), getY())) {
            x -= speed;
        }

        if (direction.equals(Direction.UP) && Map.isFree(getX(), (getY() - getSpeed()))) {
            y -= speed;
        }
        if (direction.equals(Direction.DOWN) && Map.isFree(getX(), (getY() + getSpeed()))) {
            y += speed;
        }

        frames++;

        if (frames == maxFrames) {
            frames = 0;
            animationIndex++;

            if (animationIndex > maxAnimationIndex) animationIndex = 0;
        }
    }

    @Override
    public void render(Graphics2D g) {
        AffineTransform originalTransform = g.getTransform();
        float centerX = getX() + (getWidth() / 2f);
        float centerY = getY() + (getHeight() / 2f);

        if (direction.equals(Direction.LEFT)) {
            double angle = Math.toRadians(180);
            g.rotate(angle, centerX, centerY);
        } else if (direction.equals(Direction.UP)) {
            double angle = Math.toRadians(270);
            g.rotate(angle, centerX, centerY);
        } else if (direction.equals(Direction.DOWN)) {
            double angle = Math.toRadians(90);
            g.rotate(angle, centerX, centerY);
        }
        g.drawImage(animation[animationIndex], getX() - Camera.x, getY() - Camera.y - z, getWidth(), getHeight(), null);
        g.setTransform(originalTransform);

    }

}
