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
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Resource;

/**
 *
 * @author anny
 */
public class MenuScene extends Scene {

    private BufferedImage background;
    private ImgResource rc;
    private Button introBtn;
    private Button loadBtn;
    private Button newGameBtn;

    private boolean clickState;
    private BufferedImage introImg;

    public MenuScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        rc = ImgResource.getInstance();
        introBtn = new Button("/resources/help_click.png", 200, 100, (int) (Resource.SCREEN_WIDTH * 0.083f), (int) (Resource.SCREEN_HEIGHT * 0.667f));  //遊戲說明按鈕
        newGameBtn = new Button("/resources/newGame_click.png", 200, 100, (int) (Resource.SCREEN_WIDTH * 0.417f), (int) (Resource.SCREEN_HEIGHT * 0.667f));  //新遊戲按鈕
        loadBtn = new Button("/resources/loading_click.png", 200, 100, (int) (Resource.SCREEN_WIDTH * 0.75), (int) (Resource.SCREEN_HEIGHT * 0.667f));  //載入遊戲按鈕

    }

    @Override
    public void paint(Graphics g) {
        introBtn.paint(g);
        newGameBtn.paint(g);
        loadBtn.paint(g);
    }

    @Override
    public MouseListener genMouseListener() {
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
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, introBtn)) {
                    introBtn.setImgState(1);
                }
                if(e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, newGameBtn)){
                    newGameBtn.setImgState(1);
                }
                if(e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, loadBtn)){
                    loadBtn.setImgState(1);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1 ){
                    newGameBtn.setImgState(0);
                    loadBtn.setImgState(0);
                    introBtn.setImgState(0);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, introBtn)) {
                     gsChangeListener.changeScene(MainPanel.INTRO_SCENE);
                }
                
                if(e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, loadBtn)){
                    gsChangeListener.changeScene(MainPanel.LOAD_GAME_SCENE);
                }
            }
            
            

        };
    }

    @Override
    public void logicEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
