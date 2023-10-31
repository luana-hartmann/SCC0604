package gamestates;

import entities.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import levels.LevelManager;
import main.Game;
import static main.Game.SCALE;
import ui.PauseOverlay;
import static utilz.Constants.Environment.*;
import utilz.LoadSave;

public class Playing extends State implements Statemethods {
    
    private Player player;
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;
    
    private int xLvlOffset, 
                leftBorder = (int) (0.2 * Game.GAME_WIDTH), 
                rightBorder = (int) (0.8 * Game.GAME_WIDTH),
                lvlTilesWide = LoadSave.GetLevelData()[0].length, /*max value of the leveloffset*/
                maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH, /*how many tiles we can actually see*/
                maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE; /*turning to pixels*/
    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPosition; /*random y values for small clouds*/
    private Random rnd = new Random();

    public Playing(Game game) {
        super(game);
        initClasses ();
        
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        
        smallCloudsPosition = new int[8];
        for (int i = 0; i < smallCloudsPosition.length; i++) 
            /*random value between 90 and 100 for small clouds y position*/
            smallCloudsPosition[i] = (int)(90 * Game.SCALE) + rnd.nextInt((int)(100 * Game.SCALE) );
    }
    
    private void initClasses () {
        levelManager = new LevelManager(game);
        // player = new Player (200,200, (int)(64*SCALE),(int)(64*SCALE));
        player = new Player (200,200, (int)(64*Game.SCALE),(int)(40*Game.SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());    
        pauseOverlay = new PauseOverlay(this);
    }
    
    public Player getPlayer() {
        return player;
    }

    void windowFocusLost() {
        player.resetDirBooleans();
    }

    @Override
    public void update() {
        
        if (!paused) {
            levelManager.update();
            player.update();
            checkBorder();
        } else {
            pauseOverlay.update();
        }
    }
    
    private void checkBorder (){
        int playerX = (int) player.getHitBox().x,
            diff = playerX - xLvlOffset;
        
        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;
        
        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(g);
        
        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        
        if (paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_WIDTH);
            pauseOverlay.draw(g);
        }
            
    }
    
    public void drawClouds (Graphics g) {
        for (int i = 0; i < 3; i++) 
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int)(xLvlOffset * 0.3), (int) (204 * Game.SCALE),BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        
        for (int i = 0; i < smallCloudsPosition.length; i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * i * 4 - (int)(xLvlOffset * 0.7), smallCloudsPosition[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /*1 left button, 2 middle button, 3 right button*/
        if (e.getButton() == MouseEvent.BUTTON1)
            player.setAttack(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused)
            pauseOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseMoved(e);
    }
    
    //@Override
    public void mouseDragged (MouseEvent e){
        if(paused)
            pauseOverlay.mouseDragged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_BACK_SPACE:
                Gamestate.state = Gamestate.MENU;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }
    
    public void unpauseGame () {
        //System.out.println("unpausedGame");
        paused = false;
    }
}
