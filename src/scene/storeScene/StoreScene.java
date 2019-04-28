/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene;


import scene.storeScene.product.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import scene.Scene;
import scene.storeScene.product.finProduct.*;
import scene.storeScene.product.hpmp.*;
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
    protected ImgResource rc;
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
        productOnScreen = new Product[3];
        player = Player.getPlayerInstane();
        rc = ImgResource.getInstance();
        this.initParameters();
        this.setProduct();
        this.setFunctionBtns();
        this.initProductOnScreen();
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
        
        //建立產品
        products = new Product[productsNum];
        products[0] = new Product("/resources/nothing.png","",0,"");
        products[1] = new Hamburger();
        products[2] = new Chicken();
        products[3] = new Noodle();
        products[4] = new TV();
        products[5] = new Travel();
        products[6] = new Stock();
        products[7] = new Futures();
        products[8] = new Fund();
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
//                if((products[counter] instanceof FinProduct)){
//                    costCash += products[counter].getPrice();
//                    player.getFp().add((FinProduct)products[counter]);
//                }else{
//                    Food temp = (Food)products[counter];
//                    costCash += products[counter].getPrice();
//                    hpUp += temp.getHp();
//                    mpUp += temp.getMp();
//                    costInventory += temp.getPrice();
//                }
//                 
            }
        });
        //===============left buttom==================
        this.functionBtns[ButtomCode.LEFT_BTN] = new Button("/resources/clickBtn_blue.png",(int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);  
        this.functionBtns[ButtomCode.LEFT_BTN].setLabel("<");
        counter = 1;
        this.functionBtns[ButtomCode.LEFT_BTN].setLabelSize(functionBtns[ButtomCode.LEFT_BTN].getWidth()*4);
        this.functionBtns[ButtomCode.LEFT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
//                if(counter < productsPrice.length-2){
//                    for(Product p : productOnScreen){
//                        p.setIsShown(true);
//                    }
//                    counter++;
//                    System.out.println("left"+counter);
//                    productOnScreen[0] = products[counter-1];
//                    productOnScreen[1] = products[counter];
//                    productOnScreen[2] = products[counter+1];
//                }
//                else if(counter == productsPrice.length-2){
//                    counter++;
//                    System.out.println("到最後一個選項了");
//                    productOnScreen[0] = products[counter-1];
//                    productOnScreen[1] = products[counter];
//                    productOnScreen[2] = products[0];
//                }
                changeProductSeq();
                initParameters();
            }
            
        });
        //===============right buttom==================
        this.functionBtns[ButtomCode.RIGHT_BTN] = new Button("/resources/clickBtn_blue.png",(int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.RIGHT_BTN].setLabel(">");
        this.functionBtns[ButtomCode.RIGHT_BTN].setLabelSize(functionBtns[ButtomCode.RIGHT_BTN].getWidth()*4);
        this.functionBtns[ButtomCode.RIGHT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
//                if(counter > 2){
//
//                    for(Product p : productOnScreen){
//                        p.setIsShown(true);
//                    }
//                    counter--;
//                    System.out.println("right" + counter);
//                    productOnScreen[0] = products[counter-1];
//                    productOnScreen[1] = products[counter];
//                    productOnScreen[2] = products[counter+1];
//                }
//                else if(counter == 2){
//                    counter--;
//                    System.out.println("到第一個選項了"+counter);
//                    
//                    productOnScreen[2] = products[counter+1];
//                    productOnScreen[1] = products[counter];
//                    productOnScreen[0] = products[0];
//                }
                changeProductSeq();
                initParameters();
            }
            
        });
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
    
    public void initProductOnScreen(){
//      設定第0,1,2個商品位置
        if(products == null){
            System.out.println("product is null");
            return;
//        }
//        for(int i =0; i < productOnScreen.length;i++){
//            if(i > products.length-1){
//                this.productOnScreen[i] = products[0];
//            }else{
//                this.productOnScreen[i] = products[i];
//            }
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
        paintHpMp(g);
        
        //=============畫出 info=========================
//        if(products != null && products.length>1){
//            String info= productsInfo[counter];
//            if(products[counter] instanceof FinProduct){
//                g.setFont(fontC);
//                int y = Resource.SCREEN_HEIGHT-sa-padding-(int)(Resource.SCREEN_HEIGHT*0.25f);
//                int sh = g.getFontMetrics().getHeight();
//                int i = 0;
//                sw = fm.stringWidth(info.split("  ")[0]);
//                for (String line : info.split("  ")){
//                    g.drawString(line, (int)(Resource.SCREEN_WIDTH*0.53)-sw/2,y);
//                    y += sh;
//                    if(i ==1){
//                        g.drawString("剩餘價格:"+products[counter].getPrice(), (int)(Resource.SCREEN_WIDTH*0.53)-sw/2,y);
//                    }
//                    i++;
//                }
//
//            }else{
//                g.setFont(fontC);
//                sw = fm.stringWidth(info);
//                g.drawString(info, (int)(Resource.SCREEN_WIDTH*0.53)-sw/2, Resource.SCREEN_HEIGHT-sa-padding-(int)(Resource.SCREEN_HEIGHT*0.22f));
//                g.drawString("價格:"+productOnScreen[1].getPrice(), (int)(Resource.SCREEN_WIDTH*0.53)-sw/2, Resource.SCREEN_HEIGHT-padding-(int)(Resource.SCREEN_HEIGHT*0.22f));
//            }
//        }
        
        
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
