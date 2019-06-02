package br.com.tinem.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import br.com.tinem.entities.*;
import br.com.tinem.main.Main;

public class World {

    public static Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 16;

    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            int[] pixels = new int[WIDTH * HEIGHT];
            tiles = new Tile[WIDTH * HEIGHT];

            map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);

            for (int xx = 0; xx < WIDTH; xx++) {
                for (int yy = 0; yy < HEIGHT; yy++) {

                    int pixelAtual = pixels[xx + (yy * WIDTH)];
                    int getPixelAtual = xx + (yy * WIDTH);

                    tiles[getPixelAtual] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

                    if (pixelAtual == 0xFF000000) {
                        // FLOOR
                        tiles[getPixelAtual] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

                    } else if (pixelAtual == 0xFFFFFFFF) {
                        // WALL
                        tiles[getPixelAtual] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    } else if (pixelAtual == 0xFF0015FF) {
                        // PLAYER
                        Main.player.setX(xx * 16);
                        Main.player.setY(yy * 16);
                    } else if (pixelAtual == 0xFFFF0010) {
                        // ENEMY
                        Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY);
                        Main.entities.add(en);
                        Main.enemies.add(en);
                    } else if (pixelAtual == 0xFF10FF00) {
                        // WEAPON
                        Main.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON));
                    } else if (pixelAtual == 0xFFFFFC54) {
                        // BULLET
                        Bullet bullet = new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET);
                        Main.entities.add(bullet);
                        Main.bullet.add(bullet);
                    } else if (pixelAtual == 0xFFFF00E1) {
                        // LIFEPACK
                        LifePack pack = new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK);
                        Main.entities.add(pack);
                        Main.lifePack.add(pack);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFree(int xNext, int yNext) {
        int x1 = xNext / TILE_SIZE;
        int y1 = yNext / TILE_SIZE;

        int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = yNext / TILE_SIZE;

        int x3 = xNext / TILE_SIZE;
        int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
                || (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
                || (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
                || (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
    }

    public void render(Graphics g) {
        int xStart = Camera.x >> 4;
        int yStart = Camera.y >> 4;

        int xFinal = xStart + (Main.WIDTH >> 4);
        int yFinal = yStart + (Main.HEIGHT >> 4);

        for (int xx = xStart; xx <= xFinal; xx++) {
            for (int yy = yStart; yy <= yFinal; yy++) {

                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
                    continue;
                }

                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }
}
