package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.Game;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods {
    
    private Player player;
    
    /*managers*/
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    
    /*overlays during playing*/
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    
    /*states*/
    protected boolean paused = false;
    protected boolean gameOver;
    protected boolean levelCompleted = false;
    
    /*window size*/
    private int xLvlOffset, 
                leftBorder = (int) (0.2 * Game.GAME_WIDTH), 
                rightBorder = (int) (0.8 * Game.GAME_WIDTH),
                maxLvlOffsetX;
    
    /*background features*/
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
        
        calcLvlOffset ();
        loadStartLevel ();
    }
    
    public void loadNextLevel () {
        resetAll ();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
    }
    
    private void loadStartLevel () {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
    }
    
    private void calcLvlOffset () {
        maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
    }
    
    private void initClasses () {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player (200,200, (int)(64*Game.SCALE),(int)(40*Game.SCALE), this);
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());    
        player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
        
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    void windowFocusLost() {
        player.resetDirBooleans();
    }

    @Override
    public void update() {                       
        
        if (paused) {
            pauseOverlay.update();
        } else if (levelCompleted) {
            levelCompletedOverlay.update();
        } else if (!gameOver){
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkBorder();
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
        enemyManager.draw(g, xLvlOffset);
        
        if (paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_WIDTH);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (levelCompleted) {
            levelCompletedOverlay.draw(g);
        }        
    }
    
    public void drawClouds (Graphics g) {
        for (int i = 0; i < 3; i++) 
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int)(xLvlOffset * 0.3), (int) (204 * Game.SCALE),BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        
        for (int i = 0; i < smallCloudsPosition.length; i++)
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * i * 4 - (int)(xLvlOffset * 0.7), smallCloudsPosition[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
    }
    
    public void resetAll () {
        /*reset player, enemy, lvl, etc*/
        gameOver = false;
        paused = false;
        levelCompleted = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
    }
    
    public void setGameOver (boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    public void checkEnemyHit (Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /*1 left button, 2 middle button, 3 right button*/
        if (!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttack(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if (levelCompleted)
                levelCompletedOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {   
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if (levelCompleted)
                levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver){
            if (paused)
                pauseOverlay.mouseMoved(e);
            else if (levelCompleted)
                levelCompletedOverlay.mouseMoved(e);
        }
    }
    
    //@Override
    public void mouseDragged (MouseEvent e){
        if (!gameOver)
            if(paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.keyPressed(e);
        else
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
                case KeyEvent.VK_S:
                    levelManager.save.save();
                    System.out.println("GAME SAVE");
                    break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver)
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
    
    public void setMAXLvlOffset (int lvlOffset) {
        this.maxLvlOffsetX = lvlOffset;
    }
    
    public void unpauseGame () {
        paused = false;
    }

    public void setLevelCompleted (boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
    }
    
    public boolean isPaused() {
        return paused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isLevelCompleted() {
        return levelCompleted;
    }
    
    
    
}
