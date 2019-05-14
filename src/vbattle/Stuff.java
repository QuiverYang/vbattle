package vbattle;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author anny,LC
 */
public class Stuff {

    //腳色屬性
    private String name;
    private int maxHp, maxMp, atk, lv, exp;//物件HP,ATK,LV,EXP
    private String txtpath;//參數txt路徑
    private String imgpath;//圖片路徑...存入參數txt檔
    private BufferedImage img;//角色圖片
    private int hpRate, atkRate, hpBase, atkBase;//基礎參數...存入參數txt檔
    private float speed; //角色移動速度：預設速度1/16f 角色寬
    private int cdTime;//CD時間：單位是FPS倍數週期
    private int attackedTime;//CD時間:被攻擊後的無敵時間
    private int type; // 判斷正反角 (1-->我方角 , -1-->敵方)
    private int range;
    private int sourceWidth;
    private int sourceHeight;
    private int attackType = 1;
    private Bomb bombContainer;
    private boolean immovable = false;
    private BufferedImage ghostImg;
    private BufferedImage devilImg;
    private int price;
    
    //腳色變動屬性
//    private boolean clickState;
    private int hp, mp;

    //系統屬性
    private int x0, y0, imgWidth, imgHeight, x1, y1;//起始座標,圖片尺寸,物件範圍(起始座標+圖片尺寸)
    private ImgResource rc;//共用圖片資源loader
    private float frame;  //第 int frame 偵：紀錄載入圖片X座標
    private int characterNumY0, characterNumY1;//角色編號：紀錄載入圖片Y座標
    private boolean attackCd = true;//CD狀態：初始化true
    private boolean attackedCd = true;
    private int cdCounter = 0;//CD計數器：單位是FPS倍數週期
    private int attackedCdCounter = 0;
    private static AudioClip hit;
    private boolean clickState;

    public static final int ACTOR1_PRICE = 30;
    public static final int ACTOR2_PRICE = 1;
    public static final int ACTOR3_PRICE = 1;
    public static final int ACTOR4_PRICE = 1;
    public static final int ACTOR5_PRICE = 1;

    public Stuff(int type, int x0, int y0, int imgWidth, int imgHeight, int characterNum, int lv, String txtpath) throws IOException {
        //設定建構子參數
        this.type = type;
        this.x0 = x0;
        this.y0 = y0;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.x1 = x0 + imgWidth;
        this.y1 = y0 + imgHeight;
        this.attackedTime = 30;
        this.txtpath = txtpath;
        //設定建構子參數
        //讀取參數txt檔
        rc = ImgResource.getInstance();
        BufferedReader br = new BufferedReader(new FileReader("src/" + txtpath + ".txt"));
        String status[] = br.readLine().split(",");
        img = rc.tryGetImage(status[0]);
        ghostImg = rc.tryGetImage("/resources/ghost.png"); //ghost pic
        devilImg = rc.tryGetImage("/resources/devil_1.png"); //ghost pic
        this.hpRate = Integer.parseInt(status[1]);
        this.atkRate = Integer.parseInt(status[2]);
        this.hpBase = Integer.parseInt(status[3]);
        this.atkBase = Integer.parseInt(status[4]);
        this.range = Integer.parseInt(status[5]);
        this.attackType = Integer.parseInt(status[6]);
        this.cdTime = Integer.parseInt(status[7]);
        this.speed = Float.parseFloat(status[8]);
        this.sourceWidth = Integer.parseInt(status[9]);
        this.sourceHeight = Integer.parseInt(status[10]);
        this.immovable = Boolean.parseBoolean(status[11]);
        this.characterNumY0 = characterNum*sourceHeight;
        this.characterNumY1 = characterNumY0+sourceHeight;
        br.close();
        //讀取參數txt檔
        //初始化腳色
        
        this.lv = lv;
        this.maxHp = this.hp = hpRate * lv + this.hpBase;
        this.atk = atkRate * lv + this.atkBase;
        //初始化腳色

        try {
            hit = Applet.newAudioClip(getClass().getResource("/resources/Damage3.wav"));
        } catch (Exception ex) {
        }
        clickState = false;
    }
    
    //內建GETTER SETTER
    public void setFrame(int value){
        this.frame = value;
    }
    
    public int getX0() {
        return x0;
    }

    public void setX0(int x0) {
        this.x0 = x0;
    }

    public int getY0() {
        return y0;
    }

