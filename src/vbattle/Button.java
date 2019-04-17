/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author anny
 */
public class Button {

    private int height;
    private int width;
    private BufferedImage buttonImg;
    ImgResource rc;
    private int x;
    private int y;

    private boolean clickState;
   
    

    public Button(String iconName, int width, int height) {
        rc = ImgResource.getInstance();
        buttonImg = rc.tryGetImage(iconName);
        this.height = height;
        this.width = width;
        clickState = false;
    }
    
    public int getImgWidth(){
        return this.width;
    }
    
    public int getImgHeight(){
        return this.height;
    }
    
   
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int getX(){
        return this.x;
    }
    
    public int getY(){
        return this.y;
    }

    public void paint(Graphics g) {
        g.drawImage(buttonImg, x, y, this.width, this.height, null);
        
    }


    public void setClickState(boolean a) {
        this.clickState = a;
    }
    
    public boolean getClickState(){
        return this.clickState;
    }
            

}

