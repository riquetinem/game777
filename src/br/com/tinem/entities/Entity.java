package br.com.tinem.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.tinem.main.Main;
import br.com.tinem.world.Camera;

public class Entity {

    public static BufferedImage LIFEPACK = Main.spritesheet.getSprite(6 * 16, 0, 16, 16);
    public static BufferedImage WEAPON = Main.spritesheet.getSprite(7 * 16, 0, 16, 16);
    public static BufferedImage BULLET = Main.spritesheet.getSprite(6 * 16, 16, 16, 16);
    public static BufferedImage ENEMY = Main.spritesheet.getSprite(7 * 16, 16, 16, 16);
    public static BufferedImage GUN_RIGHT = Main.spritesheet.getSprite(8 * 16, 0, 16, 16);
    public static BufferedImage GUN_LEFT = Main.spritesheet.getSprite(9 * 16, 0, 16, 16);

    protected double x;
    protected double y;
    protected int width;
    protected int height;

    private int maskX, maskY, mWidth, mHeight;

    private BufferedImage sprite;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.setHeight(height);
        this.setWidth(width);
        this.setY(y);
        this.setX(x);
        this.setSprite(sprite);

        this.setMask(0, 0, width, height);
    }

    public void render(Graphics g) {
        g.drawImage(this.getSprite(), this.getX() - Camera.x, this.getY() - Camera.y, null);
    }

    public static boolean isColling(Entity e1, Entity e2) {
        Rectangle e1Mask = new Rectangle(e1.getX() + e1.getMaskX(), e1.getY() + e1.getMaskY(), e1.getmWidth(),
                e1.getHeight());
        Rectangle e2Mask = new Rectangle(e2.getX() + e2.getMaskX(), e2.getY() + e2.getMaskY(), e2.getmWidth(),
                e2.getHeight());

        return e1Mask.intersects(e2Mask);
    }

    public void tick() {

    }

    public void setMask(int maskX, int maskY, int mWidth, int mHeight) {
        this.setMaskX(maskX);
        this.setMaskY(maskY);
        this.setmWidth(mWidth);
        this.setHeight(mHeight);
    }

    public int getX() {
        return (int) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
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

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getMaskX() {
        return maskX;
    }

    public void setMaskX(int maskX) {
        this.maskX = maskX;
    }

    public int getMaskY() {
        return maskY;
    }

    public void setMaskY(int maskY) {
        this.maskY = maskY;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }
}
