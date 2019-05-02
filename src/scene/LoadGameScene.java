/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import vbattle.Button;
import vbattle.Fontes;
import vbattle.ImgResource;
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
    private ImgResource rc;
    private String[] playerNameList;

    private BufferedImage background;
    private Font font;
    private final int SCREEN_WIDTH = Resource.SCREEN_WIDTH;
    private final int SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;
    
    public LoadGameScene(MainPanel.GameStatusChangeListener gsChangeListener) throws IOException {
        super(gsChangeListener);

        player = Player.getPlayerInstane();
        path = "Playertest";

        playerBtn[0] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.15f), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[1] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.15f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[2] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.15f), Resource.SCREEN_WIDTH / 12 * 6, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[3] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.525f), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[4] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.525f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[5] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.525f), Resource.SCREEN_WIDTH / 12 * 6, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        
        
        for(int i =0; i < playerBtn.length; i++){
            playerBtn[i].setLabelSize((int)(playerBtn[i].getWidth()/6));
        }
        
        backBtn = new Button("/resources/return1.png",  Resource.SCREEN_WIDTH / 12 -50 , Resource.SCREEN_HEIGHT /9 -50, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);   
        this.playerNameList = player.getPlayerList();

        rc = ImgResource.getInstance();
        background = rc.tryGetImage("/resources/loading_bg.png");
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
                for (int i = 0; i < PLAYER_NUM; i++) {
                    if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, playerBtn[i])) {
                        playerBtn[i].setClickState(true);
                        playerBtn[i].setImgState(1);
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn)) {
                    backBtn.setClickState(true);
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
                    if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, playerBtn[i]) && playerBtn[i].getClickState()) {
                        try {
                            player.loadPlayer(i);
                            playerBtn[i].setClickState(false);
                            gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                        } catch (IOException ex) {
                            System.out.println("player not found!");

                        }
                    }
                }

                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn) && backBtn.getClickState()) {
                    backBtn.setClickState(false);
                    gsChangeListener.changeScene(MainPanel.MENU_SCENE);
                }

            }

        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT, 0, 0, this.background.getWidth(), this.background.getHeight(), null);

        for (int i = 0; i < PLAYER_NUM; i++) {
            playerBtn[i].paintBtn(g);
        }
        backBtn.paintBtn(g);

        for (int i = 0; i < playerNameList.length; i++) {

            if (playerNameList[i] == null) {
                playerNameList[i] = "";
                playerBtn[i].setLabel("");
            }
            playerBtn[i].setLabel(playerNameList[i]);
        }

    }

    @Override
    public void logicEvent() {
        this.resize();
        
    }
    
    public void resize() {
        if (SCREEN_WIDTH != Resource.SCREEN_WIDTH || SCREEN_HEIGHT != Resource.SCREEN_HEIGHT) {
            this.backBtn.reset( (int) (Resource.SCREEN_WIDTH * 0.042f) , (int) (Resource.SCREEN_WIDTH * 0.042f), Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
        playerBtn[0].reset((int) (Resource.SCREEN_WIDTH * 0.15f), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[1].reset((int) (Resource.SCREEN_WIDTH * 0.15f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[2].reset((int) (Resource.SCREEN_WIDTH * 0.13f), Resource.SCREEN_WIDTH / 12 * 6, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[3].reset((int) (Resource.SCREEN_WIDTH * 0.525f), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[4].reset((int) (Resource.SCREEN_WIDTH * 0.525f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
        playerBtn[5].reset((int) (Resource.SCREEN_WIDTH * 0.525f), Resource.SCREEN_WIDTH / 12 * 6, Resource.SCREEN_WIDTH / 12 * 2*2, Resource.SCREEN_HEIGHT / 9*2);
       
        }
    }

}
