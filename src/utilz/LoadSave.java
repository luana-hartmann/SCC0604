  package utilz;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;

/*constants for the png files*/
/*loads all the levels and sprites for the entities*/
public class LoadSave {
    
    /*player*/
    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    /*enemies*/
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    /*playing*/
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String PLAYING_BACKGROUND_IMG = "playing_bg_img.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String BIG_CLOUDS = "big_clouds.png";
    /*menu*/
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND_IMG = "background_menu.png";
    /*pause and level finished*/
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String COMPLETED_IMG = "completed_sprite.png"; 
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String SOUND_BUTTONS = "sound_button.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    /*dead screen*/
    public static final String DEAD_BACKGROUND = "death_screen.png";

    public static BufferedImage[] GetAllLevels(){
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;
        
        try {
            file = new File (url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        File[] files = file.listFiles();             
        File[] filesSorted = new File[files.length];
        
        for (int i = 0; i < filesSorted.length; i++) 
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];
            }
              
        BufferedImage[] imgs = new BufferedImage[filesSorted.length];
        
        for (int i = 0; i < imgs.length; i++)
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        
        /*
        for (File f : files) 
            System.out.println("files: " + f.getName());
        for (File f : filesSorted) 
            System.out.println("files sorted: " + f.getName());
        */
        
        return imgs;
    }   
    
    public static BufferedImage GetSpriteAtlas (String filename) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/"+filename);
	try {	
            img = ImageIO.read(is);       
	} catch (IOException e) {
            e.printStackTrace();
	} finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  
        return img;
    } 
       
}
