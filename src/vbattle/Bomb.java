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
public abstract class Bomb {
    protected BufferedImage img,fireAtt;
    protected int rotateAngle,vx,vy,xStartLine,yGround,x,y,ga,boundTimes,toGround,yStartLine;
    protected int xf,yf,seq;//爆炸畫面使用之sub-image 需要用到的屬性
    protected int dist;
    protected AffineTransform af;
    protected boolean finished;
    protected int singleImageWidth,weaponHeight,weaponWidth;
    protected int singleImageHeight;
    
    public boolean isFinished() {
        return finished;
    }
    
    public abstract boolean checkAttack(Stuff em);
    public abstract boolean move();
    public abstract boolean checkTouchGround();
    public abstract void paint(Graphics g);

    public void setyGround(int yGround) {
        this.yGround = yGround;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
