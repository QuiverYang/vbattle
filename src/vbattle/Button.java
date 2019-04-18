/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 *
 * @author anny
 */
public class Button {

    private int height;
    private int width;
    private BufferedImage buttonImg;
    private ImgResource rc;
    private int x;
    private int y;
    private int imgState;

    private boolean clickState;

    public Button(String iconName, int width, int height, int x, int y) {
        rc = ImgResource.getInstance();
        buttonImg = rc.tryGetImage(iconName);
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        imgState = 0;

        clickState = false;
    }

    public int getImgWidth() {
        return this.width;
    }

    public int getImgHeight() {
        return this.height;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void paint(Graphics g) {
      g.drawImage(buttonImg, x, y, x+200, y+100, 2142*imgState, 0, 2142*(imgState+1), 972, null);
        
//        g.drawImage(buttonImg, x, y, this.width, this.height, null);
    }
    
    public void setImgState(int x){
        this.imgState = x;
    }

    public void setClickState(boolean a) {
        this.clickState = a;
    }

    public boolean getClickState() {
        return this.clickState;
    }

}
