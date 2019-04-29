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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import scene.Scene;
import vbattle.Fontes;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.Resource;
import vbattle.Stuff;

public class StageScene extends Scene {

    private Stuff actor1;
    private Stuff actor2;
    private int timeCount = 0; //倍數計時器：初始化
    private int eventTime = 10; // eventListener時間週期：大於0的常數
    private int battleAreaY[] = {500, 350, 200};//可放置的路
    private ImgResource rc;
    private ArrayList<Stuff> movingPlayerStuff1 = new ArrayList<>();
    private ArrayList<Stuff> movingEnemyStuff1 = new ArrayList<>();
    private ArrayList<Stuff> movingPlayerStuff2 = new ArrayList<>();
    private ArrayList<Stuff> movingEnemyStuff2 = new ArrayList<>();
    private ArrayList<Stuff> movingPlayerStuff3 = new ArrayList<>();
    private ArrayList<Stuff> movingEnemyStuff3 = new ArrayList<>();
    private int drag = -1;
    private int dragX, dragY;
    //icon屬性
    private BufferedImage icon;
    private int[] iconX = new int[5];
    private int[] iconY = new int[5];
    private int[] iconX1 = new int[5];
    private int[] iconY1 = new int[5];
    private int[] iconNum = new int[5];
    private boolean[] iconable = new boolean[5];
    //icon屬性

    private Font fontBit;
    private Font priceFontBit;
    private int money;
    private static final int MAX_MONEY = 100; //

    public StageScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        try {
            actor1 = new Stuff(1, 100, battleAreaY[1], 128, 128, 0, "actor1");  //int type(1:我方角 or 2:敵人) , int x0, int y0, int imgWidth, int imgHeight, int actorIndex(角色圖片), String txtpath(角色資訊)
             movingEnemyStuff1.add(new Stuff(-1, 800, battleAreaY[0] , 100, 100, 3, "actor2"));
        } catch (IOException ex) {
        }
        try {
            actor1 = new Stuff(1, 100, battleAreaY[1] , 128, 128, 0, "actor1");  //int type(1:我方角 or 2:敵人) , int x0, int y0, int imgWidth, int imgHeight, int actorIndex(角色圖片), String txtpath(角色資訊)
        } catch (IOException a1) {
        }
        try {
            actor2 = new Stuff(-1, 800, battleAreaY[1] , 128, 128, 3, "actor2");
        } catch (IOException a2) {
        }
        rc = ImgResource.getInstance();
        icon = rc.tryGetImage("/resources/tinyCharacters.png");

        for (int i = 0; i < 5; i++) {
            iconX[i] = (int)(Resource.SCREEN_WIDTH *(0.1f * i)+Resource.SCREEN_WIDTH *(0.4f));
            iconY[i] = (int)(Resource.SCREEN_HEIGHT * 0.7f);
            iconX1[i] = iconX[i] +100;
            iconY1[i] = iconY[i] +100;
            iconNum[i]=i;
        }

