package jdc.pacman.map;

public class Vector2i {

    private int x, y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object object) {
        Vector2i vec = (Vector2i) object;
        if (vec.x == this.x && vec.y == this.y) return true;
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
