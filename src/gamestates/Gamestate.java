package gamestates;

/*defines all game states*/
public enum Gamestate {
    PLAYING, MENU, RESTART, QUIT;
    
    public static Gamestate state = MENU;
}
