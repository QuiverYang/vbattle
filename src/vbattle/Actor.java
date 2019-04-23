/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author anny
 */
public class Actor extends Stuff {

    private int[] imgIndex;  //走路圖片的index
    private BufferedImage img; //角色圖片
    private int actorIndex;  //角色編號for圖片

    private int count;  //紀錄走路圖片的count
    private int index;  //紀錄圖片內的編號

    private boolean cdCheck;  //cd若回復 則為true

    private int[] countTime;  //給cycle計算

    private boolean atkSuccess;  //攻擊成功設為true

    private ImgResource rc;
    private int actionState;  // 1-->走路 2-->攻擊 3-->後退 (紀錄角色目前狀態)

    private boolean dieState;
    private BufferedImage dieImg;
    
    private int ghostY;
    
    public Actor(int type, int x0, int y0, int imgWidth, int imgHeight, int actorIndex, String txtpath) throws IOException {
        super(type, x0, y0, imgWidth, imgHeight, txtpath);
        this.imgIndex = new int[]{0, 1, 2, 1};
        this.atkSuccess  = this.cdCheck = false;
        this.img = this.getImg();
        this.index = this.count = 0;
        this.actorIndex = actorIndex;
        countTime = new int[5];

        rc = ImgResource.getInstance();
        
        dieState=false;
        dieImg = rc.tryGetImage("/resources/ghost.png");
        ghostY=0;
    }
    
    //移動
    @Override
    public void move() {
        if (actionState == 1) {
            if (count == 4) {
                count = 0;
            }
            this.index = imgIndex[count++];

            if (super.getType() == 1) {
                this.setX0(this.getX0() + 5);
            } else if (this.getType() == 2) {
                this.setX0(this.getX0() - 5);
            }
        }
    }
    
    public void die(){
        ghostY +=5;
        if(this.index==5){
            index=0;
        }
        this.index++;
        this.dieState = true;
    }
    
    //控制delay時間
    public boolean cycle(int n, int index) {
        this.countTime[index]++;
        if (this.countTime[index] >= n) {
            this.countTime[index] = 0;
            return true;
        }
        return false;
    }
    
    public void setIndex(int i){
        this.index = i;
    }
    
    public int getIndex(){
        return this.index;
    }
    
    @Override
    public void attack(Stuff stuff) {
        if(index==4){
            stuff.setHp(stuff.getHp() - this.getAtk());
            this.setAtkSucess(true);
            
        }else if(index ==6){
            index = 3;
        }
        
//        while (index < 5) {
//                index++;
//                this.setActionState(2);
//        }
//        if (index >= 5) {
//            index = 3;
//        }
        

    }

    public boolean cdCheck(int n) {
        if (this.cycle(n, 2)) {
            this.cdCheck = true;
        }
        return this.cdCheck;
    }
    
    
    public void setCdCheck(boolean c) {
        this.cdCheck = c;
    }

    public boolean getCdCheck() {
        return this.cdCheck;
    }
    
    
    //設定角色當前狀態
    public void setActionState(int i) {
        if (i >= 1 && i <= 3) {
            this.actionState = i;
        }
    }
    
    //碰撞判斷
    public boolean collisionCheck(Actor actor) {
        int left1, right1;
        int left2, right2;

        left1 = this.getX0();
        right1 = this.getX0() + 64;
        left2 = actor.getX0();
        right2 = actor.getX0() + 64;

        if (left1 > right2) {
            return false;
        }
        if (right1 < left2) {
            return false;
        }
        return true;

    }

    //後退
    public void back() {
        this.index = 0;
        if (this.actionState == 3) {
//        if (this.getHpPercent() <= 0.6 && this.getHpPercent() >=0.5 ||this.getHpPercent() <= 0.3 && this.getHpPercent() >=0.2 || this.getHpPercent()<=0.1) {
            this.backMotion();
        }
//        }

    }
    
    public void backMotion() {
        if (this.getType() == 1) {
            this.setX0(this.getX0() - 60);
        } else if (this.getType() == 2) {
            this.setX0(this.getX0() + 60);
        }
    }

    public int getActionState() {
        return this.actionState;
    }

    @Override
    public void paint(Graphics g) {

        if (this.getHp() >= 0) {
            g.setColor(Color.red);
            g.fillRect(this.getX0() + this.getImgWidth() / 2 - ((int) (this.getImgWidth() * this.getHpPercent()) - 10) / 2, this.getY0() - 5, (int) (this.getImgWidth() * this.getHpPercent()) - 10, 5);
        }
//        System.out.println(this.getType() + " :" + this.getHp());

        if (this.actionState == 1) {
            g.drawImage(img, this.getX0(), this.getY0(), this.getX0() + 64, this.getY0() + 64, this.index * 32, actorIndex * 32, (this.index + 1) * 32, (actorIndex + 1) * 32, null);
        } else if (this.actionState == 2 ) {
            System.out.println("index:" + index);
            g.drawImage(img, this.getX0(), this.getY0(), this.getX0() + 64, this.getY0() + 64, index * 32, actorIndex * 32, ((index) + 1) * 32, (actorIndex + 1) * 32, null);
        } else if(this.actionState ==3){
            g.drawImage(img, this.getX0(), this.getY0(), this.getX0() + 64, this.getY0() + 64, 0 * 32, actorIndex * 32, (0 + 1) * 32, (actorIndex + 1) * 32, null);
        }else if(this.dieState){
            g.drawImage(this.dieImg, this.getX0(), this.getY0()-this.ghostY, this.getX0()+this.dieImg.getWidth()/5, this.getY0()+this.dieImg.getHeight()-this.ghostY, this.index*this.getImgWidth()/5, 0, (this.index+1)*this.dieImg.getWidth()/5, this.dieImg.getHeight(), null);
        }

    }

    public void setAtkSucess(boolean a) {
        this.atkSuccess = a;
    }

    public boolean getAtkSuccess() {
        return this.atkSuccess;
    }

}

