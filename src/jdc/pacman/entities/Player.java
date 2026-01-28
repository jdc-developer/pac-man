package jdc.pacman.entities;

import jdc.pacman.PacmanGame;
import jdc.pacman.map.Map;
import jdc.zelda.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public static final float PLAYER_WIDTH = 29f;
    public static final float PLAYER_HEIGHT = 43f;

    private BufferedImage[] rightAnimation = new BufferedImage[3];
    private BufferedImage[] leftAnimation = new BufferedImage[3];
    private BufferedImage[] upAnimation = new BufferedImage[3];
    private BufferedImage[] downAnimation = new BufferedImage[3];

    private int frames = 0, maxFrames = 5, animationIndex = 0, maxAnimationIndex = 2;

    public Player(int x, int y) {
        super(x, y, PLAYER_WIDTH / 2.5f, PLAYER_HEIGHT / 2.5f, 1.0f, PacmanGame.spritesheet
                .getSprite(0, 0, PLAYER_WIDTH, PLAYER_HEIGHT));

        for (int i = 0; i < 3; i++) {
            rightAnimation[i] = PacmanGame.spritesheet.getSprite(758 , 37 + (i*PLAYER_HEIGHT), PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        for (int i = 0; i < 3; i++) {
            leftAnimation[i] = PacmanGame.spritesheet.getSprite(758 , 293 + (i*PLAYER_HEIGHT), PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        for (int i = 0; i < 3; i++) {
            upAnimation[i] = PacmanGame.spritesheet.getSprite(758 , 427 + (i*PLAYER_HEIGHT), PLAYER_WIDTH, PLAYER_HEIGHT);
        }

        for (int i = 0; i < 3; i++) {
            downAnimation[i] = PacmanGame.spritesheet.getSprite(758 , 165 + (i*PLAYER_HEIGHT), PLAYER_WIDTH, PLAYER_HEIGHT);
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
    public void render(Graphics g) {
        if (direction.equals(Direction.RIGHT)) {
            g.drawImage(rightAnimation[animationIndex], getX() - Camera.x, getY() - Camera.y - z, getWidth(), getHeight(), null);
        } else if (direction.equals(Direction.LEFT)) {
            g.drawImage(leftAnimation[animationIndex], getX() - Camera.x, getY() -  Camera.y - z, getWidth(), getHeight(),null);
        } else if (direction.equals(Direction.UP)) {
            g.drawImage(upAnimation[animationIndex], getX() - Camera.x, getY() -  Camera.y - z, getWidth(), getHeight(),null);
        } else if (direction.equals(Direction.DOWN)) {
            g.drawImage(downAnimation[animationIndex], getX() - Camera.x, getY() -  Camera.y - z, getWidth(), getHeight(),null);
        }

    }

}
