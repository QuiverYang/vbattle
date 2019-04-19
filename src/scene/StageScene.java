/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import vbattle.ImgResource;
import vbattle.MainPanel;

/**
 *
 * @author anny
 */
public class StageScene extends Scene{
    private BufferedImage characterImg;
    private ImgResource rc;
    
    
    public StageScene(MainPanel.GameStatusChangeListener gsChangeListener) throws IOException {
        super(gsChangeListener);
        rc = ImgResource.getInstance();
        
        characterImg = rc.tryGetImage("/resources/tinyCharacters.png");
        
        
    }

    @Override
    public MouseAdapter genMouseAdapter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void paint(Graphics g) {
        
    }

    @Override
    public void logicEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
