package jdc.pacman;

import jdc.pacman.commands.KeyboardCommands;
import jdc.pacman.entities.Entity;
import jdc.pacman.entities.Player;
import jdc.pacman.graphics.Spritesheet;
import jdc.pacman.graphics.UI;
import jdc.pacman.map.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PacmanGame extends Canvas implements Runnable {

    public static Random random = new Random();

    public boolean isRunning;
    public Thread thread;
    private static JFrame frame;
    public static final int WIDTH = 200;
    public static final int HEIGHT = 240;
    public static final int SCALE = 3;

    private BufferedImage image;

    public static java.util.List<Entity> entities;
    public static Spritesheet spritesheet;
    public static Player player;
    public static Map world;
    public UI ui;
    public KeyboardCommands keyboardCommands = new KeyboardCommands();

    public PacmanGame() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();

        spritesheet = new Spritesheet("/spritesheet.png");
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        ui = new UI();
        player = new Player(2, 2);
        keyboardCommands.setPlayer(player);
        addKeyListener(keyboardCommands);
        world = new Map("/level-1.png");
        entities = new ArrayList<>();

        entities.add(player);
    }

    public void initFrame() {
        frame = new JFrame("Pacman");
        frame.add(this);
        //frame.setUndecorated(true);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        PacmanGame game = new PacmanGame();
        game.start();
    }

    public synchronized void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void tick() {
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(19, 19, 19));
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        //RENDER GAME
        world.render(g2d);
        Collections.sort(entities, Entity.depthComparator);
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g2d);
        }

        ui.render(g2d);
        g2d.dispose();
        g2d = (Graphics2D) bs.getDrawGraphics();

        g2d.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta =  0;

        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();

        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }

        stop();
    }
}