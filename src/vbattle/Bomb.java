/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import vbattle.Stuff;

/**
 *
 * @author menglinyang
 */
public abstract class Bomb extends ItemOnScreen{
    protected BufferedImage fireAtt;
    protected int rotateAngle,xStartLine,yGround,ga,boundTimes,toGround,yStartLine;
    protected int xf,yf,seq;//爆炸畫面使用之sub-image 需要用到的屬性
    protected int dist;
    protected AffineTransform af;
    protected boolean finished;
    protected int singleImageWidth,weaponHeight,weaponWidth;
    protected int singleImageHeight;
    protected double vx,vy;
    
    
    public boolean isFinished() {
        return finished;
    }
    
    public abstract boolean checkAttack(Stuff em);
    public abstract boolean move();
    public abstract boolean checkTouchGround();


    public void setyGround(int yGround) {
        this.yGround = yGround;
    }
    
    public void resize(Stuff me, int dist){
        this.screenUnitWidth = Resource.SCREEN_WIDTH/1200;
        this.screenUnitHeight = Resource.SCREEN_HEIGHT/900;
    };
    
}
