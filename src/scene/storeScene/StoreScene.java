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
    
    
    protected BufferedImage backgroundImg;
    private ImgResource rc;
    protected int productsNum;
    protected int[] productsPrice;
    protected String[] productsInfo;
    protected Product[] products;
    protected int funcBtnWidthUnit = Resource.SCREEN_WIDTH/12;//functionBtn的一個單位大小
    protected int itemBtnWidthUnit = Resource.SCREEN_WIDTH/8;//itemBtn的一個單位大小
    protected final int padding = 20;//與邊框距離
    protected int itemBtnXcenter;//選單圖片中心x座標
    protected int rightBtnYcenter;//選單圖片中心y座標
    protected String[] productsIconPaths; //商品路徑
    protected Button[] functionBtns; //功能鍵們
    protected Product[] productOnScreen;
    protected Player player;
    protected int costCash,costInventory;//每個物品價值多少錢所要扣除player inventory的金額
    private int hpUp,mpUp;//每個物品所提升的hp mp;
    protected int[] pX,pY,pW,pH;//productsOnScreen的座標
    protected int counter;//按鍵左右去計算的counter
    protected Font fontBit;
    protected Font fontBit2;
    protected Font fontC;
    
    public interface ButtomCode {
        int BACK_BTN = 0;
        int BUY_BTN = 1;
        int LEFT_BTN = 2;
        int RIGHT_BTN = 3;
        int START_BTN = 4;
        int SELL_BTN = 5;
    }
    
    public StoreScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        this.productOnScreen = new Product[3];
        player = Player.getPlayerInstane();
        rc = ImgResource.getInstance();
        this.initParameters();
        this.setFunctionBtns();
        this.setProduct();
        this.initItemBtns();
    }
    //子類別不用override
    protected void initParameters(){
        backgroundImg = rc.tryGetImage("/resources/storebg.png");  //store background 圖片
        fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH/20);
        fontBit2 = Fontes.getBitFont(Resource.SCREEN_WIDTH/25);
        fontC = new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/30);
        this.itemBtnXcenter = Resource.SCREEN_WIDTH/2;
        this.rightBtnYcenter = (int)(Resource.SCREEN_HEIGHT*0.4f);
        pX=new int[3];
        pY=new int[3];
        pW=new int[3];
        pH=new int[3];
        pX[0] = itemBtnXcenter-itemBtnWidthUnit*2-2*padding;
        pY[0] = rightBtnYcenter-itemBtnWidthUnit/2;
        pW[0] = itemBtnWidthUnit;
        pH[0] = itemBtnWidthUnit;
        pX[1] = itemBtnXcenter - itemBtnWidthUnit;
        pY[1] = rightBtnYcenter-itemBtnWidthUnit;
        pW[1] = 2 * pW[0];
        pH[1] = 2 * pH[0];
        pX[2] = itemBtnXcenter + itemBtnWidthUnit+ 2*padding;
        pY[2] = pY[0];
        pW[2] = pW[0];
        pH[2] = pH[0];
    }
      
    protected void setProduct(){
        this.productsNum = 9;
        int ITEM_HAMBURGER_PRICE = 100;
        int ITEM_NOODLE_PRICE = 200;
        int ITEM_CHIKEN_PRICE = 300;
        int ITEM_TV_PRICE = 400;
        int ITEM_TRAVEL_PRICE = 500;
        int ITEM_STOCK_PRICE = 500;
        int ITEM_FUTURES_PRICE = 500;
        int ITEM_FUND_PRICE = 500;
        int ITEM_HAMBURGER = 1;
        int ITEM_NOODLE = 2;
        int ITEM_CHIKEN = 3;
        int ITEM_TV = 4;
        int ITEM_TRAVEL = 5;
        int ITEM_STOCK = 6;
        int ITEM_FUTURES = 7;
        int ITEM_FUND = 8;
        //設定物品價格
        this.productsPrice = new int[productsNum];
        productsPrice[0] = 0;
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
        this.productsIconPaths[0] = "/resources/nothing.png";
        this.productsIconPaths[ITEM_HAMBURGER] = "/resources/hamburger.jpg";
        this.productsIconPaths[ITEM_NOODLE] = "/resources/noodle.jpg";
        this.productsIconPaths[ITEM_CHIKEN] = "/resources/chiken.jpg";
        this.productsIconPaths[ITEM_TV] = "/resources/tv.jpg";
        this.productsIconPaths[ITEM_TRAVEL] = "/resources/travel.jpg";
        this.productsIconPaths[ITEM_STOCK] = "/resources/stock.jpg";
        this.productsIconPaths[ITEM_FUTURES] = "/resources/profit.jpg";
        this.productsIconPaths[ITEM_FUND] = "/resources/balance.jpg";
        
        //設定物品產品介紹
        this.productsInfo = new String[productsNum];
        productsInfo[0] = "";
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
        products[0] = new Product("/resources/nothing.png","",0,"");
        for(int i = 1; i < productsNum; i++){
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
    
    protected void setFunctionBtns(){
        
        //初始化並放置functionBtns 位置
        this.functionBtns = new Button[6];
        //===============back buttom==================
        this.functionBtns[ButtomCode.BACK_BTN] = new Button("/resources/return_blue.png",padding,padding,
                funcBtnWidthUnit, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.BACK_BTN].setCallback(new Callback() {
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
        
        //===============buy buttom==================
        this.functionBtns[ButtomCode.BUY_BTN] = new Button("/resources/clickBtn_blue.png",padding,Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),
                funcBtnWidthUnit*2, funcBtnWidthUnit);//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
        this.functionBtns[ButtomCode.BUY_BTN].setLabel("BUY");
        this.functionBtns[ButtomCode.BUY_BTN].setCallback(new Callback() {
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
        //===============left buttom==================
        this.functionBtns[ButtomCode.LEFT_BTN] = new Button("/resources/clickBtn_blue.png",(int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);  
        this.functionBtns[ButtomCode.LEFT_BTN].setLabel("<");
        this.functionBtns[ButtomCode.LEFT_BTN].setLabelSize(functionBtns[ButtomCode.LEFT_BTN].getWidth()*4);
        counter = 1;
        this.functionBtns[ButtomCode.LEFT_BTN].setCallback(new Callback(){

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
                    productOnScreen[2] = products[0];
                }
                changeProductSeq();
                initParameters();
            }
            
        });
        //===============right buttom==================
        this.functionBtns[ButtomCode.RIGHT_BTN] = new Button("/resources/clickBtn_blue.png",(int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.RIGHT_BTN].setLabel(">");
        this.functionBtns[ButtomCode.RIGHT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
                if(counter > 2){

                    for(Product p : productOnScreen){
                        p.setIsShown(true);
                    }
                    counter--;
                    System.out.println("right" + counter);
                    productOnScreen[0] = products[counter-1];
                    productOnScreen[1] = products[counter];
                    productOnScreen[2] = products[counter+1];
                }
                else if(counter == 2){
                    counter--;
                    System.out.println("到第一個選項了"+counter);
                    
                    productOnScreen[2] = products[counter+1];
                    productOnScreen[1] = products[counter];
                    productOnScreen[0] = products[0];
                }
                changeProductSeq();
                initParameters();
            }
            
        });
        this.functionBtns[ButtomCode.RIGHT_BTN].setLabelSize(functionBtns[ButtomCode.RIGHT_BTN].getWidth()*4);
        
        //===============start buttom==================
        this.functionBtns[ButtomCode.START_BTN] = new Button("/resources/clickBtn_blue.png",padding +functionBtns[ButtomCode.BACK_BTN].getWidth()+padding , padding,//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.START_BTN].setLabel("START");
        this.functionBtns[ButtomCode.START_BTN].setCallback(new Callback() {
            @Override
            public void doSomthing() {
                try {
                    player.save();
                } catch (IOException ex) {
                    System.out.println("player save problem from StorceScene back to MenuScene");;
                }
                gsChangeListener.changeScene(MainPanel.STAGE_SCENE);
            }
        });
        //===============sell buttom==================
        this.functionBtns[ButtomCode.SELL_BTN] = new Button("/resources/clickBtn_blue.png",Resource.SCREEN_WIDTH-funcBtnWidthUnit*2-padding , Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.SELL_BTN].setLabel("SELL");
        this.functionBtns[ButtomCode.SELL_BTN].setCallback(new Callback() {
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
    
    public void initItemBtns(){
//      設定第0,1,2個商品位置
        int min;
        if(products == null){
            min = 0;
        }else{
            min = Math.min(products.length, productOnScreen.length);
        }
        for(int i =0; i < min;i++){
            this.productOnScreen[i] = products[i];
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
            if(i == ButtomCode.LEFT_BTN || i == ButtomCode.RIGHT_BTN){
                System.out.println("enter left or right");
                functionBtns[i].setFont(fontBit);
            }else{
                functionBtns[i].setFont(fontBit2);
            }
        }
        for(Button btn:functionBtns){
            btn.paintBtn(g);
            System.out.println("btn:"+ btn.getWidth()+" height:"+btn.getHeight());
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
        paintHpMp(g);
        
        //=============畫出 info=========================
        if(products != null){
            String info= productsInfo[counter];
            if(products[counter] instanceof FinProduct){
                g.setFont(fontC);
                int y = Resource.SCREEN_HEIGHT-sa-padding-(int)(Resource.SCREEN_HEIGHT*0.25f);
                int sh = g.getFontMetrics().getHeight();
                int i = 0;
                sw = fm.stringWidth(info.split("  ")[0]);
                for (String line : info.split("  ")){
                    g.drawString(line, (int)(Resource.SCREEN_WIDTH*0.53)-sw/2,y);
                    y += sh;
                    if(i ==1){
                        g.drawString("剩餘價格:"+products[counter].getPrice(), (int)(Resource.SCREEN_WIDTH*0.53)-sw/2,y);
                    }
                    i++;
                }

            }else{
                g.setFont(fontC);
                sw = fm.stringWidth(info);
                g.drawString(info, (int)(Resource.SCREEN_WIDTH*0.53)-sw/2, Resource.SCREEN_HEIGHT-sa-padding-(int)(Resource.SCREEN_HEIGHT*0.22f));
                g.drawString("價格:"+productOnScreen[1].getPrice(), (int)(Resource.SCREEN_WIDTH*0.53)-sw/2, Resource.SCREEN_HEIGHT-padding-(int)(Resource.SCREEN_HEIGHT*0.22f));
            }
        }
        
        
    }
    
    protected void paintHpMp(Graphics g){
        FontMetrics fm = g.getFontMetrics();
        String pHp = this.player.getHp()+"";
        int sw = fm.stringWidth(pHp);
        //=============畫出 hp=========================
        g.drawString(pHp, (int)(Resource.SCREEN_WIDTH*0.9)-sw, Resource.SCREEN_HEIGHT/13);
        
        //=============畫出 mp=========================
        String pMp = this.player.getMp()+"";
        g.drawString(pMp, (int)(Resource.SCREEN_WIDTH*0.9)-sw, Resource.SCREEN_HEIGHT/6);
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
        changeProductSeq();
        this.resize();
    } 
    
    public void resize(){
        if(funcBtnWidthUnit!= Resource.SCREEN_WIDTH/12||itemBtnWidthUnit != Resource.SCREEN_WIDTH/8){
            System.out.println("different size");
            funcBtnWidthUnit = Resource.SCREEN_WIDTH/12;//functionBtn的一個單位大小     
            itemBtnWidthUnit = Resource.SCREEN_WIDTH/8;//itemBtn的一個單位大小 
            this.functionBtns[ButtomCode.BACK_BTN].reset(padding,padding,
                    funcBtnWidthUnit, funcBtnWidthUnit);
            this.functionBtns[ButtomCode.BUY_BTN].reset(padding,Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),
                    funcBtnWidthUnit*2, funcBtnWidthUnit);
            this.functionBtns[ButtomCode.LEFT_BTN].reset((int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                    funcBtnWidthUnit/2, funcBtnWidthUnit);
            this.functionBtns[ButtomCode.RIGHT_BTN].reset((int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                    funcBtnWidthUnit/2, funcBtnWidthUnit);
            this.functionBtns[ButtomCode.START_BTN].reset(padding +functionBtns[ButtomCode.BACK_BTN].getWidth()+padding , padding,//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                    funcBtnWidthUnit*2, funcBtnWidthUnit);
            this.functionBtns[ButtomCode.SELL_BTN].reset(Resource.SCREEN_WIDTH-funcBtnWidthUnit*2-padding , Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                    funcBtnWidthUnit*2, funcBtnWidthUnit);
            for(Button btn : functionBtns){
                btn.setLabelSize(btn.getWidth());
            }
            this.functionBtns[ButtomCode.LEFT_BTN].setLabelSize(functionBtns[ButtomCode.LEFT_BTN].getWidth()*4);
            this.functionBtns[ButtomCode.RIGHT_BTN].setLabelSize(functionBtns[ButtomCode.RIGHT_BTN].getWidth()*4);
            initParameters();
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
        }
    }
    
    private void changePlayerMp(){
        if(mpUp!=0){
            int increaseSpeed = 2;
            mpUp-=increaseSpeed;
            player.increaseMp(increaseSpeed);
        }
    }
    
    private void changePlayerInventory(){
        if(costInventory!=0){
            int increaseSpeed = 10;
            costInventory-=increaseSpeed;
            player.increaseInventory(-increaseSpeed);
        }
    }
  
    
}
