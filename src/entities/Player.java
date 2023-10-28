package entities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import main.Game;
import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import utilz.LoadSave;

public class Player extends Entity {
    
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 15;
    private int player_action = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float player_speed = 1.0f * Game.SCALE;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    
    /*gravity and jumping*/
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jump_speed = - 2.25f * Game.SCALE;
    private float fallSPeedCollision  = 0.05f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
	super(x, y, width, height);
	loadAnimations();
        initHitBox(x, y, (int)(20 * (Game.SCALE)),(int) (27 * Game.SCALE)); /*video 9*/
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
        
        if (inAir) {
            if(airSpeed < 0) player_action = JUMP;
            else player_action = FALL;
        }
       
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
        if (jump)
            jump();
        if (!left && !right && !inAir) return;
        
        float x_speed = 0;
        
        /*left and right*/
        if (left)
            x_speed -= player_speed;
        if (right)
            x_speed += player_speed;
        
        if (!inAir) {
            if(!EntityFloor(hitBox, lvlData))
                inAir = true;
        }
        
        if(inAir) {
            if (CanMoveHere(hitBox.x, hitBox.y+airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPosition(x_speed);
            } else {
                hitBox.y = GetPositionRoofFloor(hitBox, airSpeed);
                if (airSpeed > 0) {
                    /*check if hits the floor*/
                    resetInAir();
                } else {
                    airSpeed = fallSPeedCollision;
                }
                updateXPosition(x_speed);
            }
        } else {
            updateXPosition(x_speed);
        }
        moving = true;
    }
    
    private void jump () {
        if (inAir) return;
        
        inAir = true;
        airSpeed = jump_speed;
    }
    
    public void resetInAir () {
        inAir = false;
        airSpeed = 0;
    }
    
    public void updateXPosition (float x_speed) {
        if (CanMoveHere(hitBox.x+x_speed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += x_speed;
        } else {
            hitBox.x = GetEntityPositionWall (hitBox, x_speed);
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
        if(!EntityFloor(hitBox, lvlData)) inAir = true;
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
    
    public void setJump(boolean jump) {
        this.jump = jump;
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
