package main;

import main.GameWindow;

public class Game {
    private GameWindow window;
    private GamePanel panel;
    public Game () {
        panel = new GamePanel ();
        window = new GameWindow (panel);
            
    }
}
