/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import vbattle.Button;
import vbattle.MainPanel;
import vbattle.Player;
import vbattle.Resource;

/**
 *
 * @author anny
 */
public class LoadGameScene extends Scene {

    private Player player;
    private Rectangle rec;
    private final static int PLAYER_NUM = 6;
    private Button[] playerBtn = new Button[PLAYER_NUM];
    private String path;
    private Button backBtn;

    public LoadGameScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);

        player = Player.getPlayerInstane();

        path = "Playertest";
        playerBtn[0] = new Button("/resources/clickBtn.png", 400, 100, 150, 200);
        playerBtn[1] = new Button("/resources/clickBtn.png", 400, 100, 150, 350);
        playerBtn[2] = new Button("/resources/clickBtn.png", 400, 100, 150, 500);
        playerBtn[3] = new Button("/resources/clickBtn.png", 400, 100, 650, 200);
        playerBtn[4] = new Button("/resources/clickBtn.png", 400, 100, 650,350);
        playerBtn[5] = new Button("/resources/clickBtn.png", 400, 100, 650,500);
        
        backBtn = new Button("/resource/back_click2.png", 200, 100, 800, 700);
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
                for (int i = 0; i < PLAYER_NUM; i++) {
                    if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, playerBtn[i])) {
                        playerBtn[i].setImgState(1);
                    }
                }
                if(e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn)){
                    backBtn.setImgState(1);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (int i = 0; i < PLAYER_NUM; i++) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        playerBtn[i].setImgState(0);
                        backBtn.setImgState(0);
                    }
                }
                    
                    for (int i = 0; i < PLAYER_NUM; i++) {
                        if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, playerBtn[i])) {
                            try {
                                player.load("Playertest", i);
                                
                            } catch (IOException ex) {
                                System.out.println("player not found!");

                            }
                        }
                    }
                    
                    if(e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn)){
                         gsChangeListener.changeScene(MainPanel.MENU_SCENE);
                    }

                
            }

        };
    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < PLAYER_NUM; i++) {
            playerBtn[i].paint(g);
        }
        backBtn.paint(g);
    }

    @Override
    public void logicEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
