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
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
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

    public MenuScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        rc = ImgResource.getInstance();
        background = rc.tryGetImage("/resources/mainBackground1.png");

        introBtn = new Button("/resources/help_click.png", Resource.SCREEN_WIDTH / 12, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_HEIGHT / 9);  //遊戲說明按鈕
        newGameBtn = new Button("/resources/newGame_click.png", Resource.SCREEN_WIDTH / 12 * 5, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);
        loadBtn = new Button("/resources/loading_click.png", Resource.SCREEN_WIDTH / 12 * 9, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);

        typeCheck = newGameCheck = false;
        countChar = 0;
        playerName = "";
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.background, 0, 0, null);
        introBtn.paint(g);
        newGameBtn.paint(g);
        loadBtn.paint(g);

        //顯示玩家輸入的字元
        Font font = Fontes.getBitFont(Resource.SCREEN_WIDTH / 25);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        
        //顯示玩家名稱窗口
        if (newGameCheck == true) {
            g.setColor(Color.BLACK);
            g.fillRect(Resource.SCREEN_WIDTH /2-Resource.SCREEN_WIDTH / 12 * 5/2-8, (int) (Resource.SCREEN_HEIGHT * 0.278f)-8, Resource.SCREEN_WIDTH / 12 * 5+16, (int) (Resource.SCREEN_HEIGHT * 0.389f)+16);
            
            g.setColor(new Color(186, 186, 186));
            g.fillRect(Resource.SCREEN_WIDTH /2-Resource.SCREEN_WIDTH / 12 * 5/2, (int) (Resource.SCREEN_HEIGHT * 0.278f), Resource.SCREEN_WIDTH / 12 * 5, (int) (Resource.SCREEN_HEIGHT * 0.389f));

            g.setColor(Color.WHITE);
            g.fillRect((int) (Resource.SCREEN_WIDTH * 0.375f), (int) (Resource.SCREEN_HEIGHT * 0.389f)+20, Resource.SCREEN_WIDTH / 12 * 3, (int) (Resource.SCREEN_HEIGHT * 0.055f));
            enterPlayerNameBtn.paint(g);
            
            g.setColor(Color.BLACK);
            int sw = fm.stringWidth("ENTER YOUR NAME");
            int sa = fm.getAscent();
            g.drawString("ENTER YOUR NAME",(int)(Resource.SCREEN_WIDTH *0.318f) , (int) (Resource.SCREEN_HEIGHT * 0.278f)+(int) (Resource.SCREEN_HEIGHT * 0.389f)-sa/2-(int) (Resource.SCREEN_HEIGHT * 0.278f));

        }
        
        g.setColor(new Color(0, 0, 0));
        int sw = fm.stringWidth(playerName);
        int sa = fm.getAscent();
        g.drawString(playerName, (int) (Resource.SCREEN_WIDTH * 0.375f) + Resource.SCREEN_WIDTH / 12 * 3 / 2 - sw / 2 + (int) (Resource.SCREEN_WIDTH * 5 / 1200), (int) (Resource.SCREEN_HEIGHT / 2) + Resource.SCREEN_HEIGHT * 50 / 900 / 2 - sa / 2 - Resource.SCREEN_HEIGHT * 60 / 900+15);

        if (typeCheck) {
            playerName = playerName + typeName;
            typeCheck = false;
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
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, introBtn)) {
                    introBtn.setImgState(1);
                    introBtn.setClickState(true);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, newGameBtn)) {
                    newGameBtn.setImgState(1);
                    newGameBtn.setClickState(true);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, loadBtn)) {
                    loadBtn.setImgState(1);
                    loadBtn.setClickState(true);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, enterPlayerNameBtn)) {
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
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, introBtn) && introBtn.getClickState()) {
                    introBtn.setImgState(0);
                    introBtn.setClickState(false);
                    gsChangeListener.changeScene(MainPanel.INTRO_SCENE);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, newGameBtn) && newGameBtn.getClickState()) {
                    enterPlayerNameBtn = new Button("/resources/clickBtn.png", Resource.SCREEN_WIDTH / 12 * 5, Resource.SCREEN_HEIGHT / 2, Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_HEIGHT / 9);
                    enterPlayerNameBtn.setLabel("ENTER");
                    newGameBtn.setImgState(0);
                    newGameBtn.setClickState(false);
                    newGameCheck = true;
                }

                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, loadBtn) && loadBtn.getClickState()) {
                    loadBtn.setClickState(false);
                    gsChangeListener.changeScene(MainPanel.LOAD_GAME_SCENE);
                }

                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, enterPlayerNameBtn) && enterPlayerNameBtn.getClickState()) {
                    enterPlayerNameBtn.setImgState(0);
                    if (countChar > 0) {
                        Player player = Player.getPlayerInstane();
                        player.setPlayerName(playerName);
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
                    if (countChar <= 6) {
                        countChar++;
                        typeCheck = true;
                    }
                }
                if (t == KeyEvent.VK_BACK_SPACE) {
                    countChar--;
                    if (countChar >= 0) {
                        playerName = playerName.substring(0, countChar);
                    }
                }
            }
        };
    }

    @Override
    public void logicEvent() {

    }

}
