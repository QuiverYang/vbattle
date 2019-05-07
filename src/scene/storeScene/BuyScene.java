/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene;


import java.applet.Applet;
import java.applet.AudioClip;
import scene.storeScene.product.*;
import java.io.IOException;
import scene.SaveScene;
import scene.storeScene.product.finProduct.*;
import scene.storeScene.product.hpmp.*;
import scene.storeScene.product.stuffLvUp.*;
import sun.audio.*;
import vbattle.Button;
import vbattle.Button.Callback;
import vbattle.MainPanel;
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Resource;

/**
 *
 * @author menglinyang
 */
public class BuyScene extends Store{
    



                        

    public BuyScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        
        this.initParameters();
        this.setProduct();
        this.setFunctionBtns();
        this.initProductOnScreen();
        
    }
    @Override  
    public void setProduct(){
        this.productsNum = 14;
        //建立產品
        products = new Product[productsNum];
        products[0] = new Product("/resources/nothing.png","",0,"");
        products[1] = new Hamburger();
        products[2] = new Chicken();
        products[3] = new Noodle();
        products[4] = new TV();
        products[5] = new Travel();
        products[6] = new Red();
        products[7] = new Blue();
        products[8] = new Yellow();
        products[9] = new BigYellow();
        products[10] = new BigBlue();
        products[11] = new Protein();
        products[12] = new Medicine();
        products[13] = new Robot();
    }
    
    @Override
    public void setFunctionBtns(){
        
        //初始化並放置functionBtns 位置
        this.functionBtns = new Button[6];
        
        //===============back buttom==================
        this.functionBtns[ButtomCode.BACK_BTN] = new Button("/resources/return_blue.png",padding,padding,
                funcBtnWidthUnit, funcBtnWidthUnit);
        this.functionBtns[ButtomCode.BACK_BTN].setCallback(new Callback() {
            @Override
            public void doSomthing() {
                //                    player.save();
                backgroundSound.stop();
                SaveScene.currentScene = MainPanel.STORE_SCENE; //設定當前場景為商店
                SaveScene.nextScene = MainPanel.MENU_SCENE;     //設定儲存後場景為主畫面
                gsChangeListener.changeScene(MainPanel.SAVE_SCENE);
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
                //判斷錢是否夠用
                if(player.getCash()>=products[counter].getPrice()){
                    //判斷是否為藥劑
                    if((products[counter] instanceof FinProduct)){
                        costCash += products[counter].getPrice();
                        player.getFp().add((FinProduct)products[counter]);
                    }else if(products[counter] instanceof Food){//判斷是否為食物
                        if(!(player.getMp()>=100 && player.getHp()>=100)){
                            Food temp = (Food)products[counter];
                            int tempHp = temp.getHp();
                            int tempMp = temp.getMp();
                            if(!(player.getHp()>=100&&tempMp==0)||(player.getMp()>=100&&tempHp==0)){
                                costCash += products[counter].getPrice();
                                hpUp += temp.getHp();
                                mpUp += temp.getMp();
                                costInventory += temp.getPrice();
                            }
                            
                        }
                        
                    }else{//此處為升等
                        AudioClip noupgradeSound,upgradeSound;
                        noupgradeSound = Applet.newAudioClip(getClass().getResource("/resources/noupgrade.wav"));
                        upgradeSound = Applet.newAudioClip(getClass().getResource("/resources/upgrade.wav"));

                        if(player.getUnlock()[counter-6]>0){
                            //更改升級的player屬性
                            player.addUnlockContent(counter-6);
                            costCash += products[counter].getPrice();
                            upgradeSound.play();
                        }else{
                            noupgradeSound.play();
                        }
                    }
                }
                
                 
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
                if(counter < products.length-2){

                    counter++;
                    System.out.println("left"+counter);
                    productOnScreen[0] = products[counter-1];
                    productOnScreen[1] = products[counter];
                    productOnScreen[2] = products[counter+1];
                }
                else if(counter == products.length-2){
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
        this.functionBtns[ButtomCode.RIGHT_BTN].setLabelSize(functionBtns[ButtomCode.RIGHT_BTN].getWidth()*4);
        this.functionBtns[ButtomCode.RIGHT_BTN].setCallback(new Callback(){

            @Override
            public void doSomthing() {
                if(counter > 2){
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
                    backgroundSound.stop();
                    gsChangeListener.changeScene(MainPanel.SELL_SCENE);
                } catch (IOException ex) {
                    System.out.println("player save problem from StorceScene back to MenuScene");;
                }
            }
        });
    }
  
}
