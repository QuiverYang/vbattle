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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import vbattle.Button;
import vbattle.Fontes;
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

    private String[] playerNameList;

    public LoadGameScene(MainPanel.GameStatusChangeListener gsChangeListener) throws IOException {
        super(gsChangeListener);

        player = Player.getPlayerInstane();

        path = "Playertest";
        playerBtn[0] = new Button("/resources/clickBtn.png", 300, 100, 200, 200);
        playerBtn[1] = new Button("/resources/clickBtn.png", 300, 100, 200, 350);
        playerBtn[2] = new Button("/resources/clickBtn.png", 300, 100, 200, 500);
        playerBtn[3] = new Button("/resources/clickBtn.png", 300, 100, 650, 200);
        playerBtn[4] = new Button("/resources/clickBtn.png", 300, 100, 650, 350);
        playerBtn[5] = new Button("/resources/clickBtn.png", 300, 100, 650, 500);

        backBtn = new Button("/resources/return_click.png", 100, 100, 950, 650);
        player.loadPlayerList(path);
        this.playerNameList = player.getPlayerList();
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
                        playerBtn[i].setImgState(1);
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn)) {
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
                            gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                        } catch (IOException ex) {
                            System.out.println("player not found!");

                        }
                    }
                }

                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, backBtn)) {
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
        
        Font font = Fontes.getBitFont(Resource.SCREEN_WIDTH / 20);
        g.setFont(font);
        g.setColor(new Color(0, 0, 0));
        for (int i = 0; i < PLAYER_NUM; i++) {
            FontMetrics fm = g.getFontMetrics();
            if (playerNameList[i] == null) {
                playerNameList[i] = "";
            }
            int sw = fm.stringWidth(playerNameList[i]);
            int sa = fm.getAscent();
            g.drawString(playerNameList[i], playerBtn[i].getX() + playerBtn[i].getImgWidth() / 2 - sw / 2+5, playerBtn[i].getY() + playerBtn[i].getImgHeight() / 2 - sa / 2+40);
        }

    }

    @Override
    public void logicEvent() {
      
    }

}
