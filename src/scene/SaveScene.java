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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static scene.MenuScene.newGameCheck;
import vbattle.Button;
import vbattle.Fontes;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.Player;
import vbattle.Resource;

/**
 *
 * @author annyhung
 */
public class SaveScene extends Scene {

    private Button yesBtn;
    private Button noBtn;
    private Button backBtn;
    private Font font;

    private Player player;
    public static boolean saveSceneCheck;  //確認現在是否用到儲存畫面

    private BufferedImage storeBg;
    private BufferedImage stageBg;
    private ImgResource rc;
    public static int nextScene;  //儲存場景
    public static int currentScene;  //返回產景

    private final int SCREEN_WIDTH = Resource.SCREEN_WIDTH;
    private final int SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;

    public SaveScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        
        backBtn = new Button("/resources/return_blue.png", Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 -  (int)(Resource.SCREEN_WIDTH*0.054) + Resource.SCREEN_WIDTH / 12 * 5, (int) (Resource.SCREEN_HEIGHT * 0.278f) - 50, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
        yesBtn = new Button("/resources/clickBtn_blue.png", Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 + (int)(Resource.SCREEN_WIDTH*0.025f), Resource.SCREEN_HEIGHT / 2, Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_HEIGHT / 9);
        noBtn = new Button("/resources/clickBtn_blue.png", Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 + (int)(Resource.SCREEN_WIDTH*0.217f), Resource.SCREEN_HEIGHT / 2, Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_HEIGHT / 9);
        font = Fontes.getBitFont(Resource.SCREEN_WIDTH / 30);

        yesBtn.setLabel("YES");
        noBtn.setLabel("NO");

        rc = ImgResource.getInstance();
        storeBg = rc.tryGetImage("/resources/shopforsave.jpg");
        stageBg = rc.tryGetImage("/resources/stageforsave.jpg");

        saveSceneCheck = false;
        player = Player.getPlayerInstane();
        
    }

//    public void setPrevous(int i){
//        this.nextScene = i;
//    }
//    
//    public void setCurrent(int i){
//        this.currentScene = i;
//    }
    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, yesBtn)) {
                    yesBtn.setImgState(1);
                    yesBtn.setClickState(true);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, noBtn)) {
                    noBtn.setImgState(1);
                    noBtn.setClickState(true);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, backBtn)) {
                    backBtn.setImgState(1);
                    backBtn.setClickState(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    yesBtn.setImgState(0);
                    noBtn.setImgState(0);
                    backBtn.setImgState(0);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, yesBtn) && yesBtn.getClickState()) {
                    yesBtn.setImgState(0);
                    yesBtn.setClickState(false);
                    try {
                        player.save();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.out.println("player can't save game");
                    }
                    gsChangeListener.changeScene(nextScene);
                    MainPanel.backgroundSound.loop();
                }
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, noBtn) && noBtn.getClickState()) {
                    noBtn.setImgState(0);
                    noBtn.setClickState(false);
                    gsChangeListener.changeScene(nextScene);
                }

                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, backBtn) && backBtn.getClickState()) {
                    backBtn.setImgState(0);
                    backBtn.setClickState(false);
                    saveSceneCheck = true;
                    gsChangeListener.changeScene(currentScene);
                }
            }

        };
    }

    @Override
    public void paint(Graphics g) {
        //設定背景
        if (currentScene == MainPanel.STAGE_SCENE) {
            g.drawImage(stageBg, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT, null);
        } else if (currentScene == MainPanel.STORE_SCENE) {
            g.drawImage(storeBg, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT, null);
        }
        
        //設定字型
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
 
        //繪制儲存視窗
        g.setColor(Color.BLACK);
        g.fillRect(Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 - 8, (int) (Resource.SCREEN_HEIGHT * 0.278f) - 8, Resource.SCREEN_WIDTH / 12 * 5 + (int) (Resource.SCREEN_HEIGHT * 0.0177f), (int) (Resource.SCREEN_HEIGHT * 0.389f) + (int) (Resource.SCREEN_HEIGHT * 0.018f));

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2, (int) (Resource.SCREEN_HEIGHT * 0.278f), Resource.SCREEN_WIDTH / 12 * 5, (int) (Resource.SCREEN_HEIGHT * 0.389f));

        g.setColor(Color.BLACK);
        int sw = fm.stringWidth("WOULD YOU LIKE TO");
        int sa = fm.getAscent();
        g.drawString("WOULD YOU LIKE TO", Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 + Resource.SCREEN_WIDTH / 12 * 5 / 2 - sw / 2, (int) (Resource.SCREEN_HEIGHT * 0.278f) + (int) (Resource.SCREEN_HEIGHT * 0.389f) - sa / 2 - (int) (Resource.SCREEN_HEIGHT * 0.278f) + (int) (Resource.SCREEN_HEIGHT * 0.0278f));
        sw = fm.stringWidth("SAVE YOUR PROGRESS?");
        g.drawString("SAVE YOUR PROGRESS?", Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 + Resource.SCREEN_WIDTH / 12 * 5 / 2 - sw / 2, (int) (Resource.SCREEN_HEIGHT * 0.278f) + (int) (Resource.SCREEN_HEIGHT * 0.389f) - sa / 2 - (int) (Resource.SCREEN_HEIGHT * 0.278f) + sa + (int) (Resource.SCREEN_HEIGHT * 0.033f));
        
        //繪製按鈕
        this.noBtn.paintBtn(g);
        this.yesBtn.paintBtn(g);
        this.backBtn.paintBtn(g);

    }

    @Override
    public void logicEvent() {
        this.resize();
    }

    public void resize() {
        if (SCREEN_WIDTH != Resource.SCREEN_WIDTH || SCREEN_HEIGHT != Resource.SCREEN_HEIGHT) {
            this.backBtn.reset(Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 -  (int)(Resource.SCREEN_WIDTH*0.054) + Resource.SCREEN_WIDTH / 12 * 5, (int) (Resource.SCREEN_HEIGHT * 0.278f) - 50, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);  //遊戲說明按鈕
            this.yesBtn.reset( Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 + (int)(Resource.SCREEN_WIDTH*0.025f), Resource.SCREEN_HEIGHT / 2, Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_HEIGHT / 9);
            this.noBtn.reset(Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 + (int)(Resource.SCREEN_WIDTH*0.217f), Resource.SCREEN_HEIGHT / 2, Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_HEIGHT / 9);
            font = Fontes.getBitFont(Resource.SCREEN_WIDTH / 30);
        }
    }

}
