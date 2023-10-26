package main;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;

public class GamePanel extends JPanel{
    
    private MouseInputs mouseInputs;
    private float xDelta = 0, yDelta = 0;
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int player_action = IDLE;
    private int player_direction =  -1;
    private boolean moving = false;
    
    private int frames = 0;
    private long lastCheck = 0;
    
    public GamePanel () {
        setPanelSize();
 
        importImg(); 
        loadAnimations();
        
        mouseInputs = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    
    private void loadAnimations () {
        animations = new BufferedImage[9][6];
        
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i*64, j*40,64,40);
            
        
    }
    
    private void importImg () {
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");

	try {	
            img = ImageIO.read(is);
	} 
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
    }
    
    private void setPanelSize () {
        /*32 x 25 pixels*/
        Dimension size  = new Dimension(1280,800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }
    
    public void setDirection (int direction) {
        this.player_direction = direction;
        moving = true;
    }
    
    public void setMoving (boolean moving) {
        this.moving = moving;
    }
    private void updateAnimationTick () {
        
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(player_action))
                aniIndex = 0;
                
        }
        
    }
    
    public void setAnimation () {
        if (moving) player_action = RUNNING;
        else player_action = IDLE;
    }
    
    private void updatePosition () {
        if (moving) {
            switch (player_direction) {
                case LEFT:
                    xDelta -= 5;
                    break;
                case UP:
                    yDelta -= 5;
                    break;
                case RIGHT:
                    xDelta += 5;
                    break;
                case DOWN:
                    yDelta += 5;
                    break;
            }
        }
    }
    
    /*draw function from JPanel*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        updateAnimationTick();
        setAnimation ();
        updatePosition();
	g.drawImage(animations[player_action][aniIndex], (int) xDelta, (int) yDelta, 256, 160, null);   
    } 
    
    
}
