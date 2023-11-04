package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import main.Game;
import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.EntityFloor;
import static utilz.HelpMethods.GetPositionRoofFloor;
import static utilz.HelpMethods.IsFloor;

public class Crabby extends Enemy {
    
    /*attack box for crabby*/
    private Rectangle2D.Float attackBox;
    private int attackBoxOffset;
    
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y,(int) (22 * Game.SCALE), (int)(19 * Game.SCALE));
        initAttackBox();
    }
    
    public void initAttackBox () {
        attackBox = new Rectangle2D.Float(x, y, (int)(82 * Game.SCALE), (int)(19 * Game.SCALE));
        attackBoxOffset = (int)(30 * Game.SCALE);
    }
    
    public void update (int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick ();
        updateAttackBox();
    }
    
    private void updateAttackBox () {
        attackBox.x = hitBox.x - attackBoxOffset;
        attackBox.y = hitBox.y;
    }
    
    /*calculate how the enemy is gonna be able to patrol and move around*/
    public void updateBehavior (int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            updateInAir (lvlData);
        } else {
            switch(enemyState) {
                case IDLE:
                    newState (RUNNING);
                    break;
                case RUNNING:                   
                    if (seePlayer(lvlData, player)) {
                        towardsPlayer(player);
                        if(closeForAttack(player))
                            newState(ATTACK);
                    }                  
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0) 
                        attackChecked = false;
                    
                    if (aniIndex == 3 && !attackChecked)
                        checkEnemyHit(attackBox, player);
                    break;
                case HIT:
                     break;
            }
        }  
    }
    
    public void drawAttackBox (Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int)(attackBox.x - xLvlOffset), (int)attackBox.y, (int)attackBox.width,(int) attackBox.height);
    }
    
    public int flipX () {
        if(walkDir == RIGHT)
            return width;
        else
            return 0;
    }
    
    public int flipW () {
        if(walkDir == RIGHT)
            return -1;
        else
            return 1;
    }
}
