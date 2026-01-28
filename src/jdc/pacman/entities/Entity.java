package jdc.pacman.entities;

import jdc.pacman.PacmanGame;
import jdc.pacman.map.Camera;
import jdc.pacman.map.Map;
import jdc.pacman.map.Node;
import jdc.pacman.map.Vector2i;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

public abstract class Entity {

    protected double x;
    protected double y;
    protected float width;
    protected float height;
    protected int z;
    protected float speed;
    protected Direction direction;

    protected int depth;

    protected List<Node> path;

    private BufferedImage sprite;

    public Entity(double x, double y, float width, float height, float speed, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
        this.speed = speed;
    }

    public static Comparator<Entity> depthComparator = new Comparator<Entity>(){

        @Override
        public int compare(Entity o1, Entity o2) {
            if (o1.depth < o2.depth) return - 1;
            if (o1.depth > o2.depth) return + 1;
            return 0;
        }
    };


    public void tick() {

    }

    public double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static boolean isColliding(Entity e1, Entity e2) {
        Rectangle rec1 = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
        Rectangle rec2 = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());

        if (rec1.intersects(rec2) && e1.z == e2.z) {
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

    public void updateCamera() {
        Camera.x = Camera.clamp(this.getX() - (PacmanGame.WIDTH / 2), 0, Map.WIDTH*16 - PacmanGame.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (PacmanGame.HEIGHT / 2), 0, Map.HEIGHT*16 - PacmanGame.HEIGHT);
    }

    public void render(Graphics g) {
        g.drawImage(sprite, getX() - Camera.x, getY() - Camera.y, getWidth(), getHeight(), null);
        //g.setColor(Color.red);
        //g.fillRect(getX() + getMaskX() - Camera.x, getY() + getMaskY() - Camera.y, getMaskW(), getMaskH());
    }

    public int getX() {
        return (int)x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int)y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return (int)width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getHeight() {
        return (int)height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getSpeed() {
        return (int)speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
