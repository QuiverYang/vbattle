
package vbattle;

import scene.MenuScene;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.Timer;

public class VBattle {

    public static void main(String[] args) throws IOException {
        final JFrame jf = new JFrame();
//        Stuff s = new Stuff(0, 0, 96, 96, "test"); //測試
//        s.print(); //測試
        
        jf.setTitle("test");
        jf.setSize(Resource.SCREEN_WIDTH,Resource.SCREEN_HEIGHT);
        MainPanel mainPanel = new MainPanel();
        jf.add(mainPanel);
        jf.setVisible(true);
        
        Timer timer = new Timer(Resource.FPS,new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                jf.repaint();
            }
        }){
            
        };
        timer.start();
    }
    
}