        money = 0;
        fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 20);
        priceFontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 40);
    }

    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {

            public void isOnIcon(MouseEvent e) {
                for (int i = 0; i < 5; i++) {
                    if (e.getX() >= iconX[i]
                            && e.getX() <= iconX[i] + iconX1[i]
                            && e.getY() >= iconY[i]
                            && e.getY() <= iconY[i] + iconY1[i]
                            && money >= checkActorPrice(i)) {
                        iconable[i] = true;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                this.isOnIcon(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    for (int i = 0; i < 5; i++) {
                        if (iconable[i]) {
                            drag = iconNum[i];
                            dragX = e.getX() - 50;
                            dragY = e.getY() - 50;
                        }
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (drag != -1) {
                    dragX = e.getX() - 50;
                    dragY = e.getY() - 50;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (int i = 0; i < 5; i++) {
                    if (iconable[i]) {
                        iconable[i] = false;
                    }
                }
                if (drag != -1) {
                    money -= checkActorPrice(drag);
                    assign(e);
                }
                drag = -1;
            }
            
//            public void assign(MouseEvent e,ArrayList<Stuff> stuff){
            public void assign(MouseEvent e){
                if(e.getY() >= battleAreaY[1]+50 && e.getY() < battleAreaY[0]+50){
                    try {
                        movingPlayerStuff1.add(new Stuff(1, e.getX() - 50, battleAreaY[0], 100, 100, drag, "test"));
                    } catch (IOException ex) {
                    }
                }
                if (e.getY() >= battleAreaY[2] + 50 && e.getY() < battleAreaY[1] + 50) {
                    try {
                    movingPlayerStuff2.add(new Stuff(1,e.getX()-50,battleAreaY[1],100,100,drag,"test"));
                    } catch (IOException ex) {
                    }
                }
                if (e.getY() < battleAreaY[2] + 50) {
                    try {
                    movingPlayerStuff3.add(new Stuff(1,e.getX()-50,battleAreaY[2],100,100,drag,"test"));
                    } catch (IOException ex) {
                    }
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        //場景
        g.setColor(Color.BLACK);
        movingEnemyStuff1.get(0).paint(g);
//        g.fillRect(0, 0, 1200, 900);
        actor1.paint(g);
        actor2.paint(g);

        g.setFont(fontBit);
        g.setColor(new Color(0, 0, 0));
        FontMetrics fm = g.getFontMetrics();

        //玩家金額
        g.setColor(Color.BLACK);
        int sw = fm.stringWidth("$" + money + "/" + MAX_MONEY);
        g.drawString("$" + money + "/" + MAX_MONEY, Resource.SCREEN_WIDTH - sw, Resource.SCREEN_HEIGHT / 9 - 30);

        //腳色
        g.setFont(priceFontBit);
        for (int i = 0; i < 5; i++) {
//            g.setColor(Color.BLACK);
//            g.fillRect(iconX[i]-5, iconY[i]-5, 115, 120);
//            
//            g.setColor(Color.white);
//            g.fillRect(iconX[i], iconY[i], 105, 110);

            if (this.money < checkActorPrice(i)) { //如果錢不夠，角色就呈現灰階狀態
                g.drawImage(icon, iconX[i], iconY[i], iconX[i] + 100, iconY[i] + 100, 0, iconNum[i] * 32, 32, (iconNum[i] + 1) * 32, null);
                g.setColor(Color.BLACK);
                int sw1 = fm.stringWidth("$" + checkActorPrice(i) + "");
                g.drawString("$" + checkActorPrice(i) + "", iconX[i] - 5 + 115 / 2 - sw1 / 2 + 30, iconY[i] + 130);
                g.setColor(new Color(186, 186, 186, 150));
                g.fillRect(iconX[i] - 5, iconY[i] - 5, 115, 140);
            } else {
                g.setColor(new Color(186, 186, 186, 60));
                g.fillRect(iconX[i] - 5, iconY[i] - 15, 115, 140);
                g.drawImage(icon, iconX[i], iconY[i] - 10, iconX[i] + 100, iconY[i] + 90, 0, iconNum[i] * 32, 32, (iconNum[i] + 1) * 32, null);
                g.setColor(Color.BLACK);
                int sw1 = fm.stringWidth("$" + checkActorPrice(i) + "");
                g.drawString("$" + checkActorPrice(i) + "", iconX[i] + 115 / 2 - sw1 / 2 + 30, iconY[i] + 130-10);
            }

//            //各角色金額
//            g.setColor(Color.BLACK);
//            int sw1 = fm.stringWidth("$" + checkActorPrice(i) + "");
//            g.drawString("$" + checkActorPrice(i) + "", iconX[i] + 120 / 2 - sw1 / 2 + 20, iconY[i] + 150);

        }
        for (int i = 0; i < this.movingPlayerStuff1.size(); i++) {
            this.movingPlayerStuff1.get(i).paint(g);
        }
//        for (int i = 0; i < this.movingPlayerStuff2.size(); i++) {
//            this.movingPlayerStuff2.get(i).paint(g);
//        }
//        for (int i = 0; i < this.movingPlayerStuff3.size(); i++) {
//            this.movingPlayerStuff3.get(i).paint(g);
//        }
        if(drag != -1){
            g.drawImage(icon,dragX,dragY,dragX+100,dragY+100,0,drag*32,32,(drag+1)*32,null);
        }

    }

    @Override
    public void logicEvent() {
        timeCount ++;//倍數計時器：FPS為底的時間倍數
        // for (int i = 0; i < movingPlayerStuff1.size(); i++) {
        //     movingPlayerStuff1.get(i).refreshCd();
        // }
        // for (int i = 0; i < movingEnemyStuff1.size(); i++) {
        //     movingEnemyStuff1.get(i).refreshCd();
        // }
        actor1.refreshCd();
        actor2.refreshCd();

        if (timeCount == eventTime) { // 計時器重置：取所有事件的最小公倍數
            timeCount = 0;
        }

        if (timeCount % 2 == 0) {
            eventlistener();
        }
        if (timeCount % 5 == 0) {
            if (money < MAX_MONEY) {
                money += 1;
            }
        }
    }

    public void eventlistener() {
//        //任一角色沒命就停止
//        if(actor1.getHp() <= 0 || actor2.getHp() <= 0){
//            System.out.println("die");
//            return;
//        }

        //我方角 
        //如沒有碰撞就呼叫走路方法
            moveMethod(movingPlayerStuff1,movingEnemyStuff1);
            moveMethod(movingEnemyStuff1,movingPlayerStuff1);
            moveMethod(movingPlayerStuff2,movingEnemyStuff2);
            moveMethod(movingEnemyStuff2,movingPlayerStuff2);
            moveMethod(movingPlayerStuff3,movingEnemyStuff3);
            moveMethod(movingEnemyStuff3,movingPlayerStuff3);
//        //如果碰撞到就呼叫攻擊方法
            attackMethod(movingPlayerStuff1,movingEnemyStuff1);
            attackMethod(movingPlayerStuff2,movingEnemyStuff2);
            attackMethod(movingPlayerStuff3,movingEnemyStuff3);
            attackMethod(movingEnemyStuff1,movingPlayerStuff1);
            attackMethod(movingEnemyStuff2,movingPlayerStuff2);
            attackMethod(movingEnemyStuff3,movingPlayerStuff3);
//        if (actor1.collisionCheck(actor2) == false) {
//        for (int i = 0; i < movingPlayerStuff1.size(); i++) {
//            tmpmovemethod(movingPlayerStuff1.get(i));
//        }
//        for (int i = 0; i < movingPlayerStuff2.size(); i++) {
//            tmpmovemethod(movingPlayerStuff2.get(i));
//        }
//        for (int i = 0; i < movingPlayerStuff3.size(); i++) {
//            tmpmovemethod(movingPlayerStuff3.get(i));
//        }
//        }
        //如果碰撞到就呼叫攻擊方法
        // if(actor1.getHp()<=0){
        //     actor1.die();
        // }else{
        //     if (actor1.collisionCheck(actor2) == false) {
        //     tmpmovemethod(actor1);
        // }
        // if (actor1.collisionCheck(actor2) ) {
        //     tmpattckmethod(actor1,actor2);
        // }
        // }
       
        // //敵方角
        // if(actor2.getHp()<=0){
        //     actor2.die();
        // }else{
        //     if (actor2.collisionCheck(actor1) == false) {
        //     tmpmovemethod(actor2);
        // }if (actor2.collisionCheck(actor1)) {
        //     tmpattckmethod(actor2,actor1);
        // }
        // }
        
    }
    
    public void moveMethod(ArrayList<Stuff> stuff1,ArrayList<Stuff> stuff2){
        for (int i = 0; i < stuff1.size(); i++) {
            if(stuff1.get(i).collisionCheck(stuff2) == null){
                stuff1.get(i).move();
            }
        }
    }
    public void attackMethod(ArrayList<Stuff> stuff1,ArrayList<Stuff> stuff2){
        Stuff tmp;
        for(int i=0; i < stuff1.size(); i++) {
            tmp = stuff1.get(i).collisionCheck(stuff2);
            if (tmp != null) {
                stuff1.get(i).attack(tmp);
            }
        }
    }

    public int checkActorPrice(int i) {
        switch (i) {
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
