package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener{
    
    private GamePanel gamePanel;
    public MouseInputs (GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        /*1 left button, 2 middle button, 3 right button*/
        if (e.getButton() == MouseEvent.BUTTON1)
            gamePanel.getGame().getPlayer().setAttack(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //
    }

    @Override
    public void mouseEntered(MouseEvent e) {
       //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
        
}
