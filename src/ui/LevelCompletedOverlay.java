package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.Color;
import utilz.LoadSave;
import static utilz.Constants.UI.UrmButtons.*;

public class LevelCompletedOverlay {
    
    Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButton();
    }
    
    private void initImg(){
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
        bgW = (int) (img.getWidth() * Game.SCALE);
        bgH = (int) (img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH/2 - bgW/2;
        bgY = (int) (75 * Game.SCALE);        
    }
    
    private void initButton () {
        int menuX = (int)(330 * Game.SCALE);
        int nextX = (int)(445 * Game.SCALE);
        int Y = (int)(195 * Game.SCALE);
        
        menu = new UrmButton(menuX, Y, URM_SIZE, URM_SIZE, 2);
        next = new UrmButton(nextX, Y, URM_SIZE, URM_SIZE, 0);       
    }
    
    public void update () {
        next.update();
	menu.update();
    }
    
    public void draw (Graphics g) {
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_WIDTH);
        g.drawImage(img, bgX, bgY, bgW, bgH, null);

        next.draw(g);
        menu.draw(g);
    }
    
    private boolean isIn (MouseEvent e, UrmButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
    
    public void mouseMoved (MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);
        
        if (isIn(e, next))
            next.setMouseOver(true);
        else if (isIn(e, menu))
            menu.setMouseOver(true);
    }
    
    public void mouseReleased (MouseEvent e) {
        if (isIn(e, next)) {
            if(next.isMousePressed()){
                playing.loadNextLevel();
            }           
        } else if (isIn(e, menu)) {
            if(menu.isMousePressed()) {
                playing.resetAll();
                Gamestate.state = Gamestate.MENU;
            }
        }
        
        next.resetBools();
        menu.resetBools();
    }
    
    public void mousePressed (MouseEvent e) {
        if (isIn(e, next))
            next.setMousePressed(true);
        else if (isIn(e, menu))
            menu.setMousePressed(true);
    }
}
