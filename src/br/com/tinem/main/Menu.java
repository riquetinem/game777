package br.com.tinem.main;

import java.awt.*;

public class Menu {

    public static String[] options = {
            "Novo jogo",
            "Carregar Jogo",
            "Sair"
    };

    private String novoJogo = options[0];
    private String carregarJogo = options[1];
    private String sairJogo = options[2];

    public static boolean menuOn = false;

    public int currentOption = 0;
    public int maxOption = options.length - 1;

    public boolean up, down, enter;

    public boolean menuOrInit = (options[currentOption] == "Novo jogo") || (options[currentOption] == "Continuar");

    public void tick() {
        if (up) {
            up = false;
            currentOption--;
            if (currentOption < 0) {
                currentOption = maxOption;
            }
        }

        if (down) {
            down = false;
            currentOption++;
            if (currentOption > maxOption) {
                currentOption = 0;
            }
        }

        if (enter) {
            enter = false;
            if (menuOrInit) {
                Main.gameState = "NORMAL";
            } else if (options[currentOption] == "Sair") {
                Main.fecharJogo = true;
            }
        }
    }

    public void render(Graphics g) {
        // SETAR FUNDO DO MENU
        g.setColor(Color.black);
        g.fillRect(0, 0, (Main.WIDTH * Main.SCALE), (Main.HEIGHT * Main.SCALE));

        // SETAR NOME DO GAME
        g.setColor(Color.GREEN);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString(">MACONHA<", (Main.WIDTH * Main.SCALE) / 2 - 110, (Main.HEIGHT * Main.SCALE) / 2 - 160);

        // SETAR OPÇÕES DO MENU
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD, 24));
        g.drawString(novoJogo, (Main.WIDTH * Main.SCALE) / 2 - 50, 160);
        g.drawString(carregarJogo, (Main.WIDTH * Main.SCALE) / 2 - 70, 200);
        g.drawString(sairJogo, (Main.WIDTH * Main.SCALE) / 2 - 10, 240);

        if ((options[currentOption] == "Novo jogo") || (options[currentOption] == "Continuar")) {

            if(menuOn) {
                novoJogo = ">" + "Continuar";
                options[0] = "Continuar";
            }else{
                novoJogo = ">" + options[0];
            }

            carregarJogo = options[1];
            sairJogo = options[2];

        } else if (options[currentOption] == "Carregar Jogo") {
            novoJogo = options[0];
            carregarJogo = ">" + options[1];
            sairJogo = options[2];
        } else {
            novoJogo = options[0];
            carregarJogo = options[1];
            sairJogo = ">" + options[2];
        }
    }

}
