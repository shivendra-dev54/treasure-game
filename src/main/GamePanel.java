package main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class GamePanel extends Canvas implements Runnable {

    // resolution parameters
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;

    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // collision checker
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);

    // FPS
    int FPS = 60;

    // tilemanager
    TileManager tileM = new TileManager(this);

    // UI
    public UI ui = new UI(this);

    // Thread and KeyHandler
    KeyHandler keyH = new KeyHandler();
    public Thread gameThread;

    // SOUND
    Sound music = new Sound();
    Sound se = new Sound();

    // Entity and Object
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];

    // constructor
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(keyH);
    }

    public void setupGame() {
        aSetter.setObject();
        playMusic(0);
    }

    // for initializing game thread
    public void startGameThread() {
        createBufferStrategy(3);
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // Defensive: ensure BufferStrategy exists (created after setVisible)
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            int tries = 0;
            while (bs == null && tries++ < 10) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignored) {
                }
                bs = getBufferStrategy();
            }
            if (bs == null) {
                System.err.println("BufferStrategy still null.");
                return;
            }
        }

        final double drawInterval = 1_000_000_000.0 / FPS; // ns per frame
        double delta = 0.0;
        long lastTime = System.nanoTime();

        // Tiny warm-up: let the JVM/IDE finish scheduling & classloading
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }

        // Safety cap: max fixed updates to process in one loop iteration
        final int MAX_UPDATES_PER_FRAME = 5; // prevents spiral-of-death / startup spikes

        while (gameThread != null) {
            long now = System.nanoTime();
            // accumulate fraction of frames passed
            delta += (now - lastTime) / drawInterval;
            lastTime = now;

            // If delta is huge (e.g., app was suspended or started late), clamp it
            if (delta > MAX_UPDATES_PER_FRAME) {
                delta = MAX_UPDATES_PER_FRAME;
            }

            // Run up to MAX_UPDATES_PER_FRAME updates to catch up, but not more
            int updates = 0;
            while (delta >= 1.0 && updates < MAX_UPDATES_PER_FRAME) {
                update();
                delta -= 1.0;
                updates++;
            }

            // Render once per loop
            render(bs);

            // Adaptive sleep/yield to reduce CPU usage
            long elapsedAfter = System.nanoTime();
            long timeTaken = elapsedAfter - now;
            long timeUntilNextNs = (long) drawInterval - timeTaken;

            if (timeUntilNextNs > 2_000_000L) { // >2 ms
                try {
                    Thread.sleep(timeUntilNextNs / 1_000_000L - 1, (int) (timeUntilNextNs % 1_000_000L));
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            } else if (timeUntilNextNs > 0) {
                // very short wait; yield to be responsive
                Thread.yield();
            } else {
                // behind schedule — yield to let OS do work
                Thread.yield();
            }
        }
    }

    // UPDATE
    public void update() {
        player.update();
    }

    private void render(BufferStrategy bs) {
        Graphics g = null;

        try {
            g = bs.getDrawGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, screenWidth, screenHeight);

            Graphics2D g2 = (Graphics2D) g;

            // TILE MAP rendering
            tileM.draw(g2);

            // OBJECT rendering
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }

            player.draw(g2);

            ui.draw(g2);

        } finally {
            if (g != null) g.dispose();
        }

        // Swap buffers
        if (!bs.contentsLost()) {
            bs.show();
            Toolkit.getDefaultToolkit().sync(); // optional but good for smoothness
        }
    }

    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){
        music.stop();
    }

    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
