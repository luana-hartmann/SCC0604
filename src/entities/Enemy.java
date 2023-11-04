package entities;

import java.awt.geom.Rectangle2D;
import main.Game;
import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.*;

public abstract class Enemy extends Entity {
    
    protected int aniIndex, enemyState, enemyType;
    protected int aniTick, aniSpeed = 25;
    protected boolean firstUpdate = true, inAir = false;
    protected float fallSpeed, gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 0.4f * Game.SCALE;
    protected int walkDir = LEFT; 
    protected int tileY;
    protected int attackDist = Game.TILES_SIZE;
    protected int maxHealth, currentHealth;
    protected boolean active = true;
    protected boolean attackChecked;
    
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }
    
    protected void firstUpdateCheck(int[][] lvlData) {
        if (!EntityFloor(hitBox, lvlData))
                inAir = true;
            firstUpdate = false;
    }
    
    protected void updateInAir (int[][] lvlData) {
        if(CanMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitBox.y = GetPositionRoofFloor(hitBox, fallSpeed);
                tileY = (int) (hitBox.y / Game.TILES_SIZE);
            }
    }
    
    protected void move (int[][]lvlData) {
        float xSpeed = 0;
                    
        if (walkDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;
                    
        if (CanMoveHere(hitBox.x, hitBox.y, hitBox.width, hitBox.height, lvlData))
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
        this.enemyState = enemyState;
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
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
                
                switch (enemyState) {
                    case ATTACK, HIT -> enemyState = IDLE;
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
    
    public int getAniIndex() {
        return aniIndex;
    }
    
    public int getEnemyState () {
        return enemyState;
    }

    public boolean isActive() {
        return active;
    }
    
    
}
