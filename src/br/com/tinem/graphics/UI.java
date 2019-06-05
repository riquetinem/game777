package br.com.tinem.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import br.com.tinem.entities.Player;
import br.com.tinem.main.Main;

public class UI {
    public void render(Graphics g) {
        // BARRA DE VIDA
        g.setColor(Color.red);
        g.fillRect(20, 4, 50, 8);
        g.setColor(Color.GREEN);
        g.fillRect(20, 4, (int) ((Main.player.life / Main.player.maxLife) * 50), 8);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 10));

        // SETANDO A VIDA DO PLAYER PARA 0 CASO A VIDA DELE FOR MENOR QUE 0
        if(Main.player.life < 0){
            Main.player.life = 0;
        }

        g.drawString((int) (Main.player.life) + "/" + (int) Main.player.maxLife, 25, 11);

        // MUNIÇÃO
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 8));
        g.drawString("Munição: " + Main.player.ammo, Main.WIDTH - 60, Main.HEIGHT - 10);

        // FPS
        // g.drawString("FPS:" + Main.fra, Main.WIDTH - 235, Main.HEIGHT - 5);
    }
}
