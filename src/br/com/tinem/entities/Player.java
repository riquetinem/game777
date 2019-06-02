package br.com.tinem.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.tinem.main.Main;
import br.com.tinem.world.Camera;
import br.com.tinem.world.World;

public class Player extends Entity {
    private boolean moved = false;

    public boolean right, up, left, down;
    private int right_dir = 0, left_dir = 1;
    private int dir = right_dir;
    private double speed = 1.5;

    private boolean hasGun = false;
    public static int ammo = 0;

    public boolean isDamaged = false;
    private int framesDamage = 0;

    private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;

    private BufferedImage playerDamage;

    public double life = 100, maxLife = 100;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        playerDamage = Main.spritesheet.getSprite(0, World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);

        for (int i = 0; i < 4; i++) {
            rightPlayer[i] = Main.spritesheet.getSprite(32 + (i * World.TILE_SIZE), 0, World.TILE_SIZE,
                    World.TILE_SIZE);
        }

        for (int i = 0; i < 4; i++) {
            leftPlayer[i] = Main.spritesheet.getSprite(32 + (i * World.TILE_SIZE), World.TILE_SIZE, World.TILE_SIZE,
                    World.TILE_SIZE);
        }
    }

    public void tick() {
        moved = false;
        if (right && World.isFree((int) (x + speed), this.getY())) {
            moved = true;
            dir = right_dir;
            x = x + speed;
        } else if (left && World.isFree((int) (x - speed), this.getY())) {
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
        this.checkCollisionWeapon();

        if(isDamaged){
            this.framesDamage++;
            if(this.framesDamage == 8){
                this.framesDamage = 0;
                isDamaged = false;
            }
        }

        Camera.x = Camera.clamp(this.getX() - (Main.WIDTH / 2), 0, World.WIDTH * World.TILE_SIZE - Main.WIDTH);
        Camera.y = Camera.clamp(this.getY() - (Main.HEIGHT / 2), 0, World.HEIGHT * World.TILE_SIZE - Main.HEIGHT);
    }

    private void checkCollisionBullet() {
        for (int i = 0; i < Main.bullet.size(); i++) {
            Bullet bullet = Main.bullet.get(i);

            if (isColling(this, bullet)) {
                ammo = ammo + Bullet.AMMO;

                Main.bullet.remove(bullet);
                Main.entities.remove(bullet);
            }
        }
    }

    private void checkCollisionWeapon() {
        for(int i = 0; i < Main.entities.size(); i++){
            Entity arma = Main.entities.get(i);

            if (arma instanceof Weapon){
                if(Entity.isColling(this, arma)){
                    hasGun = true;
                    Main.entities.remove(arma);
                }
            }
        }
    }

    private void checkCollisionLifePack() {
        for (int i = 0; i < Main.lifePack.size(); i++) {
            LifePack pack = Main.lifePack.get(i);

            if (life < 100) {

                if (isColling(this, pack)) {
                    life = life + LifePack.VIDA;

                    if (life > 100) {
                        life = 100;
                    }

                    Main.lifePack.remove(pack);
                    Main.entities.remove(pack);
                }
            }
        }
    }

    public void render(Graphics g) {
        if(!isDamaged){
            if (dir == right_dir) {
                g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun){
                    // arma para direita
                    g.drawImage(Entity.GUN_RIGHT, this.getX() - Camera.x + 12, this.getY() - Camera.y, null);
                }
            } else if (dir == left_dir) {
                g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
                if(hasGun){
                    //  arma para esquerda
                    g.drawImage(Entity.GUN_LEFT, this.getX() - Camera.x - 11, this.getY() - Camera.y, null);
                }
            }
        }else{
            g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }

    }

}
