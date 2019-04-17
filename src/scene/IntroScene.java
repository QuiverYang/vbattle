/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import vbattle.Button;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.MainPanel.GameStatusChangeListener;

/**
 *
 * @author menglinyang
 */
public class IntroScene extends Scene{
    
    private BufferedImage img;
    private ImgResource rc;
    private Button introBtn;
    private Button loadBtn;
    private Button newGameBtn;
    private boolean clickState;
    private BufferedImage introImg;
    
    public IntroScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        
        rc = ImgResource.getInstance();
        introImg =  rc.tryGetImage("/resources/BlueSky.png");
        introBtn = new Button("/resources/1ILL.jpg", 200, 100);
        
//        loadBtn = new Button("/resources/1ILL.jpg", 5, 100, );
        introBtn.setX(200);
        introBtn.setY(500);

        

    }

    @Override
    public MouseListener genMouseListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void paint(Graphics g) {
        introBtn.paint(g);
    }

    @Override
    public void logicEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
