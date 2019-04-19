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
    private final int funcBtnWidthUnit = 100;//functionBtn的一個單位大小
    private final int itemBtnWidthUnit = 150;//itemBtn的一個單位大小
    private final int padding = 20;//與邊框距離
    private int itemBtnXcenter;//選單圖片中心x座標
    private int itemBtnYcenter;//選單圖片中心y座標
    private int[] itemBtnSizeFactor;//用來放大選單圖片
    private String[] itemBtnIconPaths;
    private int itemBtnNum;
    
    private Button[] functionBtns;
    private Button[] itemBtns;
    private Player player;
    private int cost;//每個物品價值多少錢所要扣除player inventory的金額
    

    
    public StoreScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        cost = 100;
        this.itemBtnXcenter = (int)(Resource.SCREEN_WIDTH*0.2f)+this.funcBtnWidthUnit/2+padding + this.itemBtnWidthUnit/2;
        this.itemBtnYcenter = (int)(Resource.SCREEN_HEIGHT*0.333f);
        player = Player.getPlayerInstane();
        
        //測試player所擁有金額
        player.setInventory(10000);
        
        rc = ImgResource.getInstance();
        
        //button建構子特殊 Button(String iconName, int width, int height, int x, int y)
        backgroundImg = rc.tryGetImage("/resources/storeBg.png");  //store background 圖片
        //初始化並放置functionBtns 位置
        itemBtnNum = 5;
        this.functionBtns = new Button[5];
        
        
        this.functionBtns[this.BACK_BTN] = new Button("/resources/return_click.png", funcBtnWidthUnit, funcBtnWidthUnit,padding,padding); 
        
        this.functionBtns[this.BACK_BTN].setCallback(new Callback() {
            @Override
            public void doSomthing() {
                gsChangeListener.changeScene(MainPanel.MENU_SCENE);
            }
        });
        
        
        this.functionBtns[this.BUY_BTN] = new Button("/resources/clickBtn.png",  funcBtnWidthUnit*2, funcBtnWidthUnit,padding,Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-120); //120是視窗本身吃掉的高度
        this.functionBtns[this.BUY_BTN].setLabel("BUY");
        
        this.functionBtns[this.BUY_BTN].setCallback(new Callback() {
            @Override
            public void doSomthing() {
                player.increaseInventory(itemBtns[0].getIntData());
            }
        });
        
        this.functionBtns[this.LEFT_BTN] = new Button("/resources/clickBtn.png", funcBtnWidthUnit/2, funcBtnWidthUnit,
                (int) (Resource.SCREEN_WIDTH * 0.1f)-funcBtnWidthUnit/2/2, (int) (Resource.SCREEN_HEIGHT * 0.333f)-funcBtnWidthUnit/2);  
        this.functionBtns[this.LEFT_BTN].setLabel("<");
        this.functionBtns[this.RIGHT_BTN] = new Button("/resources/clickBtn.png", funcBtnWidthUnit/2, funcBtnWidthUnit,
                (int) (Resource.SCREEN_WIDTH * 0.9f)-funcBtnWidthUnit/2/2, (int) (Resource.SCREEN_HEIGHT * 0.333f)-funcBtnWidthUnit/2);
        this.functionBtns[this.RIGHT_BTN].setLabel(">");

        this.functionBtns[this.START_BTN] = new Button("/resources/clickBtn.png", funcBtnWidthUnit*2, funcBtnWidthUnit,
                Resource.SCREEN_WIDTH-funcBtnWidthUnit*2-padding , Resource.SCREEN_HEIGHT-funcBtnWidthUnit-padding-120);
        this.functionBtns[this.START_BTN].setLabel("START");

        //設定物品tiembtnIcon圖片
        this.itemBtnIconPaths = new String[itemBtnNum];
        this.itemBtnIconPaths[0] = "/resources/atk_up.png";
        this.itemBtnIconPaths[1] = "/resources/atk_up.png";
        this.itemBtnIconPaths[2] = "/resources/atk_up.png";
        this.itemBtnIconPaths[3] = "/resources/atk_up.png";
        this.itemBtnIconPaths[4] = "/resources/atk_up.png";
        
        //初始化並放置itemBtns 位置
        this.itemBtns = new Button[5];
        this.itemBtnSizeFactor = new int[this.itemBtns.length];
        for(int i = 0; i < this.itemBtns.length; i++){
            this.itemBtnSizeFactor[i] = 1;
        }
        this.itemBtnSizeFactor[1] = 2;
        this.itemBtns[0] = new Button(this.itemBtnIconPaths[0],itemBtnWidthUnit*itemBtnSizeFactor[0],itemBtnWidthUnit*itemBtnSizeFactor[0],
                itemBtnXcenter-itemBtnWidthUnit/2,itemBtnYcenter-itemBtnWidthUnit/2);
        for(int i = 1; i < this.itemBtns.length;i++){
                this.itemBtns[i] = new Button(this.itemBtnIconPaths[i],itemBtnWidthUnit*itemBtnSizeFactor[i],itemBtnWidthUnit*itemBtnSizeFactor[i],
                this.itemBtns[i-1].getX()+this.itemBtns[i-1].getImgWidth()+padding*3,itemBtnYcenter-itemBtnWidthUnit*itemBtnSizeFactor[i]/2);
            
        }
        
        //設定物品價格
        this.itemBtns[0].setIntData(100);
        this.itemBtns[0].setIntData(100);
        this.itemBtns[0].setIntData(100);
        this.itemBtns[0].setIntData(100);
        this.itemBtns[0].setIntData(100);
        

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
                
//                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, introBtn)) {
//                    introBtn.setImgState(1);
//                }
//                if(e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, newGameBtn)){
//                    newGameBtn.setImgState(1);
//                }
//                if(e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, loadBtn)){
//                    loadBtn.setImgState(1);
//                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                
                if(e.getButton() == MouseEvent.BUTTON1){
                    for(int i = 0; i < functionBtns.length; i++){
                        functionBtns[i].setImgState(0);
                        functionBtns[i].setClickState(false);
                        if(isOnBtn(e,functionBtns[i])){
                            functionBtns[i].setIsClicked(true);
                            
                        }
                        
                    }
                }
                
//                if(e.getButton() == MouseEvent.BUTTON1 ){
//                    newGameBtn.setImgState(0);
//                    loadBtn.setImgState(0);
//                    introBtn.setImgState(0);
//                }
//                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, introBtn)) {
//                     gsChangeListener.changeScene(MainPanel.INTRO_SCENE);
//                }
//                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, newGameBtn)) {
//                     gsChangeListener.changeScene(MainPanel.STORE_SCENE);
//                }
                
            }
            @Override
            public void mouseDragged(MouseEvent e){
                System.out.println("dragggggggg");
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
        if(cost != 0){
            player.increaseInventory(-1);
            cost--;
        }
        
        String msg = this.player.getInventory()+"";
        int sw = fm.stringWidth(msg);
        int sa = fm.getAscent();
        g.drawString(msg, Resource.SCREEN_WIDTH-sw-20, sa);
        

    }

    @Override
    public void logicEvent() {

        if(functionBtns[BACK_BTN].isIsClicked()){
           functionBtns[BACK_BTN].action();
        }

    }

    
    
}
