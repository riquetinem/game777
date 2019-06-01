package br.com.gtinen.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.gtinen.main.Game;
import br.com.gtinen.world.Camera;
import br.com.gtinen.world.World;

public class Player extends Entity {
	private boolean moved = false;

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.5;

	public static int ammo = 0;

	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;

	public static double life = 100, maxLife = 100;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * World.TILE_SIZE), 0, World.TILE_SIZE,
					World.TILE_SIZE);
		}

		for (int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * World.TILE_SIZE), World.TILE_SIZE, World.TILE_SIZE,
					World.TILE_SIZE);
		}
	}

	public void tick() {
		moved = false;
		if (right && World.isFree((int) (x + speed), (int) this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;
		} else if (left && World.isFree((int) (x - speed), (int) this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;
		}

		if (up && World.isFree((int) x, (int) (y - speed))) {
			moved = true;
			y -= speed;
		} else if (down && World.isFree((int) x, (int) (y + speed))) {
			moved = true;
			y += speed;
		}

		if (moved) {
			frames++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}

		}

		this.checkCollisionLifePack();
		this.checkCollisionBullet();

		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void checkCollisionBullet() {
		for (int i = 0; i < Game.bullet.size(); i++) {
			Bullet bullet = Game.bullet.get(i);

			if (super.isColling(this, bullet)) {
				this.ammo = this.ammo + Bullet.AMMO;

				Game.bullet.remove(bullet);
				Game.entities.remove(bullet);
			}

		}
	}

	public void checkCollisionLifePack() {
		for (int i = 0; i < Game.lifePack.size(); i++) {
			LifePack pack = Game.lifePack.get(i);

			if (super.isColling(this, pack)) {
				this.life = this.life + LifePack.VIDA;

				if (life > 100) {
					life = 100;
				}

				Game.lifePack.remove(pack);
				Game.entities.remove(pack);
			}

		}
	}

	public void render(Graphics g) {
		if (dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if (dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

}
