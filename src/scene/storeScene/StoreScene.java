/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import scene.Scene;
import vbattle.Button;
import vbattle.Button.Callback;
import vbattle.FinProduct;
import vbattle.Fontes;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Player;
import vbattle.Product;
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
    
    private int itemsNum=6;
    
    private final int ITEM_HAMBURGER = 0;
    private final int ITEM_NOODLE = 1;
    private final int ITEM_CHIKEN = 2;
    private final int ITEM_TV = 3;
    private final int ITEM_TRAVEL = 4;
    private final int ITEM_STOCK = 5;
    
    private int[] itemsPrice;
    private final int ITEM_HAMBURGER_PRICE = 100;
    private final int ITEM_NOODLE_PRICE = 200;
    private final int ITEM_CHIKEN_PRICE = 300;
    private final int ITEM_TV_PRICE = 400;
    private final int ITEM_TRAVEL_PRICE = 500;
    private final int ITEM_STOCK_PRICE = 900;
    
    private String[] itemsInfo;
    private Product[] products;
    
    private int funcBtnWidthUnit = Resource.SCREEN_WIDTH/12;//functionBtn的一個單位大小
    private int itemBtnWidthUnit = Resource.SCREEN_WIDTH/8;//itemBtn的一個單位大小
    private final int padding = 20;//與邊框距離
    private int itemBtnXcenter;//選單圖片中心x座標
    private int rightBtnYcenter;//選單圖片中心y座標
    private String[] itemBtnIconPaths;
    private Button[] functionBtns;
    private Button[] itemBtns ;
    private Product[] productOnScreen;
    private Player player;
    private int costCash;//每個物品價值多少錢所要扣除player inventory的金額
    private int hpUp,mpUp,valueUp;//每個物品所提升的hp mp inventory's value;
    private int x0,y0,w0,h0,x1,y1,h1,w1,x2,y2,h2,w2;
    private int counter;
    Font fontBit;
    

    
    public StoreScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        costCash = 0;
//        this.itemBtns = new Button[3];
        this.productOnScreen = new Product[3];
        this.initParameters();
        
        player = Player.getPlayerInstane();
        
        //測試player所擁有金額
