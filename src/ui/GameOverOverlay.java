package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import main.Game;
import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.image.BufferedImage;
import utilz.LoadSave;

public class GameOverOverlay {
    
    private Playing playing;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        initImg();
    }
    
    private void initImg () {
        img = LoadSave.GetSpriteAtlas(LoadSave.DEAD_BACKGROUND);
        bgW = (int) (img.getWidth() * Game.SCALE);
        bgH = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH/2 - bgW/2;
        bgY = (int) (100 * Game.SCALE);       
    }
    
    public void draw (Graphics g) {
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        
        g.setColor(Color.white);
        g.drawImage(img, bgX, bgY, bgW, bgH, null);
    }
    
    public void keyPressed (KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
    
}
