/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Graphics;
import java.io.IOException;
import javax.imageio.ImageIO;
import vbattle.Stuff;

/**
 *
 * @author menglinyang
 */
public class Water extends Bomb{
    
    boolean show;
    
    public Water(){
        try {
            img = ImageIO.read(getClass().getResource("/water.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            fireAtt = ImageIO.read(getClass().getResource("/Special11.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public Water(int x, int y, int yGround){
        this();
        this.x = x;
        this.y = y;
        this.yGround = yGround;
        this.weaponHeight = img.getHeight();
        singleImageWidth = fireAtt.getWidth()/5;
        singleImageHeight = fireAtt.getHeight()/5;
        
    }
    @Override
    public boolean move() {
        y+=10;
        return true;
    }

    @Override
    public boolean checkTouchGround() {
        int bottom = y+weaponHeight;
        int bottom2 = this.yGround;
        if(bottom > bottom2){
            boundTimes++;
            y = yGround-80;
            if(boundTimes > 20){
                finished = true;
            }
            return true;
        }else{
            return false;
        }
    }
    @Override
    public boolean checkAttack(Stuff em){
        int left,right,top,bottom;
        int left2,right2,top2,bottom2;
        left = x;
        right = x+80;
        top = y;
        bottom = y+80;
        left2 = em.getX0();
        right2 = em.getX1();
        top2 = em.getY0();
        bottom2 = em.getY1();
        if(bottom < top2) return false;
        if(top > bottom2) return false;
        if(right < left2) return false;
        if(left > right2) return false;
        y = top2;
        x = (left2+right2)/2-40;
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
            
            xf = singleImageWidth*(seq%5);
            yf = singleImageHeight*((seq++/5));
            img = fireAtt;
            //80為圖片大小
            g.drawImage(fireAtt,x,y,x+80,y+80,xf,yf,xf+singleImageWidth,yf+singleImageHeight,null);
            
        }else{
            g.drawImage(img,x,y,x+80,y+80,0,0,100,100,null);
        }
    }
    
}
