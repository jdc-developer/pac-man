package jdc.pacman.entities;

import jdc.pacman.PacmanGame;
import jdc.pacman.map.Map;
import jdc.pacman.map.Node;
import jdc.pacman.map.Vector2i;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Enemy extends Entity {

    public static final float ENEMY_SPRITE_WIDTH = 42f;
    public static final float ENEMY_SPRITE_HEIGHT = 43f;

    public static final float ENEMY_WIDTH = ENEMY_SPRITE_WIDTH / 2.5f;
    public static final float ENEMY_HEIGHT = ENEMY_SPRITE_HEIGHT / 2.5f;

    private BufferedImage[] animationRight = new BufferedImage[2];
    private BufferedImage[] animationLeft = new BufferedImage[2];
    private BufferedImage[] animationDown = new BufferedImage[2];
    private BufferedImage[] animationUp = new BufferedImage[2];

    private EnemyType type;

    protected List<Node> path;

    public Enemy(double x, double y, EnemyType type) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, 1.0f, 14f, 14f);

        int spriteX = 0;
        switch (type) {
            case RED:
                spriteX = 25;
                break;
            case BLUE:
                spriteX = 109;
                break;
        }

        for (int i = 0; i < 2; i++) {
            animationRight[i] = PacmanGame.spritesheet.getSprite(spriteX , 37 + (i*ENEMY_SPRITE_HEIGHT), ENEMY_SPRITE_WIDTH, ENEMY_SPRITE_HEIGHT);
            animationDown[i] = PacmanGame.spritesheet.getSprite(spriteX , 123 + (i*ENEMY_SPRITE_HEIGHT), ENEMY_SPRITE_WIDTH, ENEMY_SPRITE_HEIGHT);
            animationLeft[i] = PacmanGame.spritesheet.getSprite(spriteX , 208 + (i*ENEMY_SPRITE_HEIGHT), ENEMY_SPRITE_WIDTH, ENEMY_SPRITE_HEIGHT);
            animationUp[i] = PacmanGame.spritesheet.getSprite(spriteX , 293 + (i*ENEMY_SPRITE_HEIGHT), ENEMY_SPRITE_WIDTH, ENEMY_SPRITE_HEIGHT);
        }

        maxAnimationIndex = 1;
        maxFrames = 7;
        rect = new Rectangle(getX(), getY(), getMaskWidth(), getMaskHeight());
        direction = Direction.RIGHT;
    }

    @Override
    public void tick() {

        /*if (path == null || path.isEmpty()) {
            Vector2i start = new Vector2i(getX(), getY());
            Vector2i end = new Vector2i(PacmanGame.player.getX() / 16, PacmanGame.player.getY() / 16);
            path = AStar.findPath(PacmanGame.world, start, end);
        }

        followPath(path);*/

        rect.setBounds(getX(), getY(), getMaskWidth(), getMaskHeight());
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
        /*g.setColor(Color.red);
        g.drawRect(getMaskX(), getMaskY(), getMaskWidth(), getMaskHeight());*/

        BufferedImage animation = null;

        switch (direction) {
            case RIGHT:
                animation = animationRight[animationIndex];
                break;
            case DOWN:
                animation = animationDown[animationIndex];
                break;
            case LEFT:
                animation = animationLeft[animationIndex];
                break;
            case UP:
                animation = animationUp[animationIndex];
                break;
        }

        g.drawImage(animation, getX(), getY(), getWidth(), getHeight(), null);

    }

    public double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static boolean isColliding(Entity e1, Entity e2) {
        Rectangle rec1 = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
        Rectangle rec2 = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());

        if (rec1.intersects(rec2)) {
            return true;
        }

        return false;
    }

    public void followPath(List<Node> path) {
        if (path != null) {
            if (!path.isEmpty()) {
                Vector2i target = path.get(path.size() - 1).tile;
                //xprev = x;
                //yprev = y;

                if (x < target.x * 16) x++;
                else if (x > target.x * 16) x--;

                if (y < target.y * 16) y++;
                else if (y > target.y * 16) y--;

                if (x == target.x * 16 && y == target.y * 16) {
                    path.remove(path.size() - 1);
                }
            }
        }
    }
}
