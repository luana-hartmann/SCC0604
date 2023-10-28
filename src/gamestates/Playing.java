package gamestates;

import entities.Player;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import levels.LevelManager;
import main.Game;
import static main.Game.SCALE;

public class Playing extends State implements Statemethods {
    
    private Player player;
    private LevelManager levelManager;

    public Playing(Game game) {
        super(game);
        initClasses ();
    }
    
    private void initClasses () {
        levelManager = new LevelManager(game);
        // player = new Player (200,200, (int)(64*SCALE),(int)(64*SCALE));
        player = new Player (200,200, (int)(64*Game.SCALE),(int)(40*Game.SCALE));
        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());     
    }
    
    public Player getPlayer() {
        return player;
    }

    void windowFocusLost() {
        player.resetDirBooleans();
    }

    @Override
    public void update() {
        levelManager.update();
        player.update();
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        player.render(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /*1 left button, 2 middle button, 3 right button*/
        if (e.getButton() == MouseEvent.BUTTON1)
            player.setAttack(true);
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
    public void mouseMoved(MouseEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_BACK_SPACE:
                Gamestate.state = Gamestate.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }
    
}
