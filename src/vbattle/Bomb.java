/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author menglinyang
 */
public abstract class Bomb {
    protected BufferedImage img,fireAtt;
    protected int rotateAngle,vx,vy,xStartLine,yGround,x,y,ga,boundTimes,toGround,yStartLine;
    protected int xf,yf,seq;//爆炸畫面使用之sub-image 需要用到的屬性
    protected int dist;
    protected double scaleFactor;
    protected AffineTransform af;
    protected boolean finished;
    
    public boolean isFinished() {
        return finished;
    }
    
    abstract boolean checkAttack(Stuff em);
    abstract boolean move();
    abstract boolean checkTouchGround();
    abstract void paint(Graphics g);
}
