package scene;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import vbattle.Bomb;
import vbattle.BombA;
import vbattle.Button;
import vbattle.Coin;
import vbattle.Fontes;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.Player;
import vbattle.Resource;
import vbattle.Stuff;

/**
 *
 * @author anny,LC
 */
public class StageScene extends Scene {

    //遊戲控制
    private int timeCount = 0; //倍數計時器：初始化
    private int eventTime = 100; // eventListener時間週期：大於0的常數
    private int money = 200;
    private int MAX_MONEY = 200; //
    private boolean gameOver = false;
    private Player player;
    private int hp, mp;
    private int maxHp;      //存取玩家原始hp
    private int maxMp;
    private int cash;
    private BufferedReader br;
    private ArrayList<Integer> delay = new ArrayList<>();
    private ArrayList<Integer> type = new ArrayList<>();
    private ArrayList<Integer> areaI = new ArrayList<>();
    private int stageCounter = 0;
    private int genCounter = 0;

    //物件控制
    private int battleAreaY[] = {Resource.SCREEN_WIDTH/2,  (int)(Resource.SCREEN_WIDTH*0.375),  (int)(Resource.SCREEN_WIDTH*0.25)};//可放置的路 (original:600,450,300)
    private ArrayList<ArrayList<Stuff>> stuffList = new ArrayList<>();
    private ArrayList<Stuff> dieStuff = new ArrayList<>();
    private ArrayList<Coin> coins = new ArrayList();
    private int drag = -1;
    private int dragX, dragY;
    private float genRate = 0.05f;// 怪物產生機率
    private int[] iconX = new int[5];
    private int[] iconY = new int[5];
    private int[] iconNum = new int[5];
    private int iconSize = 100;
    private boolean[] dragable = new boolean[5];
    private BombA bombContainer;

    //場景元件
    private ImgResource rc;
    private BufferedImage icon, background;
    private Button gameOverBtn, returnBtn;
    private Font fontBit, priceFontBit, gameFontBit;
    private Color lightGray;

    private BufferedImage winImg;
    private BufferedImage loseImg;
    private BufferedImage energyIng;
    private BufferedImage coinImg;
    private AudioClip clickSound;
    private AudioClip winSound;
    private AudioClip loseSound;
    public static AudioClip stageBgm;

    private  int SCREEN_WIDTH = Resource.SCREEN_WIDTH;
    private  int SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;

