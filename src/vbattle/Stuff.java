package vbattle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Stuff {
    private int x0,y0,imgWidth,imgHeight,x1,y1;//起始座標,圖片尺寸,物件範圍(起始座標+圖片尺寸)
    private BufferedImage img;//載入圖片
    private int hp,atk,lv,exp,mp;//物件HP,ATK,LV,EXP
    private String txtpath;//參數txt路徑
    private String imgpath;//圖片路徑...存入參數txt檔
    private int hpRate,atkRate,hpBase,atkBase,speed;//基礎參數...存入參數txt檔
    private String test;//測試
    private boolean clickState;
    private int belong;
    
    private int type; // 判斷正反角 (1-->我方角 , 2-->敵方)
    private int maxHp;
    private int cd; //角色cd時間
    
    public Stuff(int type,int x0,int y0,int imgWidth,int imgHeight,String txtpath) throws IOException{
        //設定建構子參數
        this.type = type;
        this.x0 = x0;
        this.y0 = y0;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.x1 = x0 + imgWidth;
        this.y1 = y0 + imgHeight;
        this.txtpath = txtpath;
        //設定建構子參數
        //讀取參數txt檔
        BufferedReader br = new BufferedReader(new FileReader("src/"+txtpath+".txt"));
        String status[] = br.readLine().split(",");
        this.test = status[0];//測試
        this.img = ImageIO.read(getClass().getResource(status[0]));
        this.hpRate = Integer.parseInt(status[1]);
        this.atkRate = Integer.parseInt(status[2]);
        this.hpBase = Integer.parseInt(status[3]);
        this.atkBase = Integer.parseInt(status[4]);
        this.cd = Integer.parseInt(status[5]);
        br.close();
        //讀取參數txt檔
        //初始化腳色
        this.lv = 1;
        this.maxHp = hpRate * lv + this.hpBase;
        this.atk = atkRate * lv + this.atkBase;
        this.hp = this.maxHp;
        //初始化腳色
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
    public String getTest() {
        return test;
    }
    public void setTest(String test) {
        this.test = test;
    }

    public boolean isClickState() {
        return clickState;
    }

    public void setClickState(boolean clickState) {
        this.clickState = clickState;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getBelong() {
        return belong;
    }
    public void setBelong(int belong) {
        this.belong = belong;
    }
    //內建GETTER SETTER
    
    public void setCoordinate(int x,int y){
        this.x0 = x - imgWidth/2;
        this.y0 = y - imgHeight/2;
        this.x1 = x0 + imgWidth;
        this.y1 = y0 + imgHeight;
    }
    
    public float getHpPercent(){
         return (float)this.hp/(float)this.maxHp;
     }
    
    public void lvup(){
        
    }
    public void move(){
        int way = 0;
        switch(this.belong){
            case 0:
                way = 1;
                break;
            case 1:
                way = -1;
                break;
        }
        this.x0 +=  way;
        this.x1 = this.x0 + this.imgWidth;
    }
    public void attack(Stuff stuff){
        
    }
    public void print(){ //測試
        System.out.println(this.test+"\n"+
        this.hpRate+"\n"+
        this.atkRate+"\n"+
        this.hpBase+"\n"+
        this.atkBase);
    }
    
    public void paint(Graphics g){
        g.drawImage(this.img, x0, y0, x1, y1,0,64,32,96, null);
    }
}
