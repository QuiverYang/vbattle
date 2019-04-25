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
    private final int SELL_BTN = 5;
    
    private int productsNum=8;
    
    private final int ITEM_HAMBURGER = 0;
    private final int ITEM_NOODLE = 1;
    private final int ITEM_CHIKEN = 2;
    private final int ITEM_TV = 3;
    private final int ITEM_TRAVEL = 4;
    private final int ITEM_STOCK = 5;
    private final int ITEM_FUTURES = 6;
    private final int ITEM_FUND = 7;
    
    private int[] productsPrice;
    private final int ITEM_HAMBURGER_PRICE = 100;
    private final int ITEM_NOODLE_PRICE = 200;
    private final int ITEM_CHIKEN_PRICE = 300;
    private final int ITEM_TV_PRICE = 400;
    private final int ITEM_TRAVEL_PRICE = 500;
    private final int ITEM_STOCK_PRICE = 500;
    private final int ITEM_FUTURES_PRICE = 500;
    private final int ITEM_FUND_PRICE = 500;
    
    private String[] productsInfo;
    private Product[] products;
    
    private int funcBtnWidthUnit = Resource.SCREEN_WIDTH/12;//functionBtn的一個單位大小
    private int itemBtnWidthUnit = Resource.SCREEN_WIDTH/8;//itemBtn的一個單位大小
    private final int padding = 20;//與邊框距離
    private int itemBtnXcenter;//選單圖片中心x座標
    private int rightBtnYcenter;//選單圖片中心y座標
    private String[] productsIconPaths; //商品路徑
    private Button[] functionBtns; //功能鍵們
    private Product[] productOnScreen;
    private Player player;
    private int costCash,costInventory;//每個物品價值多少錢所要扣除player inventory的金額
    private int hpUp,mpUp;//每個物品所提升的hp mp;
    private int x0,y0,w0,h0,x1,y1,h1,w1,x2,y2,h2,w2;//productBtns商品的座標
    private int counter;//按鍵左右去計算的counter
    Font fontBit;
    

    
    public StoreScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
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
        this.productsPrice = new int[productsNum];
        productsPrice[ITEM_HAMBURGER] = ITEM_HAMBURGER_PRICE;
        productsPrice[ITEM_NOODLE] = ITEM_NOODLE_PRICE;
        productsPrice[ITEM_CHIKEN] = ITEM_CHIKEN_PRICE;
        productsPrice[ITEM_TV] = ITEM_TV_PRICE;
        productsPrice[ITEM_TRAVEL] = ITEM_TRAVEL_PRICE;
        productsPrice[ITEM_STOCK] = ITEM_STOCK_PRICE;
        productsPrice[ITEM_FUTURES] = ITEM_FUTURES_PRICE;
        productsPrice[ITEM_FUND] = ITEM_FUND_PRICE;
        
        //設定物品tiembtnIcon圖片
        this.productsIconPaths = new String[productsNum];
        this.productsIconPaths[ITEM_HAMBURGER] = "/resources/hamburger.jpg";
        this.productsIconPaths[ITEM_NOODLE] = "/resources/noodle.jpg";
        this.productsIconPaths[ITEM_CHIKEN] = "/resources/chiken.jpg";
        this.productsIconPaths[ITEM_TV] = "/resources/tv.jpg";
        this.productsIconPaths[ITEM_TRAVEL] = "/resources/travel.jpg";
        this.productsIconPaths[ITEM_STOCK] = "/resources/stock.jpg";
        this.productsIconPaths[ITEM_FUTURES] = "/resources/stock.jpg";
        this.productsIconPaths[ITEM_FUND] = "/resources/stock.jpg";
        
        //設定物品產品介紹
        this.productsInfo = new String[productsNum];
        this.productsInfo[ITEM_HAMBURGER] = "漢堡:HP+20,MP+10";
        this.productsInfo[ITEM_NOODLE] = "麵食:HP+30";
        this.productsInfo[ITEM_CHIKEN] = "雞腿:HP+40";
        this.productsInfo[ITEM_TV] = "電視:MP+20";
        this.productsInfo[ITEM_TRAVEL] = "旅遊:MP+30";
        this.productsInfo[ITEM_STOCK] = "股票:購買一張股票  風險0.30/利潤0.08";
        this.productsInfo[ITEM_FUTURES] = "期貨:購買一張股票  風險0.60/利潤0.16";
        this.productsInfo[ITEM_FUND] = "基金:購買一張股票  風險0.20/利潤0.04";
        
        //建立產品
        products = new Product[productsNum];
        for(int i = 0; i < productsNum; i++){
            String pName = productsInfo[i].substring(0, productsInfo[i].indexOf(":"));
            int price = productsPrice[i];
            String info = productsInfo[i];
            int indexOfHp = productsInfo[i].indexOf("HP+");
            int indexOfMp = productsInfo[i].indexOf("MP+");
            //判斷是否為金融商品
            if(indexOfHp == -1 && indexOfMp == -1){
                int indexOfRisk = productsInfo[i].indexOf("風險");
                int indexOfProfit = productsInfo[i].indexOf("利潤");
                double risk = Double.parseDouble(productsInfo[i].substring(indexOfRisk+2, indexOfRisk+6));
                double profit = Double.parseDouble(productsInfo[i].substring(indexOfProfit+2, indexOfProfit+6));
                products[i] = new FinProduct(productsIconPaths[i],pName,price,risk,profit,productsInfo[i]);
            }else{
                int pHp, pMp;
                if(indexOfHp != -1){
                    pHp = Integer.parseInt(productsInfo[i].substring(indexOfHp+3, indexOfHp+5));
                }else{
                    pHp = 0;
                }
                if(indexOfMp != -1){
                    pMp = Integer.parseInt(productsInfo[i].substring(indexOfMp+3, indexOfMp+5));
                }else{
                    pMp = 0;
                }
                products[i] = new Product(productsIconPaths[i],pName,price,info);
                products[i].setHp(pHp);
                products[i].setMp(pMp);
            }
        }

        
    }
    
    private void initFunctionBtns(){
        //button建構子特殊 Button(String iconName, int width, int height, int x, int y)
        backgroundImg = rc.tryGetImage("/resources/storebg.png");  //store background 圖片
        //初始化並放置functionBtns 位置
        this.functionBtns = new Button[6];
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
                System.out.println(counter);
                //判斷是不是金融商品
                if((products[counter] instanceof FinProduct)){
                    costCash += products[counter].getPrice();
                    player.getFp().add((FinProduct)products[counter]);
                }else{
                    costCash += products[counter].getPrice();
                    hpUp += products[counter].getHp();
                    mpUp += products[counter].getMp();
                    costInventory += products[counter].getPrice();
                }
                 
            }
        });
        this.functionBtns[this.LEFT_BTN] = new Button("/resources/clickBtn_blue.png");  
        this.functionBtns[this.LEFT_BTN].setLabel("<");
        counter = 1;
        this.functionBtns[this.LEFT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
                if(counter < productsPrice.length-2){
                    for(Product p : productOnScreen){
                        p.setIsShown(true);
                    }
                    counter++;
                    System.out.println("left"+counter);
                    productOnScreen[0] = products[counter-1];
                    productOnScreen[1] = products[counter];
                    productOnScreen[2] = products[counter+1];
                }
                else if(counter == productsPrice.length-2){
                    counter++;
                    System.out.println("到最後一個選項了");
                    productOnScreen[0] = products[counter-1];
                    productOnScreen[1] = products[counter];
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

                    for(Product p : productOnScreen){
                        p.setIsShown(true);
                    }
                    counter--;
                    System.out.println("right" + counter);
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
        
        this.functionBtns[this.SELL_BTN] = new Button("/resources/clickBtn_blue.png");
        this.functionBtns[this.SELL_BTN].setLabel("SELL");
        this.functionBtns[this.SELL_BTN].setCallback(new Callback() {
            @Override
            public void doSomthing() {
                try {
                    player.save();
                    gsChangeListener.changeScene(MainPanel.SELL_SCENE);
                } catch (IOException ex) {
                    System.out.println("player save problem from StorceScene back to MenuScene");;
                }
            }
        });
    }
    
    private void initItemBtns(){
        
        
        
//      設定第0,1,2個商品位置

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
        
        String info= productsInfo[counter];;
        if(products[counter] instanceof FinProduct){
            g.setFont(new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/40));
            int y = Resource.SCREEN_HEIGHT-sa-padding-(int)(Resource.SCREEN_HEIGHT*0.22f);
            int sh = g.getFontMetrics().getHeight();
            for (String line : info.split("  ")){
                sw = fm.stringWidth(line);
                g.drawString(line, (int)(Resource.SCREEN_WIDTH*0.55)-sw/2,y);
                y += sh;
            }
            
        }else{
            g.setFont(new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/30));
            sw = fm.stringWidth(info);
            g.drawString(info, (int)(Resource.SCREEN_WIDTH*0.55)-sw/2, Resource.SCREEN_HEIGHT-sa-padding-(int)(Resource.SCREEN_HEIGHT*0.22f));
        }
        
        
        
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
        changePlayerCash();
        changePlayerHp();
        changePlayerMp();
        changePlayerInventory();
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
        this.functionBtns[this.START_BTN].reset(padding +functionBtns[BACK_BTN].getWidth()+padding , padding,//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        this.functionBtns[this.SELL_BTN].reset(Resource.SCREEN_WIDTH-funcBtnWidthUnit*2-padding , Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        for(Button btn : functionBtns){
            btn.setLabelSize(btn.getWidth());
        }
        this.functionBtns[this.LEFT_BTN].setLabelSize(functionBtns[LEFT_BTN].getWidth()*4);
        this.functionBtns[this.RIGHT_BTN].setLabelSize(functionBtns[RIGHT_BTN].getWidth()*4);
        
        
        this.productOnScreen[0].reset(x0, y0, w0, h0);
        this.productOnScreen[1].reset(x1,y1,w1,h1);
        this.productOnScreen[2].reset(x2,y2,w2,h2);
    }
    
    private void changePlayerCash(){
        if(costCash != 0){
            int decreseSpeed = -10;//每針扣除錢的速度
            costCash+=decreseSpeed;
            player.increaseCash(decreseSpeed);
        }
    }
    private void changePlayerHp(){
        if(hpUp != 0){
            int increseSpeed = 5;//每針加血的速度
            hpUp-=increseSpeed;
            player.increaseHp(increseSpeed);
        }
    }
    private void changePlayerMp(){
        if(mpUp!=0){
            int increaseSpeed = 5;
            mpUp-=increaseSpeed;
            player.increaseMp(increaseSpeed);
        }
    }
    private void changePlayerInventory(){
        if(costInventory!=0){
            int increaseSpeed = 5;
            costInventory-=increaseSpeed;
            player.increaseInventory(-increaseSpeed);
        }
    }
  
    
}
