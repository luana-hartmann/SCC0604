package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import levels.LevelManager;


public class Save {
    
    LevelManager levelManager;

    public Save(LevelManager levelManager) {
        this.levelManager = levelManager;
    }
    
    public void save () {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("saving.dat")));
            DataStorage ds = new DataStorage();
            ds.level = levelManager.lvlIndex;  
            
            /*writes the DataStorage object*/
            oos.writeObject(ds);
        } catch (Exception e) {
            System.out.println("Save Exception");
        }
    }
    
    public void load () {
        try {
             ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("saving.dat")));
             
             /*reads the DataStorage object*/
             DataStorage ds = (DataStorage)ois.readObject();
             
             levelManager.lvlIndex = ds.level;
        } catch (Exception e) {
            System.out.println("Load Exception");
        }
    }
    
    
}
