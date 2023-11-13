package entities;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import main.Game;
import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

/*public class for the player*/
public class Player extends Entity {
    
    /*game parameters*/
    private Playing playing;
    private int[][] lvlData;
    
    /*state parameters*/
    private BufferedImage[][] animations;    
    private boolean moving = false, attacking = false;
    private boolean left, right, jump;   
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    
    /*gravity and jumping*/
    private float jump_speed = - 2.25f * Game.SCALE;
    private float fallSPeedCollision  = 0.05f * Game.SCALE;
    
    /*status bar ui*/
    private BufferedImage statusBarImg;
    
    /*position and size of the status bar*/
    private int statusBar_width = (int) (192 * Game.SCALE);
    private int statusBar_height = (int) (58 * Game.SCALE);
    private int statusBar_x = (int) (10 * Game.SCALE);
    private int statusBar_y = (int) (10 * Game.SCALE);
    
    /*position and size of the actual bar*/
    private int healthBar_width = (int) (150 * Game.SCALE);
    private int healthBar_height = (int) (4 * Game.SCALE);
    private int healthBar_x = (int) (34 * Game.SCALE);
    private int healthBar_y = (int) (14 * Game.SCALE);
    
    /*manage the health bar*/
    private int health_width = healthBar_width;
    
    /*manage the direction of the sprite*/
    private int flipX = 0, flipW = 1;
    
    /*manage the attack*/
    private boolean attackChecked = false;
    
 
    public Player(float x, float y, int width, int height, Playing playing) {
	super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = 1.0f * Game.SCALE;
	loadAnimations();
        initHitBox(20,27);
        initAttackBox ();
    }
    
    public void setSpawn (Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitBox.x = x;
        hitBox.y = y;
    }
    
    private void initAttackBox () {
        attackBox = new Rectangle2D.Float(x, y,(int) (20 * Game.SCALE), (int) (20 * Game.SCALE)); 
    }
    
    public void update() {
        updateHealthBar();
        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }      
        updateAttackBox();
        updatePosition();
        if (attacking) checkAttack();
        updateAnimationTick();
        setAnimation();
    }
    
    private void checkAttack () {
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }
    
    private void updateAttackBox () {
        if(right) {
            attackBox.x = hitBox.x + hitBox.width + (int)(10 * Game.SCALE);
        } else if (left) {
            attackBox.x = hitBox.x - hitBox.width - (int)(10 * Game.SCALE);
        }
        attackBox.y = hitBox.y + (int)(10 * Game.SCALE);
    }
    
    private void updateHealthBar() {
        health_width = (int) ((currentHealth / (float)maxHealth) * healthBar_width);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[state][aniIndex], 
                    (int) (hitBox.x - xDrawOffset) - lvlOffset + flipX, 
                    (int) (hitBox.y - yDrawOffset), 
                    width * flipW, 
                    height, null);
        //drawHitBox(g, lvlOffset);
        //drawAttackBox(g, lvlOffset);
        drawUI(g);
    }
    
    private void drawUI (Graphics g) {
        g.drawImage(statusBarImg, statusBar_x, statusBar_y, statusBar_width, statusBar_height, null);
        g.setColor(Color.red);
        g.fillRect(healthBar_x + statusBar_x, healthBar_y + statusBar_y, health_width, healthBar_height);
    }
     
    private void updateAnimationTick () {
        
        aniTick++;
        if(aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)){
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }                              
        }        
    }
    
    public void setAnimation () {
        
        int startAni = state;
        
        if (moving) state = RUNNING;
        else state = IDLE;
        
        if (inAir) {
            if(airSpeed < 0) state = JUMP;
            else state = FALL;
        }
       
        if (attacking) {
            state = ATTACK;
            if (startAni != ATTACK) {
                aniIndex = 1;
                aniTick = 0;
            }
        }
        
        if (startAni != state)
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
        if(!inAir)
            if((!left && !right) || (left && right))
                return;
        
        float x_speed = 0;
        
        /*left and right*/
        if (left) {
            x_speed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right){            
            x_speed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }
        
        if (!inAir) {
            if(!EntityFloor(hitBox, lvlData))
                inAir = true;
        }
        
        if(inAir) {
            if (CanMoveHere(hitBox.x, hitBox.y+airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
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
    
    public void changeHealth(int value) {
        currentHealth += value;
        
        if (currentHealth <=  0) {
            currentHealth = 0;
            //gameOver();
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }
    
    private void loadAnimations () {	
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[7][8];        
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i*64, j*40,64,40);
        
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }
    
    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if(!EntityFloor(hitBox, lvlData)) inAir = true;
    }

    /*avoid bug when switching web browser*/
    public void resetDirBooleans () {
        left = false;
        right = false;
    }

    /*getters and setters of directions*/    
    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
    
    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    } 
    
    /*attacking*/   
    public void setAttack (boolean attacking) {
        this.attacking = attacking;       
    }
    
    public void resetAll() {
        resetDirBooleans ();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;
        
        hitBox.x = x;
        hitBox.y = y;
        
        if(!EntityFloor(hitBox, lvlData)) inAir = true;
        
    }  
}
