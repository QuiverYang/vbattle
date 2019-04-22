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
    private int introY;

    public IntroScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        rc = ImgResource.getInstance();
        introImg = rc.tryGetImage("/resources/intro1.jpg");  //遊戲說明圖片 
        backBtn = new Button("/resources/back_click2.png", Resource.SCREEN_WIDTH/12*9 , Resource.SCREEN_HEIGHT /9*7, Resource.SCREEN_WIDTH /12*2, Resource.SCREEN_HEIGHT /9);  //退回按鈕

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
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn)) {
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1200, 900);
        g.drawImage(introImg, 0, this.introY+300, null);
        backBtn.paint(g);

    }

    @Override
    public void logicEvent() {
        this.introY -= 1;
    }

}
