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
        introBtn = new Button("/resources/help_click.png",(int) (Resource.SCREEN_WIDTH * 0.083f), (int) (Resource.SCREEN_HEIGHT * 0.667f),200,100);  //遊戲說明按鈕
        newGameBtn = new Button("/resources/newGame_click.png",(int) (Resource.SCREEN_WIDTH * 0.417f), (int) (Resource.SCREEN_HEIGHT * 0.667f),200,100);  //新遊戲按鈕
        loadBtn = new Button("/resources/loading_click.png",(int) (Resource.SCREEN_WIDTH * 0.75), (int) (Resource.SCREEN_HEIGHT * 0.667f),200,100);  //載入遊戲按鈕
        enterPlayerNameBtn = new Button("/resources/clickBtn.png", 500, 550,200,100);

        typeCheck = newGameCheck = false;
        countChar = 0;
        playerName = "";
    }

    @Override
    public void paint(Graphics g) {
        introBtn.paint(g);
        newGameBtn.paint(g);
        loadBtn.paint(g);

        //顯示玩家輸入的字元
        Font font = Fontes.getBitFont(Resource.SCREEN_WIDTH / 25);
        g.setFont(font);
        g.setColor(new Color(0, 0, 0));
        
        FontMetrics fm = g.getFontMetrics();
        //顯示玩家名稱窗口
        if (newGameCheck == true) {
            g.setColor(Color.BLUE);
            g.fillRect(400, 350, 400, 350);

            g.setColor(Color.WHITE);
            g.fillRect(450, 450, 300, 50);
            enterPlayerNameBtn.paint(g);
            int sw1 = fm.stringWidth("ENTER");
            int sa1 = fm.getAscent();
            g.setColor(Color.BLACK);
            g.drawString("ENTER", enterPlayerNameBtn.getX() + enterPlayerNameBtn.getImgWidth() / 2 - sw1 / 2 +5, enterPlayerNameBtn.getY() + enterPlayerNameBtn.getImgHeight() / 2 - sa1 / 2+40 );
        }
        
        
        int sw = fm.stringWidth(playerName);
        int sa = fm.getAscent();
        g.drawString(playerName, 450 + 300 / 2 - sw / 2 + 5, 450 + 50 / 2 - sa / 2 + 30);
        
        if (typeCheck) {
            System.out.println("type");
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
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, newGameBtn)) {
                    newGameBtn.setImgState(1);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, loadBtn)) {
                    loadBtn.setImgState(1);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, enterPlayerNameBtn)) {
                    enterPlayerNameBtn.setImgState(1);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    newGameBtn.setImgState(0);
                    loadBtn.setImgState(0);
                    introBtn.setImgState(0);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, introBtn)) {
                    introBtn.setImgState(0);
                    gsChangeListener.changeScene(MainPanel.INTRO_SCENE);
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, newGameBtn)) {
                    newGameBtn.setImgState(0);
                    newGameCheck = true;
                }

                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, loadBtn)) {
                    gsChangeListener.changeScene(MainPanel.LOAD_GAME_SCENE);
                }

                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, enterPlayerNameBtn)) {
                    enterPlayerNameBtn.setImgState(0);
                    Player player = Player.getPlayerInstane();
                    player.setPlayerName(playerName);
                    gsChangeListener.changeScene(MainPanel.STORE_SCENE);
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
// 以下是更新按鈕位置相對於螢幕大小        
//        introBtn = new Button("/resources/help_click.png",,,200,100);  //遊戲說明按鈕
//        newGameBtn = new Button("/resources/newGame_click.png",, ,200,100);  //新遊戲按鈕
//        loadBtn = new Button("/resources/loading_click.png",(int) (Resource.SCREEN_WIDTH * 0.75), (int) (Resource.SCREEN_HEIGHT * 0.667f),200,100);  //載入遊戲按鈕
        
//        introBtn.setX((int) (Resource.SCREEN_WIDTH * 0.083f));
//        introBtn.setY( (int) (Resource.SCREEN_HEIGHT * 0.667f));
//        newGameBtn.setX((int) (Resource.SCREEN_WIDTH * 0.417f));
//        newGameBtn.setY((int) (Resource.SCREEN_HEIGHT * 0.667f));
//        loadBtn.setX((int) (Resource.SCREEN_WIDTH * 0.75f)); 
//        loadBtn.setY((int)(Resource.SCREEN_HEIGHT * 0.667f));

    }

}
