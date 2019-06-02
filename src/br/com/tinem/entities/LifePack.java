package br.com.tinem.entities;

import java.awt.image.BufferedImage;

public class LifePack extends Entity{
    public final static int VIDA = 10;

    public LifePack(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);
    }
}
