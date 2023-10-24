package inputs;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import main.GamePanel;

public class KeyboardInputs implements KeyListener{
    private GamePanel gamePanel;
    
    public KeyboardInputs (GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
              
    @Override
    public void keyTyped (KeyEvent e) {
        //
    }
            
    @Override
    public void keyReleased (KeyEvent e) {
        //
    }
            
    @Override
    public void keyPressed (KeyEvent e) {
        
        switch(e.getKeyCode()) {
            case KeyEvent.VK_W:
                gamePanel.changeY(-5);
            break;
            case KeyEvent.VK_A:
                gamePanel.changeX(-5);
            break;
            case KeyEvent.VK_S:
                gamePanel.changeY(5);
            break;
            case KeyEvent.VK_D:
                gamePanel.changeX(5);
            break;
        }
    }
}