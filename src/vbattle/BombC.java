/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import vbattle.Stuff;

/**
 *
 * @author menglinyang
 */
public class BombC extends Bomb{
    ArrayList<Water> water;
    double slope;
    int showTimes,r;
    // Stuff myself, dist 0
    public BombC(Stuff me,int dist){
        try{
            img  = ImageIO.read(getClass().getResource("/tube2.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        water = new ArrayList();
        this.x = me.getX0();
        this.y = me.getY0()-img.getHeight();
        this.yGround=y+(me.getY1()-me.getY0())+img.getHeight();
        slope=0;
        af = AffineTransform.getTranslateInstance(x, y);
        r = (int)(Math.random()*20)+5;
        
    }
    @Override
    public void resize(Stuff me, int dist){
        
    }
    
    @Override
    public boolean move() {
        
        y = y - (int)slope;
        slope += 0.1;
        showTimes++;
        //更換照片角度分30部分取其中一個r值為要到下去的值
        if(showTimes%30==r){
            water.add(new Water(x,y,yGround)); 
            rotateAngle=90;
        }else if(showTimes%30<r&&showTimes%30>=r-5){
            rotateAngle+=18;
            x+=screenUnitWidth*10;
        }else if(showTimes%30>r&&showTimes%30<=r+5){
            rotateAngle-=18;
            x+=screenUnitWidth*10;
        }else{
            rotateAngle=0;
            x+=screenUnitWidth*10;
        }
        for(int i = 0; i < water.size(); i++){
            if(water.get(i).isFinished()){
                water.remove(i);
            }else{
                water.get(i).move();
            }
        }
        //預設超過2000時要remove此bombC
        if(x > 2000){
            this.finished = true;
        }
        return true;
    }

    @Override
    public boolean checkTouchGround() {
        for(Water w : water){
            w.checkTouchGround();
        }
        return false;
    }
    
    @Override
    public boolean checkAttack(Stuff em){
        for(Water w : water){
            return w.checkAttack(em);
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        af.setToTranslation(x, y);
        af.rotate(Math.toRadians(rotateAngle),img.getWidth()/2,img.getHeight()/2);
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(img,af,null);
        for(Water w : water){
            w.paint(g);
        }
    }
    
}
