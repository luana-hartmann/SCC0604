package main;

import javax.swing.JPanel;
import java.awt.Graphics;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel{
    
    private MouseInputs mouseInputs;
    private int xDelta = 0, yDelta = 0;
    
    public GamePanel () {
        addKeyListener(new KeyboardInputs(this));
        
        mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    
    public void changeX (int value) {
        this.xDelta += value;
        repaint();
    }
    
    public void changeY (int value) {
        this.yDelta += value;
        repaint();
    }
    
    public void setRecPos (int x, int y) {
        this.yDelta = y;
        this.xDelta = x;
        repaint();
    }
    
    /*draw function from JPanel*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.fillRect(xDelta,yDelta ,200,50);
    }
}
