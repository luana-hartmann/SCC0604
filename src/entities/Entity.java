package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    
    /*private means only this class can use the variable*/
    /*protected means only classes that extends this abstract can use this variable*/
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitBox;

    public Entity(float x, float y, int width, int height) {
	this.x = x;
	this.y = y;
        this.width = width;
	this.height = height;
    }

    protected void drawHitBox (Graphics g) {
        /*debugging the hitBox*/
        g.setColor(Color.PINK);
        g.drawRect((int)hitBox.x, (int)hitBox.y, (int)hitBox.width, (int)hitBox.height);
    }
    
    protected void initHitBox(float x, float y, int width, int height) {
        hitBox = new Rectangle2D.Float (x, y, width, height);
    }
    
/*
    protected void updateHitBox (){
        hitBox.x = (int)x;
        hitBox.y = (int)y;
    }
*/
    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }
    
    
}