//        player.setInventory(20000);
//        player.setCash(6000);
//        player.setHp(100);
//        player.setMp(70);
        
        rc = ImgResource.getInstance();
        this.initFunctionBtns();
        this.initItemBtns();

    }
    
    private void initParameters(){
        fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH/20);
        this.itemBtnXcenter = Resource.SCREEN_WIDTH/2;
        this.rightBtnYcenter = (int)(Resource.SCREEN_HEIGHT*0.4f);
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
        this.itemsPrice = new int[itemsNum];
        itemsPrice[ITEM_HAMBURGER] = ITEM_HAMBURGER_PRICE;
        itemsPrice[ITEM_NOODLE] = ITEM_NOODLE_PRICE;
        itemsPrice[ITEM_CHIKEN] = ITEM_CHIKEN_PRICE;
        itemsPrice[ITEM_TV] = ITEM_TV_PRICE;
        itemsPrice[ITEM_TRAVEL] = ITEM_TRAVEL_PRICE;
        itemsPrice[ITEM_STOCK] = ITEM_STOCK_PRICE;
        
        //設定物品tiembtnIcon圖片
        this.itemBtnIconPaths = new String[itemsNum];
        this.itemBtnIconPaths[ITEM_HAMBURGER] = "/resources/hamburger.jpg";
        this.itemBtnIconPaths[ITEM_NOODLE] = "/resources/noodle.jpg";
        this.itemBtnIconPaths[ITEM_CHIKEN] = "/resources/chiken.jpg";
        this.itemBtnIconPaths[ITEM_TV] = "/resources/tv.jpg";
        this.itemBtnIconPaths[ITEM_TRAVEL] = "/resources/travel.jpg";
        this.itemBtnIconPaths[ITEM_STOCK] = "/resources/stock.jpg";
        
        //設定物品產品介紹
        this.itemsInfo = new String[itemsNum];
        this.itemsInfo[ITEM_HAMBURGER] = "漢堡:HP+20,MP+10";
        this.itemsInfo[ITEM_NOODLE] = "麵食:HP+30";
        this.itemsInfo[ITEM_CHIKEN] = "雞腿:HP+40";
        this.itemsInfo[ITEM_TV] = "電視:MP+20";
        this.itemsInfo[ITEM_TRAVEL] = "旅遊:MP+30";
        this.itemsInfo[ITEM_STOCK] = "股票:購買一張股票風險0.30 利潤0.08";
        
        //建立產品
        products = new Product[itemsNum];
        for(int i = 0; i < itemsNum; i++){
            String pName = itemsInfo[i].substring(0, 2);
            int price = itemsPrice[i];
            String info = itemsInfo[i];
            int indexOfHp = itemsInfo[i].indexOf("HP+");
            int indexOfMp = itemsInfo[i].indexOf("MP+");
            //判斷是否為金融商品
            if(indexOfHp == -1 && indexOfMp == -1){
                int indexOfRisk = itemsInfo[i].indexOf("風險");
                int indexOfProfit = itemsInfo[i].indexOf("利潤");
                double risk = Double.parseDouble(itemsInfo[i].substring(indexOfRisk+2, indexOfRisk+6));
                double profit = Double.parseDouble(itemsInfo[i].substring(indexOfProfit+2, indexOfProfit+6));
                products[i] = new FinProduct(itemBtnIconPaths[i],pName,price,risk,profit,itemsInfo[i]);
            }else{
                int pHp, pMp;
                if(indexOfHp != -1){
                    pHp = Integer.parseInt(itemsInfo[i].substring(indexOfHp+3, indexOfHp+5));
                }else{
                    pHp = 0;
                }
                if(indexOfMp != -1){
                    pMp = Integer.parseInt(itemsInfo[i].substring(indexOfMp+3, indexOfMp+5));
                }else{
                    pMp = 0;
                }
                products[i] = new Product(itemBtnIconPaths[i],pName,price,info);
                products[i].setHp(pHp);
                products[i].setMp(pMp);
            }
        }

        
    }
    
    private void initFunctionBtns(){
        //button建構子特殊 Button(String iconName, int width, int height, int x, int y)
        backgroundImg = rc.tryGetImage("/resources/storebg.png");  //store background 圖片
        //初始化並放置functionBtns 位置
        this.functionBtns = new Button[5];
        this.functionBtns[this.BACK_BTN] = new Button("/resources/return_blue.png");
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
        this.functionBtns[this.BUY_BTN] = new Button("/resources/clickBtn_blue.png");//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
        this.functionBtns[this.BUY_BTN].setLabel("BUY");
        this.functionBtns[this.BUY_BTN].setCallback(new Callback() {
            @Override
            public void doSomthing() {
                costCash += itemsPrice[counter];
                costCash += productOnScreen[counter].getPrice();
                
                 
            }
        });
        this.functionBtns[this.LEFT_BTN] = new Button("/resources/clickBtn_blue.png");  
        this.functionBtns[this.LEFT_BTN].setLabel("<");
        counter = 1;
        this.functionBtns[this.LEFT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
                if(counter < itemsPrice.length-2){
//                    for (Button itemBtn : itemBtns) {
//                        itemBtn.setIsShown(true);
//                    }
                    for(Product p : productOnScreen){
                        p.setIsShown(true);
                    }
                    counter++;
                    System.out.println("left"+counter);
//                    itemBtns[0].changeIcon(itemBtnIconPaths[counter-1]);
//                    itemBtns[1].changeIcon(itemBtnIconPaths[counter]);
//                    itemBtns[2].changeIcon(itemBtnIconPaths[counter+1]);
                    productOnScreen[0] = products[counter-1];
                    productOnScreen[1] = products[counter];
                    productOnScreen[2] = products[counter+1];
                }
                else if(counter == itemsPrice.length-2){
                    counter++;
                    System.out.println("到最後一個選項了");
//                    itemBtns[0].changeIcon(itemBtnIconPaths[counter-1]);
//                    itemBtns[1].changeIcon(itemBtnIconPaths[counter]);
                    productOnScreen[0] = products[counter-1];
                    productOnScreen[1] = products[counter];
//                    itemBtns[2].setIsShown(false);
                    productOnScreen[2].setIsShown(false);
                }
                
            }
            
        });
        this.functionBtns[this.RIGHT_BTN] = new Button("/resources/clickBtn_blue.png");
        this.functionBtns[this.RIGHT_BTN].setLabel(">");
        this.functionBtns[this.RIGHT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
                if(counter > 1){
//                    for (Button itemBtn : itemBtns) {
//                        itemBtn.setIsShown(true);
//                    }
                    for(Product p : productOnScreen){
                        p.setIsShown(true);
                    }
                    counter--;
                    System.out.println("right" + counter);
//                    itemBtns[0].changeIcon(itemBtnIconPaths[counter-1]);
//                    itemBtns[1].changeIcon(itemBtnIconPaths[counter]);
//                    itemBtns[2].changeIcon(itemBtnIconPaths[counter+1]);
                    productOnScreen[0] = products[counter-1];
                    productOnScreen[1] = products[counter];
                    productOnScreen[2] = products[counter+1];
                }
                else if(counter == 1){
                    counter--;
                    System.out.println("到第一個選項了");
                    productOnScreen[2] = products[counter+1];
                    productOnScreen[1] = products[counter];
                    productOnScreen[0].setIsShown(false);
//                    itemBtns[0].setIsShown(false);
//                    itemBtns[1].changeIcon(itemBtnIconPaths[counter]);
//                    itemBtns[2].changeIcon(itemBtnIconPaths[counter+1]);
                }
            }
            
        });

        this.functionBtns[this.START_BTN] = new Button("/resources/clickBtn_blue.png");
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
        
        
        
