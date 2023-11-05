package inputs;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import main.GamePanel;
import gamestates.Gamestate;
import static gamestates.Gamestate.MENU;
import static gamestates.Gamestate.PLAYING;

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
        switch(Gamestate.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            default:
                break;   
        }
    }
            
    @Override
    public void keyPressed (KeyEvent e) {
        switch(Gamestate.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            default:
                break;   
        }
    }
}