    public StageScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);

        for (int i = 0; i < 6; i++) {
            stuffList.add(new ArrayList<Stuff>());
        }
        for (int i = 0; i < 5; i++) {
            iconX[i] = (int) (Resource.SCREEN_WIDTH * (0.1f * i) + Resource.SCREEN_WIDTH * (0.3f));
            iconY[i] = (int) (Resource.SCREEN_HEIGHT * 0.8f);
            iconNum[i] = i;
        }

        rc = ImgResource.getInstance();
        icon = rc.tryGetImage("/resources/tinyCharacters.png");
        background = rc.tryGetImage("/resources/background5.png");
        winImg = rc.tryGetImage("/resources/win.png");
        loseImg = rc.tryGetImage("/resources/lose.png");
        energyIng = rc.tryGetImage("/resources/energy.png");
        coinImg = rc.tryGetImage("/resources/coin.png");
        fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 23);
        priceFontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 45);
        gameFontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 37);

        lightGray = new Color(186, 186, 186, 150);
        returnBtn = new Button("/resources/return_click.png", 20, 20, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
        gameOverBtn = new Button("/resources/clickBtn.png", Resource.SCREEN_WIDTH / 12 * 8, (int) (Resource.SCREEN_HEIGHT / 9 * 6), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);

        try {
            clickSound = Applet.newAudioClip(getClass().getResource("/resources/pop.wav"));
            winSound = Applet.newAudioClip(getClass().getResource("/resources/winerSound.wav"));
            loseSound = Applet.newAudioClip(getClass().getResource("/resources/LosingSoundEffects.wav"));
            stageBgm = Applet.newAudioClip(getClass().getResource("/resources/StageGamesSound.wav"));
            stageBgm.loop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cash = 0;
        player = Player.getPlayerInstane();
        hp = player.getHp();
        mp = player.getMp();
        try {
            br = new BufferedReader(new FileReader("stage1.txt"));
            while (br.ready()) {
                String str[] = br.readLine().split(",");
                delay.add(Integer.parseInt(str[0]));
                type.add(Integer.parseInt(str[1]));
                areaI.add(Integer.parseInt(str[2]));
            }
        } catch (Exception e) {
        }

    }

    //玩家控制
    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {

            public void isOnIcon(MouseEvent e) {
                for (int i = 0; i < 5; i++) {
                    if (e.getX() >= iconX[i]
                            && e.getX() <= iconX[i] + iconSize
                            && e.getY() >= iconY[i]
                            && e.getY() <= iconY[i] + iconSize
                            && money >= checkActorPrice(i)) {
                        dragable[i] = true;
                    }
                }
            }

            //判斷點擊惡魔
            public boolean isOnDevilPic(MouseEvent e, Stuff dieStuff) {
                if (e.getX() >= dieStuff.getX0()
                        && e.getX() <= dieStuff.getX0() + dieStuff.getImgWidth()
                        && e.getY() >= dieStuff.getY0()
                        && e.getY() <= dieStuff.getY0() + dieStuff.getImgHeight()) {
                    return true;
                }
                return false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                this.isOnIcon(e);
                if (e.getButton() == MouseEvent.BUTTON1) {
                    for (int i = 0; i < 5; i++) {
                        if (dragable[i]) {
                            drag = iconNum[i];
                            dragX = e.getX() - (iconSize / 2);
                            dragY = e.getY() - (iconSize / 2);
                        }
                    }
                }

                if (gameOver && Button.isOnBtn(e, gameOverBtn)) {
                    gameOverBtn.setClickState(true);
                    gameOverBtn.setImgState(1);
                }
                if (Button.isOnBtn(e, returnBtn)) {
                    returnBtn.setClickState(true);
                    returnBtn.setImgState(1);
                }

                //判斷點擊惡魔
                for (int i = 0; i < dieStuff.size(); i++) {
                    if (e.getButton() == MouseEvent.BUTTON1 && isOnDevilPic(e, dieStuff.get(i))) {
                        dieStuff.get(i).setClickState(true);
                    }
                }

                for (int i = 0; i < coins.size(); i++) {
                    if (Coin.isOnCoin(e, coins.get(i))) {
                        Coin.CoinClicked(getClass().getResource("/resources/coin.wav"));
                        coins.remove(i);
                        cash += 10;
                        player.increaseCash(10);//設定每隻怪物增加10元
                    }
                }
//                
//                if(e.getButton() == MouseEvent.BUTTON1){
//                    a = new BombA(e.getX(),e.getY(),e.getY(),200);
//                    b = new BombA(e.getX(),e.getY(),e.getY(),100);
//                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (drag != -1) {
                    dragX = e.getX() - (iconSize / 2);
                    dragY = e.getY() - (iconSize / 2);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                returnBtn.setImgState(0);
                returnBtn.setIsClicked(false);
                if (Button.isOnBtn(e, returnBtn) && returnBtn.getClickState()) {
                    returnBtn.setClickState(false);
                    returnBtn.setIsClicked(true);
                    SaveScene.currentScene = MainPanel.STAGE_SCENE;  //設定當前場景為stage scene
                    SaveScene.nextScene = MainPanel.STORE_SCENE;     //設定儲存後場景為商店
                    gsChangeListener.changeScene(MainPanel.SAVE_SCENE);
                }

                for (int i = 0; i < 5; i++) {
                    if (dragable[i]) {
                        dragable[i] = false;
                    }
                }


                //判斷點擊惡魔
                for (int i = 0; i < dieStuff.size(); i++) {
                    if (isOnDevilPic(e, dieStuff.get(i)) && dieStuff.get(i).getClickState()) {  //如果點擊惡魔成功，將此惡魔物件刪除
                        clickSound.play();
                        dieStuff.remove(i);
                    }
                }

                if (gameOver) {
                    gameOverBtn.setImgState(0);
                    gameOverBtn.setIsClicked(false);
                }

                if (gameOver && Button.isOnBtn(e, gameOverBtn) && gameOverBtn.getClickState()) {
                    gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                    
                    MainPanel.backgroundSound.loop();
                    for (int i = 0; i < player.getFp().size(); i++) {
                        player.getFp().get(i).changeValue();
                    }
                }

                for (int i = 0; i < 5; i++) {
                    if (dragable[i]) {
                        dragable[i] = false;
                    }
                    if (drag != -1) {
                        assign(e);
                    }
                    drag = -1;
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(this.background, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT, null);
        g.setFont(this.priceFontBit);
        FontMetrics fm = g.getFontMetrics();

        //腳色
        for (int i = 0; i < 5; i++) {
            g.setColor(Color.white);
            g.fillRect(iconX[i], iconY[i], (int)(Resource.SCREEN_WIDTH*0.092), (int)(Resource.SCREEN_HEIGHT*0.122));
            g.drawImage(icon, iconX[i] + (int)(Resource.SCREEN_WIDTH*0.0083), iconY[i], iconX[i] + (int)(Resource.SCREEN_WIDTH*0.075), iconY[i] + (int)(Resource.SCREEN_HEIGHT*0.089), 0, iconNum[i] * 32, 32, (iconNum[i] + 1) * 32, null);
            if (this.money < checkActorPrice(i)) {
                g.setColor(lightGray);
                g.fillRect(iconX[i], iconY[i], (int)(Resource.SCREEN_WIDTH*0.092), (int)(Resource.SCREEN_HEIGHT*0.122));
            }
            //價格
            g.setColor(Color.BLACK);
            int sw = fm.stringWidth(checkActorPrice(i) + "");
            g.drawString(checkActorPrice(i) + "", iconX[i] + (int)(Resource.SCREEN_WIDTH*0.092) / 2 - sw / 2, iconY[i] + (int)(Resource.SCREEN_HEIGHT*0.113));

        }
        //走路角色
        if (gameOver == false) {
            for (int i = 0; i < stuffList.size(); i++) {
                for (int j = 0; j < stuffList.get(i).size(); j++) {
                    stuffList.get(i).get(j).paint(g);
                }
            }
        }

        for (int i = 0; i < dieStuff.size(); i++) {
            dieStuff.get(i).paint(g);
        }

        if (drag != -1) {
            g.drawImage(icon, dragX, dragY, dragX + iconSize, dragY + iconSize, 0, drag * 32, 32, (drag + 1) * 32, null);
        }

        //金錢
        int sw = fm.stringWidth(money + "/" + MAX_MONEY);
        g.setColor(lightGray);
        g.fillRect(Resource.SCREEN_WIDTH / 12 * 9, Resource.SCREEN_HEIGHT / 9 - (int)(Resource.SCREEN_HEIGHT*0.089), (int)(Resource.SCREEN_WIDTH*0.242), (int)(Resource.SCREEN_HEIGHT*0.055f));
        g.setFont(this.fontBit);
        g.setColor(Color.white);
        g.drawString(money + "/" + MAX_MONEY, Resource.SCREEN_WIDTH - (int) (sw * 2.2), Resource.SCREEN_HEIGHT / 9 - (int)(Resource.SCREEN_HEIGHT*0.044));

        g.drawImage(this.energyIng, Resource.SCREEN_WIDTH / 12 * 9 - (int)(Resource.SCREEN_WIDTH*0.025), Resource.SCREEN_HEIGHT / 9 - (int)(Resource.SCREEN_HEIGHT*0.1), (int)(Resource.SCREEN_WIDTH*0.066f), (int)(Resource.SCREEN_HEIGHT*0.08f), null);

        //cash
        g.setColor(lightGray);
        g.fillRect(Resource.SCREEN_WIDTH / 12 * 9, Resource.SCREEN_HEIGHT / 9, (int)(Resource.SCREEN_WIDTH*0.242), (int)(Resource.SCREEN_HEIGHT*0.055f));
        g.drawImage(this.coinImg, Resource.SCREEN_WIDTH / 12 * 9 - (int)(Resource.SCREEN_WIDTH*0.025), Resource.SCREEN_HEIGHT / 9 - (int)(Resource.SCREEN_HEIGHT*0.02), Resource.SCREEN_WIDTH / 12 * 9 - (int)(Resource.SCREEN_WIDTH*0.025) + (int)(Resource.SCREEN_HEIGHT*0.09f), Resource.SCREEN_HEIGHT / 9 + (int)(Resource.SCREEN_HEIGHT*0.09f) -  (int)(Resource.SCREEN_HEIGHT*0.02), 0, 0, this.coinImg.getWidth() / 6, this.coinImg.getHeight(), null);
        g.setColor(Color.WHITE);

        sw = fm.stringWidth(cash + "");
        g.drawString(cash + "", Resource.SCREEN_WIDTH - (int) (sw * 3), Resource.SCREEN_HEIGHT / 9 + (int)(Resource.SCREEN_HEIGHT*0.044));

        this.returnBtn.paintBtn(g);

        //WIN畫面
//        g.drawImage(this.winImg, Resource.SCREEN_WIDTH/2-(int)(this.winImg.getWidth()*1.5)/2, Resource.SCREEN_HEIGHT/2-(int)(this.winImg.getHeight()*1.5)/2, (int)(this.winImg.getWidth()*1.5), (int)(this.winImg.getHeight()*1.5), null);
        //lose畫面
//        g.drawImage(this.loseImg, Resource.SCREEN_WIDTH/2-(int)(this.loseImg.getWidth()*1.2)/2, Resource.SCREEN_HEIGHT/2-(int)(this.loseImg.getHeight()*1.2)/2, (int)(this.loseImg.getWidth()*1.2), (int)(this.loseImg.getHeight()*1.2), null);
        for (Coin coin : coins) {
            coin.paint(g);
        }
        this.returnBtn.paintBtn(g);

        g.setColor(Color.white);
        g.setFont(this.fontBit);
        g.fillRect((int) (Resource.SCREEN_WIDTH * 1 / 5f), (int) (Resource.SCREEN_HEIGHT * 1 / 32f - 2), (int) (Resource.SCREEN_WIDTH * 1 / 2f), 14);
        g.fillRect((int) (Resource.SCREEN_WIDTH * 1 / 5f), (int) (Resource.SCREEN_HEIGHT * 3 / 32f - 2), (int) (Resource.SCREEN_WIDTH * 1 / 2f), 14);

        g.setColor(Color.red);
        g.drawString("HP", (int) (Resource.SCREEN_WIDTH * 4 / 30f), (int) (Resource.SCREEN_HEIGHT * 2 / 32f));
        g.fillRect((int) (Resource.SCREEN_WIDTH * 1 / 5f), (int) (Resource.SCREEN_HEIGHT * 1 / 32f), (int) (Resource.SCREEN_WIDTH * 1 / 2f) * hp / 100, 10);

        g.setColor(Color.blue);
        g.drawString("MP", (int) (Resource.SCREEN_WIDTH * 4 / 30f), (int) (Resource.SCREEN_HEIGHT * 4 / 32f));
        g.fillRect((int) (Resource.SCREEN_WIDTH * 1 / 5f), (int) (Resource.SCREEN_HEIGHT * 3 / 32f), (int) (Resource.SCREEN_WIDTH * 1 / 2f) * mp / 100, 10);

        if (gameOver == true) {
            stageBgm.stop();
            g.setColor(lightGray);
            g.fillRect(0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT);
            gameOverBtn.paintBtn(g);

            g.drawImage(this.loseImg, Resource.SCREEN_WIDTH / 2 - (int) (Resource.SCREEN_WIDTH*0.502) / 2, Resource.SCREEN_HEIGHT / 2 - (int) (Resource.SCREEN_HEIGHT * 0.213)/ 2, (int) (Resource.SCREEN_WIDTH*0.502), (int) (Resource.SCREEN_HEIGHT * 0.213), null);
//            g.drawString("GAMEOVER", Resource.SCREEN_WIDTH/5*2, Resource.SCREEN_HEIGHT/2);
            g.setFont(gameFontBit);
            g.setColor(Color.BLACK);
            int sw1 = fm.stringWidth("CONTINUE");
            g.drawString("CONTINUE", gameOverBtn.getX() + gameOverBtn.getWidth() / 2 - sw1 / 2 - (int) (Resource.SCREEN_WIDTH*0.00833), gameOverBtn.getY() + (int) (Resource.SCREEN_HEIGHT * 0.0611));
        }

    }

    @Override
    public void logicEvent() {
        timeCount++;//倍數計時器：FPS為底的時間倍數
        if (gameOver == false) {
            if (timeCount % 2 == 0) {
                eventlistener();
            }
            if (timeCount % 100 == 0) {
                mp--;
            }
            if (timeCount % 2 == 0) {
                if (money < MAX_MONEY) {
                    money += 1;
                }
            }
            if (timeCount % 25 == 0) {
                stuffGen();
            }
        }

        if (timeCount == eventTime) {
            timeCount = 0;
        }
        bombCollision();
        resize();
    }

    public void eventlistener() {
        //我方角 
        //如沒有碰撞就呼叫走路方法
        for (int i = 0; i < stuffList.size() / 2; i++) {
            collisionCheck(stuffList.get(i), stuffList.get(i + 3));
            collisionCheck(stuffList.get(i + 3), stuffList.get(i));
            gameOver(stuffList.get(i + 3));
            for (int j = 0; j < stuffList.get(i).size(); j++) {
                stuffList.get(i).get(j).refreshCd();
                //刷新每隻怪物的cd時間與mp的關係
//                stuffList.get(i).get(j).setCdTime(100*50/this.maxMp);

                stuffList.get(i).get(j).setCdTime(500 - (int) ((double) (9 / 2) * mp));

                System.out.println("cdtime: " + stuffList.get(i).get(j).getCdTime());
            }
            for (int j = 0; j < stuffList.get(i + 3).size(); j++) {
                stuffList.get(i + 3).get(j).refreshCd();
            }
        }
        ghostMethod(dieStuff);
    }

    private void stuffGen() {
//        if(stageCounter == delay.size()){
//            return;
//        }
//        if(genCounter == delay.get(stageCounter)){
//            try {
//                stuffList.get(areaI.get(stageCounter)+3).add(new Stuff(-1, Resource.SCREEN_WIDTH , battleAreaY[areaI.get(stageCounter)] , iconSize , iconSize , type.get(stageCounter)+1 ,"actor"+type.get(stageCounter)));
//                while((delay.get(++stageCounter)) == 0){
//                    stuffList.get(areaI.get(stageCounter)+3).add(new Stuff(-1, Resource.SCREEN_WIDTH , battleAreaY[areaI.get(stageCounter)] , iconSize , iconSize , type.get(stageCounter)+1 ,"actor"+type.get(stageCounter)));
//                }
//                genCounter = 0;
//            } catch (Exception e) {
//            }
//        }
//        genCounter++;

        //隨機產生
        this.genRate += 0.005f;
        try {
            for (int i = 0; i < stuffList.size() / 2; i++) {
                if ((float) (Math.random()) < genRate) {
                    stuffList.get(i + 3).add(new Stuff(-1, Resource.SCREEN_WIDTH, battleAreaY[i], iconSize, iconSize, 3, "actor2"));
                }
            }
        } catch (IOException ex) {
        }

    }

    private void collisionCheck(ArrayList<Stuff> stuff1, ArrayList<Stuff> stuff2) {
        Stuff tmp;
        for (int i = 0; i < stuff1.size(); i++) {
            if (stuff1.get(i).collisionCheck(stuff2) == null) {
                stuff1.get(i).move();
            }
            tmp = stuff1.get(i).collisionCheck(stuff2);
            if (tmp != null && tmp.getHp() > 0) {
                stuff1.get(i).attack(tmp);
            }
            if (tmp != null && tmp.getHp() < 1) {
                dieStuff.add(tmp);
                coins.add(new Coin(tmp.getX0(), tmp.getY0(), tmp.getImgWidth(), tmp.getImgHeight()));
                stuff2.remove(tmp);
            }
        }
    }

    private void bombCollision() {
        for (int i = 0; i < stuffList.size(); i++) {
            for (int j = 0; j < stuffList.get(i).size(); j++) {
                bombContainer = stuffList.get(i).get(j).animation();
                if (bombContainer != null) {
                    for (int k = 0; k < stuffList.get(i + 3).size(); k++) {
                        if (bombContainer.checkAttack(stuffList.get(i + 3).get(k)) == true) {
                            stuffList.get(i + 3).get(k).back(stuffList.get(i).get(j));
                        } else {
                            bombContainer.checkTouchGround();
                        }

                    }
                    bombContainer = null;
                }
            }
        }
    }

    private void gameOver(ArrayList<Stuff> stuff) {
        for (int i = 0; i < stuff.size(); i++) {
            if (stuff.get(i).getX1() < 0) {
                hp -= 50;
                stuff.remove(i);
            }
        }
        if (hp <= 0) {
            hp = mp = 0;
            player.setHp(hp);   //存回player內
            player.setMp(mp);
            this.loseSound.play();
//            gameOverBtn.setLabel("CONTINUE");
            this.gameOver = true;
        }
    }

    private void ghostMethod(ArrayList<Stuff> ghost) {
        for (int i = 0; i < ghost.size(); i++) {
            ghost.get(i).die();
            if (ghost.get(i).getY0() < 0 && ghost.get(i).getType() != 1) {
                hp -= 2;    //如果沒有點擊到惡魔則減少hp
                ghost.remove(i);
            }
        }
    }

    private int checkActorPrice(int i) {
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

    private void assign(MouseEvent e) {
        for (int i = 0; i < stuffList.size() / 2; i++) {
            if (e.getY() > battleAreaY[i] - iconSize && e.getY() < battleAreaY[i] + (iconSize / 2)) {
                try {
                    money -= checkActorPrice(drag);
                    stuffList.get(i).add(new Stuff(1, e.getX() - (iconSize / 2), battleAreaY[i], iconSize, iconSize, drag, "test" + drag));
                } catch (IOException ex) {
                }
            }
        }
    }

    public void resize() {
        
        if (SCREEN_WIDTH != Resource.SCREEN_WIDTH || SCREEN_HEIGHT != Resource.SCREEN_HEIGHT) {
            SCREEN_WIDTH = Resource.SCREEN_WIDTH ;
            SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;
            
            returnBtn.reset(20, 20, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
            gameOverBtn.reset(Resource.SCREEN_WIDTH / 12 * 8, (int) (Resource.SCREEN_HEIGHT / 9 * 6), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);

            fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 23);
            priceFontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 45);
            gameFontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 37);
            
            for (int i = 0; i < 5; i++) {
            iconX[i] = (int) (Resource.SCREEN_WIDTH * (0.1f * i) + Resource.SCREEN_WIDTH * (0.3f));
            iconY[i] = (int) (Resource.SCREEN_HEIGHT * 0.8f);
             }
            iconSize = Resource.SCREEN_WIDTH/12;
            battleAreaY[0] = Resource.SCREEN_WIDTH/2;
            battleAreaY[1] = (int)(Resource.SCREEN_WIDTH*0.375);
            battleAreaY[2] = (int)(Resource.SCREEN_WIDTH*0.25);
            
            for(int i=0; i<stuffList.size(); i++){
            for(int j=0; j<stuffList.get(i).size(); j++){
                stuffList.get(i).get(j).resize();
                }
              }
            for (int i = 0; i < stuffList.size()/2; i++) {
                for(int j=0; j<stuffList.get(i).size(); j++){
                    stuffList.get(i).get(j).setY0(battleAreaY[i]);
                }
                
                for(int j=0; j<stuffList.get(i+3).size(); j++){
                    stuffList.get(i+3).get(j).setY0(battleAreaY[i]);
                }
            }
            
            for(int i=0; i<coins.size(); i++){
                coins.get(i).setHeight(iconSize);
                coins.get(i).setWidth(iconSize);
            }
                
            
        }
    }
}
