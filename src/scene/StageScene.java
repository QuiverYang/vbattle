package scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import vbattle.Button;
import vbattle.Fontes;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.Resource;
import vbattle.Stuff;

public class StageScene extends Scene{
    private int timeCount = 0; //倍數計時器：初始化
    private int eventTime = 100; // eventListener時間週期：大於0的常數
    private int battleAreaY[] ={600,450,300};//可放置的路
    private ImgResource rc;
    private ArrayList<Stuff> movingPlayerStuff1 = new ArrayList<>();
    private ArrayList<Stuff> movingEnemyStuff1 = new ArrayList<>();
    private ArrayList<Stuff> movingPlayerStuff2 = new ArrayList<>();
    private ArrayList<Stuff> movingEnemyStuff2 = new ArrayList<>();
    private ArrayList<Stuff> movingPlayerStuff3 = new ArrayList<>();
    private ArrayList<Stuff> movingEnemyStuff3 = new ArrayList<>();
    private ArrayList<Stuff> dieStuff = new ArrayList<>();
    private int drag = -1;
    private int dragX,dragY;
    private float genRate = 0.2f;
    //icon屬性
    private BufferedImage icon;
    private int[] iconX = new int[5];
    private int[] iconY = new int[5];
    private int[] iconX1 = new int[5];
    private int[] iconY1 = new int[5];
    private int[] iconNum = new int[5];
    private int iconSize = 100;
    private boolean[] dragable = new boolean[5];
    //icon屬性
    private Button gameOverBtn;
    
    Font fontBit,priceFontBit,game;
    private Color lightGray;
    private int money;
    private int MAX_MONEY = 100; //
    private BufferedImage background;
    private boolean gameOver = false;

