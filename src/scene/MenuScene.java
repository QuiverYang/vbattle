/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.applet.Applet;
import java.applet.AudioClip;
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
import vbattle.Button;
import vbattle.Fontes;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Player;
import vbattle.Resource;

/**
 *
 * @author anny
 */
public class MenuScene extends Scene {

    private BufferedImage background;
    private BufferedImage introImg;
    private ImgResource rc;

    private Button introBtn;
    private Button loadBtn;
    private Button newGameBtn;
    private Button enterPlayerNameBtn;

    public static boolean newGameCheck;  //顯示輸入窗口
    private static boolean typeCheck;  //確認玩家按下字元
    private static char typeName;  //當前玩家輸入的字元
    private static String playerName;
    private static int countChar; //玩家名稱字元個數
    private Player player;
    private Font font;

    private int SCREEN_WIDTH = Resource.SCREEN_WIDTH;
    private int SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;
    

    
    
    public MenuScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        rc = ImgResource.getInstance();
        background = rc.tryGetImage("/resources/mainBackground1.png");

        introBtn = new Button("/resources/help_click.png",Resource.SCREEN_WIDTH / 12, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);  //遊戲說明按鈕
        newGameBtn = new Button("/resources/newGame_click.png",Resource.SCREEN_WIDTH / 12 * 5, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);
        loadBtn = new Button("/resources/loading_click.png",Resource.SCREEN_WIDTH / 12 * 9, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);
        enterPlayerNameBtn = new Button("/resources/clickBtn.png", Resource.SCREEN_WIDTH / 12 * 5, Resource.SCREEN_HEIGHT / 2, Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_HEIGHT / 9);
        enterPlayerNameBtn.setLabel("OK");
        
