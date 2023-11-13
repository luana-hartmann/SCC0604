package utilz;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import entities.Crabby;
import static utilz.Constants.EnemyConstants.CRABBY;

/*methods that are really commom through the game implementation*/
public class HelpMethods {
    
    public static boolean CanMoveHere (float x, float y, float width, float height, int[][] lvlData) {
        
        /*checks if the corners of the hitBox are in a solid space*/
        if(!IsSolid(x, y, lvlData))
            if(!IsSolid(x+width, y+height, lvlData))
                if(!IsSolid(x+width, y, lvlData))
                    if(!IsSolid(x, y+height, lvlData))
                        return true;
        
        return false;
    }
    
    private static boolean IsSolid (float x, float y, int[][]  lvlData) {
        
        /*get the width of the full level*/
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        
        /*checks if it's out of the game window*/
        if (x < 0 || x >= maxWidth) return true;
        if (y < 0 || y >= Game.GAME_HEIGHT) return true;
        
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        
        return tileSolid((int) xIndex, (int)yIndex, lvlData);
    }
    
    public static boolean tileSolid (int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[(int) yTile][(int) xTile];
        
        /*checks if it's the scenario*/
        if (value >= 48 || value < 0 || value != 11) return true;
        
        return false;
    }
    
    public static float GetEntityPositionWall(Rectangle2D.Float hitBox, float x_speed) {
        
        int currentTile = (int)(hitBox.x / Game.TILES_SIZE);
        
        if (x_speed > 0) {
            //right
            int tileXPosition = currentTile * Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitBox.width);           
            return tileXPosition + xOffset - 1;
        } else {
            //left
            return (currentTile * Game.TILES_SIZE);
        }
    }
    
    public static float GetPositionRoofFloor (Rectangle2D.Float hitBox, float airSpeed) {
        
        int currentTile = (int)(hitBox.y / Game.TILES_SIZE);
        
        if(airSpeed > 0) {
            /*falling or touching floor*/
            int tileYPosition = currentTile * Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitBox.height);           
            return tileYPosition + yOffset - 1;
        } else {
            /*jumping*/
             return (currentTile * Game.TILES_SIZE);
        }
    }
    
    public static boolean EntityFloor(Rectangle2D.Float hitBox, int[][] lvlData) {
        /*checks the pixel bellow bottomleft and bottomright*/
        if (!IsSolid(hitBox.x, hitBox.y+hitBox.height+1, lvlData))
            if (!IsSolid(hitBox.x+hitBox.width, hitBox.y+hitBox.height+1, lvlData))
                return false;
        return true;
    }
    
    public static boolean IsFloor (Rectangle2D.Float hitBox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0)
            return IsSolid(hitBox.x + xSpeed + hitBox.width, hitBox.y + hitBox.height + 1, lvlData); 
        else
            return IsSolid(hitBox.x + xSpeed, hitBox.y + hitBox.height + 1, lvlData);       
    }
    
    public static boolean sightClear_aux (int xSmaller, int xBigger, int y, int[][] lvlData) {
        for (int i = 0; i < (xBigger - xSmaller); i++) {
            if (tileSolid  (xSmaller + i, y, lvlData))
                return false;
            /*checking underneath*/
            if (!tileSolid(xSmaller + i, y+1, lvlData))
                return false;
        }
                
        return true;
    }
    
    public static boolean sightClear (int[][] lvlData, Rectangle2D.Float hitBox_1, Rectangle2D.Float hitBox_2, int tileY) {
        int xTile_1 = (int) (hitBox_1.x/Game.TILES_SIZE);
        int xTile_2 = (int) (hitBox_2.x/Game.TILES_SIZE);
        
        if (xTile_1 > xTile_2)
            return sightClear_aux(xTile_2, xTile_1, tileY, lvlData);
        else
            return sightClear_aux(xTile_1, xTile_2, tileY, lvlData);
    }
    
    public static int[][] GetLevelData (BufferedImage img) {
        int [][]lvlData = new int[img.getHeight()][img.getWidth()]; 
        
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48)
                    value = 0;
                lvlData[j][i] = color.getRed();
            }
        return lvlData;
    }
    
    public static ArrayList<Crabby> GetCrabs (BufferedImage img) {
        ArrayList<Crabby> list = new ArrayList<>();
        
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == CRABBY)
                    list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        return list;
    }
    
    public static Point GetPlayerSpawn (BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100)
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
            }
        return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
    }
}
