/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import scene.*;
import scene.storeScene.product.*;
import scene.storeScene.product.finProduct.*;
import scene.storeScene.product.stuffLvUp.StuffLevel;
import vbattle.Button;
import vbattle.Fontes;
import vbattle.ImgResource;
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Player;
import vbattle.Resource;

/**
 *
 * @author menglinyang
 */
public abstract class Store extends Scene{
    protected BufferedImage backgroundImg;
    protected ImgResource rc;
    protected int productsNum;
    protected Product[] products;
    protected int funcBtnWidthUnit = Resource.SCREEN_WIDTH/12;//functionBtn的一個單位寬大小
    protected int funcBtnHeightUnit = Resource.SCREEN_HEIGHT/9;//functionBtn的一個單位高
    protected int itemBtnWidthUnit = Resource.SCREEN_WIDTH/8;//itemBtn的一個單位大小
    protected int itemBtnHeightUnit = Resource.SCREEN_HEIGHT/6;//itemBtn的一個單位大小
   
    protected final int padding = 20;//與邊框距離
    protected int itemBtnXcenter;//選單圖片中心x座標
    protected int rightBtnYcenter;//選單圖片中心y座標
    protected String[] productsIconPaths; //商品路徑
    protected Button[] functionBtns; //功能鍵們
    protected Product[] productOnScreen;
    protected Player player;
    protected int costCash,costInventory;//每個物品價值多少錢所要扣除player inventory的金額
    protected int hpUp,mpUp;//每個物品所提升的hp mp;
    protected int[] pX,pY,pW,pH;//productsOnScreen的座標
    protected int counter;//按鍵左右去計算的counter
    protected Font fontBit,fontBit2,fontBitSmall;
    protected Font fontC,fontSmall;
    protected final AudioClip backgroundSound;
    
    public interface ButtomCode {
        int BACK_BTN = 0;
        int BUY_BTN = 1;
        int LEFT_BTN = 2;
        int RIGHT_BTN = 3;
        int START_BTN = 4;
        int SELL_BTN = 5;
    }
    
    public Store(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        backgroundSound = Applet.newAudioClip(getClass().getResource("/resources/storeMusic.wav"));
//        backgroundSound.loop();
        productOnScreen = new Product[3];
        player = Player.getPlayerInstane();
        rc = ImgResource.getInstance();
    }
    
