package main;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;
import java.awt.Dimension;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel{
    
    private MouseInputs mouseInputs;
    private float xDelta = 0, yDelta = 0;
    private float xDir = 0.03f, yDir = 0.03f;
    private int frames = 0;
    private long lastCheck = 0;
    private Color color = new Color (150,20,90);
    private Random random;
    
    public GamePanel () {
        setPanelSize();
        
        random = new Random ();
        mouseInputs = new MouseInputs(this);
        
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    
    private void setPanelSize () {
        /*32 x 25 pixels*/
        Dimension size  = new Dimension(1280,800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }
    
    public void changeX (int value) {
        this.xDelta += value;
        //repaint();
    }
    
    public void changeY (int value) {
        this.yDelta += value;
        //repaint();
    }
    
    public void setRecPos (int x, int y) {
        this.yDelta = y;
        this.xDelta = x;
        //repaint();
    }
    
    /*draw function from JPanel*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        updateRec();
        g.setColor(color);
        g.fillRect((int)xDelta,(int)yDelta ,200,50);

    }
    
    private void updateRec () {
        xDelta+=xDir;
        if (xDelta > 400 || xDelta < 0) {
            xDir *= -1;
            color = getRandomColor();
        }
        
        yDelta+=yDir;
        if (yDelta > 400 || yDelta < 0) {
            yDir *= -1;
            color = getRandomColor();
        }
    }
    
    private Color getRandomColor () {
        int r = random.nextInt(255);
        int b = random.nextInt(255);
        int g = random.nextInt(255);
        
        return new Color (r,b,g);
    }
}
