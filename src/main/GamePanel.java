package main;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel{
    
    private MouseInputs mouseInputs;
    private Game game;
    
    public GamePanel (Game game) {
        setPanelSize();
        this.game = game;
        
        mouseInputs = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    
    private void setPanelSize () {
        /*32 x 25 pixels*/
        Dimension size  = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);

        //System.out.println("panel size: " + GAME_WIDTH+ " x " + GAME_HEIGHT);
    }
    
    public void updateGame () {

    }
    
    /*draw function from JPanel*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        game.render(g);

    } 
     

    public Game getGame() {
        return game;
    }
}
