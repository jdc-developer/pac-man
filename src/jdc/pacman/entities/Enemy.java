package jdc.pacman.entities;

import jdc.pacman.PacmanGame;
import jdc.pacman.map.Map;
import jdc.pacman.map.Node;
import jdc.pacman.map.Tile;
import jdc.pacman.map.Vector2i;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.util.Objects.isNull;

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
    private double lastX, lastY;
    private boolean movingToTile;

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
            case YELLOW:
                spriteX = 151;
                break;
            case GREEN:
                spriteX = 193;
                break;
            case PURPLE:
                spriteX = 235;
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
        isAnimated = true;
        depth = 2;
        rect = new Rectangle(getX(), getY(), getMaskWidth(), getMaskHeight());
        direction = Direction.UP;
    }

    @Override
    public void tick() {

        /*if (path == null || path.isEmpty()) {
            Vector2i start = new Vector2i(getX(), getY());
            Vector2i end = new Vector2i(PacmanGame.player.getX(), PacmanGame.player.getY());
            path = AStar.findPath(PacmanGame.world, start, end);
        }

        followPath(path);*/

        maskX = x + 2;
        maskY = y + 1;
        rect.setBounds(getMaskX(), getMaskY(), getMaskWidth(), getMaskHeight());

        if (direction.equals(Direction.RIGHT) && Map.checkCollision((getMaskX() + getSpeed()), getMaskY(), rect, Direction.RIGHT)) {
            x += speed;
        }
        if (direction.equals(Direction.LEFT) && Map.checkCollision((getMaskX() - getSpeed()), getMaskY(), rect, Direction.LEFT)) {
            x -= speed;
        }

        if (direction.equals(Direction.UP) && Map.checkCollision(getMaskX(), (getMaskY() - getSpeed()), rect, Direction.UP)) {
            y -= speed;
        }

        if (direction.equals(Direction.DOWN) && Map.checkCollision(getMaskX(), (getMaskY() + getSpeed()), rect, Direction.DOWN)) {
            y += speed;
        }

        if (direction.equals(Direction.UP) && y == lastY && isNull(targetedTile)) {
            Tile tile = Map.getClosestOpenedTile(getX(), getMaskY() - 2, rect, Direction.UP, direction);
            if (!isNull(tile)) {
                targetedTile = tile;
                movingToTile = true;

                if (tile.getX() > getX()) direction = Direction.RIGHT;
                if (tile.getX() < getX()) direction = Direction.LEFT;
            }
        }

        if (movingToTile) {
            if (getMaskX() == targetedTile.getX()) {
                direction = Direction.UP;
                movingToTile = false;
                targetedTile = null;
            }
        }

        if (lastY != y) lastY = y;
        if (lastX != x) lastX = x;
        super.tick();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.red);
        g.drawRect(getMaskX(), getMaskY(), getMaskWidth(), getMaskHeight());

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

                if (x < target.getX()) x++;
                else if (x > target.getX()) x--;

                if (y < target.getY()) y++;
                else if (y > target.getY()) y--;

                if (x == target.getX() && y == target.getY()) {
                    path.remove(path.size() - 1);
                }
            }
        }
    }
}
