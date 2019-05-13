/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.imageio.ImageIO;
import vbattle.Stuff;

/**
 *
 * @author menglinyang
 */
public class BombB extends Bomb{
    //         Stuff myself,  dist 0
    public BombB(Stuff me,int dist){
        try{
            img  = ImageIO.read(getClass().getResource("/needle.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        try {
            fireAtt = ImageIO.read(getClass().getResource("/Ice5.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        singleImageWidth = fireAtt.getWidth()/5;
        singleImageHeight = fireAtt.getHeight()/6;
        weaponWidth = weaponHeight = img.getHeight()/417*80;
        this.x = me.getX1();
        this.y = (me.getY0()+me.getY1())/2-weaponWidth/2;
        
        
    }
    @Override
    public void resize(Stuff me, int dist){
        super.resize(me, dist);
        this.x = me.getX1();
        this.y = (me.getY0()+me.getY1())/2-weaponWidth/2;
    }

    @Override
    public boolean move() {
        //未碰撞
        if(boundTimes == 0){

            x +=(int)(screenUnitWidth*15);
            return false;
        }
        return true;
    }

    @Override
    public boolean checkTouchGround() {
        return false;
    }
    @Override
    public boolean checkAttack(Stuff em){
        int left,right,top,bottom;
        int left2,right2,top2,bottom2;
        left = x;
        right = x;//設定子彈進入身體後爆炸
        top = y;
        bottom = y;
        left2 = em.getX0();
        right2 = em.getX1();
        top2 = em.getY0();
        bottom2 = em.getY1();
        if(bottom < top2) return false;
        if(top > bottom2) return false;
        if(right < left2) return false;
        if(left > right2) return false;
        boundTimes++;
        if(boundTimes > 20){
            finished = true;
            return false;
        }
        return true;
    };

    @Override
    public void paint(Graphics g) {

        //發生碰撞的話
        if(boundTimes >= 1){
            //只給fire這張圖檔使用的（300*300）
            singleImageWidth = img.getWidth()/5;
            singleImageHeight = img.getHeight()/5;
            xf = singleImageWidth*(seq%5);
            yf = singleImageHeight*((seq++/5));
            img = fireAtt;
            //80為圖片大小
            g.drawImage(img,x,y,x+(int)(screenUnitWidth*80),y+(int)(screenUnitWidth*80),xf,yf,xf+singleImageWidth,yf+singleImageWidth,null);
            
        }else{
            g.drawImage(img,x,y,x+(int)(screenUnitWidth*80),y+(int)(screenUnitWidth*80),0,0,417,417,null);
        }
    }
}
