package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

/*manipulates the game window*/
public class GameWindow {
    protected JFrame jframe;
    
    public GameWindow (GamePanel panel) {
        jframe = new JFrame ();
        
        jframe.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); /*exit the window close operation*/
        jframe.add(panel);     
        jframe.setResizable(false); /*do not resize the window*/
        jframe.pack(); /*creates a window that fits the dimension*/
        jframe.setLocationRelativeTo(null);       
        jframe.setVisible(true); /*show the window*/     
        jframe.addWindowFocusListener(new WindowFocusListener() {
            
            
            @Override
            public void windowGainedFocus(WindowEvent e) {
                //
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                panel.getGame().windowFocusLost();
            }
        });
    }
    
}
