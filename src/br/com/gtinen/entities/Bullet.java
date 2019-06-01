package br.com.gtinen.entities;

import java.awt.image.BufferedImage;

public class Bullet extends Entity {

	public static final int AMMO = 6;

	public Bullet(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

}
