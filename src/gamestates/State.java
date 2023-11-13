package gamestates;

import java.awt.event.MouseEvent;

import main.Game;
import ui.MenuButton;

/*class to help manage the change of states*/
public class State {
    
    protected Game game;
    
    public State (Game game) {
        this.game = game;
    }
    public boolean isIn (MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }
    
    public Game getGagme () {
        return game;
    }
    
}
