package jdc.pacman.map;

import jdc.pacman.PacmanGame;
import jdc.pacman.entities.Direction;
import jdc.pacman.entities.Dot;
import jdc.pacman.entities.Enemy;
import jdc.pacman.entities.EnemyType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Map {

    private static  Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 1;

    public Map(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResourceAsStream(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            tiles = new Tile[map.getWidth() * map.getHeight()];
            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    int pixel = pixels[xx + (yy * map.getWidth())];
                    tiles[xx + (yy * WIDTH)] = new Tile(xx * TILE_SIZE, yy * TILE_SIZE);

                    switch (pixel) {
                        case 0xFF000000:
                            tiles[xx + (yy * WIDTH)] = new Tile(xx * TILE_SIZE, yy * TILE_SIZE);
                            break;
                        case 0xFFFFFFFF:
                            tiles[xx + (yy * WIDTH)] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE);
                            break;
                        case 0xFF0000FF:
                            PacmanGame.player.setX(xx);
                            PacmanGame.player.setY(yy - 1);
                            break;
                        case 0XFFDFEC0C:
                            Dot dot = new Dot(xx, yy, 1, 1, PacmanGame.player);
                            PacmanGame.entities.add(dot);
                            break;
                        case 0XFFFF0000:
                            Enemy enemy = new Enemy(xx, yy, EnemyType.PURPLE);
                            PacmanGame.entities.add(enemy);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(Graphics g) {
        for (int xx = 0; xx <= WIDTH; xx++) {
            for (int yy = 0; yy <= HEIGHT; yy++) {
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
                    continue;
                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }

    public static boolean checkCollision(int xNext, int yNext, Rectangle rect, Direction direction) {
        double rectEdge;

        if (Direction.UP.equals(direction) || Direction.DOWN.equals(direction)) rectEdge = rect.getWidth();
        else rectEdge = rect.getHeight();

        for (int i = 0; i < rectEdge; i++) {
            int verifyX = 0;
            int verifyY = 0;

            switch (direction) {
                case UP:
                    verifyX = xNext + i;
                    verifyY = yNext;
                    break;
                case DOWN:
                    verifyX = xNext + i;
                    verifyY = yNext + (int)rect.getHeight();
                    break;
                case RIGHT:
                    verifyX = xNext + (int)rect.getWidth();
                    verifyY = yNext + i;
                    break;
                case LEFT:
                    verifyX = xNext;
                    verifyY = yNext + i;
                    break;
            }
            Tile tile = tiles[verifyX + (verifyY * WIDTH)];

            if (tile instanceof WallTile) {
                WallTile wallTile = (WallTile) tile;
                wallTile.setHasHit(true);
                return false;
            }
        }

        return true;
    }

    private static Tile findTileBasedOnObjectInfo(int x, int y, Direction currentDirection, Rectangle rect, int dimension)
            throws ArrayIndexOutOfBoundsException {

        int verifyX = 0;
        int verifyY = 0;

        for (int i = 0; i < dimension; i++) {
            switch (currentDirection) {
                case LEFT:
                    verifyX = x - i;
                    verifyY = y;
                    break;
                case RIGHT:
                    verifyX = x + i;
                    verifyY = y;
                    break;
                case UP:
                    verifyX = x;
                    verifyY = y - i;
                    break;
                case DOWN:
                    verifyX = x;
                    verifyY = y + i;
                    break;
            }

            Tile tile = tiles[verifyX + (verifyY * WIDTH)];
            if (tile instanceof WallTile) continue;

            if (!checkWallTileForNextTiles(x, y, currentDirection, rect, dimension)) return tile;
        }

        return null;
    }

    private static boolean checkWallTileForNextTiles(int x, int y, Direction currentDirection, Rectangle rect, int dimension) {
        int verifyXNextTiles = 0;
        int verifyYNextTiles = 0;
        boolean foundWallTile = false;

        double secondDimension = 0;
        if (dimension == WIDTH) secondDimension = rect.getWidth();
        else secondDimension = rect.getHeight();

        for (int j = 0; j < secondDimension; j++) {
            switch (currentDirection) {
                case LEFT:
                    verifyXNextTiles = x - j;
                    verifyYNextTiles = y;
                    break;
                case RIGHT:
                    verifyXNextTiles = x + j;
                    verifyYNextTiles = y;
                    break;
                case UP:
                    verifyXNextTiles = x;
                    verifyYNextTiles = y - j;
                    break;
                case DOWN:
                    verifyXNextTiles = x;
                    verifyYNextTiles = y + j;
                    break;
            }

            Tile checkTile = tiles[verifyXNextTiles + (verifyYNextTiles * WIDTH)];
            if (checkTile instanceof WallTile) {
                foundWallTile = true;
                break;
            }
        }

        System.out.println("Found wall tile: " + foundWallTile);

        return foundWallTile;
    }

    public static Tile getClosestOpenedTile(int x, int y, Rectangle rect, Direction direction, Direction currentDirection) {
        try {
            switch (direction) {
                case UP:
                    return findTileBasedOnObjectInfo(x, y, currentDirection, rect, WIDTH);
                case DOWN:
                    int startY = y + (int) rect.getHeight();
                    return findTileBasedOnObjectInfo(x, startY, currentDirection, rect, WIDTH);
                case LEFT:
                    return findTileBasedOnObjectInfo(x, y, currentDirection, rect, HEIGHT);
                case RIGHT:
                    int startX = x + (int)rect.getWidth();
                    return findTileBasedOnObjectInfo(startX, y, currentDirection, rect, HEIGHT);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

        return null;
    }

    public static Tile[] getTiles() {
        return tiles;
    }
}
