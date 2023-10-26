package entities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import main.Game;
import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import utilz.LoadSave;

public class Player extends Entity {
    
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int player_action = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down;
    private float player_speed = 2.0f;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    public Player(float x, float y, int width, int height) {
	super(x, y, width, height);
	loadAnimations();
        initHitBox(x, y, 20 * (Game.SCALE), 28 * Game.SCALE); /*video 9*/
    }
    
    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(animations[player_action][aniIndex], (int) (hitBox.x - xDrawOffset), (int) (hitBox.y - yDrawOffset), width, height, null);
        drawHitBox(g);
    }
     
    private void updateAnimationTick () {
        
        aniTick++;
        if(aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(player_action)){
                aniIndex = 0;
                attacking = false;
            }                              
        }        
    }
    
    public void setAnimation () {
        
        int startAni = player_action;
        
        if (moving) player_action = RUNNING;
        else player_action = IDLE;
       
        if (attacking) player_action = ATTACK_1;
        
        if (startAni != player_action)
            resetAniTIck();
    }
    
    private void resetAniTIck() {
       aniTick = 0;
       aniIndex = 0;
    }
    
    private void updatePosition () {
        
        /*not moving*/
        moving = false;
        if (!left && !right && !up && !down) return;
        
        float x_speed = 0, y_speed = 0;
        
        /*left and right*/
        if (left && !right)
            x_speed = -player_speed;
        else if (right && !left)
            x_speed = player_speed;
     
        /*up and down*/
        if (up && !down)
            y_speed = -player_speed;
        else if (down && !up)
            y_speed = player_speed;
 /*       
        if (CanMoveHere(x+x_speed, y+y_speed, width, height, lvlData)) {
            this.x += x_speed;
            this.y += y_speed;
            moving = true;
        }
 */
        if (CanMoveHere(hitBox.x+x_speed, hitBox.y+y_speed, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += x_speed;
            hitBox.y += y_speed;
            moving = true;
        }
        
    }
    
    private void loadAnimations () {	
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[9][6];        
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                // animations[j][i] = img.getSubimage(i*64, j*40,56,72);
                animations[j][i] = img.getSubimage(i*64, j*40,64,40);
    }
    
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
    }

    /*avoid bug when switching web browser*/
    public void resetDirBooleans () {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    /*getter and setter of directions*/    
    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isDown() {
        return down;
    }  
    
    /*attacking*/   
    public void setAttack (boolean attacking) {
        this.attacking = attacking;       
    }

    
}
