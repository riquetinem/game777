package br.com.gtinen.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import br.com.gtinen.entities.Player;
import br.com.gtinen.main.Game;

public class UI {
	public void render(Graphics g) {
		// BARRA DE VIDA
		g.setColor(Color.red);
		g.fillRect(20, 4, 50, 8);
		g.setColor(Color.GREEN);
		g.fillRect(20, 4, (int) ((Player.life / Player.maxLife) * 50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString((int) (Player.life) + "/" + (int) Player.maxLife, 25, 11);
		
		// MUNIÇÃO
		g.setColor(Color.BLUE);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString("Munição: " + Player.ammo, Game.WIDTH - 60, Game.HEIGHT - 10);
	}
}
