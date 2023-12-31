package levels;


import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import data.Save;
import main.Game;
import utilz.LoadSave;
import gamestates.Gamestate;

/*class that constructs the level*/
public class LevelManager {
    
    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Level> levels;
    public int lvlIndex = 0;
    
    /*saving*/
    public Save save = new Save(this);

    public LevelManager(Game game){
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildLevels();
        save.load();
    }
    
    public void loadNextLevel () {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("NO MORE LEVELS! GAME COMPLETED!");
            System.out.println("Criadores:\nLuana Hartmann Franco da Cruz\nJoao Pedro Gomes");
            Gamestate.state = Gamestate.MENU;
            save.save();
        } 
        else {
            System.out.println("NEXT LEVEL");
        }
        
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMAXLvlOffset(newLevel.getLvlOffset());
    }
    
    private void buildLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }
    
    public void draw (Graphics g, int lvlOffset) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i*Game.TILES_SIZE - lvlOffset, j*Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE,  null);
            }
        
    }
    
    public void update () {
        
    }
    
    public Level getCurrentLevel () {
        return levels.get(lvlIndex);
    }
    
    public int amountLevels () {
        return levels.size();
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
