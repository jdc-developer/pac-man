package jdc.pacman.entities;

import jdc.pacman.PacmanGame;
import jdc.pacman.map.Map;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public static final float PLAYER_SPRITE_WIDTH = 29f;
    public static final float PLAYER_SPRITE_HEIGHT = 43f;

    public static final float PLAYER_WIDTH = 29f / 2.5f;
    public static final float PLAYER_HEIGHT = 43f / 2.5f;

    private BufferedImage[] animation = new BufferedImage[3];
    private Rectangle rect;

    private int frames = 0, maxFrames = 5, animationIndex = 0, maxAnimationIndex = 2;

    public Player(int x, int y) {
        super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT, 1.0f, 14f, 14f);

        for (int i = 0; i < 3; i++) {
            animation[i] = PacmanGame.spritesheet.getSprite(758 , 37 + (i*PLAYER_SPRITE_HEIGHT), PLAYER_SPRITE_WIDTH, PLAYER_SPRITE_HEIGHT);
        }

        direction = Direction.RIGHT;
    }

    public void tick() {
        rect = new Rectangle(getX(), getY(), getMaskWidth(), getMaskHeight());
        if (direction.equals(Direction.RIGHT) && Map.checkCollision((getX() + getSpeed()), getY(), rect, Direction.RIGHT)) {
            x += speed;
        }
        if (direction.equals(Direction.LEFT) && Map.checkCollision((getX() - getSpeed()), getY(), rect, Direction.LEFT)) {
            x -= speed;
        }

        if (direction.equals(Direction.UP) && Map.checkCollision(getX(), (getY() - getSpeed()), rect, Direction.UP)) {
            y -= speed;
        }

        if (direction.equals(Direction.DOWN) && Map.checkCollision(getX(), (getY() + getSpeed()), rect, Direction.DOWN)) {
            y += speed;
        }

        maskX = x - 1;
        maskY = y + 1;
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
        g.setColor(Color.red);
        g.drawRect(getMaskX(), getMaskY(), getMaskWidth(), getMaskHeight());

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
        g.drawImage(animation[animationIndex], getX(), getY(), getWidth(), getHeight(), null);

        g.setTransform(originalTransform);

    }

}
