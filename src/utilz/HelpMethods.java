package utilz;

import java.awt.geom.Rectangle2D;
import main.Game;

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
        
        /*checks if it's out of the game window*/
        if (x < 0 || x >= Game.GAME_WIDTH) return true;
        if (y < 0 || y >= Game.GAME_HEIGHT) return true;
        
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        
        int value = lvlData[(int) yIndex][(int) xIndex];
        
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
    
    public static boolean EntityFloor(Rectangle2D.Float hitBox,int[][] lvlData) {
        /*checks the pixel bellow bottomleft and bottomright*/
        if (!IsSolid(hitBox.x, hitBox.y+hitBox.height+1, lvlData))
            if (!IsSolid(hitBox.x+hitBox.width, hitBox.y+hitBox.height+1, lvlData))
                return false;
        return true;
    }
}
