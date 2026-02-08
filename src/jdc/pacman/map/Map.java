package jdc.pacman.map;

import jdc.pacman.PacmanGame;
import jdc.pacman.entities.Direction;
import jdc.pacman.entities.Dot;
import jdc.pacman.entities.Player;

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
                    tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE);

                    switch (pixel) {
                        case 0xFF000000:
                            //Chão
                            tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE);
                            break;
                        case 0xFFFFFFFF:
                            //Parede
                            tiles[xx + (yy * WIDTH)] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE);
                            break;
                        case 0xFF0000FF:
                            //Player
                            PacmanGame.player.setX(xx);
                            PacmanGame.player.setY(yy);
                            break;
                        case 0XFFDFEC0C:
                            Dot dot = new Dot(xx, yy, 1, 1, PacmanGame.player);
                            PacmanGame.entities.add(dot);
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

    public static boolean checkCollision(int xNext, int yNext, Rectangle playerRect, Direction direction) {
        double playerRectEdge;

        if (Direction.UP.equals(direction) || Direction.DOWN.equals(direction)) playerRectEdge = playerRect.getWidth();
        else playerRectEdge = playerRect.getHeight();

        for (int i = 0; i < playerRectEdge; i++) {
            int verifyX = 0;
            int verifyY = 0;

            switch (direction) {
                case UP:
                    verifyX = (xNext - 1) + i;
                    verifyY = yNext;
                    break;
                case DOWN:
                    verifyX = (xNext - 1) + i;
                    verifyY = (yNext + 1 + (int)playerRect.getHeight());
                    break;
                case RIGHT:
                    verifyX = (xNext + (int)playerRect.getWidth() - 1);
                    verifyY = yNext + i;
                    break;
                case LEFT:
                    verifyX = xNext - 2;
                    verifyY = yNext + i;
                    break;
            }
            Tile tile = tiles[verifyX + (verifyY * WIDTH)];

            if (tile instanceof WallTile) {
                tile.setHasHit(true);
                return false;
            }
        }

        return true;
    }

    public static Tile[] getTiles() {
        return tiles;
    }
}
