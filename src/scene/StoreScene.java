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
import java.io.IOException;
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
    
    
    private int funcBtnWidthUnit = Resource.SCREEN_WIDTH/12;//functionBtn的一個單位大小
    private int itemBtnWidthUnit = Resource.SCREEN_WIDTH/8;//itemBtn的一個單位大小
    private final int padding = 20;//與邊框距離
    private int itemBtnXcenter;//選單圖片中心x座標
    private int rightBtnYcenter;//選單圖片中心y座標
    private boolean[] itemBtnsIsShown;//用來放大選單圖片
    private String[] itemBtnIconPaths;
    private int itemsNum=5;
    private int[] itemsPrice;
    
    private Button[] functionBtns;
    private Button[] itemBtns ;
    private Player player;
    private int cost;//每個物品價值多少錢所要扣除player inventory的金額
    private int x0,y0,w0,h0,x1,y1,h1,w1,x2,y2,h2,w2;
    private int counter;
    Font fontBit;
    

    
    public StoreScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        cost = 0;
        this.itemBtns = new Button[3];
        this.itemBtnsIsShown = new boolean[this.itemBtns.length];
        for(int i = 0; i < 3; i++){
            this.itemBtnsIsShown[i] = true;
        }
        this.initParameters();
        
        player = Player.getPlayerInstane();
        
        //測試player所擁有金額
        player.setInventory(10000);
        
        rc = ImgResource.getInstance();
        this.initFunctionBtns();
        this.initItemBtns();

    }
    
    private void initParameters(){
        fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH/20);
        this.itemBtnXcenter = Resource.SCREEN_WIDTH/2;
        this.rightBtnYcenter = (int)(Resource.SCREEN_HEIGHT*0.333f);
        this.itemsPrice = new int[itemsNum];
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
        //設定物品價格
        itemsPrice[ITEM_ATTCK] = ITEM_ATTCK_PRICE;
        itemsPrice[ITEM_LEVEL] = ITEM_LEVEL_PRICE;
        itemsPrice[ITEM_FOOD] = ITEM_FOOD_PRICE;
        itemsPrice[ITEM_STOCK] = ITEM_STOCK_PRICE;
        itemsPrice[ITEM_ENTERTAINMENT] = ITEM_ENTERTAINMENT_PRICE;
        
        //設定物品tiembtnIcon圖片
        this.itemBtnIconPaths = new String[itemsNum];
        this.itemBtnIconPaths[0] = "/resources/atk_up.png";
        this.itemBtnIconPaths[1] = "/resources/BlueSky.png";
        this.itemBtnIconPaths[2] = "/resources/Animal.png";
        this.itemBtnIconPaths[3] = "/resources/atk_up.png";
        this.itemBtnIconPaths[4] = "/resources/BlueSky.png";
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
                cost += itemsPrice[counter];
                    //把被買的選項暫時選為false等扣完錢後再重新設定為true
//                    itemBtns[1].setIsClicked(false);
//                    itemBtns[1].setClickState(true);
            }
        });
        this.functionBtns[this.LEFT_BTN] = new Button("/resources/clickBtn.png", (int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);  
        this.functionBtns[this.LEFT_BTN].setLabel("<");
        counter = 1;
        this.functionBtns[this.LEFT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
                if(counter < itemsPrice.length-2){
                    for (Button itemBtn : itemBtns) {
                        itemBtn.setIsShown(true);
                    }
                    counter++;
                    System.out.println("left"+counter);
                    itemBtns[0].changeIcon(itemBtnIconPaths[counter-1]);
                    itemBtns[1].changeIcon(itemBtnIconPaths[counter]);
                    itemBtns[2].changeIcon(itemBtnIconPaths[counter+1]);
                }
                if(counter == itemsPrice.length-2){
                    counter++;
                    System.out.println("到最後一個選項了");
                    itemBtns[0].changeIcon(itemBtnIconPaths[counter-1]);
                    itemBtns[1].changeIcon(itemBtnIconPaths[counter]);
                    itemBtns[2].setIsShown(false);
                }
                
            }
            
        });
        this.functionBtns[this.RIGHT_BTN] = new Button("/resources/clickBtn.png", (int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, (int) (Resource.SCREEN_HEIGHT * 0.333f)-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[this.RIGHT_BTN].setLabel(">");
        this.functionBtns[this.RIGHT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
                if(counter > 1){
                    for (Button itemBtn : itemBtns) {
                        itemBtn.setIsShown(true);
                    }
                    counter--;
                    System.out.println("right" + counter);
                    itemBtns[0].changeIcon(itemBtnIconPaths[counter-1]);
                    itemBtns[1].changeIcon(itemBtnIconPaths[counter]);
                    itemBtns[2].changeIcon(itemBtnIconPaths[counter+1]);
                }
                if(counter == 1){
                    counter--;
                    System.out.println("到第一個選項了");
                    itemBtns[0].setIsShown(false);
                    itemBtns[1].changeIcon(itemBtnIconPaths[counter]);
                    itemBtns[2].changeIcon(itemBtnIconPaths[counter+1]);
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
        
        
        
        //設定第0個商品位置
        this.itemBtns[0] = new Button(this.itemBtnIconPaths[0],x0,y0,w0,h0);
        this.itemBtns[1] = new Button(this.itemBtnIconPaths[1],x1,y1,w1,h1);
        this.itemBtns[2] = new Button(this.itemBtnIconPaths[2],x2,y2,w2,h2);
        for (Button itemBtn : itemBtns) {
            itemBtn.setIsShown(true);
        }
        //clickState為是否在被選擇的圖片上（放大的那張）
        this.itemBtns[1].setIsClicked(true);
        
        
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
                System.out.println("logicEvent clicked");
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
        this.resize();
    } 
    private void resize(){
        
        funcBtnWidthUnit = Resource.SCREEN_WIDTH/12;//functionBtn的一個單位大小     
        itemBtnWidthUnit = Resource.SCREEN_WIDTH/8;//itemBtn的一個單位大小 
        this.initParameters();
        
        this.functionBtns[this.BACK_BTN].reset(padding,padding,
                funcBtnWidthUnit, funcBtnWidthUnit);
        this.functionBtns[this.BUY_BTN].reset(padding,Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.133f),
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        this.functionBtns[this.LEFT_BTN].reset((int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[this.RIGHT_BTN].reset((int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, (int) (Resource.SCREEN_HEIGHT * 0.333f)-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[this.START_BTN].reset(Resource.SCREEN_WIDTH-funcBtnWidthUnit*2-padding , Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.133f),//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        for(Button btn : functionBtns){
            btn.setLabelSize(btn.getWidth());
        }
        this.functionBtns[this.LEFT_BTN].setLabelSize(functionBtns[LEFT_BTN].getWidth()*4);
        this.functionBtns[this.RIGHT_BTN].setLabelSize(functionBtns[RIGHT_BTN].getWidth()*4);
        this.itemBtns[0].reset(x0,y0,w0,h0);
        this.itemBtns[1].reset(x1,y1,w1,h1);
        this.itemBtns[2].reset(x2,y2,w2,h2);
//        for(int i = 3; i < this.itemBtns.length;i++){
//            this.itemBtns[i].reset(x0,y0,w0,h0);
//        }
        
        
        
    }
}
