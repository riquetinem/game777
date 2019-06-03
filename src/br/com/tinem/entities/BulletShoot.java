package br.com.tinem.entities;

import br.com.tinem.main.Main;
import br.com.tinem.world.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BulletShoot extends Entity{

    private int dx;
    private int dy;
    private double speed = 4;

    private int life = 20, curLife = 0;

    public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
        super(x, y, width, height, sprite);

        this.dx = dx;
        this.dy = dy;
    }


    public void tick(){
        x+=dx * speed;
        y+=dy * speed;

        curLife++;
        if (curLife == life){
            Main.bulletShoots.remove(this);
        }
    }

    public void render(Graphics g){
        g.setColor(Color.BLUE);
        g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
    }
}