    protected void initParameters(){
        backgroundImg = rc.tryGetImage("/resources/storebg.png");  //store background 圖片
        fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH/20);
        fontBit2 = Fontes.getBitFont(Resource.SCREEN_WIDTH/25);
        fontBitSmall = Fontes.getBitFont(Resource.SCREEN_WIDTH/60);
        fontC = new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/30);
        fontSmall= new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/40);
        this.itemBtnXcenter = Resource.SCREEN_WIDTH/2;
        this.rightBtnYcenter = (int)(Resource.SCREEN_HEIGHT*0.4f);
        pX=new int[3];
        pY=new int[3];
        pW=new int[3];
        pH=new int[3];
        pX[0] = itemBtnXcenter-itemBtnWidthUnit*2-2*padding;
        pY[0] = rightBtnYcenter-itemBtnHeightUnit/2;
        pW[0] = itemBtnWidthUnit;
        pH[0] = itemBtnHeightUnit;
        pX[1] = itemBtnXcenter - itemBtnWidthUnit;
        pY[1] = rightBtnYcenter-itemBtnHeightUnit;
        pW[1] = 2 * pW[0];
        pH[1] = 2 * pH[0];
        pX[2] = itemBtnXcenter + itemBtnWidthUnit+ 2*padding;
        pY[2] = pY[0];
        pW[2] = pW[0];
        pH[2] = pH[0];
    }
      
    public abstract void setProduct();
    
    public abstract void setFunctionBtns();
    
    public void initProductOnScreen(){
//      設定第0,1,2個商品位置
        if(products == null){
            System.out.println("product is null");
            return;
        }
        for(int i =0; i < productOnScreen.length;i++){
            if(i > products.length-1){
                this.productOnScreen[i] = products[0];
            }else{
                this.productOnScreen[i] = products[i];
            }
        }
    }
    
    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1){
                    for(int i = 0; i < functionBtns.length; i++){
                        if(Button.isOnBtn(e,functionBtns[i])){
                            functionBtns[i].setClickState(true);
                            functionBtns[i].setImgState(1);
                        }
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                
                if(e.getButton() == MouseEvent.BUTTON1){
                    for(int i = 0; i < functionBtns.length; i++){
                        functionBtns[i].setImgState(0);
                        functionBtns[i].setIsClicked(false);
                        if(Button.isOnBtn(e,functionBtns[i])&&functionBtns[i].getClickState()){
                            functionBtns[i].setClickState(false);
                            functionBtns[i].setIsClicked(true);
                        }
                        functionBtns[i].setClickState(false);
                    }
                }
            }
            
        };
    }

    @Override
    public void paint(Graphics g) {
        //=============畫背景=========================
        g.drawImage(backgroundImg, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT-20,0,0,backgroundImg.getWidth(),backgroundImg.getHeight(), null);//-20是mac視窗的按鈕列高度
        //=============畫功能按鍵=========================        
        for(int i = 0; i < functionBtns.length; i++){
            if(i == BuyScene.ButtomCode.LEFT_BTN || i == BuyScene.ButtomCode.RIGHT_BTN){
                functionBtns[i].setFont(fontBit);
            }else{
                functionBtns[i].setFont(fontBit2);
            }
        }
        for(Button btn:functionBtns){
            btn.paintBtn(g);
        }
        //=============畫選單========================= 
        for(Product p:productOnScreen){
            if(p != null){
                p.paint(g);
            }
        }
        //=============畫player金錢========================= 
        g.setFont(fontBit);
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        String asset = String.valueOf(this.player.getInventory());
        
        //=============畫出 asset=========================
        int sw = fm.stringWidth(asset);
        int sa = fm.getAscent();
        g.drawString(asset, (int)(Resource.SCREEN_WIDTH*0.85)-sw, (int)(Resource.SCREEN_HEIGHT*0.9));
        
        //=============畫出 cash=========================
        String cash = this.player.getCash()+"";
        sw = fm.stringWidth(cash);
        g.drawString(cash, (int)(Resource.SCREEN_WIDTH*0.4)-sw, (int)(Resource.SCREEN_HEIGHT*0.9));
        
        //=============畫出 hpmp=========================
        String pHp = this.player.getHp()+"";
        sw = fm.stringWidth(pHp);
        //=============畫出 hp=========================
        g.drawString(pHp, (int)(Resource.SCREEN_WIDTH*0.9)-sw, Resource.SCREEN_HEIGHT/13);
        
        //=============畫出 mp=========================
        String pMp = this.player.getMp()+"";
        sw = fm.stringWidth(pMp);
        g.drawString(pMp, (int)(Resource.SCREEN_WIDTH*0.9)-sw, Resource.SCREEN_HEIGHT/6);
        
        //=============畫出 info=========================
        if(products != null && products.length>1){
            String info= productOnScreen[1].getInfo();
            if(products[counter] instanceof FinProduct){
                g.setFont(fontSmall);
                int y = Resource.SCREEN_HEIGHT-sa-padding-(int)(Resource.SCREEN_HEIGHT*0.25f);
                int sh = g.getFontMetrics().getHeight();
                int i = 0;
                sw = fm.stringWidth(info.split("  ")[0]);
                for (String line : info.split("  ")){
                    g.drawString(line, (int)(Resource.SCREEN_WIDTH*0.53)-sw/4,y);
                    y += sh;
                    if(i ==1){
                        FinProduct temp = (FinProduct)products[counter];
                        g.drawString("剩餘價值:"+temp.getValue(), (int)(Resource.SCREEN_WIDTH*0.53)-sw/4,y);                    }
                    i++;
                }

            }else{
                g.setFont(fontC);
                sw = fm.stringWidth(info);
                g.drawString(info, (int)(Resource.SCREEN_WIDTH*0.5)-sw/4, Resource.SCREEN_HEIGHT-sa-padding-(int)(Resource.SCREEN_HEIGHT*0.24f));
                g.drawString("價格:"+productOnScreen[1].getPrice(), (int)(Resource.SCREEN_WIDTH*0.5)-sw/4, Resource.SCREEN_HEIGHT-padding-(int)(Resource.SCREEN_HEIGHT*0.24f));
                if(products[counter] instanceof StuffLevel){
                    g.setFont(fontBitSmall);
                    String name= products[counter].getName();
                    int lv;
                    
                    lv =player.getUnlock()[counter-6];//第一隻怪物商品放在第六個位置
                    
                    g.setColor(Color.red);
                    String msg = "lv"+lv;
                    sw = fm.stringWidth(msg);
                    
                    g.drawString(msg, products[counter].getX()+products[counter].getWidth()-sw/2, products[counter].getY()+20);
                }
            }   
        }
    }
    
    
    @Override
    public void logicEvent() {
        for(int i = 0; i < functionBtns.length; i++){
            if(functionBtns[i].isClicked()){
                functionBtns[i].action();
                functionBtns[i].setIsClicked(false);
            }
        }
        //處理扣錢
        changePlayerCash();
        changePlayerHp();
        changePlayerMp();
        changePlayerInventory();
        changeProductSeq();
        this.resize();
    } 
    
    public void resize(){
        if(funcBtnWidthUnit!= Resource.SCREEN_WIDTH/12||itemBtnWidthUnit != Resource.SCREEN_WIDTH/8
                ||funcBtnHeightUnit !=Resource.SCREEN_HEIGHT/9 ||itemBtnHeightUnit != Resource.SCREEN_HEIGHT/6){
            System.out.println("different size");
            funcBtnWidthUnit = Resource.SCREEN_WIDTH/12;//functionBtn的一個單位寬大小
            funcBtnHeightUnit = Resource.SCREEN_HEIGHT/9;//functionBtn的一個單位高
            itemBtnWidthUnit = Resource.SCREEN_WIDTH/8;//itemBtn的一個單位大小
            itemBtnHeightUnit = Resource.SCREEN_HEIGHT/6;//itemBtn的一個單位大小
            
            this.functionBtns[BuyScene.ButtomCode.BACK_BTN].reset(padding,padding,
                    funcBtnWidthUnit, funcBtnHeightUnit);
            this.functionBtns[BuyScene.ButtomCode.BUY_BTN].reset(padding,Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),
                    funcBtnWidthUnit*2, funcBtnHeightUnit);
            this.functionBtns[BuyScene.ButtomCode.LEFT_BTN].reset((int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                    funcBtnWidthUnit/2, funcBtnHeightUnit);
            this.functionBtns[BuyScene.ButtomCode.RIGHT_BTN].reset((int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                    funcBtnWidthUnit/2, funcBtnHeightUnit);
            this.functionBtns[BuyScene.ButtomCode.START_BTN].reset(padding +functionBtns[BuyScene.ButtomCode.BACK_BTN].getWidth()+padding , padding,//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                    funcBtnWidthUnit*2, funcBtnHeightUnit);
            this.functionBtns[BuyScene.ButtomCode.SELL_BTN].reset(Resource.SCREEN_WIDTH-funcBtnWidthUnit*2-padding , Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                    funcBtnWidthUnit*2, funcBtnHeightUnit);
            for(Button btn : functionBtns){
                btn.setLabelSize(btn.getWidth());
            }
            this.functionBtns[BuyScene.ButtomCode.LEFT_BTN].setLabelSize(functionBtns[BuyScene.ButtomCode.LEFT_BTN].getWidth()*4);
            this.functionBtns[BuyScene.ButtomCode.RIGHT_BTN].setLabelSize(functionBtns[BuyScene.ButtomCode.RIGHT_BTN].getWidth()*4);
            initParameters();
        }

    }
    
    protected void changePlayerCash(){
        if(costCash != 0){
            int decreseSpeed = -10;//每針扣除錢的速度
            costCash+=decreseSpeed;
            player.increaseCash(decreseSpeed);
        }
    }
    
    private void changePlayerHp(){
        if(hpUp != 0){
            int increseSpeed = 2;//每針加血的速度
            hpUp-=increseSpeed;
            player.increaseHp(increseSpeed);
            if(player.getHp()>100){
                player.setHp(100);
            }
        }
    }
    
    private void changePlayerMp(){
        if(mpUp!=0){
            int increaseSpeed = 2;
            mpUp-=increaseSpeed;
            player.increaseMp(increaseSpeed);
            if(player.getMp()>100){
                player.setMp(100);
            }
        }
    }
    
    private void changePlayerInventory(){
        if(costInventory!=0){
            int increaseSpeed = 10;
            costInventory-=increaseSpeed;
            player.increaseInventory(-increaseSpeed);
        }
    }
    
    protected void changeProductSeq(){
        if(products != null){
            for(int i = 0; i < pX.length;i++){
                if(productsNum-i>0){
                    this.productOnScreen[i].reset(pX[i], pY[i], pW[i], pH[i]);
                }
            }
        }
        
    }
}
