/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author menglinyang
 */
public class BombA extends Bomb{
    
    public BombA(int x, int y, int toGroundDist, int dist){
        this.x = this.xStartLine = x;
        this.y = this.yStartLine = y;
        scaleFactor = 0.2;
        this.yGround = y+toGroundDist;//ground
        
        vx = 20;//x方向速度
        
        this.dist = dist;//投擲距離
        
        //設定螢幕範圍內拋物線比較好看到的重力加速度ga
        if(dist < 200){
            ga = 10;
        }else if(dist< 300){
            ga = 8;
        }else if(dist< 400){
            ga = 5;
        }else if(dist< 600){
            ga = 3;
        }else if(dist< 800){
            ga = 2;
        }else{
            ga = 1;
        }
        vy = -ga*dist/2/vx;//y方向速度
        
        try{
            img  = ImageIO.read(getClass().getResource("/capsule.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        try {
            fireAtt = ImageIO.read(getClass().getResource("/Fire3.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        af = AffineTransform.getTranslateInstance(x, y);
    }

    @Override
    public boolean move(){
        
        //未碰撞
        if(boundTimes == 0){
            x =  x + vx;
            rotateAngle+=36;
            y = vy + y;
            vy = vy + ga;
            return false;
        }else{
            vx = 0;
            vy = 0;
            x = xStartLine+dist;
            y = yGround;
        }
        return true;
 
    }
    
    @Override
    public void paint(Graphics g){
        af.setToTranslation(x, y);
        af.scale(scaleFactor, scaleFactor);
        af.rotate(Math.toRadians(rotateAngle),img.getWidth()/2,img.getHeight()/2);
        Graphics2D g2d = (Graphics2D)g;
        
        
        //發生碰撞的話
        if(boundTimes >= 1){
            //只給fire這張圖檔使用的（300*360）
            int singleImageWidth = img.getWidth()/5;
            int singleImageHeight = img.getHeight()/6;
            xf = singleImageWidth*(seq%5);
            yf = singleImageHeight*((seq++/6));
            img = fireAtt;
            g2d.drawImage(img,x,y,x+60,y+60,xf,yf,xf+singleImageWidth,yf+singleImageWidth,null);
            
        }else{
            g2d.drawImage(img, af, null);
        }
    }

    
    
    //判斷碰撞
    @Override
    public boolean checkAttack(Stuff em){
        int left,right,top,bottom;
        int left2,right2,top2,bottom2;
        left = x;
        right = x+(int)(img.getWidth()*scaleFactor);
        top = y;
        bottom = y;//設定要子彈上方碰到敵方上方才爆炸(子彈整個進入敵人身體)
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
            System.out.println("finish");
            finished = true;
            return false;
        }
        return true;
    }
    @Override
    public boolean checkTouchGround(){
        int bottom = y+(int)(img.getHeight()*scaleFactor);
        int bottom2 = this.yGround;
        if(bottom > bottom2){
            boundTimes++;
            if(boundTimes > 20){
                System.out.println("finish");
                finished = true;
            }
            return true;
        }else{
            return false;
        }

    }
}
