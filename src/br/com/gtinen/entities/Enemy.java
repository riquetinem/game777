package br.com.gtinen.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.gtinen.main.Game;
import br.com.gtinen.world.Camera;
import br.com.gtinen.world.World;

public class Enemy extends Entity {

	private double speed = 1;

	private static int dano = 2, danoCritico = 5;

	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;

	private BufferedImage[] sprites;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(112 + 16, 16, 16, 16);
	}

	public void tick() {
		if (isCollidingWithPlayer() == false) {
			if (x < Game.player.getX() && World.isFree((int) (x + speed), this.getY())
					&& !isColliding((int) (x + speed), this.getY())) {
				x += speed;
			} else if (x > Game.player.getX() && World.isFree((int) (x - speed), this.getY())
					&& !isColliding((int) (x - speed), this.getY())) {
				x -= speed;
			}

			if (y < Game.player.getY() && World.isFree(this.getX(), (int) ((int) y + speed))
					&& !isColliding(this.getX(), (int) y + speed)) {
				y += speed;
			} else if (y > Game.player.getY() && World.isFree(this.getX(), (int) ((int) y - speed))
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

			if (Game.rand.nextInt(100) < 10) {
				Game.player.life = Game.player.life - this.dano;
			} else if (Game.rand.nextInt(100) < 2) {
				Game.player.life = Game.player.life - this.danoCritico;
			}

			System.out.println(Game.player.life);

			if (Game.player.life <= 0) {
				// GAME OVER
				System.exit(1);
			}
		}
	}

	public void render(Graphics g) {
		g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}

	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX(), this.getY(), World.TILE_SIZE, World.TILE_SIZE);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);

		return enemyCurrent.intersects(player);
	}

	public boolean isColliding(double xNext, double yNext) {
		Rectangle enemyCurrent = new Rectangle((int) xNext, (int) yNext, World.TILE_SIZE, World.TILE_SIZE);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
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
