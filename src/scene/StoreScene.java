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
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import vbattle.Button;
import vbattle.Fontes;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Player;
import vbattle.Resource;

/**
 *
 * @author menglinyang
 */
public class StoreScene extends Scene{
    
    
    private BufferedImage background;
    private ImgResource rc;
    private Button introBtn;
    private Button loadBtn;
    private Button newGameBtn;
    private Player player;

    
    public StoreScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
//      player = Player.getInstance();
        //測試新建一個player
        player = new Player();
        player.setInventory(9999);
        
        try {
            background = ImageIO.read(getClass().getResource("/resources/storeBg.png"));
        } catch (IOException ex) {
            Logger.getLogger(StoreScene.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        rc = ImgResource.getInstance();
        introBtn = new Button("/resources/help_click.png", 200, 100, (int) (Resource.SCREEN_WIDTH * 0.083f), (int) (Resource.SCREEN_HEIGHT * 0.667f));  //遊戲說明按鈕
        newGameBtn = new Button("/resources/newGame_click.png", 200, 100, (int) (Resource.SCREEN_WIDTH * 0.417f), (int) (Resource.SCREEN_HEIGHT * 0.667f));  //新遊戲按鈕
        loadBtn = new Button("/resources/loading_click.png", 200, 100, (int) (Resource.SCREEN_WIDTH * 0.75), (int) (Resource.SCREEN_HEIGHT * 0.667f));  //載入遊戲按鈕

    }
    
    @Override
    public MouseAdapter genMouseListener() {
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
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, newGameBtn)) {
                     gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                }
                
            }
            @Override
            public void mouseDragged(MouseEvent e){
                System.out.println("dragggggggg");
            }
            
            

        };
    }

    @Override
    public void paint(Graphics g) {
        //畫背景
        for(int i = 0; i < Resource.SCREEN_WIDTH/10;i++){
            
            g.drawImage(background,i*10,0,null);
        }
        
        //畫出player金錢
        Font font = Fontes.getBitFont(Resource.SCREEN_WIDTH/20);
        
//        Font font = new Font("DialogInput", Font.BOLD, Resource.SCREEN_WIDTH/20);
//        font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new java.io.File(Clazz.class.getResource("/resources/segoescb.ttf").toURI()));
        g.setFont(font);
        g.setColor(new Color(0,0,0));
        FontMetrics fm = g.getFontMetrics();
        String msg = this.player.getInventory()+"";
        int sw = fm.stringWidth(msg);
        int sa = fm.getAscent();
        g.drawString(msg, Resource.SCREEN_WIDTH-sw-20, sa);
        

    }

    @Override
    public void logicEvent() {
        
    }
    
    
}
