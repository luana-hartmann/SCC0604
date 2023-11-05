package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import utilz.LoadSave;
import static utilz.Constants.UI.buttons.*;

public class MenuButton {
    
    private int xPosition, yPosition, rowIndex, index;
    private int xOffsetCenter = B_WIDTH/2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public MenuButton(int xPosition, int yPosition, int rowIndex, Gamestate state) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }
    
    private void initBounds() {
        bounds = new Rectangle(xPosition - xOffsetCenter, yPosition, B_WIDTH, B_HEIGHT);        
    }
    
    public Rectangle getBounds() {
	return bounds;
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, 
                                       rowIndex * B_HEIGHT_DEFAULT, 
                                     B_WIDTH_DEFAULT,
                                     B_HEIGHT_DEFAULT);
        }
    }
    
    public void draw (Graphics g) {
        g.drawImage(imgs[index], 
                    xPosition - xOffsetCenter, 
                  yPosition, 
               B_WIDTH,
              B_HEIGHT,
             null);
    }
    
    public void update() {
        index = 0;
        if (mouseOver) index = 1;
        if (mousePressed) index = 2;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
    
    public void applyGamestate () {
        Gamestate.state = state;
    }
    
    public void resetBools () {
        mouseOver = false;
        mousePressed = false;
    }

}
