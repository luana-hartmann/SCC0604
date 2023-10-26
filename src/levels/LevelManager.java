package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import static main.Game.TILES_SIZE;
import utilz.LoadSave;

public class LevelManager {
    
    private Game game;
    private BufferedImage[] levelSprite;
    private Level lvlOne;

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        lvlOne = new Level(LoadSave.GetLevelData());
    }
    
    public void draw (Graphics g) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < Game.TILES_IN_WIDTH; i++) {
                int index = lvlOne.getSpriteIndex(i, j);
                /*image x y width height observer*/
                g.drawImage(levelSprite[index], i*Game.TILES_SIZE, j*Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE,  null);
            }
        
    }
    
    public void update () {
        
    }
    
    public Level getCurrentLevel () {
        return lvlOne;
    }

    private void importOutsideSprites() {
        
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32,32,32);
            }
    }
    
    
}
