/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene;

import java.awt.Graphics;
import java.io.IOException;
import vbattle.Button;
import vbattle.MainPanel;
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Resource;

/**
 *
 * @author menglinyang
 */
public class SellScene extends StoreScene{
    
    FinProduct[] products;

    public SellScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
    }

    @Override
    public void setProduct(){
        this.productsNum = player.getFp().size()+1;
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
            productsPrice[i] = player.getFp().get(i-1).getValue();
            productsIconPaths[i] = player.getFp().get(i-1).getFileName();
            productsInfo[i] = player.getFp().get(i-1).getInfo();
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
        counter = 1;
    }
    
    @Override
    protected void setFunctionBtns(){
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
                    costCash += products[counter].getPrice();
                    player.getFp().remove(--counter);
                    setProduct();
                    initItemBtns();
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
                    if(counter < productsPrice.length-2){
                        counter++;
                        productOnScreen[0] = products[counter-1];
                        productOnScreen[1] = products[counter];
                        productOnScreen[2] = products[counter+1];
                    }
                    else if(counter == productsPrice.length-2){
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
    protected void paintHpMp(Graphics g){

    }
    
    @Override
    protected void changePlayerCash(){
        if(costCash > 0){
            int decreseSpeed = 10;//每針扣除錢的速度
            costCash-=decreseSpeed;
            player.increaseCash(decreseSpeed);
        }
    }
  
}

