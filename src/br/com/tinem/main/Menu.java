package br.com.tinem.main;

import br.com.tinem.world.World;

import java.awt.*;
import java.io.*;

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

    public static boolean pause = false;

    public static boolean saveExists = false;
    public static boolean saveGame = false;

    public boolean menuOrInit = (options[currentOption] == "Novo jogo") || (options[currentOption] == "Continuar");

    public void tick() {
        File file = new File("save.txt");

        if(file.exists()){
            saveExists = true;
        }else{
            saveExists = false;
        }

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
            if (options[currentOption] == "Sair") {
                Main.fecharJogo = true;
            } else if(options[currentOption] == "Carregar Jogo"){
                file = new File("save.txt");

                if(file.exists()){
                    String save = loadGame(10);
                    applySave(save);
                }
            }else if (menuOrInit) {
                Main.gameState = "NORMAL";

                file = new File("save.txt");
                file.delete();
            }
        }
    }

    public void render(Graphics g) {
        // SETAR FUNDO DO MENU
        if (!menuOn) {
            g.setColor(Color.black);
            g.fillRect(0, 0, (Main.WIDTH * Main.SCALE), (Main.HEIGHT * Main.SCALE));
        } else {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, (Main.WIDTH * Main.SCALE), (Main.HEIGHT * Main.SCALE));

        }

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

            if (menuOn) {
                novoJogo = ">" + "Continuar";
                options[0] = "Continuar";
            } else {
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

    public static void saveGame(String[] val1, int[] val2, int encode) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("save.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < val1.length; i++) {
            String current = val1[i];
            current += ":";
            char[] value = Integer.toString(val2[i]).toCharArray();

            for (int n = 0; n < value.length; n++) {
                value[n] += encode;
                current += value[n];
            }

            try {
                writer.write(current);
                if (i < val1.length - 1) {
                    writer.newLine();
                }
            } catch (IOException e) {

            }
        }
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {

        }
    }

    public static String loadGame(int encode) {
        String line = "";
        File file = new File("save.txt");

        if (file.exists()) {
            try {
                String singleLine = null;
                BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
                try {
                    while ((singleLine = reader.readLine()) != null) {
                        String[] trans = singleLine.split(":");
                        char[] val = trans[1].toCharArray();
                        trans[1] = "";

                        for (int i = 0; i < val.length; i++) {
                            val[i] -= encode;
                            trans[1] += val[i];
                        }

                        line += trans[0];
                        line += ":";
                        line += trans[1];
                        line += "/";
                    }
                } catch (IOException e) {

                }
            } catch (FileNotFoundException e) {

            }
        }

        return line;
    }

    public static void applySave(String str) {
        String[] spl = str.split("/");

        for (int i = 0; i < spl.length; i++) {
            String[] spl2 = spl[i].split(":");
            switch (spl2[0]){
                case "level":
                    World.restartGame("level" + spl2[1] + ".png");
                    Main.gameState = "NORMAL";
                    pause = false;
                    break;
            }
        }
    }
}
