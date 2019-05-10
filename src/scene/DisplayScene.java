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
import vbattle.Coin;
import vbattle.Ghost;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Resource;

/**
 *
 * @author menglinyang
 */
public class DisplayScene extends StageScene{
    
    private String hint1,typing,mainHint;
    private int counter,mouseCounter,sw;
    private  Font fontC,fontMain;
    private  BufferedImage mouseImg,redImg;
    private  ImgResource rc;
    private int x0,y0,movingIconX,movingIconY,step,width;
    private boolean animationFinished;
    private final Coin coin;
    private final Ghost ghost;
    
    public DisplayScene(GameStatusChangeListener gsChangeListener){
        super(gsChangeListener);
        
        typing = "";
        fontC = new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/40);
        fontMain = new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/20);
        rc = ImgResource.getInstance();
        mouseImg = rc.tryGetImage("/resources/mouse.png");
        redImg = rc.tryGetImage("/resources/tinyCharacters.png");
        movingIconX = x0 = (int)(Resource.SCREEN_WIDTH * 0.3f);
        movingIconY = y0 = (int) (Resource.SCREEN_HEIGHT * 0.8f);
        step = 1;
        width = Resource.SCREEN_WIDTH/12;
        coin = new Coin(Resource.SCREEN_WIDTH/5*4,Resource.SCREEN_HEIGHT/5*3,width,width);
        ghost = new Ghost(Resource.SCREEN_WIDTH/5*3,Resource.SCREEN_HEIGHT/5*3,width,width);
        
    }
    @Override
    public MouseAdapter genMouseAdapter() {
        super.genMouseAdapter();
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(Coin.isOnCoin(e, coin)){
                    Coin.CoinClicked(getClass().getResource("/resources/coin.wav"));
                    animationFinished = true;
                }
                if(Ghost.isOnGhost(e, ghost)){
                    Ghost.ghostClicked(getClass().getResource("/resources/pop.wav"));
                    animationFinished = true;
                }
                if(animationFinished){
                    step++;
                    animationFinished = false;
                    
                }
                if(step==5){
                    StageScene.stageBgm.stop();
                    gsChangeListener.changeScene(MainPanel.STAGE_SCENE);
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(fontMain);
        g.setColor(Color.WHITE);
        FontMetrics fm2 = g.getFontMetrics();
        if(step <4){
            mainHint = "操作說明";
        }else{
            mainHint = "準備完成";
        }
        int sw2 = fm2.stringWidth(mainHint);
        g.drawString(mainHint, Resource.SCREEN_WIDTH/2-sw2/2, Resource.SCREEN_HEIGHT/3);
        switch(step){
            case 1:
                paintDraging(g);
                break;
            case 2:
                paintCoin(g);
                break;
            case 3:
                paintGhost(g);
                break;
            case 4:
                paintReady(g);
                break;
        }
    }
    
    @Override
    public void logicEvent() {
        resize();
    }
    
    private void paintDraging(Graphics g){
        g.setFont(fontC);
        FontMetrics fm = g.getFontMetrics();
        hint1 = "拖曳細胞";
        sw = fm.stringWidth(hint1);
        counter++;

        if(counter %1 ==0 && mouseCounter < 3){//mouseCounter計算移動次數 設定為3次
            //設計x y 移動比例為1:2
            x0-=4;
            y0-=8;
            if(x0 <= movingIconX-100 && y0 <= movingIconY-200 ){// 100,200的部分需修改至正確位置
                mouseCounter++;
                if(mouseCounter != 3){
                    x0 = movingIconX;
                    y0 = movingIconY;
                }
            }
        }else if(mouseCounter >=3){
            // 100,200的部分需修改至正確位置
            x0 = movingIconX-100;
            y0 = movingIconY -200;
            this.animationFinished = true;
        }
        g.drawString(hint1, x0+50-sw/2, y0+3);
        g.drawImage(redImg, x0, y0, x0+width, y0+width, 0, 0, 32, 32, null);
        g.drawImage(mouseImg,x0+width/2, y0+width/2, x0+width/2+width/3, y0+width/2+width/3, 0, 0, 200, 200, null);
    }
    
    public void paintCoin(Graphics g){
        g.setFont(fontC);
        FontMetrics fm = g.getFontMetrics();
        hint1 = "點擊金幣 獲得金錢";
        sw = fm.stringWidth(hint1);
        counter++;
        coin.paint(g);
        g.drawString(hint1, coin.getX()+coin.getWidth()/2-sw/2, coin.getY()-3);

    }
    
    public void paintGhost(Graphics g){
        g.setFont(fontC);
        FontMetrics fm = g.getFontMetrics();
        hint1 = "細菌消滅後 點擊惡魔 避免受傷";
        sw = fm.stringWidth(hint1);

        counter++;
        ghost.paint(g);
        g.drawString(hint1, ghost.getX()+ghost.getWidth()/2-sw/2, ghost.getY()-3);
    }
    
    public void paintReady(Graphics g){
        g.setFont(fontMain);
        FontMetrics fm = g.getFontMetrics();
        int sw2 = fm.stringWidth("點擊後遊戲開始");
        int sa = fm.getHeight();
        g.drawString("點擊後遊戲開始", Resource.SCREEN_WIDTH/2-sw2/2, Resource.SCREEN_HEIGHT/3+sa);
        animationFinished = true;
    }
    
    @Override
    public void resize(){
        super.resize();
        fontC = new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/40);
        fontMain = new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/20);
        movingIconX = (int)(Resource.SCREEN_WIDTH * 0.3f);
        movingIconY = (int) (Resource.SCREEN_HEIGHT * 0.8f);
        width = Resource.SCREEN_WIDTH/12;
        coin.reset(Resource.SCREEN_WIDTH/5*4,Resource.SCREEN_HEIGHT/5*3,width,width);
        ghost.reset(Resource.SCREEN_WIDTH/5*3,Resource.SCREEN_HEIGHT/5*3,width,width);
    }
}
