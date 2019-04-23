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
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
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

    public LoadGameScene(MainPanel.GameStatusChangeListener gsChangeListener) throws IOException {
        super(gsChangeListener);

        player = Player.getPlayerInstane();

        path = "Playertest";

        playerBtn[0] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.125f), (int) (Resource.SCREEN_HEIGHT * 0.278f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[1] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.125f), (int) (Resource.SCREEN_HEIGHT * 0.444f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[2] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.125f), (int) (Resource.SCREEN_HEIGHT * 0.611f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[3] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.5f), (int) (Resource.SCREEN_HEIGHT * 0.278f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[4] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.5f), (int) (Resource.SCREEN_HEIGHT * 0.444f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[5] = new Button("/resources/clickBtn1.png", (int) (Resource.SCREEN_WIDTH * 0.5f), (int) (Resource.SCREEN_HEIGHT * 0.611f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);

        backBtn = new Button("/resources/return1.png", (int) (Resource.SCREEN_WIDTH * 0.842f), (int) (Resource.SCREEN_HEIGHT * 0.778f), Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
        player.loadPlayerList(path);
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
                            player.load("Playertest", i);
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
//        g.setColor(Color.LIGHT_GRAY);
//        g.fillRect(0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT);

//        g.setColor(Color.WHITE);
//        g.fillRect(Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT/9+50, Resource.SCREEN_WIDTH / 12*10, (int)(Resource.SCREEN_HEIGHT/9*4.7));
        Font font = Fontes.getBitFont(Resource.SCREEN_WIDTH / 10);
        g.setFont(font);
        g.setColor(Color.white);
        FontMetrics fm = g.getFontMetrics();
//        int sw1 = fm.stringWidth("LOAD  GAME");
//        int sa1 = fm.getAscent();
//        g.drawString("LOAD GAME",Resource.SCREEN_WIDTH/2 - sw1/2, (int) (Resource.SCREEN_HEIGHT /9));

        for (int i = 0; i < PLAYER_NUM; i++) {
            playerBtn[i].paint(g);
        }
        backBtn.paint(g);

        for (int i = 0; i < PLAYER_NUM; i++) {

            if (playerNameList[i] == null) {
                playerNameList[i] = "";
                playerBtn[i].setLabel("");
            }
            int sw = fm.stringWidth(playerNameList[i]);
            int sa = fm.getAscent();
            playerBtn[i].setLabel(playerNameList[i]);
        }

    }

    @Override
    public void logicEvent() {
        this.backBtn.reset((int) (Resource.SCREEN_WIDTH * 0.842f), (int) (Resource.SCREEN_HEIGHT * 0.778f), Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
        playerBtn[0].reset((int) (Resource.SCREEN_WIDTH * 0.125f), (int) (Resource.SCREEN_HEIGHT * 0.278f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[1].reset((int) (Resource.SCREEN_WIDTH * 0.125f), (int) (Resource.SCREEN_HEIGHT * 0.444f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[2].reset((int) (Resource.SCREEN_WIDTH * 0.125f), (int) (Resource.SCREEN_HEIGHT * 0.611f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[3].reset((int) (Resource.SCREEN_WIDTH * 0.5f), (int) (Resource.SCREEN_HEIGHT * 0.278f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[4].reset((int) (Resource.SCREEN_WIDTH * 0.5f), (int) (Resource.SCREEN_HEIGHT * 0.444f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
        playerBtn[5].reset((int) (Resource.SCREEN_WIDTH * 0.5f), (int) (Resource.SCREEN_HEIGHT * 0.611f), Resource.SCREEN_WIDTH / 12 * 4, Resource.SCREEN_HEIGHT / 12 * 2);
    }

}
