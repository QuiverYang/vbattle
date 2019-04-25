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
import java.io.IOException;
import scene.Scene;
import vbattle.Button;
import vbattle.MainPanel;
import vbattle.Resource;

/**
 *
 * @author menglinyang
 */
public class SellScene extends StoreScene{
    
    FinProduct[] products;

    public SellScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
    }

    @Override
    public void setProduct(){
        this.productsNum = player.getFp().size()+1;
        System.out.println("productsNum "+productsNum);
        //設定物品價格
        this.productsPrice = new int[productsNum];
        productsPrice[0] = 0;
        //設定物品tiembtnIcon圖片
        this.productsIconPaths = new String[productsNum];
        this.productsIconPaths[0] = "/resources/nothing.png";
        //設定物品產品介紹
        this.productsInfo = new String[productsNum];
        productsInfo[0] = "";
        for(int i = 1; i < productsNum; i++){
            productsPrice[i] = player.getFp().get(i).getValue();
            productsIconPaths[i] = player.getFp().get(i).getFileName();
            productsInfo[i] = player.getFp().get(i).getInfo();
        }

        
        //建立產品
        products = new FinProduct[productsNum];
        products[0] = new FinProduct("/resources/nothing.png","",0,0,0,"");
        for(int i = 1; i < productsNum; i++){
            String pName = productsInfo[i].substring(0, productsInfo[i].indexOf(":"));
            int price = productsPrice[i];
            String info = productsInfo[i];
            int indexOfRisk = productsInfo[i].indexOf("風險");
            int indexOfProfit = productsInfo[i].indexOf("利潤");
            double risk = Double.parseDouble(productsInfo[i].substring(indexOfRisk+2, indexOfRisk+6));
            double profit = Double.parseDouble(productsInfo[i].substring(indexOfProfit+2, indexOfProfit+6));
            products[i] = new FinProduct(productsIconPaths[i],pName,price,risk,profit,info);
        }
    }
    
    @Override
    protected void setFunctionBtns(){
        //初始化並放置functionBtns 位置
        this.functionBtns = new Button[6];
        this.functionBtns[ButtomCode.BACK_BTN] = new Button("/resources/return_blue.png");
        this.functionBtns[ButtomCode.BACK_BTN].setCallback(new Button.Callback() {
            @Override
            public void doSomthing() {
                try {
                    player.save();
                    gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                } catch (IOException ex) {
                    System.out.println("player save problem from StorceScene back to MenuScene");;
                }
            }
        });
        this.functionBtns[ButtomCode.BUY_BTN] = new Button("/resources/clickBtn_blue.png");//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
        this.functionBtns[ButtomCode.BUY_BTN].setLabel("SELL");
        this.functionBtns[ButtomCode.BUY_BTN].setCallback(new Button.Callback() {
            @Override
            public void doSomthing() {
                costCash -= products[counter].getPrice();
                player.getFp().remove((FinProduct)products[counter]);
            }
        });
        this.functionBtns[ButtomCode.LEFT_BTN] = new Button("/resources/clickBtn_blue.png");  
        this.functionBtns[ButtomCode.LEFT_BTN].setLabel("<");
        counter = 1;
        this.functionBtns[ButtomCode.LEFT_BTN].setCallback(new Button.Callback(){

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
                
            }
            
        });
        this.functionBtns[ButtomCode.RIGHT_BTN] = new Button("/resources/clickBtn_blue.png");
        this.functionBtns[ButtomCode.RIGHT_BTN].setLabel(">");
        this.functionBtns[ButtomCode.RIGHT_BTN].setCallback(new Button.Callback(){

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
                    System.out.println("到第一個選項了");
                    System.out.println("0:"+productOnScreen[0].isIsShown());
                    System.out.println("1:"+productOnScreen[1].isIsShown());
                    productOnScreen[2] = products[counter+1];
                    productOnScreen[1] = products[counter];
                    productOnScreen[0] = products[0];
                }
            }
            
        });

        this.functionBtns[ButtomCode.START_BTN] = new Button("/resources/nothing.png");
        this.functionBtns[ButtomCode.START_BTN].setLabel("START");
        this.functionBtns[ButtomCode.START_BTN].setCallback(new Button.Callback() {
            @Override
            public void doSomthing() {

            }
        });
        
        this.functionBtns[ButtomCode.SELL_BTN] = new Button("/resources/nothing.png");
        this.functionBtns[ButtomCode.SELL_BTN].setLabel("SELL");
        this.functionBtns[ButtomCode.SELL_BTN].setCallback(new Button.Callback() {
            @Override
            public void doSomthing() {
                
            }
        });
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
            if(p != null){
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
        if(productsNum>1){
            String info= productsInfo[counter];
            g.setFont(new Font("Courier",Font.BOLD,Resource.SCREEN_WIDTH/40));
            int y = Resource.SCREEN_HEIGHT-sa-padding-(int)(Resource.SCREEN_HEIGHT*0.25f);
            int sh = g.getFontMetrics().getHeight();
            int i = 0;
            sw = fm.stringWidth(info.split("  ")[0]);
            for (String line : info.split("  ")){
                g.drawString(line, (int)(Resource.SCREEN_WIDTH*0.55)-sw/2,y);
                y += sh;
                if(i ==1){
                    g.drawString("剩餘價值:" + products[counter].getValue(), (int)(Resource.SCREEN_WIDTH*0.55)-sw/2,y);
                }
                i++;
            }
        }
        

    }
    
    @Override
    public void initItemBtns(){
//      設定第0,1,2個商品位置
        for(int i =0; i < products.length;i++){
            this.productOnScreen[i] = products[i];
        }
    }

    


    
}

