package br.com.gtinen.entities;

import java.awt.image.BufferedImage;

public class LifePack extends Entity{
	
	public static final int VIDA = 10;

	public LifePack(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}

}
