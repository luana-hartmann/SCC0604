package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import entities.Crabby;
import static utilz.HelpMethods.*;

public class Level {
    
    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private int lvlTilesWide, /*max value of the leveloffset*/
                maxTilesOffset, /*how many tiles we can actually see*/
                maxLvlOffsetX;
    private Point playerSpawn;
    
    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffset();
        calcPlayerSpawn();
    }
    
    private void calcPlayerSpawn () {
        playerSpawn = GetPlayerSpawn(img);
    }
    
    public int getSpriteIndex (int x, int y) {
        return lvlData[y][x];
    }
    
    public int[][] getLevelData () {
        return lvlData;
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void calcLvlOffset() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
    }
    
    public int getLvlOffset () {
        return maxLvlOffsetX;
    }
    
    public ArrayList<Crabby> getCrabs () {
        return crabs;
    }
    
    public Point getPlayerSpawn () {
        return playerSpawn;
    }
}
