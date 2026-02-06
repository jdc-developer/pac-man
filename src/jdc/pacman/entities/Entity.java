package jdc.pacman.entities;

import jdc.pacman.map.Node;
import jdc.pacman.map.Vector2i;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public abstract class Entity {

    protected double x;
    protected double y;
    protected float width;
    protected float height;
    protected float speed;
    protected Direction direction;

    protected float maskWidth;
    protected float maskHeight;
    protected double maskX;
    protected double maskY;

    protected int depth;

    protected List<Node> path;

    public Entity(double x, double y, float width, float height, float speed, float maskWidth, float maskHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.maskWidth = maskWidth;
        this.maskHeight = maskHeight;
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

    public abstract void render(Graphics2D g);

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

    public int getMaskWidth() {
        return (int)maskWidth;
    }

    public void setMaskWidth(float maskWidth) {
        this.maskWidth = maskWidth;
    }

    public int getMaskHeight() {
        return (int)maskHeight;
    }

    public void setMaskHeight(float maskHeight) {
        this.maskHeight = maskHeight;
    }

    public int getMaskX() {
        return (int)maskX;
    }

    public void setMaskX(double maskX) {
        this.maskX = maskX;
    }

    public int getMaskY() {
        return (int)maskY;
    }

    public void setMaskY(double maskY) {
        this.maskY = maskY;
    }
}
