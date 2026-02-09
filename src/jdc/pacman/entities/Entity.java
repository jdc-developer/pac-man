package jdc.pacman.entities;

import java.awt.*;
import java.util.Comparator;

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
    protected Rectangle rect;

    protected int frames = 0, maxFrames = 5, animationIndex = 0, maxAnimationIndex = 2;
    protected int depth;
    protected boolean isAnimated;

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
        if (isAnimated) {
            frames++;

            if (frames == maxFrames) {
                frames = 0;
                animationIndex++;

                if (animationIndex > maxAnimationIndex) animationIndex = 0;
            }
        }
    };
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

    public int getHeight() {
        return (int)height;
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


    public int getMaskHeight() {
        return (int)maskHeight;
    }

    public int getMaskX() {
        return (int)maskX;
    }

    public int getMaskY() {
        return (int)maskY;
    }
}
