package vbattle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author anny,LC
 */
public class Stuff {
    //腳色屬性
    
    private int maxHp,maxMp,atk,lv,exp;//物件HP,ATK,LV,EXP
    private String txtpath;//參數txt路徑
    private String imgpath;//圖片路徑...存入參數txt檔
    private int hpRate,atkRate,hpBase,atkBase;//基礎參數...存入參數txt檔
    private float speed = 1/16f; //角色移動速度：測試速度1/16f 角色寬
    private int cdTime;//CD時間：單位是FPS倍數週期
    private int type; // 判斷正反角 (1-->我方角 , -1-->敵方)
    
    //腳色變動屬性
    private boolean clickState;
    private int hp,mp;
    
    //系統屬性
    private int x0,y0,imgWidth,imgHeight,x1,y1;//起始座標,圖片尺寸,物件範圍(起始座標+圖片尺寸)
    private ImgResource rc;//共用圖片資源loader
    private BufferedImage img;//角色圖片
    private float frame;  //第 int frame 偵：紀錄載入圖片X座標
    private int characterNumY0,characterNumY1;//角色編號：紀錄載入圖片Y座標
    private boolean attackCd = true;//CD狀態：初始化true
    private int cdCounter = 0;//CD計數器：單位是FPS倍數週期
    
    private BufferedImage ghost;
    private int price;
    
    public static final int ACTOR1_PRICE = 50;
    public static final int ACTOR2_PRICE = 100;
    public static final int ACTOR3_PRICE = 200;
    public static final int ACTOR4_PRICE = 300;
    public static final int ACTOR5_PRICE = 500;
    
    
    public Stuff(int type,int x0,int y0,int imgWidth,int imgHeight, int characterNum,String txtpath) throws IOException{
        //設定建構子參數
        this.type = type;
        this.x0 = x0;
        this.y0 = y0;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.x1 = x0 + imgWidth;
        this.y1 = y0 + imgHeight;
        this.characterNumY0 = characterNum*32;
        this.characterNumY1 = characterNumY0+32;
        this.cdTime = 100;
        this.txtpath = txtpath;
        //設定建構子參數
        //讀取參數txt檔
        rc = ImgResource.getInstance();
        BufferedReader br = new BufferedReader(new FileReader("src/"+txtpath+".txt"));
        String status[] = br.readLine().split(",");
        img = rc.tryGetImage(status[0]);
        ghost = rc.tryGetImage("/resources/ghost.png"); //ghost pic
        this.hpRate = Integer.parseInt(status[1]);
        this.atkRate = Integer.parseInt(status[2]);
        this.hpBase = Integer.parseInt(status[3]);
        this.atkBase = Integer.parseInt(status[4]);
        br.close();
        //讀取參數txt檔
        //初始化腳色
        this.lv = 1;
        this.maxHp = this.hp = hpRate * lv + this.hpBase;
        this.atk = atkRate * lv + this.atkBase;
        //初始化腳色
    }
    public int getPrice(){
        return this.price;
    }
    
