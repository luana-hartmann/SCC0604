package entities;

import main.Game;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.EntityFloor;
import static utilz.HelpMethods.GetPositionRoofFloor;
import static utilz.HelpMethods.IsFloor;

public class Crabby extends Enemy {
    
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y,(int) (22 * Game.SCALE), (int)(19 * Game.SCALE));
    }
    
    public void update (int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick ();
    }
    
    /*calculate how the enemy is gonna be able to patrol and move around*/
    public void updateMove (int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            updateInAir (lvlData);
        } else {
            switch(enemyState) {
                case IDLE:
                    //enemyState = RUNNING;
                    newState (RUNNING);
                    break;
                case RUNNING:                   
                    if (seePlayer(lvlData, player))
                        towardsPlayer(player);
                    if(closeForAttack(player))
                        newState(ATTACK);
                        //enemyState = ATTACK;
                    
                    move(lvlData);
                    break;
            }
        }  
    }
    
}
