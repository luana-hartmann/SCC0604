package entities;

import java.awt.geom.Rectangle2D;

import main.Game;
import static utilz.Constants.*;
import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

/*abstract class for all the enemies in the game*/
public abstract class Enemy extends Entity {
    
    /*enemy type parameters*/
    protected int enemyType;
    
    /*check if the game has just started*/
    protected boolean firstUpdate = true;
    
    /*check if the enemy is alive*/
    protected boolean active = true;
 
    /*walking*/
    protected int walkDir = LEFT; 
    protected int tileY;
    
    /*attacking*/ 
    protected int attackDist = Game.TILES_SIZE;
    protected boolean attackChecked;
    
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = 0.4f * Game.SCALE;
    }
    
    protected void firstUpdateCheck(int[][] lvlData) {
        if (!EntityFloor(hitBox, lvlData))
                inAir = true;
            firstUpdate = false;
    }
    
    protected void updateInAir (int[][] lvlData) {
        if(CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
            } else {
                inAir = false;
                hitBox.y = GetPositionRoofFloor(hitBox, airSpeed);
                tileY = (int) (hitBox.y / Game.TILES_SIZE);
            }
    }
    
    protected void move (int[][]lvlData) {
        float xSpeed = 0;
                    
        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;
                    
        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData))
            if (IsFloor(hitBox, xSpeed, lvlData)) {
                hitBox.x += xSpeed;
                return;
            }
                    
        changeWalkDir();
    }
    
    protected void towardsPlayer (Player player) {
        if (player.hitBox.x > hitBox.x)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }
    
    protected boolean seePlayer(int[][]lvlData, Player player) {
        /*get y position and turn into the current tile y*/
        int playerTileY = (int) player.getHitBox().y / Game.TILES_SIZE;
        if (playerTileY == tileY)
            if (playerInRange(player))
                if (sightClear(lvlData, hitBox, player.hitBox, tileY))
                    return true;
        return false;
    }
    
    private boolean playerInRange (Player player) {
        /*absolute value between the player and the enemy*/
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDist * 5;
    }
    
    protected boolean closeForAttack (Player player) {
        /*absolute value between the player and the enemy*/
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDist;
    }
    
    protected void newState (int enemyState) {
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }
    
    public void hurt (int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEAD);
        else
            newState(HIT);
    }
    
    protected void checkEnemyHit (Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.hitBox))
            player.changeHealth(-GetEnemyDamage(enemyType));
        attackChecked = true;
    }
    
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                aniIndex = 0;
                
                switch (state) {
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }               
            }
        }
    }
    
    protected void changeWalkDir () {
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }
    
    public void resetEnemy () {
        hitBox.x = x;
        hitBox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }
    
    public boolean isActive() {
        return active;
    }
    
}
