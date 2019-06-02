package br.com.tinem.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import br.com.tinem.graphics.Spritesheet;
import br.com.tinem.main.Main;
import br.com.tinem.world.*;

public class Enemy extends Entity {

    private double speed = 1;

    private int dano = 2, danoCritico = 5;

    private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;

    private BufferedImage[] sprites;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        sprites = new BufferedImage[2];
        sprites[0] = Main.spritesheet.getSprite(112, World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
        sprites[1] = Main.spritesheet.getSprite(112 + World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
    }

    public void tick() {
        if (!isCollidingWithPlayer()) {
            if (x < Main.player.getX() && World.isFree((int) (x + speed), this.getY())
                    && !isColliding((int) (x + speed), this.getY())) {
                x += speed;
            } else if (x > Main.player.getX() && World.isFree((int) (x - speed), this.getY())
                    && !isColliding((int) (x - speed), this.getY())) {
                x -= speed;
            }

            if (y < Main.player.getY() && World.isFree(this.getX(), (int) ((int) y + speed))
                    && !isColliding(this.getX(), (int) y + speed)) {
                y += speed;
            } else if (y > Main.player.getY() && World.isFree(this.getX(), (int) ((int) y - speed))
                    && !isColliding(this.getX(), (int) y - speed)) {
                y -= speed;
            }

            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }
        } else {

            if (Main.rand.nextInt(100) < 10) {
                Main.player.life = Main.player.life - dano;
                Main.player.isDamaged = true;
            } else if (Main.rand.nextInt(100) < 2) {
                Main.player.life = Main.player.life - danoCritico;
            }

            if (Main.player.life <= 0) {
                // GAME OVER
                Main.entities.clear();
                Main.enemies.clear();
                Main.lifePack.clear();
                Main.bullet.clear();
                Main.entities = new ArrayList<Entity>();
                Main.enemies = new ArrayList<Enemy>();
                Main.lifePack = new ArrayList<LifePack>();
                Main.bullet = new ArrayList<Bullet>();
                Main.spritesheet = new Spritesheet("/spritesheet.png");
                Main.player = new Player(0, 0, 16, 16, Main.spritesheet.getSprite(32, 0, 16, 16));
                Main.entities.add(Main.player);
                Main.world = new World("/map.png");
            }
        }
    }

    public void render(Graphics g) {
        g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
    }

    public boolean isCollidingWithPlayer() {
        Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE);
        Rectangle player = new Rectangle(Main.player.getX(), Main.player.getY(), 16, 16);

        return enemyCurrent.intersects(player);
    }

    public boolean isColliding(double xNext, double yNext) {
        Rectangle enemyCurrent = new Rectangle((int) xNext, (int) yNext, World.TILE_SIZE, World.TILE_SIZE);
        for (int i = 0; i < Main.enemies.size(); i++) {
            Enemy e = Main.enemies.get(i);
            if (e == this) {
                continue;
            }

            Rectangle targetEnemy = new Rectangle(e.getX(), e.getY(), World.TILE_SIZE, World.TILE_SIZE);
            if (enemyCurrent.intersects(targetEnemy)) {
                return true;
            }
        }

        return false;
    }
}
