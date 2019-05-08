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
 * @author menglinyang
 */
public class ItemOnScreen {
    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected String name;
    protected boolean isShown;
    protected BufferedImage img;
    protected ImgResource rc;
    protected String fileName;
    
    public ItemOnScreen(){
        this.rc = ImgResource.getInstance();
        this.img = rc.tryGetImage(fileName);
        this.isShown = true;
    }
    
    public ItemOnScreen(String fileName){
        this.fileName = fileName;
        this.rc = ImgResource.getInstance();
        this.img = rc.tryGetImage(fileName);
        this.isShown = true;
    }
    
    public ItemOnScreen(String fileName,int x, int y, int width, int height){
        this(fileName);
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    
    
    public void reset(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void paint(Graphics g) {
        
        g.drawImage(img, x, y, x+this.width, y+this.height,0,0,img.getWidth(),img.getHeight(), null);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsShown() {
        return isShown;
    }

    public void setIsShown(boolean isShown) {
        this.isShown = isShown;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

  
}