        player = Player.getPlayerInstane();
        try {
            player.loadTotal();
        } catch (IOException ex) {
            Logger.getLogger(MenuScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        typeCheck = newGameCheck = false;
        countChar = 0;
        playerName = "";
        
        font = Fontes.getBitFont(Resource.SCREEN_WIDTH / 25);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.background, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT, 0, 0, background.getWidth(), background.getHeight(), null);
        introBtn.paintBtn(g);
        newGameBtn.paintBtn(g);
        loadBtn.paintBtn(g);

        //顯示玩家輸入的字元
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        //顯示玩家名稱窗口
        if (newGameCheck) {
            g.setColor(Color.BLACK);
            g.fillRect(Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2 - 8, (int) (Resource.SCREEN_HEIGHT * 0.278f) - 8, Resource.SCREEN_WIDTH / 12 * 5 + 16, (int) (Resource.SCREEN_HEIGHT * 0.389f) + 16);

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(Resource.SCREEN_WIDTH / 2 - Resource.SCREEN_WIDTH / 12 * 5 / 2, (int) (Resource.SCREEN_HEIGHT * 0.278f), Resource.SCREEN_WIDTH / 12 * 5, (int) (Resource.SCREEN_HEIGHT * 0.389f));

            g.setColor(Color.WHITE);
            g.fillRect((int) (Resource.SCREEN_WIDTH * 0.375f), (int) (Resource.SCREEN_HEIGHT * 0.389f) + 20, Resource.SCREEN_WIDTH / 12 * 3, (int) (Resource.SCREEN_HEIGHT * 0.055f));
            enterPlayerNameBtn.paintBtn(g);

            g.setColor(Color.BLACK);
            int sw = fm.stringWidth("ENTER YOUR NAME");
            int sa = fm.getAscent();
            g.drawString("ENTER YOUR NAME", (int) (Resource.SCREEN_WIDTH * 0.318f), (int) (Resource.SCREEN_HEIGHT * 0.278f) + (int) (Resource.SCREEN_HEIGHT * 0.389f) - sa / 2 - (int) (Resource.SCREEN_HEIGHT * 0.278f));

        }

        g.setColor(Color.BLACK);
        int sw = fm.stringWidth(playerName);
        int sa = fm.getAscent();
        g.drawString(playerName, (int) (Resource.SCREEN_WIDTH * 0.375f) + Resource.SCREEN_WIDTH / 12 * 3 / 2 - sw / 2 + (int) (Resource.SCREEN_WIDTH * 5 / 1200), (int) (Resource.SCREEN_HEIGHT / 2) + Resource.SCREEN_HEIGHT * 50 / 900 / 2 - sa / 2 - Resource.SCREEN_HEIGHT * 60 / 900 + 15);

//        if (typeCheck) {
//            playerName = playerName + typeName;
//            typeCheck = false;
//        }

    }

    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, introBtn) && newGameCheck==false) {
                    introBtn.setImgState(1);
                    introBtn.setClickState(true);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, newGameBtn)&& newGameCheck==false) {
                    newGameBtn.setImgState(1);
                    newGameBtn.setClickState(true);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, loadBtn)&& newGameCheck==false) {
                    loadBtn.setImgState(1);
                    loadBtn.setClickState(true);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, enterPlayerNameBtn)) {
                    enterPlayerNameBtn.setImgState(1);
                    enterPlayerNameBtn.setClickState(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    newGameBtn.setImgState(0);
                    loadBtn.setImgState(0);
                    introBtn.setImgState(0);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, introBtn) && introBtn.getClickState()) {
                    introBtn.setImgState(0);
                    introBtn.setClickState(false);
                    gsChangeListener.changeScene(MainPanel.INTRO_SCENE);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, newGameBtn) && newGameBtn.getClickState()) {
                    newGameBtn.setImgState(0);
                    newGameBtn.setClickState(false);
                    newGameCheck = true;
                }

                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, loadBtn) && loadBtn.getClickState()) {
                    loadBtn.setClickState(false);
                    gsChangeListener.changeScene(MainPanel.LOAD_GAME_SCENE);
                }

                if (e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, enterPlayerNameBtn) && enterPlayerNameBtn.getClickState()) {
                    enterPlayerNameBtn.setImgState(0);
                    if (playerName.length() > 0) {
                        player.setPlayerName(playerName);
                        player.setPlayerIndex(-1); //表示是新玩家
                        player.defaultPlayer();  //初始化玩家數值

                        enterPlayerNameBtn.setClickState(false);
                        gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                    }

                }
            }

        };
    }

    public static KeyAdapter genKeyAdapter() {
        return new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int t = e.getKeyCode();
                if (t >= KeyEvent.VK_A && t <= KeyEvent.VK_Z || t >= KeyEvent.VK_0 && t <= KeyEvent.VK_9) {
                    typeName = (char) t;
                    if (playerName.length() < 5) {
                        playerName += typeName;
                        
                    }
                }
                if (t == KeyEvent.VK_BACK_SPACE) {
                    if (playerName.length() > 0) {
                        try{
                            playerName = playerName.substring(0, playerName.length() - 1);
                        }catch(java.lang.StringIndexOutOfBoundsException ex){
                            System.out.println("StringIndexOutOfBoundsExceptio from typing");
                        }
                        
                    }
                }
            }
        };
    }

    @Override
    public void logicEvent() {
// 以下是更新按鈕位置相對於螢幕大小        
        this.resize();
    }

    public void resize() {
        if (SCREEN_WIDTH != Resource.SCREEN_WIDTH || SCREEN_HEIGHT != Resource.SCREEN_HEIGHT) {
            SCREEN_WIDTH = Resource.SCREEN_WIDTH;
            SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;
            introBtn.reset(Resource.SCREEN_WIDTH / 12, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);  //遊戲說明按鈕
            newGameBtn.reset(Resource.SCREEN_WIDTH / 12 * 5, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);
            loadBtn.reset(Resource.SCREEN_WIDTH / 12 * 9, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);
            enterPlayerNameBtn.reset(Resource.SCREEN_WIDTH / 12 * 5, Resource.SCREEN_HEIGHT / 2, Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_HEIGHT / 9);
            font = Fontes.getBitFont(Resource.SCREEN_WIDTH / 25);
        }
    }

}