    //內建GETTER SETTER
    public int getType(){
        return this.type;
    }
    public void setType(int type){
        this.type = type;
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
    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }
    public int getImgHeight() {
        return imgHeight;
    }
    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }
    public int getX1() {
        return x1;
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
    public int getLv() {
        return lv;
    }
    public void setLv(int lv) {
        this.lv = lv;
    }
    public int getExp() {
        return exp;
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
    public int getHpRate() {
        return hpRate;
    }
    public void setHpRate(int hpRate) {
        this.hpRate = hpRate;
    }
    public int getAtkRate() {
        return atkRate;
    }
    public void setAtkRate(int atkRate) {
        this.atkRate = atkRate;
    }
    public int getHpBase() {
        return hpBase;
    }
    public void setHpBase(int hpBase) {
        this.hpBase = hpBase;
    }
    public int getAtkBase() {
        return atkBase;
    }
    public void setAtkBase(int atkBase) {
        this.atkBase = atkBase;
    }

    public boolean isClickState() {
        return clickState;
    }

    public void setClickState(boolean clickState) {
        this.clickState = clickState;
    }
    //內建GETTER SETTER
    
    public void setCoordinate(int x,int y){
        this.x0 = x - imgWidth/2;
        this.y0 = y - imgHeight/2;
        this.x1 = x0 + imgWidth;
        this.y1 = y0 + imgHeight;
    }
    
    //腳色方法
    public void walkFrame(){
        //控制更新範圍：0~3
        frame ++;
        if (frame == 3) {
            frame = 0;
        }
    }
    public void move() {
        walkFrame();
        //移動座標
        this.x0 = x0 + (int)(imgWidth*speed*type);
        this.x1 = x0 +imgWidth;
    }
    public void attack(Stuff actor) {
        if(attackCd){//cd中不進攻擊狀態
            if(frame < 3 || frame > 6){ //進入攻擊狀態：防止重複初始化frame;
                frame = 3;
            }
            
            frame += 1/4f;//播放動畫
            
            if(frame >=5){//動畫完成觸發攻擊效果
                actor.back();
                actor.hp = actor.hp - this.atk;
                frame = 0;
                attackCd = false;
            }
        }else{
            walkFrame();
        }
    }
    public void back() {
        frame = 0;
        x0 = x0 - 100 * type;
        this.x1 = x0 +imgWidth;
    }
    public void die(){
       frame += 1/2f;
       
       y0 -=15;
       y1 -=15;
       
       if(frame >= 5){
           frame =3;
       }
    }
    public void refreshCd(){
        if(!attackCd){
            cdCounter++;
            if(cdCounter ==cdTime){
                attackCd = true;
                cdCounter = 0;
            }
        }
    }
    public Stuff collisionCheck(ArrayList<Stuff> actor) {
        for (int i = 0; i < actor.size(); i++) {
            if(this.x0 <actor.get(i).x1 && this.x1 > actor.get(i).x0){
                return actor.get(i);
            }
        }
        return null;
    // public boolean collisionCheck(Stuff actor) {
    //     int left1, right1;
    //     int left2, right2;
    //     int top1, top2;
    //     int bottom1, bottom2;
        
    //     left1 = this.x0;
    //     right1 = this.x0 + imgWidth*2/3;
    //     top1 = this.y0;
    //     bottom1 = this.y1;
        
    //     left2 = actor.getX0();
    //     right2 = actor.x0 + imgWidth*2/3;
    //     top2 = actor.y0;
    //     bottom2 = actor.y0+ imgWidth*2/3;
        
    //     if (left1 > right2) {
    //         return false;
    //     }
    //     if (right1 < left2) {
    //         return false;
    //     }
    //     if(top1 > bottom2){
    //         return false;
    //     }
    //     if(bottom1 < top2){
    //         return false;
    //     }
    //     return true;
    }
    //腳色方法
    
    public void lvup(){
        
    }
    public void print(){ //測試
//        System.out.println(this.test+"\n"+
//        this.hpRate+"\n"+
//        this.atkRate+"\n"+
//        this.hpBase+"\n"+
//        this.atkBase);
    }
    public float getHpPercent(){
         return (float)this.hp/(float)this.maxHp;
    }
    
    public void paint(Graphics g){
        //HP顯示
        if (this.hp > 0) {
            g.setColor(Color.red);
            g.fillRect(x0 + (int)(imgWidth*1/4f), this.getY0() - 5, (int) (this.getImgWidth() * this.getHpPercent()*(1/2f)), 5);
            //角色顯示
            g.drawImage(img,x0,y0,x1,y1,(int)frame*32,characterNumY0, ((int)frame+1)*32,characterNumY1,null);
        }else if(this.hp<=0){
            if(this.type==1){
                g.drawImage(this.ghost, x0, y0 , x0+imgWidth, y1 , (int)frame*32, 0, (((int)frame+1)*32), this.ghost.getHeight(), null);
            }else{
                g.drawImage(this.ghost, x0+imgWidth, y0 , x0, y1 , (int)frame*32, 0, (((int)frame+1)*32), this.ghost.getHeight(), null);
            }
        }
    }
}
