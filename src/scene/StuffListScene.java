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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vbattle.Button;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.Player;
import vbattle.Resource;

/**
 *
 * @author annyhung
 */
public class StuffListScene extends Scene {

    private Button returnBtn;
    private Button[] stuffBtn;
    private Player player;

    private BufferedImage tinyImg, bigImg, background;
    private ImgResource rc;
    private int[] unlock;
    private int[] iconX, iconY;
    private int currentStuff;
    private String name, hp, atk, info;
    private Font font;

    public StuffListScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        player = Player.getPlayerInstane();
        unlock = player.getUnlock();
        rc = ImgResource.getInstance();
        name = hp = atk = info = "";
        currentStuff = -1;

        tinyImg = rc.tryGetImage("/resources/tinyCharacters.png");
        bigImg = rc.tryGetImage("/resources/bigCharacters.png");
        background = rc.tryGetImage("/resources/almanacBg.png");

        
        returnBtn = new Button("/resources/return_blue.png", 20, 20, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);

        //icon座標
        iconX = new int[10];
        iconY = new int[10];
        for (int i = 0; i < 5; i++) {
            iconX[i] = (int) (Resource.SCREEN_WIDTH * (0.1f * i) + Resource.SCREEN_WIDTH * (0.05f));
            iconY[i] = (int) (Resource.SCREEN_HEIGHT * 0.40f);
            System.out.println(i + " " + iconX[i] + " y:" + iconY[i]);
        }
        for (int i = 5; i < 10; i++) {
            iconX[i] = (int) (Resource.SCREEN_WIDTH * (0.1f * (i - 5)) + Resource.SCREEN_WIDTH * (0.05f));
            iconY[i] = (int) (Resource.SCREEN_HEIGHT * 0.7f);
        }
        
        //btn
        font = new Font("Courier", Font.BOLD, Resource.SCREEN_WIDTH / 35);
        for (int i = 0; i < 10; i++) {
            stuffBtn = new Button[10];
        }
        for (int i = 0; i < unlock.length; i++) {
            stuffBtn[i] = new Button("/resources/btnIcon.png", iconX[i], iconY[i], 100, 100);
            stuffBtn[i + 5] = new Button("/resources/btnIcon.png", iconX[i + 5], iconY[i + 5], 100, 100);
        }

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
                for (int i = 0; i < stuffBtn.length; i++) {
                    if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, stuffBtn[i])) {
                        System.out.println(i + "ss");
                        stuffBtn[i].setClickState(true);
                        stuffBtn[i].setImgState(1);
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, returnBtn)) {
                    returnBtn.setClickState(true);
                    returnBtn.setImgState(1);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (int i = 0; i < stuffBtn.length; i++) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        stuffBtn[i].setImgState(0);

                        returnBtn.setImgState(0);
                    }
                }
                for (int i = 0; i < stuffBtn.length; i++) {
                    if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, stuffBtn[i]) && stuffBtn[i].getClickState()) {
                        currentStuff = i;
                        load(i);
                        stuffBtn[i].setClickState(false);
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, returnBtn) && returnBtn.getClickState()) {
                    returnBtn.setClickState(false);
                    gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT, 0, 0, this.background.getWidth(), this.background.getHeight(), null);
        returnBtn.paintBtn(g);
        
        for (int i = 0; i < unlock.length; i++) {
            if (unlock[i] != 0) {
                stuffBtn[i].paintBtn(g);
                stuffBtn[i + 5].paintBtn(g);
                if (i < 2) {
                    g.drawImage(tinyImg, iconX[i + 5] + 8, iconY[i + 5] + 10, iconX[i + 5] + 70 + 8, iconY[i + 5] + 70 + 10, 0, (i + 3) * 32, 32, (i + 3 + 1) * 32, null);
                }
                if (i >= 2) {
                    g.drawImage(bigImg, iconX[i + 5] + 8, iconY[i + 5] + 10, iconX[i + 5] + 80 + 8, iconY[i + 5] + 70 + 10, 0, (i) * 64, 64, (i + 1) * 64, null);
                }
                if (i < 3) {
                    g.drawImage(tinyImg, iconX[i] + 8, iconY[i] + 10, iconX[i] + 70 + 8, iconY[i] + 70 + 10, 0, i * 32, 32, (i + 1) * 32, null);
                }
                if (i > 2) {
                    g.drawImage(bigImg, iconX[i] + 8, iconY[i] + 10, iconX[i] + 70 + 8, iconY[i] + 70 + 10, 0, (i - 3) * 64, 64, (i - 3 + 1) * 64, null);
                }
            }

        }
        switch (this.currentStuff) {
            case 0:

                info = "sssss";
                break;
            case 1:
                info = "sdsd";
                break;
            case 2:
                info = "Sd";
                break;
            case 3:
                info = "3";
                break;
            case 4:
                info = "4";
                break;
        }
        if (currentStuff != -1) {
            if (currentStuff < 3) {
                g.drawImage(tinyImg, 900, 300, 1050, 450, 0, currentStuff * 32, 32, (currentStuff + 1) * 32, null);
            } else if (currentStuff > 2 && currentStuff < 5) {
                g.drawImage(bigImg, 900, 300, 1100, 500, 0, (currentStuff - 3) * 64, 64, (currentStuff - 3 + 1) * 64, null);
            } else if (currentStuff > 4 && currentStuff < 7) {
                g.drawImage(tinyImg, 900, 300, 1050, 450, 0, (currentStuff - 2) * 32, 32, (currentStuff - 2 + 1) * 32, null);
            } else if (currentStuff > 6) {
                g.drawImage(bigImg, 900, 300, 1100, 500, 0, (currentStuff - 5) * 64, 64, (currentStuff - 5 + 1) * 64, null);
            }

            g.setFont(font);
            g.setColor(Color.white);
            FontMetrics fm = g.getFontMetrics();
            int sw = fm.stringWidth(name);
            g.drawString(name, 790 + 380 / 2 - sw / 2, 520);
            g.drawString("HP:" + hp, 930, 550);
            g.drawString("ATK:" + atk, 930, 580);
        }

    }

    private void load(int i) {
        try {
            BufferedReader br;
            String[] status = null;
            int lv = 1;
            if (i < 5) {
                br = new BufferedReader(new FileReader("src/" + "test" + i + ".txt"));
                status = br.readLine().split(",");
                lv = unlock[i];
            } else if (i > 4) {
                br = new BufferedReader(new FileReader("src/" + "actor" + (i - 5) + ".txt"));
                status = br.readLine().split(",");
            }

//            String[] status = br.readLine().split(",");
            int hpRate = Integer.parseInt(status[1]);
            int atkRate = Integer.parseInt(status[2]);
            int hpBase = Integer.parseInt(status[3]);
            int atkBase = Integer.parseInt(status[4]);
            this.name = status[12];

            this.hp = (hpRate * lv + hpBase) + "";
            this.atk = (atkRate * lv + atkBase) + "";

        } catch (FileNotFoundException ex) {
            Logger.getLogger(StuffListScene.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StuffListScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void logicEvent() {
    }

}
