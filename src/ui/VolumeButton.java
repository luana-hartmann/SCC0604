package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.VolumeButton.*;
import utilz.LoadSave;

public class VolumeButton extends PauseButton {
    
    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0, buttonX, minX, maxX;
    private boolean mouseOver, mousePressed;
    
    public VolumeButton(int x, int y, int width, int height) {
        super(x + width/2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH/2;
        buttonX = x + width/2;
        this.x = x;
        this.width = width;
        minX = x;
        maxX = x + width;
        loadImages();
        
    }
    
    public void loadImages () {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) 
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        
        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }
    
    public void update () {
        index = 0;
        if(mouseOver) index = 1;
        if(mousePressed) index = 2;
    }
    
    public void draw(Graphics g) {
        
        g.drawImage(slider, x+10, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH/2, y, VOLUME_WIDTH, height, null);
        
    }
    
    public void changeX (int x) {
        if (x < minX) buttonX = minX;
        else if (x > maxX) buttonX = maxX;
        else buttonX = x;
        
        bounds.x = buttonX - VOLUME_WIDTH/2;
    }
    
    public void resetBools () {
        mouseOver = false;
        mousePressed = false;
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
}
