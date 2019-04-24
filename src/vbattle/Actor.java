package vbattle;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

/**
 *
 * @author anny
 */
public class Actor extends Stuff {

    private float frame;  //第 int frame 偵：紀錄載入圖片X座標
    private int actorIndex;  //角色編號：紀錄載入圖片Y座標
    private float speed = 1/16f; //角色移動速度：測試速度1/16f 角色寬
    private boolean attack = true;//CD狀態：初始化true
    private int cdTime;//CD時間：單位是FPS倍數週期
    private int cdCount = 0;//CD計數器：單位是FPS倍數週期
    private ImgResource rc;

    public Actor(int type, int x0, int y0, int imgWidth, int imgHeight, int actorIndex, String txtpath) throws IOException {
        super(type, x0, y0, imgWidth, imgHeight, txtpath);
        this.actorIndex = actorIndex;
        this.cdTime = 50;

        rc = ImgResource.getInstance();

    }
    public void walk(){
        //控制更新範圍：0~3
            frame ++;
            if (frame == 3) {
                frame = 0;
            }
    }
    public void move() {
            //移動座標
            this.setX0(this.getX0() + (int)(super.getImgWidth()*speed) * super.getType());
    }
    
    public void attack(Actor actor) {

        if(attack){//cd中不進攻擊狀態
            if(frame < 3 || frame > 6){ //進入攻擊狀態：防止重複初始化frame;
                frame = 3;
            }
                frame += 1/4f;//播放動畫
            if(frame >=5){//動畫完成觸發攻擊效果
                actor.back();
                actor.setHp(actor.getHp() - this.getAtk());
                frame = 0;
                attack = false;
            }
        }else{
            walk();
        }
    }
    
        public void back() {
        this.frame = 0;
        this.setX0(this.getX0() -100* super.getType());
    }
    
    public void refreshCd(){
//                System.out.print(attack);
        if(!attack){
            cdCount++;
            if(cdCount ==cdTime){
                attack = true;
                cdCount = 0;
            }
        }
    }
    
    //碰撞判斷
    public boolean collisionCheck(Actor actor) {
        int left1, right1;
        int left2, right2;

        left1 = this.getX0();
        right1 = this.getX0() + super.getImgWidth()*3/4;
        left2 = actor.getX0();
        right2 = actor.getX0() + super.getImgWidth()*3/4;

        if (left1 > right2) {
            return false;
        }
        if (right1 < left2) {
            return false;
        }
        return true;

    }
    @Override
    public void paint(Graphics g) {
        
        if (this.getHp() >= 0) {
            g.setColor(Color.red);
            g.fillRect(this.getX0() + this.getImgWidth() / 2 - ((int) (this.getImgWidth() * this.getHpPercent()) - 10) / 2, this.getY0() - 5, (int) (this.getImgWidth() * this.getHpPercent()) - 10, 5);
        }
            g.drawImage(super.getImg(),getX0(),getY0(),getX0()+getImgWidth(),getY0()+getImgHeight(),(int)frame*32,actorIndex*32, ((int)frame + 1)*32, (actorIndex+1)*32,null);
    }

}

