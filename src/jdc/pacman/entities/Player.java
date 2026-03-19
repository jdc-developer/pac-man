package jdc.pacman.entities;

import jdc.pacman.PacmanGame;
import jdc.pacman.map.Map;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static java.util.Objects.isNull;

public class Player extends Entity {

    public static final float PLAYER_SPRITE_WIDTH = 29f;
    public static final float PLAYER_SPRITE_HEIGHT = 43f;

    public static final float PLAYER_WIDTH = PLAYER_SPRITE_WIDTH / 2.5f;
    public static final float PLAYER_HEIGHT = PLAYER_SPRITE_HEIGHT / 2.5f;

    private Direction directionToChange;

    private BufferedImage[] animation = new BufferedImage[3];

    public Player(int x, int y) {
        super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT, 1.0f, 15f, 15f);

        for (int i = 0; i < 3; i++) {
            animation[i] = PacmanGame.spritesheet.getSprite(758 , 37 + (i*PLAYER_SPRITE_HEIGHT), PLAYER_SPRITE_WIDTH, PLAYER_SPRITE_HEIGHT);
        }

        rect = new Rectangle(getX(), getY(), getMaskWidth(), getMaskHeight());
        isAnimated = true;
        direction = Direction.RIGHT;
    }

    public void tick() {
        maskX = x - 1;
        maskY = y + 1;

        rect.setBounds(getMaskX(), getMaskY(), getMaskWidth(), getMaskHeight());

        if (!isNull(directionToChange) && isNull(targetedTile)) {
            int offsetY = 0;
            int offsetX = 0;

            switch (directionToChange) {
                case UP:
                    offsetY = 1;
                    offsetX = 0;
                    break;
                case DOWN:
                    offsetY = -1;
                    offsetX = 0;
                    break;
                case LEFT:
                    offsetY = 0;
                    offsetX = 1;
                    break;
                case RIGHT:
                    offsetY = 0;
                    offsetX = -1;
                    break;
            }

            targetedTile = Map.getClosestOpenedTile(getMaskX() - offsetX, getMaskY() - offsetY, rect, directionToChange, direction);
            if (targetedTile != null) {
                System.out.println("Tile X: " + targetedTile.getX());
                System.out.println("Tile Y: " + targetedTile.getY());
            }
            System.out.println("Player X: " + getMaskX());
            System.out.println("Player Y: " + getMaskY());
        }

        if (!isNull(targetedTile)) {
            if (Direction.UP.equals(directionToChange) || Direction.DOWN.equals(directionToChange)) {

                if (Direction.LEFT.equals(direction) && getMaskX() == targetedTile.getX() - rect.getWidth()) {
                    direction = directionToChange;
                    targetedTile = null;
                    directionToChange = null;
                } else if (!Direction.LEFT.equals(direction) && getMaskX() == targetedTile.getX()) {
                    direction = directionToChange;
                    targetedTile = null;
                    directionToChange = null;
                }
            }

            if (Direction.LEFT.equals(directionToChange) || Direction.RIGHT.equals(directionToChange)) {
                if (Direction.UP.equals(direction) && getMaskY() == targetedTile.getY() - rect.getHeight()) {
                    direction = directionToChange;
                    targetedTile = null;
                    directionToChange = null;
                } else if (!Direction.UP.equals(direction) && getMaskY() == targetedTile.getY()) {
                    direction = directionToChange;
                    targetedTile = null;
                    directionToChange = null;
                }
            }
        }

        if (direction.equals(Direction.RIGHT) && Map.checkCollision((getMaskX() + getSpeed()), getMaskY(), rect, direction)) {
            x += speed;
        }
        if (direction.equals(Direction.LEFT) && Map.checkCollision((getMaskX() - getSpeed()), getMaskY(), rect, direction)) {
            x -= speed;
        }

        if (direction.equals(Direction.UP) && Map.checkCollision(getMaskX(), (getMaskY() - getSpeed()), rect, direction)) {
            y -= speed;
        }

        if (direction.equals(Direction.DOWN) && Map.checkCollision(getMaskX(), (getMaskY() + getSpeed()), rect, direction)) {
            y += speed;
        }

        super.tick();
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

        if (!isNull(targetedTile)) {
            g.setColor(Color.blue);
            g.drawRect(targetedTile.getX(), targetedTile.getY(), 1, 1);
        }

    }

    public Rectangle getRect() {
        return rect;
    }

    public Direction getDirectionToChange() {
        return directionToChange;
    }

    public void setDirectionToChange(Direction directionToChange) {
        this.directionToChange = directionToChange;
    }
}
