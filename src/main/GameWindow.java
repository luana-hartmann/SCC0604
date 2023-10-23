package main;

import javax.swing.JFrame;

public class GameWindow {
    private JFrame jframe;
    
    public GameWindow () {
            jframe = new JFrame ();
            jframe.setSize(400,400); /*size of thew window*/
            jframe.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); /*exit the window close operation*/
            jframe.setVisible(true); /*show the window*/
    }
    
}
