/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import scene.storeScene.product.finProduct.FinProduct;
import java.io.IOException;
import scene.storeScene.product.Product;
import vbattle.Button;
import vbattle.MainPanel;
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Resource;

/**
 *
 * @author menglinyang
 */
public class SellScene extends Store{

    public SellScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        this.initParameters();
        this.setProduct();
        this.setFunctionBtns();
        this.initProductOnScreen();
    }

    @Override
    public void setProduct(){  
        productsNum = player.getFp().size()+1;
        System.out.println("ssssssssss"+productsNum);
        //建立產品
        products = new FinProduct[productsNum];
        products[0] = new FinProduct("/resources/nothing.png","",0,0,0,"",0);
        for(int i = 1; i < productsNum; i++){
            String pName = player.getFp().get(i-1).getName();
            int price = player.getFp().get(i-1).getPrice();
            String info = player.getFp().get(i-1).getInfo();
            double risk = player.getFp().get(i-1).getRisk();
            double profit = player.getFp().get(i-1).getProfit();
            String path = player.getFp().get(i-1).getFileName();
            int value = player.getFp().get(i-1).getValue();
            System.out.println("valueooooooooo" + value);
            products[i] = new FinProduct(path,pName,price,risk,profit,info,value);
        }
        counter = 1;
    }
    
    @Override
    public void setFunctionBtns(){
        //初始化並放置functionBtns 位置
        this.functionBtns = new Button[6];
        this.functionBtns[ButtomCode.BACK_BTN] = new Button("/resources/return_blue.png",padding,padding,
                funcBtnWidthUnit, funcBtnWidthUnit);
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
        //在這個位置變成sell
        this.functionBtns[ButtomCode.BUY_BTN] = new Button("/resources/clickBtn_blue.png",padding,Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),
                funcBtnWidthUnit*2, funcBtnWidthUnit);//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
        this.functionBtns[ButtomCode.BUY_BTN].setLabel("SELL");
        this.functionBtns[ButtomCode.BUY_BTN].setCallback(new Button.Callback() {
            @Override
            public void doSomthing() {
                if(productsNum > 1){
                    FinProduct temp = (FinProduct)products[counter];
                    costCash += temp.getValue();
                    player.getFp().remove(--counter);
                    setProduct();
                    initProductOnScreen();
                }
                
            }
        });
        this.functionBtns[ButtomCode.LEFT_BTN] = new Button("/resources/clickBtn_blue.png",(int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.LEFT_BTN].setLabel("<");
        counter = 1;
        this.functionBtns[ButtomCode.LEFT_BTN].setCallback(new Button.Callback(){
            @Override
            public void doSomthing() {
                if(products.length>1){
                    if(counter < products.length-2){
                        counter++;
                        productOnScreen[0] = products[counter-1];
                        productOnScreen[1] = products[counter];
                        productOnScreen[2] = products[counter+1];
                    }
                    else if(counter == products.length-2){
                        counter++;
                        System.out.println("到最後一個選項了"+counter);
                        productOnScreen[0] = products[counter-1];
                        productOnScreen[1] = products[counter];
                        productOnScreen[2] = products[0];
                    }
                    changeProductSeq();
                    initParameters();
                    
                }
                
            }
            
        });
        this.functionBtns[ButtomCode.RIGHT_BTN] = new Button("/resources/clickBtn_blue.png",(int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, this.rightBtnYcenter-funcBtnWidthUnit/2,
                funcBtnWidthUnit/2, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.RIGHT_BTN].setLabel(">");
        this.functionBtns[ButtomCode.RIGHT_BTN].setCallback(new Button.Callback(){
            @Override
            public void doSomthing() {
                if(products.length>1){
                    if(counter > 2){
                        counter--;
                        productOnScreen[0] = products[counter-1];
                        productOnScreen[1] = products[counter];
                        productOnScreen[2] = products[counter+1];
                    }
                    else if(counter == 2){
                        counter--;
                        System.out.println("到第一個選項了");
                        productOnScreen[0] = products[0];
                        productOnScreen[1] = products[counter];
                        productOnScreen[2] = products[counter+1];
                    }
                    changeProductSeq();
                    initParameters();
                } 
            }
            
        });

        this.functionBtns[ButtomCode.START_BTN] = new Button("/resources/nothing.png",padding +functionBtns[ButtomCode.BACK_BTN].getWidth()+padding , padding,//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.START_BTN].setLabel("");
        this.functionBtns[ButtomCode.START_BTN].setCallback(new Button.Callback() {
            @Override
            public void doSomthing() {

            }
        });
        
        this.functionBtns[ButtomCode.SELL_BTN] = new Button("/resources/nothing.png",Resource.SCREEN_WIDTH-funcBtnWidthUnit*2-padding , Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-(int)(Resource.SCREEN_HEIGHT*0.22f),//(int)(Resource.SCREEN_HEIGHT*0.133f是螢幕索引吃掉的部分
                funcBtnWidthUnit*2, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.SELL_BTN].setLabel("");
        this.functionBtns[ButtomCode.SELL_BTN].setCallback(new Button.Callback() {
            @Override
            public void doSomthing() {
                
            }
        });
    }
    
    @Override
    protected void changePlayerCash(){
        if(costCash > 0){
            int decreseSpeed = 10;//每針扣除錢的速度
            costCash-=decreseSpeed;
            player.increaseCash(decreseSpeed);
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
    


  
}

