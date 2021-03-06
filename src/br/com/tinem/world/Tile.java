package br.com.tinem.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.tinem.main.Main;

public class Tile {
    public static BufferedImage TILE_FLOOR = Main.spritesheet.getSprite(0, 0, 16, 16);
    public static BufferedImage TILE_WALL = Main.spritesheet.getSprite(16, 0, 16, 16);

    private BufferedImage sprite;
    private int x, y;

    public Tile(int x, int y, BufferedImage sprite) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
    }
}
