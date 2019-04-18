/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import vbattle.Button;
import vbattle.ImgResource;
import vbattle.MainPanel;

/**
 *
 * @author anny
 */
public class IntroScene extends Scene{
    private BufferedImage introImg;
    private ImgResource rc;
    private Button backBtn;
    
    public IntroScene(MainPanel.GameStatusChangeListener gsChangeListener){
        super(gsChangeListener);
        rc = ImgResource.getInstance();
        introImg = rc.tryGetImage("/resources/BlueSky.png");  //遊戲說明圖片 
        backBtn = new Button("/resources/back_click2.png", 200, 100, 900, 700);  //退回按鈕
    }

    @Override
    public MouseListener genMouseListener() {
        return new MouseAdapter(){
            
            public boolean isOnBtn(MouseEvent e, Button btn) {
                if (e.getX() >= btn.getX()
                        && e.getX() <= btn.getX() + btn.getImgWidth() && e.getY() >= btn.getY() && e.getY() <= btn.getY() + btn.getImgHeight()) {
                    return true;
                }
                return false;
            }
            
             @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn)) {
                    backBtn.setImgState(1);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn)) {
                    backBtn.setImgState(0);
                     gsChangeListener.changeScene(MainPanel.MENU_SCENE);
                }
                if(e.getButton() == MouseEvent.BUTTON1 ){
                    backBtn.setImgState(0);
                }
                
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(introImg, 0, 0, null);
        backBtn.paint(g);
    }

    @Override
    public void logicEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