    public void setY0(int y0) {
        this.y0 = y0;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public int getX1() {
        return x0 + imgWidth;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }
    
    public void setMaxHp(int hp) {
    this.maxHp = hp;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getExp() {
        return exp;
    }

    public int getType() {
        return this.type;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getTxtpath() {
        return txtpath;
    }

    public void setTxtpath(String txtpath) {
        this.txtpath = txtpath;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public int getCdTime() {
        return cdTime;
    }

    public void setCdTime(int cdTime) {
        this.cdTime = cdTime;
    }
    
    public Bomb getBomb(){
        return bombContainer;
    }
    
    public int getSourceHeight(){
        return sourceHeight;
    }
    
    public int getSourceWidth(){
        return sourceWidth;
    }
    
    //GETTER SETTER
    
    public void setCoordinate(int x,int y){
        this.x0 = x - imgWidth/2;
        this.y0 = y - imgHeight/2;
        this.x1 = x0 + imgWidth;
        this.y1 = y0 + imgHeight;
    }

    public void setClickState(boolean a) {
        this.clickState = a;
    }

    public boolean getClickState() {
        return this.clickState;
    }

    //腳色方法
    public void walkFrame() {
        //控制更新範圍：0~3
        frame ++;
        if (frame >= 3) {
            frame = 0;
        }
    }

    public void move() {
        walkFrame();
        //移動座標
        this.x0 = x0 + (int) (imgWidth * speed * type);
        this.x1 = x0 + imgWidth;
    }

    public void attack(Stuff attacked) {
        if(attackCd && attacked.attackedCd){//cd中不進攻擊狀態
            if(frame < 3 || frame > 6){ //進入攻擊狀態：防止重複初始化frame;
                frame = 3;
                
                if(range > 0 ){
                    switch(attackType){
                        case 0:
                            break;
                        case 1:
                            bombContainer = new BombA(this,Math.abs(x0+imgWidth/2-(attacked.x0 + attacked.imgWidth/2)));
                            break;
                        case 2:
                            bombContainer = new BombB(this,Math.abs(x0+imgWidth/2-(attacked.x0 + attacked.imgWidth/2)));
                            break;
                        case 3:
                            bombContainer = new BombC(this,0);
                            break;
                    }
                }
            }

            frame += 1 / 4f;//播放動畫

            if (frame >= 5) {//動畫完成觸發攻擊效果
                if (range < 1) {
                    attacked.attacked(this);
                }else{
                    frame = 0;
                }
                attackCd = false;
            }
        }else{
            walkFrame();
        }
    }
    
    public Bomb animation(){
        if(bombContainer != null){
            bombContainer.move();
            return bombContainer;
        }
        return null;
    }

    public void attacked(Stuff attacker) {
        if(attackedCd){
            hp = hp - attacker.atk;
            frame = 0;
            back();
            hit.play();
            attackedCd = false;
        }
    }
    
    public void back(){
        if(!immovable){
            x0 = x0 - 100 * type;
            this.x1 = x0 + imgWidth;
        }
    }

    public void die() {
        frame += 1 / 2f;

        y0 -= 15;
        y1 -= 15;

        if (frame >= 5) {
            frame = 3;
        }
    }

    public void refreshCd() {
        if (!attackCd) {
            cdCounter++;
            if (cdCounter == cdTime) {
                attackCd = true;
                cdCounter = 0;
            }
        }
        if (!attackedCd) {
            attackedCdCounter++;
            if(attackedCdCounter == attackedTime){
                attackedCd = true;
                attackedCdCounter = 0;
            }
        }
    }

    public Stuff collisionCheck(ArrayList<Stuff> actor) {
        for (int i = 0; i < actor.size(); i++) {
            if (type == 1 && this.x0 < actor.get(i).x1 && this.x1 + (range * type) > actor.get(i).x0) {
                return actor.get(i);
            }
            if(type == -1 && this.x0 + (range*type) <actor.get(i).x1 && this.x1 > actor.get(i).x0){
                return actor.get(i);
            }
        }
        return null;
    }
    
    public Stuff collisionCheck(Stuff tower){
            if (type == 1 && this.x0 < tower.x1 && this.x1 + (range * type) > tower.x0) {
                return tower;
            }
            if(type == -1 && this.x0 + (range*type) <tower.x1 && this.x1 > tower.x0){
                return tower;
            }
        return null;
    }

    public void lvup() {//商店購買等級使用
        this.lv += 1;
    }

    public float getHpPercent() {
        return (float) this.hp / (float) this.maxHp;
    }

    public void paint(Graphics g) {
        //HP顯示
        if (this.hp > 0) {
            g.setColor(Color.red);
            g.fillRect(x0 + (int) (imgWidth * 1 / 4f), this.getY0() - (int) (Resource.SCREEN_HEIGHT * 0.0056), (int) (this.getImgWidth() * this.getHpPercent() * (1 / 2f)), (int) (Resource.SCREEN_HEIGHT * 0.0056));
            //角色顯示
            g.drawImage(img,x0,y0,x1,y1,(int)frame*this.sourceWidth,characterNumY0, ((int)frame+1)*this.sourceWidth,characterNumY1,null);
        }else if(this.hp <= 0){
            if(this.type==1){
                g.drawImage(this.ghostImg, x0, y0 , x0+imgWidth, y1 , (int)frame*32, 0, (((int)frame+1)*32), this.ghostImg.getHeight(), null);
            }else{
                g.drawImage(this.devilImg, x0, y0 , x0+imgWidth, y1 , (int)frame*32, 0, (((int)frame+1)*32), this.devilImg.getHeight(), null);
            }
        }
        
        if(bombContainer != null){
            bombContainer.paint(g);
        }
    }
}