    public StageScene(MainPanel.GameStatusChangeListener gsChangeListener){
        super(gsChangeListener);
        rc = ImgResource.getInstance();
        icon = rc.tryGetImage("/resources/tinyCharacters.png");
        background = rc.tryGetImage("/resources/background5.png");
        for (int i = 0; i < 5; i++) {
            iconX[i] = (int)(Resource.SCREEN_WIDTH *(0.1f * i)+Resource.SCREEN_WIDTH *(0.26f));
            iconY[i] = (int)(Resource.SCREEN_HEIGHT * 0.77f);
            iconX1[i] = iconX[i] + iconSize;
            iconY1[i] = iconY[i] + iconSize;
            iconNum[i]=i;
        }

        money = 0;
        fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 20);
        priceFontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 45);
        lightGray = new Color(186,186,186,150);
        
    }

   @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {

            public void isOnIcon(MouseEvent e) {
                    for (int i = 0; i < 5; i++) {
                        if (e.getX() >= iconX[i]
                            && e.getX() <= iconX1[i]
                            && e.getY() >= iconY[i]
                            && e.getY() <= iconY1[i]
                            && money >= checkActorPrice(i))
                        dragable[i] = true;
                    }
            }
            
            @Override
            public void mousePressed(MouseEvent e){
                this.isOnIcon(e);
                    if(e.getButton() == MouseEvent.BUTTON1){
                        for (int i = 0; i < 5; i++) {
                            if (dragable[i]){
                                drag = iconNum[i];
                                dragX = e.getX() - (iconSize/2);
                                dragY = e.getY() - (iconSize/2);
                            }
                        }
                    }
                if (gameOver && e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, gameOverBtn)) {
                    gameOverBtn.setImgState(1);
                    gameOverBtn.setClickState(true);
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e){
                if(drag != -1){
                    dragX = e.getX() - (iconSize/2);
                    dragY = e.getY() - (iconSize/2);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                    for (int i = 0; i < 5; i++) {
                        if(dragable[i]){
                            dragable[i] = false;
                        }
                    }
                    if(drag != -1){
                        assign(e);
                    }
                    drag = -1;
                if (gameOver && e.getButton() == MouseEvent.BUTTON1 && Button.isOnBtn(e, gameOverBtn) && gameOverBtn.getClickState()) {
                    gameOverBtn.setImgState(0);
                    gameOverBtn.setClickState(false);
                    gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                }
            }
            
            //待整合
            public void assign(MouseEvent e){
                if(e.getY() > battleAreaY[0] - iconSize && e.getY() < battleAreaY[0] + (iconSize/2)){
                    try {
                        money -= checkActorPrice(drag);
                        movingPlayerStuff1.add(new Stuff(1,e.getX() - (iconSize/2),battleAreaY[0], iconSize,iconSize,drag,"test"+drag));
                    } catch (IOException ex) {
                    }
                }
                if(e.getY() > battleAreaY[1] - iconSize && e.getY() < battleAreaY[1] + (iconSize/2)){
                    try {
                        money -= checkActorPrice(drag);
                        movingPlayerStuff2.add(new Stuff(1,e.getX() - (iconSize/2),battleAreaY[1],iconSize,iconSize,drag,"test"+drag));
                    } catch (IOException ex) {
                    }
                }
                if(e.getY() > battleAreaY[2] - iconSize && e.getY() < battleAreaY[2] + (iconSize/2)){
                    try {
                        money -= checkActorPrice(drag);
                        movingPlayerStuff3.add(new Stuff(1,e.getX() - (iconSize/2),battleAreaY[2],iconSize,iconSize,drag,"test"+drag));
                    } catch (IOException ex) {
                    }
                }
            }
            //待整合
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.background, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT, null);
        //場景
//        g.setColor(Color.BLACK);
//        g.fillRect(0, 0, 1200, 900);
        g.setFont(this.priceFontBit);
        FontMetrics fm = g.getFontMetrics();
        
        //腳色
        for (int i = 0; i < 5; i++) {
            g.setColor(Color.white);
            g.fillRect(iconX[i], iconY[i], 110, 110);
            g.drawImage(icon, iconX[i]+100/2-80/2, iconY[i],iconX[i]+80+100/2-80/2,iconY[i]+80,0,iconNum[i]*32,32,(iconNum[i]+1)*32,null);
            if(this.money<checkActorPrice(i)){
                g.setColor(lightGray);
                g.fillRect(iconX[i], iconY[i], 110, 110);
            }
             g.setColor(Color.BLACK);
            int sw = fm.stringWidth("$"+checkActorPrice(i)+"");
            g.drawString("$"+checkActorPrice(i)+"", iconX[i]+110/2-sw/2, iconY[i]+102);
            
        }
        
        
        //待整合
        for (int i = 0; i < this.movingPlayerStuff1.size(); i++) {
            this.movingPlayerStuff1.get(i).paint(g);
        }
        for (int i = 0; i < this.movingPlayerStuff2.size(); i++) {
            this.movingPlayerStuff2.get(i).paint(g);
        }
        for (int i = 0; i < this.movingPlayerStuff3.size(); i++) {
            this.movingPlayerStuff3.get(i).paint(g);
        }
        for (int i = 0; i < this.movingEnemyStuff1.size(); i++) {
            this.movingEnemyStuff1.get(i).paint(g);
        }
        for (int i = 0; i < this.movingEnemyStuff2.size(); i++) {
            this.movingEnemyStuff2.get(i).paint(g);
        }
        for (int i = 0; i < this.movingEnemyStuff3.size(); i++) {
            this.movingEnemyStuff3.get(i).paint(g);
        }
        for (int i = 0; i < dieStuff.size(); i++) {
            dieStuff.get(i).paint(g);
        }
        //待整合
        
        
        if(drag != -1){
            g.drawImage(icon,dragX,dragY,dragX+iconSize,dragY+iconSize,0,drag*32,32,(drag+1)*32,null);
        }
        
        //金額
        g.setFont(this.fontBit);
        g.setColor(Color.white);
        int sw =fm. stringWidth("$"+money+"/"+MAX_MONEY);
        g.drawString("$"+money+"/"+MAX_MONEY, Resource.SCREEN_WIDTH/12*9, Resource.SCREEN_HEIGHT/9-40);
        if(gameOver == true){
            gameOverBtn.paintBtn(g);
            g.drawString("GAMEOVER", Resource.SCREEN_WIDTH/5*2, Resource.SCREEN_HEIGHT/2);
            g.setFont(priceFontBit);
            g.drawString("TO_STORE", gameOverBtn.getX()+25, gameOverBtn.getY()+50);
        }
        
       
    }

    @Override
    public void logicEvent() {
        timeCount ++;//倍數計時器：FPS為底的時間倍數
        if(gameOver == false){
            for (int i = 0; i < movingPlayerStuff1.size(); i++) {
                movingPlayerStuff1.get(i).refreshCd();
            }
            for (int i = 0; i < movingEnemyStuff1.size(); i++) {
                movingEnemyStuff1.get(i).refreshCd();
            }
            for (int i = 0; i < movingPlayerStuff2.size(); i++) {
                movingPlayerStuff2.get(i).refreshCd();
            }
            for (int i = 0; i < movingEnemyStuff2.size(); i++) {
                movingEnemyStuff2.get(i).refreshCd();
            }
            for (int i = 0; i < movingPlayerStuff3.size(); i++) {
                movingPlayerStuff3.get(i).refreshCd();
            }
            for (int i = 0; i < movingEnemyStuff3.size(); i++) {
                movingEnemyStuff3.get(i).refreshCd();
            }
            if(timeCount%2 == 0 ){
                eventlistener();
            }
            if(timeCount%100 == 0){
                stuffGen();
            }
            if(timeCount%2 == 0){
                if(money < MAX_MONEY){
                    money+=1;
                }
            }
        }
        
        
        if(timeCount == eventTime){ // 計時器重置：3600結束場景
            timeCount = 0;
        }
    }
    
    
    public void stuffGen(){
        this.genRate += 0.01f;
        float random = (float)(Math.random());
        if(random < genRate){
            try {
                movingEnemyStuff1.add(new Stuff(-1, Resource.SCREEN_WIDTH, battleAreaY[0] , iconSize, iconSize, 3, "actor2"));
            } catch (IOException ex) {
            }
        }
        random = (float)(Math.random());
        if(random < genRate){
            try {
                movingEnemyStuff2.add(new Stuff(-1, Resource.SCREEN_WIDTH, battleAreaY[1] , iconSize, iconSize, 3, "actor2"));
            } catch (IOException ex) {
            }
        }
        random = (float)(Math.random());
        if(random < genRate){
            try {
                movingEnemyStuff3.add(new Stuff(-1, Resource.SCREEN_WIDTH, battleAreaY[2] , iconSize, iconSize, 3, "actor2"));
            } catch (IOException ex) {
            }
        }
    }
    
    public void eventlistener(){
        //我方角 
        //如沒有碰撞就呼叫走路方法
            moveMethod(movingPlayerStuff1,movingEnemyStuff1);
            moveMethod(movingEnemyStuff1,movingPlayerStuff1);
            moveMethod(movingPlayerStuff2,movingEnemyStuff2);
            moveMethod(movingEnemyStuff2,movingPlayerStuff2);
            moveMethod(movingPlayerStuff3,movingEnemyStuff3);
            moveMethod(movingEnemyStuff3,movingPlayerStuff3);
//        //如果碰撞到就呼叫碰撞方法
            fightMethod(movingPlayerStuff1,movingEnemyStuff1);
            fightMethod(movingPlayerStuff2,movingEnemyStuff2);
            fightMethod(movingPlayerStuff3,movingEnemyStuff3);
            fightMethod(movingEnemyStuff1,movingPlayerStuff1);
            fightMethod(movingEnemyStuff2,movingPlayerStuff2);
            fightMethod(movingEnemyStuff3,movingPlayerStuff3);
            ghostMethod(dieStuff);
            gameOver(movingEnemyStuff1);
            gameOver(movingEnemyStuff2);
            gameOver(movingEnemyStuff3);
    }
    
    private void gameOver(ArrayList<Stuff> stuff){
        for (int i = 0; i < stuff.size(); i++) {
            if(stuff.get(i).getX1() < 0){
                gameOverBtn = new Button("/resources/clickBtn.png",Resource.SCREEN_WIDTH / 12, (int) (Resource.SCREEN_HEIGHT / 9 * 6.5), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);
                this.gameOver = true;
            }
        }
    }
    
    private void moveMethod(ArrayList<Stuff> stuff1,ArrayList<Stuff> stuff2){
        for (int i = 0; i < stuff1.size(); i++) {
            if(stuff1.get(i).collisionCheck(stuff2) == null){
                stuff1.get(i).move();
            }
        }
    }
    
    private void fightMethod(ArrayList<Stuff> stuff1,ArrayList<Stuff> stuff2){
        Stuff tmp;
        for(int i=0; i < stuff1.size(); i++) {
            tmp = stuff1.get(i).collisionCheck(stuff2);
            if (tmp != null && tmp.getHp() > 0) {
                stuff1.get(i).attack(tmp);
            }
            if(tmp != null && tmp.getHp() < 1){
                dieStuff.add(tmp);
                stuff2.remove(tmp);
            }
        }
    }
    
    private void ghostMethod(ArrayList<Stuff> ghost){
        for (int i = 0; i < ghost.size(); i++) {
            ghost.get(i).die();
            if(ghost.get(i).getX0() < 0)
                ghost.remove(i);
        }
    }
    
    public int checkActorPrice(int i){
        switch(i){
            case 0:
                return Stuff.ACTOR1_PRICE;
            case 1:
                return Stuff.ACTOR2_PRICE;
            case 2:
                return Stuff.ACTOR3_PRICE;
            case 3:
                return Stuff.ACTOR4_PRICE;
            case 4:
                return Stuff.ACTOR5_PRICE;
        }
        return -1;
    }
}
