package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.UrmButtons.*;
import static utilz.Constants.UI.VolumeButton.*;
import utilz.LoadSave;
import gamestates.Playing;

public class PauseOverlay {
    
    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private SoundButton musicButton, sfxButton;
    private UrmButton menuB, replayB, unpausedB;
    private VolumeButton volumeButton;
    
    public PauseOverlay (Playing playing) {
        this.playing = playing;
        loadBackground ();
        creatSoundButton ();
        createUrmButton();
        createVolumeButton();
    }
    
    private void createVolumeButton () {
        int vX = (int)(309 * Game.SCALE);
        int vY = (int)(278 * Game.SCALE);
        
        volumeButton = new VolumeButton (vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }
    
    private void createUrmButton () {
        int menuX = (int)(313 * Game.SCALE);
        int replayX = (int)(387 * Game.SCALE);
        int unpausedX = (int)(462 * Game.SCALE);
        int bY = (int)(325 * Game.SCALE);
        
        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpausedB = new UrmButton(unpausedX, bY, URM_SIZE, URM_SIZE, 0);       
    }
    
    private void creatSoundButton () {
        int soundX = (int)(450 * Game.SCALE);
        int musicY = (int)(140 * Game.SCALE);
        int sfxY = (int)(186 * Game.SCALE);
        musicButton = new SoundButton (soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton (soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }
    private void loadBackground () {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int)(backgroundImg.getWidth() * Game.SCALE);
        bgH = (int)(backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH/2 - bgW/2 -60;
        bgY = (int)(25*Game.SCALE);
    }
    
    public void update () {
        musicButton.update();
        sfxButton.update(); 
        
        menuB.update();
        replayB.update();
        unpausedB.update();
        volumeButton.update();
    }
    
    public void draw (Graphics g) {
        /*background pause*/
        g.drawImage(backgroundImg, bgX, bgY, bgH, bgH, null);
        
        /*music buttons*/
        musicButton.draw(g);
        sfxButton.draw(g);
        
        /*urm buttons*/
        menuB.draw(g);
        replayB.draw(g);
        unpausedB.draw(g); 
        
        /*volume button*/
        volumeButton.draw(g);
    }
    
    public void mouseDragged (MouseEvent e) {
        if (volumeButton.isMousePressed()) {
             volumeButton.changeX(e.getX());
        }
    }
    
    public void mousePressed(MouseEvent e) {
        /*music buttons*/
        if (isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        /*urm buttons*/
        else if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
        else if (isIn(e, unpausedB))
            unpausedB.setMousePressed(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        /*music buttons*/
        if (isIn(e, musicButton)) {
            if(musicButton.isMousePressed())
                musicButton.setMuted(!musicButton.isMuted());
        }     
        else if (isIn(e, sfxButton)) {
            if(sfxButton.isMousePressed())
                sfxButton.setMuted(!sfxButton.isMuted());
        }
        /*urm buttons*/
        else if (isIn(e, menuB)) {
            if(menuB.isMousePressed())
                Gamestate.state = Gamestate.MENU;
        }
        else if (isIn(e, replayB)) {
            if(replayB.isMousePressed())
                System.out.println("REPLAY LVL");
        }
        else if (isIn(e, unpausedB)) {
            if(unpausedB.isMousePressed()) 
                playing.unpauseGame();
                //System.out.println("UNPAUSED");
        }
        
        /*reset buttons*/
        musicButton.resetBools();
        sfxButton.resetBools();
        menuB.resetBools();
        replayB.resetBools();
        unpausedB.resetBools();
        volumeButton.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpausedB.setMouseOver(false);
        volumeButton.setMouseOver(false);
        
        /*music buttons*/
        if (isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if (isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        /*urm buttons*/
        else if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, unpausedB))
            unpausedB.setMouseOver(true);
        else if (isIn(e, volumeButton))
            volumeButton.setMouseOver(true);
    }
    
    private boolean isIn (MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    
}