package main;

import entities.Player;
import java.awt.Graphics;
import levels.LevelManager;
import main.GameWindow;
import main.GamePanel;

public class Game implements Runnable {
    
    private GameWindow window;
    private GamePanel panel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    
    
    private Player player;
    private LevelManager levelManager;
    
    public Game () {
        initClasses();
        panel = new GamePanel (this);
        window = new GameWindow (panel);
        
        panel.requestFocus();
        
        startGameLoop();   
    }
    
    private void initClasses () {
        player = new Player (200,200, (int)(64*SCALE),(int)(40*SCALE));
        levelManager = new LevelManager(this);
    }
    
    private void startGameLoop () {
        gameThread = new Thread (this);
        gameThread.start();
    }
    
    public void update () {
        player.update();
        levelManager.update();
    }
    
    public void render (Graphics g) {
        player.render(g);
        levelManager.draw(g);
    }
    
    @Override
    public void run () {
        
        /*duration each frame should last*/
        double timePerFrame = 1000000000.0 / FPS_SET;      
        /*duration of each update to avoid lagg*/
        double timePerUpdate = 1000000000.0 / UPS_SET; 
        
        long previousTime = System.nanoTime();
           
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        
        double deltaU = 0;
        double deltaF = 0;
        
        while (true) {                     
            long currentTime = System.nanoTime();
            
            deltaU += (currentTime - previousTime)/timePerUpdate;
            deltaF += (currentTime - previousTime)/timePerFrame;
            previousTime = currentTime;
            
            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }
            
            if (deltaF >= 1) {
                panel.repaint();
                frames++;
                deltaF--;
            }
            
            /*checks the FPS of the game*/           
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: "+frames+" | UPDATES: "+updates);
                frames=0;
                updates=0;
            }
            
        }
    }
    
    public Player getPlayer() {
        return player;
    }

    void windowFocusLost() {
        player.resetDirBooleans();
    }
}
