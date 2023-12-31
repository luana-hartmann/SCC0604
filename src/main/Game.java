package main;

import data.Save;
import java.awt.Graphics;

import gamestates.Menu;
import gamestates.Playing;
import gamestates.Gamestate;
import static gamestates.Gamestate.MENU;
import static gamestates.Gamestate.PLAYING;
import levels.LevelManager;

/*controls the game*/
public class Game implements Runnable {
    
    private GameWindow window;
    private GamePanel panel;
    private LevelManager levelManager = new LevelManager(this);
    private Save save = new Save(levelManager);
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
     
    private Playing playing;
    private Menu menu;
    
    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1f;
    
    /*visible size, not actual size of the level*/
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
     
    public Game () {
        initClasses();
        panel = new GamePanel (this);
        window = new GameWindow (panel);
        panel.setFocusable(true);
        panel.requestFocus();
        
        startGameLoop();   
    }
    
    private void initClasses () {
        menu = new Menu(this);
        playing = new Playing(this);
    }
    
    private void startGameLoop () {
        gameThread = new Thread (this);
        gameThread.start();
    }
    
    public void update () {
        
        switch(Gamestate.state) {
            case PLAYING:
                playing.update();
                break;
            case MENU:               
                menu.update();
                break;
            case RESTART:
                levelManager.lvlIndex = 0;
                levelManager.save.save();
                System.out.println("GAME RESTARTED. OPEN AGAIN.");
                Gamestate.state = MENU;
            case QUIT:
                System.out.println("QUIT");
            default:
                System.exit(0);
                break;
        }
    }
    
    public void render (Graphics g) {
        
        switch(Gamestate.state) {
            case PLAYING:
                if(playing.isPaused())
                    window.jframe.setTitle("Sorry :(  No music or sound effects available");
                else if (playing.isLevelCompleted())
                    window.jframe.setTitle("Congratulations! Level Cleared. On to the Next Challenge!");
                else if(playing.isGameOver())
                    window.jframe.setTitle("Oops! Game Over. Press ESC to restart the level!");
                else
                    window.jframe.setTitle("Defeat all the crabbies to proceed! Move A and D - jump SPACE - attack CLICK - pause ESC - save S");
                playing.draw(g);
                break;
            case MENU:
                window.jframe.setTitle("Platafform Game! Click PLAY or press ENTER to start!");
                menu.draw(g);
                break;
            default:
                break;
        }
        
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
    

    void windowFocusLost() {
        if(Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }
    
    public Menu getMenu () {
        return menu;
    }
    
    public Playing getPlaying () {
        return playing;
    }
}
