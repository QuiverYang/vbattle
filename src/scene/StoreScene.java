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
import vbattle.Button;
import vbattle.Button.Callback;
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
    
    
    private BufferedImage backgroundImg;
    private ImgResource rc;
    private final int BACK_BTN = 0;
    private final int BUY_BTN = 1;
    private final int LEFT_BTN = 2;
    private final int RIGHT_BTN = 3;
    private final int START_BTN = 4;
    private final int ITEM_ATTCK = 0;
    private final int ITEM_LEVEL = 1;
    private final int ITEM_FOOD = 2;
    private final int ITEM_STOCK = 3;
    private final int ITEM_ENTERTAINMENT = 4;
    private final int ITEM_ATTCK_PRICE = 100;
    private final int ITEM_LEVEL_PRICE = 200;
    private final int ITEM_FOOD_PRICE = 300;
    private final int ITEM_STOCK_PRICE = 400;
    private final int ITEM_ENTERTAINMENT_PRICE = 500;
    
    
    private final int funcBtnWidthUnit = 100;//functionBtn的一個單位大小
    private final int itemBtnWidthUnit = 150;//itemBtn的一個單位大小
    private final int padding = 20;//與邊框距離
    private int itemBtnXcenter;//選單圖片中心x座標
    private int rightBtnYcenter;//選單圖片中心y座標
    private boolean[] itemBtnsIsShown;//用來放大選單圖片
    private String[] itemBtnIconPaths;
    private int itemBtnNum=5;
    
    private Button[] functionBtns;
    private Button[] itemBtns ;
    private Player player;
    private int cost;//每個物品價值多少錢所要扣除player inventory的金額
    private int x0,y0,w0,h0,x1,y1,h1,w1,x2,y2,h2,w2;
    private int counter;
    

    
    public StoreScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        cost = 0;
        this.itemBtns = new Button[itemBtnNum];
        this.itemBtnsIsShown = new boolean[this.itemBtns.length];
        for(int i = 0; i < 3; i++){
            this.itemBtnsIsShown[i] = true;
        }
        this.itemBtnXcenter = Resource.SCREEN_WIDTH/2;
        this.rightBtnYcenter = (int)(Resource.SCREEN_HEIGHT*0.333f);
        x0 = itemBtnXcenter-itemBtnWidthUnit*2-2*padding;
        y0 = rightBtnYcenter-itemBtnWidthUnit/2;
        w0 = itemBtnWidthUnit;
        h0 = itemBtnWidthUnit;
        x1 = itemBtnXcenter - itemBtnWidthUnit;
        y1 = rightBtnYcenter-itemBtnWidthUnit;
        w1 = 2 * w0;
        h1 = 2 * h0;
        x2 = itemBtnXcenter + itemBtnWidthUnit+ 2*padding;
        y2 = y0;
        w2 = w0;
        h2 = h0;
        
        player = Player.getPlayerInstane();
        
        //測試player所擁有金額
        player.setInventory(10000);
        
        rc = ImgResource.getInstance();
        this.initFunctionBtns();
        this.initItemBtns();

    }
    
    private void initFunctionBtns(){
        //button建構子特殊 Button(String iconName, int width, int height, int x, int y)
        backgroundImg = rc.tryGetImage("/resources/storeBg.png");  //store background 圖片
        //初始化並放置functionBtns 位置
        this.functionBtns = new Button[5];
        this.functionBtns[this.BACK_BTN] = new Button("/resources/return_click.png",padding,padding,
                funcBtnWidthUnit, funcBtnWidthUnit);
        this.functionBtns[this.BACK_BTN].setCallback(new Callback() {
            @Override
            public void doSomthing() {
                try {
                    player.save();
                    gsChangeListener.changeScene(MainPanel.MENU_SCENE);
                } catch (IOException ex) {
                    System.out.println("player save problem from StorceScene back to MenuScene");;
                }
            }
        });
        this.functionBtns[this.BUY_BTN] = new Button("/resources/clickBtn.png",padding,Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.133f),
                funcBtnWidthUnit*2, funcBtnWidthUnit);//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
        this.functionBtns[this.BUY_BTN].setLabel("BUY");
        this.functionBtns[this.BUY_BTN].setCallback(new Callback() {
            @Override
            public void doSomthing() {
                for(int i = 0; i < itemBtns.length;i++){
                    if(itemBtns[i].isClicked()){
                        cost += itemBtns[i].getIntData();
                        //把被買的選項暫時選為false等扣完錢後再重新設定為true
                        itemBtns[i].setIsClicked(false);
                        itemBtns[i].setClickState(true);
                    }
                    
                }
            }
        });
        this.functionBtns[this.LEFT_BTN] = new Button("/resources/clickBtn.png", (int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);  
        this.functionBtns[this.LEFT_BTN].setLabel("<");
        counter = 1;
        this.functionBtns[this.LEFT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
                
                if(counter <= itemBtns.length-2){
                    if(counter !=0){
                        itemBtns[counter-1].setIsShown(false);
                    }
                    itemBtns[counter].reset(x0, y0, w0, h0);
                    itemBtns[counter+1].reset(x1, y1, w1, h1);
                    if(counter < itemBtns.length-2){
                        itemBtns[counter+2].reset(x2, y2, w2, h2);
                        itemBtns[counter+2].setIsShown(true);
                    }
                    itemBtns[counter].setIsClicked(false);
                    itemBtns[++counter].setIsClicked(true);
                }
            }
            
        });
        this.functionBtns[this.RIGHT_BTN] = new Button("/resources/clickBtn.png", (int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, (int) (Resource.SCREEN_HEIGHT * 0.333f)-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[this.RIGHT_BTN].setLabel(">");
        this.functionBtns[this.RIGHT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
                if(counter >= 1){
                    if(counter != itemBtns.length-1){
                        itemBtns[counter+1].setIsShown(false);
                    }
                    itemBtns[counter].reset(x2, y2, w2, h2);
                    itemBtns[counter-1].reset(x1, y1, w1, h1);
                    if(counter > 1){
                        itemBtns[counter-2].reset(x0, y0, w0, h0);
                        itemBtns[counter-2].setIsShown(true);
                    }
                    itemBtns[counter].setIsClicked(false);
                    itemBtns[--counter].setIsClicked(true);
                }
            }
            
        });

        this.functionBtns[this.START_BTN] = new Button("/resources/clickBtn.png",Resource.SCREEN_WIDTH-funcBtnWidthUnit*2-padding , Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.133f),//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        this.functionBtns[this.START_BTN].setLabel("START");
        this.functionBtns[this.START_BTN].setCallback(new Callback() {
            @Override
            public void doSomthing() {
                try {
                    player.save();
                } catch (IOException ex) {
                    System.out.println("player save problem from StorceScene back to MenuScene");;
                }
                //須將MainPanel.MENU_SCENE更改至開始遊戲的SCENE
                gsChangeListener.changeScene(MainPanel.MENU_SCENE);
            }
        });
    }
    
    private void initItemBtns(){
        //設定物品tiembtnIcon圖片
        this.itemBtnIconPaths = new String[itemBtnNum];
        this.itemBtnIconPaths[0] = "/resources/atk_up.png";
        this.itemBtnIconPaths[1] = "/resources/BlueSky.png";
        this.itemBtnIconPaths[2] = "/resources/Animal.png";
        this.itemBtnIconPaths[3] = "/resources/atk_up.png";
        this.itemBtnIconPaths[4] = "/resources/atk_up.png";
        
        //初始化並放置itemBtns 位置
        
        //設定第0個商品位置
        
        this.itemBtns[0] = new Button(this.itemBtnIconPaths[0],x0,y0,w0,h0);
        this.itemBtns[1] = new Button(this.itemBtnIconPaths[1],x1,y1,w1,h1);
        this.itemBtns[2] = new Button(this.itemBtnIconPaths[2],x2,y2,w2,h2);
        for(int i = 3; i < this.itemBtns.length;i++){
            this.itemBtns[i] = new Button(this.itemBtnIconPaths[i],x0,y0,w0,h0);
        }
        //設定初始商品為第0-2項為顯示
        for(int i = 0; i < 3; i++){
            this.itemBtns[i].setIsShown(true);
        }
        //clickState為是否在被選擇的圖片上（放大的那張）
        this.itemBtns[1].setIsClicked(true);
        //設定物品價格
        this.itemBtns[ITEM_ATTCK].setIntData(ITEM_ATTCK_PRICE);
        this.itemBtns[ITEM_LEVEL].setIntData(ITEM_LEVEL_PRICE);
        this.itemBtns[ITEM_FOOD].setIntData(ITEM_FOOD_PRICE);
        this.itemBtns[ITEM_STOCK].setIntData(ITEM_STOCK_PRICE);
        this.itemBtns[ITEM_ENTERTAINMENT].setIntData(ITEM_ENTERTAINMENT_PRICE);
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
                if(e.getButton() == MouseEvent.BUTTON1){
                    for(int i = 0; i < functionBtns.length; i++){
                        if(isOnBtn(e,functionBtns[i])){
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
                        if(isOnBtn(e,functionBtns[i])&&functionBtns[i].getClickState()){
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
        //畫背景
        for(int i = 0; i < Resource.SCREEN_WIDTH/10;i++){
            
            g.drawImage(backgroundImg,i*10,0,null);
        }
        //畫功能按鍵
        for(Button btn:functionBtns){
            btn.paint(g);
        }
        //畫選單
        for(Button btn:itemBtns){
            if(btn.isIsShown()){
                btn.paint2(g);
            }
        }
        
        //畫出player金錢
        Font fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH/20);
        
        Font fontDia = new Font("DialogInput", Font.BOLD, Resource.SCREEN_WIDTH/20);
        g.setFont(fontBit);
        g.setColor(new Color(0,0,0));
        FontMetrics fm = g.getFontMetrics();
        
        
        String msg = this.player.getInventory()+"";
        int sw = fm.stringWidth(msg);
        int sa = fm.getAscent();
        g.drawString(msg, Resource.SCREEN_WIDTH-sw-20, sa);
        
    }
    
    @Override
    public void logicEvent() {
        for(int i = 0; i < functionBtns.length; i++){
            if(functionBtns[i].isClicked()){
                System.out.println("func clicked");
                System.out.println("cost:"+cost);
                functionBtns[i].action();
                functionBtns[i].setIsClicked(false);
            }
        }
        if(cost != 0){
            int decreseSpeed = -10;//每針扣除錢的速度
            cost+=decreseSpeed;
            player.increaseInventory(decreseSpeed);
            if(cost==0){
                for(int i = 0; i < itemBtns.length;i++){
                    if(itemBtns[i].getClickState()){
                        itemBtns[i].setIsClicked(true);
                        itemBtns[i].setClickState(false);
                    }
                }
//                for(int i = 0; i < functionBtns.length; i++){
//                    functionBtns[i].setIsClicked(false);
//                }
            }
        }
        

    } 
}
