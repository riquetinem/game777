package br.com.gtinen.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.gtinen.main.Game;
import br.com.gtinen.world.Camera;

public class Entity {
	
	public static BufferedImage LIFEPACK = Game.spritesheet.getSprite(6 * 16, 0, 16, 16);
	public static BufferedImage WEAPON = Game.spritesheet.getSprite(7 * 16, 0, 16, 16);
	public static BufferedImage BULLET = Game.spritesheet.getSprite(6 * 16, 16, 16, 16);
	public static BufferedImage ENEMY = Game.spritesheet.getSprite(7 * 16, 16, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.setHeight(height);
		this.setWidth(width);
		this.setY(y);
		this.setX(x);
		this.setSprite(sprite);;
	}
	
	public void render(Graphics g) {
		g.drawImage(this.getSprite(), this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
	public void tick() {
		
	}
	
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
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}
	
	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

}
