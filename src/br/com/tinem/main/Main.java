package br.com.tinem.main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import br.com.tinem.entities.*;
import br.com.tinem.graphics.Spritesheet;
import br.com.tinem.graphics.UI;
import br.com.tinem.world.World;

public class Main extends Canvas implements Runnable, KeyListener {

    private static final long serialVersionUID = 1L;
    private static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public final static int WIDTH = 240;
    public final static int HEIGHT = 160;
    public final static int SCALE = 3;

    private int CUR_LEVEL = 1;

    private BufferedImage image;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<LifePack> lifePack;
    public static List<Bullet> bullet;
    public static List<BulletShoot> bulletShoots;

    public static Spritesheet spritesheet;
    public static World world;
    public static Player player;
    public static UI ui;
    public Menu menu;

    public static Random rand;

    public static String gameState = "MENU";

    private boolean showMessageGameOver = true;
    private int framesGameOver = 0;
    private boolean showMessageWin = true;
    private int framesWin = 0;

    private boolean restartGame = false;
    public static boolean fecharJogo = false;
    public static boolean saveGame = false;

    public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelart.ttf");
    public Font newFont;

    public static int[] pixels;
    public static int[] lightMapPixels;
    public  BufferedImage lightmap;

    public Main() {
        rand = new Random();
        addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        // Inicializando objetos
        ui = new UI();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        try {
            lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
        lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels, 0, lightmap.getWidth());

        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        lifePack = new ArrayList<LifePack>();
        bullet = new ArrayList<Bullet>();
        bulletShoots = new ArrayList<BulletShoot>();


        spritesheet = new Spritesheet("/spritesheet.png");
        player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
        entities.add(player);
        world = new World("/level1.png");

        try {
            newFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(16f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        menu = new Menu();

    }

    public void initFrame() {
        frame = new JFrame("M A C O N H A");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main Main = new Main();
        Main.start();
    }

    public static void applyLight(){
        for (int xx = 0; xx < WIDTH; xx++){
            for (int yy = 0; yy < HEIGHT; yy++){
                int posicao = xx + (yy * Main.WIDTH);
                if(lightMapPixels[posicao] == 0xffffffff){
                    pixels[posicao] = 0;
                }
            }
        }
    }

    public void tick() {

        if (gameState.equals("NORMAL")) {
            if (saveGame) {
                saveGame = false;
                String[] opt1 = {
                        "level"
                };

                int[] opt2 = {
                        this.CUR_LEVEL
                };

                Menu.saveGame(opt1, opt2, 10);
                System.out.println("Jogo salvo");
            }

            this.restartGame = false;
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                if (e instanceof Player) {
                    // é um jogador
                    e.tick();
                } else if (e instanceof Enemy) {
                    // é um inimigo
                    e.tick();
                }
            }

            for (int i = 0; i < bulletShoots.size(); i++) {
                bulletShoots.get(i).tick();
            }

            if (enemies.size() == 0) {
                // AVANÇAR PROXIMA FASE

                CUR_LEVEL++;
                int MAX_LEVEL = 2;
                if (CUR_LEVEL > MAX_LEVEL) {
                    CUR_LEVEL = 1;
                }

                if (gameState != "WIN") {
                    String newWorld = "level" + CUR_LEVEL + ".png";
                    World.restartGame(newWorld);
                }

            }
        } else if (gameState.equals("FAIL")) {
            this.framesGameOver++;
            if (this.framesGameOver == 30) {
                this.framesGameOver = 0;
                if (this.showMessageGameOver) {
                    this.showMessageGameOver = false;
                } else {
                    this.showMessageGameOver = true;
                }
            }

            if (restartGame) {
                this.restartGame = false;
                this.gameState = "NORMAL";
                CUR_LEVEL = 1;
                String newWorld = "level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }
        } else if (gameState.equals("MENU")) {
            menu.tick();
        }

        if (fecharJogo) {
            System.exit(1);
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();

        if (bs == null) {
            // lidando com graficos e performace
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = image.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Render map
        world.render(g);

        // Render entidades
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g);
        }

        for (int i = 0; i < bulletShoots.size(); i++) {
            bulletShoots.get(i).render(g);
        }

        // applyLight();
        ui.render(g);

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

        if (gameState.equals("FAIL")) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
            g.setFont(new Font("arial", Font.BOLD, 36));
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", (WIDTH * SCALE) / 2 - 100, (HEIGHT * SCALE) / 2);
            g.setFont(new Font("arial", Font.BOLD, 20));
            if (showMessageGameOver) {
                g.drawString("Press START to restart", (WIDTH * SCALE) / 2 - 100, (HEIGHT * SCALE) / 2 + 40);
            }
        } else if (gameState.equals("MENU")) {
            menu.render(g);
        }

        bs.show();
    }

    @Override
    public void run() {
        // Sound.music.loop();

        // pega o tempo do computador em nano segundo (mais preciso)
        long lastTime = System.nanoTime();
        // define o fps do Main
        double amountOfTicks = 60.0;
        // verifica o momento para realizar o update do Game
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta = 0;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }

        stop();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            // BOTAO DIREITO
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            // BOTAO ESQUERDO
            player.left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            // BOTAO DE CIMA
            player.up = true;

            if (gameState.equals("MENU")) {
                menu.up = true;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            // BOTAO DE BAIXO
            player.down = true;

            if (gameState.equals("MENU")) {
                menu.down = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_Z) {
            player.shoot = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            if (gameState.equals("MENU")) {
                menu.enter = true;
            } else if (gameState.equals("FAIL")) {
                this.restartGame = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Menu.menuOn = true;
            gameState = "MENU";
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            this.saveGame = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            // DIREITA
            player.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            // ESQUERDA
            player.left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            // CIMA
            player.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            // BAIXO
            player.down = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
