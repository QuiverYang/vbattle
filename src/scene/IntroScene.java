/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import vbattle.Button;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.Resource;

/**
 *
 * @author anny
 */
public class IntroScene extends Scene {

    private BufferedImage introImg;
    private BufferedImage gradientImg;
    private ImgResource rc;
    private Button backBtn;
    private Color darkBlue;
    private int introY;
    private int SCREEN_WIDTH = Resource.SCREEN_WIDTH;
    private int SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;

    public IntroScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        rc = ImgResource.getInstance();
        introImg = rc.tryGetImage("/resources/intro.png");  //遊戲說明圖片 
        backBtn = new Button("/resources/return1.png",  (int)(Resource.SCREEN_WIDTH *0.842f), (int)(Resource.SCREEN_HEIGHT *0.778f), Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);//退回按鈕
        darkBlue = new Color(21,25,54);
        this.introY = 0;
    }

    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {
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
                    backBtn.setClickState(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn)&& backBtn.getClickState()) {
                    backBtn.setImgState(0);
                    gsChangeListener.changeScene(MainPanel.MENU_SCENE);
                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    backBtn.setImgState(0);
                }

            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(darkBlue);
        g.fillRect(0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT);
        
        g.drawImage(introImg, 0, this.introY+300, Resource.SCREEN_WIDTH, introImg.getHeight()+this.introY+300, 0, 0, introImg.getWidth(), introImg.getHeight(), null);
        backBtn.paintBtn(g);

    }

    @Override
    public void logicEvent() {
        this.introY -= 1;
        this.resize();
        

    }
    
    public void resize() {
        if (SCREEN_WIDTH != Resource.SCREEN_WIDTH || SCREEN_HEIGHT != Resource.SCREEN_HEIGHT) {
            SCREEN_WIDTH = Resource.SCREEN_WIDTH;
            SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;
           this.backBtn.reset( (int)(Resource.SCREEN_WIDTH *0.842f), (int)(Resource.SCREEN_HEIGHT *0.778f), Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
        }

}
}
