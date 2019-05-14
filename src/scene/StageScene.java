package scene;

import vbattle.Bomb;
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
import javax.swing.JPanel;
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
    private int money = 0;
    private int MAX_MONEY = 200; //

    //物件控制
    private int battleAreaY[] = {Resource.SCREEN_WIDTH/2,  (int)(Resource.SCREEN_WIDTH*0.375),  (int)(Resource.SCREEN_WIDTH*0.25)};//可放置的路 (original:600,450,300)
    private ArrayList<ArrayList<Stuff>> stuffList = new ArrayList<>();
    private ArrayList<Stuff> dieStuff = new ArrayList<>();
    private ArrayList<Coin> coins = new ArrayList();
    private int drag = -1;
    private int dragX, dragY;
    private float genRate = 0.5f;// 怪物產生機率
    private int[] iconX0 = new int[5];
    private int[] iconY0 = new int[5];
    private int[] iconX1 = new int[5];
    private int[] iconY1 = new int[5];
    private int[] iconNum = new int[5];
    private int[] iconX = new int[5];
    private int iconWidth = 110;
    private int iconHeight = 110;
    private int iconBigWidth = 150;
    private int iconBigHeight = 150;
    private boolean[] dragable = new boolean[5];
    private Bomb bombContainer;
    private Stuff towerA,towerB;
    
    //場景元件
    private ImgResource rc = ImgResource.getInstance();
    private BufferedImage iconTiny,iconBig, background;
    private Button gameOverBtn, returnBtn;
    private Font fontBit, priceFontBit, gameFontBit;
    private Color lightGray;

    private BufferedImage winImg;
    private BufferedImage loseImg;
    private BufferedImage energyImg;
    private BufferedImage coinImg;
    private AudioClip clickSound;
    private AudioClip winSound;
    private AudioClip loseSound;
    public static AudioClip stageBgm;
    private boolean win = false;

    private int SCREEN_WIDTH = Resource.SCREEN_WIDTH;
    private int SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;
    
    private float widthRate = 1;
    private float heightRate = 1;
    private Money moneyC = new Money();
    
    private Cash cashC = new Cash();
    
    class Money extends JPanel{
        private int x,y,width,height,sX,sY,iX,iY,iwidth,iheight;
        BufferedImage img;
        
        public Money(){
            img = rc.tryGetImage("/resources/energy.png");
            money = 200;
            MAX_MONEY = 200; //
            x = Resource.SCREEN_WIDTH / 12 * 9;
            y = (int)(Resource.SCREEN_HEIGHT*0.015f);
            width = Resource.SCREEN_WIDTH / 12 * 3;
            height = (int)(Resource.SCREEN_HEIGHT*0.06f);
            sX = Resource.SCREEN_WIDTH - Resource.SCREEN_WIDTH/5;
            sY = Resource.SCREEN_HEIGHT / 16;
            iX = sX - Resource.SCREEN_WIDTH/12;
            iY = sY - Resource.SCREEN_HEIGHT/16;
            iwidth = 90;
            iheight = 90;
        }
        
        public void resize(){
            x = (int)(x * widthRate);
            y = (int)(y * heightRate);
            width = (int)(width * widthRate);
            height = (int)(height * heightRate);
            sX = (int)(sX * widthRate);
            sY = (int)(sY * heightRate);
            iX = (int)(iX * widthRate);
            iY = (int)(iY * heightRate);
            iwidth = (int)(iwidth * widthRate);
            iheight = (int)(iheight * heightRate);
        }
        
        @Override
        public void paint(Graphics g){
            g.setColor(lightGray);
            g.fillRect(x,y,width,height);
            g.setFont(fontBit);
            g.setColor(Color.white);
            g.drawString(money + "/" + MAX_MONEY, sX, sY);
            g.drawImage(img,iX ,iY , iwidth, iheight, null);
        }
    }
    
    class Cash extends JPanel{
        private int x,y,width,height,sX,sY,iX,iY,iwidth,iheight,iX1,iY1;
        BufferedImage img;
        
        public Cash(){
            img = rc.tryGetImage("/resources/energy.png");
            x = moneyC.x;
            y = moneyC.y + 80;
            width = moneyC.width;
            height = moneyC.height;
            sX = Resource.SCREEN_WIDTH - Resource.SCREEN_WIDTH/5;
            sY = Resource.SCREEN_HEIGHT / 16 + 80;
            iX = moneyC.iX;
            iY = moneyC.iY + 80;
            iwidth = 90;
            iheight = 90;
            iX1 = iX + iwidth;
            iY1 = iY + iheight;
        }
        
        public void resize(){
            x = (int)(x * widthRate);
            y = (int)(y * heightRate);
            width = (int)(width * widthRate);
            height = (int)(height * heightRate);
            sX = (int)(sX * widthRate);
            sY = (int)(sY * heightRate);
            iX = (int)(iX * widthRate);
            iY = (int)(iY * heightRate);
            iX1 = (int)(iX1 * widthRate);
            iY1 = (int)(iY1 * heightRate);
            iwidth = (int)(iwidth * widthRate);
            iheight = (int)(iheight * heightRate);
        }
        
        @Override
        public void paint(Graphics g){
            g.setColor(lightGray);
            g.fillRect(x,y,width,height);
            g.setFont(fontBit);
            g.drawImage(coinImg,iX ,iY,iX1,iY1,0, 0, 60,60,null);
            g.setColor(Color.white);
            g.drawString(cash + "", sX, sY);
        }
    }

    public StageScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        init();
    }
    
    private void init(){
        for (int i = 0; i < 6; i++) {
            stuffList.add(new ArrayList<Stuff>());
        }
        for (int i = 0; i < 5; i++) {
            iconX0[i] = (int) (Resource.SCREEN_WIDTH * (0.1f * i) + Resource.SCREEN_WIDTH * (0.3f));
            iconY0[i] = (int) (Resource.SCREEN_HEIGHT * 0.8f);
            iconX1[i] = iconX0[i] + (int)(Resource.SCREEN_WIDTH*0.075);
            iconY1[i] = iconY0[i] + (int)(Resource.SCREEN_HEIGHT*0.09);
            iconNum[i] = i;
            iconX[i] = iconX0[i] + (int) (Resource.SCREEN_WIDTH * 0.01f);
        }
        iconTiny = rc.tryGetImage("/resources/tinyCharacters.png");
        iconBig = rc.tryGetImage("/resources/bigCharacters.png");
        winImg = rc.tryGetImage("/resources/win.png");
        loseImg = rc.tryGetImage("/resources/lose.png");
        energyImg = rc.tryGetImage("/resources/energy.png");
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
        maxHp = player.getHpMax();
        maxMp = player.getMpMax();
        hp = player.getHp();
        mp = player.getMp();
        background = rc.tryGetImage("/resources/background"+(player.getStage()+1)+".png");
        try {
            br = new BufferedReader(new FileReader("src/stage"+player.getStage()+".txt"));
            while(br.ready()){
                String str[] = br.readLine().split(",");
                delay.add(Integer.parseInt(str[0]));
                type.add(Integer.parseInt(str[1]));
                areaI.add(Integer.parseInt(str[2]));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            towerA = new Stuff(1,0,(int)(Resource.SCREEN_WIDTH*0.25),57*5,72*5,0,1,"towerA"+player.getStage());
            towerA.setHp(hp);
            towerA.setMaxHp(maxHp);
            towerB = new Stuff(-1,(int)(Resource.SCREEN_WIDTH*0.75),(int)(Resource.SCREEN_WIDTH*0.25),57*5,72*5,0,1,"towerB"+player.getStage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    //玩家控制
    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {

            public void isOnIcon(MouseEvent e) {
                for (int i = 0; i < 5; i++) {
                    if (e.getX() >= iconX0[i]
                            && e.getX() <= iconX0[i] + iconWidth
                            && e.getY() >= iconY0[i]
                            && e.getY() <= iconY0[i] + iconWidth
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
                        && e.getY() <= dieStuff.getY0() + dieStuff.getImgHeight() && dieStuff.getType()!=1) {
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
                            dragX = e.getX() - (iconWidth / 2);
                            dragY = e.getY() - (iconHeight / 2);
                        }
                    }
                }

                if ((gameOver || win) && Button.isOnBtn(e, gameOverBtn)) {
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
                        clickSound.play();
                        dieStuff.remove(i);
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
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (drag != -1) {
                    dragX = e.getX() - (iconWidth / 2);
                    dragY = e.getY() - (iconHeight / 2);
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

                if (gameOver) {
                    gameOverBtn.setImgState(0);
                    gameOverBtn.setIsClicked(false);
                }

                if (win && Button.isOnBtn(e, gameOverBtn) && gameOverBtn.getClickState()) {
                    gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                    
                    MainPanel.backgroundSound.loop();
                    for (int i = 0; i < player.getFp().size(); i++) {
                        player.getFp().get(i).changeValue();
                    }
                    
                    if(player.getStage()+1 < 5){
                        player.setStage(player.getStage()+1);
                    }
                    try {
                        player.setHp(towerA.getHp());   //存回player內
                        if(mp<0){
                            mp = 0;
                        }
                        player.setMp(mp);
                        player.save();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                
                if (gameOver&& Button.isOnBtn(e, gameOverBtn) && gameOverBtn.getClickState()) {
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
        g.drawImage(this.background, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT, 0, 0, this.background.getWidth(), this.background.getHeight(),null);
        
        g.setFont(this.priceFontBit);
        FontMetrics fm = g.getFontMetrics();
        
        if(drag!=-1){
            paintShadow(g);
        }
        
        g.setColor(Color.WHITE);
        paintIcon(g);
        paintStuff(g);
        towerA.paint(g);
        towerB.paint(g);        
        paintDrag(g);
        moneyC.paint(g);
        cashC.paint(g);
        paintStage(g);

        this.returnBtn.paintBtn(g);

        for (Coin coin : coins) {
            coin.paint(g);
        }
        paintHpMp(g);
        if (gameOver) {
            paintGameOver(g);
        }
        if (win){
            paintWin(g);
        }
    }
    
    public void paintStage(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(fontBit);
        String stage = "STAGE:"+this.player.getStage()+"";
        g.drawString(stage, (int)(Resource.SCREEN_WIDTH/17), (int)(Resource.SCREEN_HEIGHT/9*7.9));
    }
    
    private void paintShadow(Graphics g){
        g.setColor(lightGray);
        if(dragY < battleAreaY[2] && dragX+(iconWidth) < Resource.SCREEN_WIDTH/2){
            g.fillRect(dragX, battleAreaY[2]+(iconHeight / 2), iconWidth, iconHeight);
        }else if(dragY < battleAreaY[1]&& dragX+(iconWidth) < Resource.SCREEN_WIDTH/2){
            g.fillRect(dragX, battleAreaY[1]+(iconHeight / 2), iconWidth, iconHeight);
        }else if(dragY < battleAreaY[0]&& dragX+(iconWidth) < Resource.SCREEN_WIDTH/2){
            g.fillRect(dragX, battleAreaY[0]+(iconHeight / 2), iconWidth, iconHeight);
        }
    }
    
    private void paintIcon(Graphics g) {
        g.setColor(Color.white);
        int[] unlock = player.getUnlock();
        for (int i = 0; i < 3; i++) {
            g.fillRect(iconX0[i], iconY0[i], iconWidth, iconHeight);
            g.drawImage(iconTiny, iconX[i],iconY0[i], iconX1[i], iconY1[i], 0, iconNum[i] * 32, 32, (iconNum[i] + 1) * 32, null);
        }
        for(int i = 0; i < 2; i++){
            g.fillRect(iconX0[i+3], iconY0[i+3], iconWidth, iconHeight);
            g.drawImage(iconBig, iconX[i+3] , iconY0[i+3], iconX1[i+3], iconY1[i+3], 0, iconNum[i] * 64, 64, (iconNum[i] + 1) * 64, null);
       
        }
        FontMetrics fm = g.getFontMetrics();
        int fh = fm.getHeight();
        for(int i=0; i<5; i++){
            if (this.money < checkActorPrice(i) || unlock[i]==0) {
                g.setColor(lightGray);
                g.fillRect(iconX0[i], iconY0[i], iconWidth, iconHeight);
            }
            //價格
            g.setColor(Color.BLACK);
            g.drawString(checkActorPrice(i) + "", (iconX0[i]+iconX1[i])/2, iconY1[i] + fh);
        }
    }
    
    private void paintStuff(Graphics g){
        if (gameOver == false || win == false)  {
            for (int i = 0; i < stuffList.size(); i++) {
                for (int j = 0; j < stuffList.get(i).size(); j++) {
                    stuffList.get(i).get(j).paint(g);
                    if(stuffList.get(i).get(j).getBomb() != null){
                        stuffList.get(i).get(j).getBomb().paint(g);
                    }
                }
            }
        }

        for (int i = 0; i < dieStuff.size(); i++) {
            dieStuff.get(i).paint(g);
        }
    }
    
    private void paintDrag(Graphics g){
        if (drag != -1) {
            if(drag <3){
                g.drawImage(iconTiny, dragX, dragY, dragX + iconWidth, dragY + iconHeight, 0, drag * 32, 32, (drag + 1) * 32, null);
            }else {
                g.drawImage(iconBig, dragX, dragY, dragX + iconBigWidth, dragY + iconBigHeight, 0, (drag-3) * 64, 64, (drag-3 + 1) * 64, null);
            }
        }
    }

    private void paintHpMp(Graphics g){
        g.setColor(Color.white);
        g.setFont(this.fontBit);
        g.fillRect((int) (Resource.SCREEN_WIDTH * 1 / 5f), (int) (Resource.SCREEN_HEIGHT * 1 / 32f - 2), (int) (Resource.SCREEN_WIDTH * 1 / 2f), 14);
        g.fillRect((int) (Resource.SCREEN_WIDTH * 1 / 5f), (int) (Resource.SCREEN_HEIGHT * 3 / 32f - 2), (int) (Resource.SCREEN_WIDTH * 1 / 2f), 14);
        
        g.setColor(Color.red);
        g.drawString("HP", (int) (Resource.SCREEN_WIDTH * 4 / 30f), (int) (Resource.SCREEN_HEIGHT * 2 / 32f));
        g.fillRect((int) (Resource.SCREEN_WIDTH * 1 / 5f), (int) (Resource.SCREEN_HEIGHT * 1 / 32f),
                (int) (Resource.SCREEN_WIDTH * 1 / 2f) * towerA.getHp() / towerA.getMaxHp(), 10);
        System.out.println("towerA.getHp() "+towerA.getHp());
        System.out.println("towerA.getMaxHp() "+towerA.getMaxHp());

        g.setColor(Color.blue);
        g.drawString("MP", (int) (Resource.SCREEN_WIDTH * 4 / 30f), (int) (Resource.SCREEN_HEIGHT * 4 / 32f));
        g.fillRect((int) (Resource.SCREEN_WIDTH * 1 / 5f), (int) (Resource.SCREEN_HEIGHT * 3 / 32f), (int) (Resource.SCREEN_WIDTH * 1 / 2f) * mp / player.getMpMax(), 10);
    }
    
    private void paintGameOver(Graphics g){
        stageBgm.stop();
        g.setColor(lightGray);
        g.fillRect(0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT);
        gameOverBtn.paintBtn(g);
        g.drawImage(this.loseImg, Resource.SCREEN_WIDTH / 2 - (int) (Resource.SCREEN_WIDTH*0.502) / 2, Resource.SCREEN_HEIGHT / 2 - (int) (Resource.SCREEN_HEIGHT * 0.213)/ 2, (int) (Resource.SCREEN_WIDTH*0.502), (int) (Resource.SCREEN_HEIGHT * 0.213), null);
        g.setFont(gameFontBit);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Color.BLACK);
        int sw = fm.stringWidth("CONTINUE");
        g.drawString("CONTINUE", gameOverBtn.getX() + gameOverBtn.getWidth() / 2 - sw / 2 , gameOverBtn.getY() + (int) (Resource.SCREEN_HEIGHT * 0.0611));
    }
    
    private void paintWin(Graphics g){
        stageBgm.stop();
        g.setColor(lightGray);
        g.fillRect(0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT);
        gameOverBtn.paintBtn(g);
        g.drawImage(this.winImg, Resource.SCREEN_WIDTH / 2 - (int) (Resource.SCREEN_WIDTH*0.502) / 2, Resource.SCREEN_HEIGHT / 2 - (int) (Resource.SCREEN_HEIGHT * 0.213)/ 2, (int) (Resource.SCREEN_WIDTH*0.502), (int) (Resource.SCREEN_HEIGHT * 0.213), null);
        g.setFont(gameFontBit);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(Color.BLACK);
        int sw1 = fm.stringWidth("CONTINUE");
        g.drawString("CONTINUE", gameOverBtn.getX() + gameOverBtn.getWidth() / 2 - sw1 / 2 , gameOverBtn.getY() + (int) (Resource.SCREEN_HEIGHT * 0.0611));
    }
    
    @Override
    public void logicEvent() {
        if(!win && !gameOver){
            timeCount++;//倍數計時器：FPS為底的時間倍數
            if (gameOver  == false || win == false) {
                if (timeCount % 2 == 0) {
                    eventlistener();
                    resize();
                }
                if (timeCount % 100 == 0) {
                    if(mp>0){
                        mp--;
                    }
                    stuffRandom();
                }
                if (timeCount % 2 == 0) {
                    if (money < MAX_MONEY) {
                        money += 1;
                    }
                }
            }

            if (timeCount == eventTime) {
                timeCount = 0;
            }
            bombCollision();
        }
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
                stuffList.get(i).get(j).setCdTime((int)(stuffList.get(i).get(j).getCdTime()
                + stuffList.get(i).get(j).getCdTime() * (float)maxMp / (float)mp / 50f));
                
                
                
                System.out.print("cd"+(int)(stuffList.get(i).get(j).getCdTime()
                + stuffList.get(i).get(j).getCdTime() * (float)maxMp / (float)mp / 50f));
                
            }
            for (int j = 0; j < stuffList.get(i + 3).size(); j++) {
                stuffList.get(i + 3).get(j).refreshCd();
            }
        }
        
        towerA.refreshCd();
        towerB.refreshCd();
        dieMethod();
        ghostMethod(dieStuff);
        if(towerA.getHp() <= 0){
            gameOver = true;
        }
        
        if(towerB.getHp() <= 0){
            win = true;
            winSound.play();
        }
    }
   
    private void stuffRandom(){
        if(player.getStage() < 4){
            if(stageCounter == delay.size()){
                return;
            }
            if(genCounter == delay.get(stageCounter)){
                if(type.get(stageCounter)< 2){
                    try {
                        stuffList.get(areaI.get(stageCounter)+3).add(new Stuff(-1, Resource.SCREEN_WIDTH , battleAreaY[areaI.get(stageCounter)] , iconWidth , iconHeight , type.get(stageCounter)+3 ,1,"actor"+type.get(stageCounter)));
                        while((delay.get(++stageCounter)) == 0){
                            stuffList.get(areaI.get(stageCounter)+3).add(new Stuff(-1, Resource.SCREEN_WIDTH , battleAreaY[areaI.get(stageCounter)] , iconWidth , iconHeight , type.get(stageCounter)+3 ,1,"actor"+type.get(stageCounter)));
                        }
                        genCounter = 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                
                if(type.get(stageCounter) >= 2){
                     try {
                        stuffList.get(areaI.get(stageCounter)+3).add(new Stuff(-1, Resource.SCREEN_WIDTH , battleAreaY[areaI.get(stageCounter)] , iconBigWidth , iconBigHeight , type.get(stageCounter) ,1,"actor"+type.get(stageCounter)));
                        while((delay.get(++stageCounter)) == 0){
                            stuffList.get(areaI.get(stageCounter)+3).add(new Stuff(-1, Resource.SCREEN_WIDTH , battleAreaY[areaI.get(stageCounter)] , iconBigWidth , iconBigHeight , type.get(stageCounter) ,1,"actor"+type.get(stageCounter)));
                        }
                        genCounter = 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            genCounter++;
        }else{
            this.genRate += 0.005f;
            int ran = (int)(Math.random()*5);
            if(ran < 2){
                try {
                    for (int i = 0; i < stuffList.size()/2; i++) {
                        if((float)(Math.random()) < genRate){
                            stuffList.get(i+3).add(new Stuff(-1, Resource.SCREEN_WIDTH, battleAreaY[i] , iconWidth, iconHeight,ran+3,1, "actor"+ran));
                        }
                    }
                } catch (IOException ex) {
                }
            }
            if(ran >= 2){
                try {
                    for (int i = 0; i < stuffList.size()/2; i++) {
                        if((float)(Math.random()) < genRate){
                            stuffList.get(i+3).add(new Stuff(-1, Resource.SCREEN_WIDTH, battleAreaY[i] , iconBigWidth, iconBigHeight, ran,1, "actor"+ran));
                        }
                    }
                } catch (IOException ex) {
                }
            }
        }
    }
    
    private void collisionCheck(ArrayList<Stuff> stuff1, ArrayList<Stuff> stuff2) {
        Stuff tmp;
        for (int i = 0; i < stuff1.size(); i++) {
            tmp = stuff1.get(i).collisionCheck(stuff2);
            if (tmp == null) {
                switch(stuff1.get(i).getType()){
                    case 1:
                        tmp = stuff1.get(i).collisionCheck(towerB);
                        if(tmp != null && tmp.getHp() > 0 ) {
                            stuff1.get(i).attack(tmp);
                        }else{
                            stuff1.get(i).move();
                        }
                    break;
                        
                    case -1:
                        tmp = stuff1.get(i).collisionCheck(towerA);
                        if(tmp != null && tmp.getHp() > 0 ) {
                            stuff1.get(i).attack(tmp);
                        }else{
                            stuff1.get(i).move();
                        }
                    break;
                }
            }
            if (tmp != null && tmp.getHp() > 0) {
                stuff1.get(i).attack(tmp);
            }
            if (tmp != null && tmp.getHp() <= 0) {
                dieStuff.add(tmp);
                coins.add(new Coin(tmp.getX0(), tmp.getY0(), iconWidth, iconHeight));
                stuff2.remove(tmp);
            }
        }
    }
    
    private void bombCollision(){
        for (int i = 0; i < stuffList.size()/2; i++) {
            for (int j = 0; j < stuffList.get(i).size(); j++) {
                bombContainer = stuffList.get(i).get(j).animation();
                if (bombContainer != null) {
                    if(bombContainer.checkAttack(towerB) == true){
                        towerB.attacked(stuffList.get(i).get(j));
                    }else {
                        bombContainer.checkTouchGround();
                    }
                    for (int k = 0; k < stuffList.get(i + 3).size(); k++) {
                        if (bombContainer.checkAttack(stuffList.get(i + 3).get(k)) == true) {
                            stuffList.get(i + 3).get(k).attacked(stuffList.get(i).get(j));
                        }
                        else {
                            bombContainer.checkTouchGround();
                        }
                    }
                    if(bombContainer.checkTouchGround()){
                        bombContainer = null;
                    }
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
        if (towerA.getHp() <= 0) {
            player.setHp(0);   //存回player內
            player.setMp(0);
            this.loseSound.play();
            this.gameOver = true;
        }
    }
    
    private void dieMethod(){
        for (int i = 0; i < stuffList.size(); i++) {
            for (int j = 0; j < stuffList.get(i).size(); j++) {
                Stuff tmp = stuffList.get(i).get(j);
                if(tmp.getHp() <= 0){
                    dieStuff.add(tmp);
                    stuffList.get(i).remove(tmp);
                }
            }
        }
    }

    private void ghostMethod(ArrayList<Stuff> ghost) {
        for (int i = 0; i < ghost.size(); i++) {
            ghost.get(i).die();
            if (ghost.get(i).getY0() < 0 && ghost.get(i).getType() != 1) {
                towerA.setHp(towerA.getHp()-2);    //如果沒有點擊到惡魔則減少hp
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
            int[] unlock = player.getUnlock();
            if (e.getY() > battleAreaY[i] - iconHeight && e.getY() < battleAreaY[i] + (iconHeight / 2)&& e.getX() < Resource.SCREEN_WIDTH/2 &&unlock[drag] != 0) {
                try {
                    money -= checkActorPrice(drag);
                    
                    if(drag <3)
                         stuffList.get(i).add(new Stuff(1, e.getX() - (iconWidth / 2), battleAreaY[i], iconWidth, iconHeight, drag,unlock[drag], "test" + drag));
                    if(drag >=3)
                        stuffList.get(i).add(new Stuff(1, e.getX() - (iconWidth / 2), battleAreaY[i], iconBigWidth, iconBigHeight, drag-3, unlock[drag],"test" + drag));
                } catch (IOException ex) {
                }
            }
        }
    }

    public void resize() {
        if (SCREEN_WIDTH != Resource.SCREEN_WIDTH || SCREEN_HEIGHT != Resource.SCREEN_HEIGHT) {
            widthRate =  (float)Resource.SCREEN_WIDTH /(float)SCREEN_WIDTH ;
            heightRate = (float)Resource.SCREEN_HEIGHT/(float)SCREEN_HEIGHT;
            SCREEN_WIDTH = Resource.SCREEN_WIDTH ;
            SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;
            
            
            //按鈕
            returnBtn.reset(20, 20, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
            gameOverBtn.reset(Resource.SCREEN_WIDTH / 12 * 8, (int) (Resource.SCREEN_HEIGHT / 9 * 6), Resource.SCREEN_WIDTH / 12 * 2, Resource.SCREEN_WIDTH / 12);
            //字體
            fontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 23);
            priceFontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 45);
            gameFontBit = Fontes.getBitFont(Resource.SCREEN_WIDTH / 37);
            //下方角色欄
            for (int i = 0; i < 5; i++) {
                iconX0[i] *= widthRate;
                iconY0[i] *= heightRate;
                iconX1[i]  *= widthRate;
                iconY1[i] *= heightRate;
                iconX[i] *= widthRate;
             }
            iconWidth *= widthRate;
            iconHeight *= heightRate;
            iconBigWidth *= widthRate;
            iconBigHeight *= heightRate;
            //路線y軸
            battleAreaY[0] = Resource.SCREEN_WIDTH/2;
            battleAreaY[1] = (int)(Resource.SCREEN_WIDTH*0.375);
            battleAreaY[2] = (int)(Resource.SCREEN_WIDTH*0.25);
            //全部角色大小
            for(int i=0; i<stuffList.size(); i++){
                for(int j=0; j<stuffList.get(i).size(); j++){
                    stuffList.get(i).get(j).setX0((int)(stuffList.get(i).get(j).getX0()*widthRate));
                    stuffList.get(i).get(j).setX1((int)(stuffList.get(i).get(j).getX1()*widthRate));
                    stuffList.get(i).get(j).setY0((int)(stuffList.get(i).get(j).getY0()*heightRate));
                    stuffList.get(i).get(j).setY1((int)(stuffList.get(i).get(j).getY1()*heightRate));
                    stuffList.get(i).get(j).range(widthRate);
                }
            }
            //塔x軸
            towerA.setX0((int)(towerA.getX0()*widthRate));
            towerB.setX0((int)(towerB.getX0()*widthRate));
            towerA.setX1((int)(towerA.getX1()*widthRate));
            towerB.setX1((int)(towerB.getX1()*widthRate));
            //塔y軸
            towerA.setY0((int)(towerA.getY0()*heightRate));
            towerB.setY0((int)(towerB.getY0()*heightRate));
            towerA.setY1((int)(towerA.getY1()*heightRate));
            towerB.setY1((int)(towerB.getY1()*heightRate));
            //coin大小
            for(int i=0; i<coins.size(); i++){
                coins.get(i).setHeight((int)(coins.get(i).getHeight()*heightRate));
                coins.get(i).setWidth((int)(coins.get(i).getWidth()*widthRate));

            }
            moneyC.resize();
            cashC.resize();
            
            //bomb大小
            for (int i = 0; i < stuffList.size(); i++) {
                for (int j = 0; j < stuffList.get(i).size(); j++) {
                    if(stuffList.get(i).get(j).getBomb() != null){
                        stuffList.get(i).get(j).getBomb().resize(stuffList.get(i).get(j), 100);
                    }
                }
            }
            
        }
    }


}