//        //設定第0個商品位置
//        this.itemBtns[0] = new Button(this.itemBtnIconPaths[0]);
//        this.itemBtns[1] = new Button(this.itemBtnIconPaths[1]);
//        this.itemBtns[2] = new Button(this.itemBtnIconPaths[2]);
//        for (Button itemBtn : itemBtns) {
//            itemBtn.setIsShown(true);
//        }
//        //clickState為是否在被選擇的圖片上（放大的那張）
//        this.itemBtns[1].setIsClicked(true);
        this.productOnScreen[0] = products[0];
        this.productOnScreen[1] = products[1];
        this.productOnScreen[2] = products[2];
        
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
        //畫背景
        g.drawImage(backgroundImg, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT-20,0,0,backgroundImg.getWidth(),backgroundImg.getHeight(), null);//-20是mac視窗的按鈕列高度
        //畫功能按鍵
        for(Button btn:functionBtns){
            btn.paintBtn(g);
        }
        //畫選單
//        for(Button btn:itemBtns){
//            if(btn.isIsShown()){
//                btn.paint(g);
//            }
//        }
        for(Product p:productOnScreen){
            if(p.isIsShown()){
                p.paint(g);
            }
        }
        
        //畫出player金錢
        g.setFont(fontBit);
        g.setColor(new Color(255,255,255));
        FontMetrics fm = g.getFontMetrics();
        String asset = String.valueOf(this.player.getInventory());
        
        int sw = fm.stringWidth(asset);
        int sa = fm.getAscent();
        g.drawString(asset, (int)(Resource.SCREEN_WIDTH*0.85)-sw, (int)(Resource.SCREEN_HEIGHT*0.9));
        
        String cash = this.player.getCash()+"";
        int sw2 = fm.stringWidth(cash);
        g.drawString(cash, (int)(Resource.SCREEN_WIDTH*0.4)-sw, (int)(Resource.SCREEN_HEIGHT*0.9));
        
        String pHp = this.player.getHp()+"";
        g.drawString(pHp, (int)(Resource.SCREEN_WIDTH*0.82)-sw, Resource.SCREEN_HEIGHT/13);
        
        String pMp = this.player.getMp()+"";
        g.drawString(pMp, (int)(Resource.SCREEN_WIDTH*0.82)-sw, Resource.SCREEN_HEIGHT/6);
        
        
        
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
        //處理扣錢
        if(costCash != 0){
            int decreseSpeed = -10;//每針扣除錢的速度
            costCash+=decreseSpeed;
            player.increaseCash(decreseSpeed);
            if(costCash==0){
                for(int i = 0; i < itemBtns.length;i++){
                    if(itemBtns[i].getClickState()){
                        itemBtns[i].setIsClicked(true);
                        itemBtns[i].setClickState(false);
                    }
                }
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
        this.functionBtns[this.BUY_BTN].reset(padding,Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        this.functionBtns[this.LEFT_BTN].reset((int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[this.RIGHT_BTN].reset((int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[this.START_BTN].reset(Resource.SCREEN_WIDTH-funcBtnWidthUnit*2-padding , Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        for(Button btn : functionBtns){
            btn.setLabelSize(btn.getWidth());
        }
        this.functionBtns[this.LEFT_BTN].setLabelSize(functionBtns[LEFT_BTN].getWidth()*4);
        this.functionBtns[this.RIGHT_BTN].setLabelSize(functionBtns[RIGHT_BTN].getWidth()*4);
        
        
//        this.itemBtns[0].reset(x0,y0,w0,h0);
//        this.itemBtns[1].reset(x1,y1,w1,h1);
//        this.itemBtns[2].reset(x2,y2,w2,h2);
//        for(int i = 3; i < this.itemBtns.length;i++){
//            this.itemBtns[i].reset(x0,y0,w0,h0);
//        }
        this.productOnScreen[0].reset(x0, y0, w0, h0);
        this.productOnScreen[1].reset(x1,y1,w1,h1);
        this.productOnScreen[2].reset(x2,y2,w2,h2);
    }
}