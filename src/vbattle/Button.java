/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
    private boolean isShown;
    private String label;
    private boolean clickState;
    private boolean isClicked;
    private int intData;
    private Callback callback;
    public interface Callback{
        void doSomthing();
    }
    
    public static boolean isOnBtn(MouseEvent e, Button btn) {
        if (e.getX() >= btn.getX()
                && e.getX() <= btn.getX() + btn.getImgWidth() && e.getY() >= btn.getY() && e.getY() <= btn.getY() + btn.getImgHeight()) {
            return true;
        }
        return false;
    }

    public Button(String iconName, int x, int y, int width, int height) {
        rc = ImgResource.getInstance();
        buttonImg = rc.tryGetImage(iconName);
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        imgState = 0;

        clickState = false;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public int getIntData() {
        return intData;
    }

    public void setIntData(int Data) {
        this.intData = Data;
    }
    
    

    public int getImgWidth() {
        return this.width;
    }
    
    public boolean isIsShown() {
        return isShown;
    }

    public void setIsShown(boolean isShown) {
        this.isShown = isShown;
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
    
    //for img with 2 types
    public void paint(Graphics g) {
        g.drawImage(buttonImg, x, y, x+this.getImgWidth(), y+this.getImgHeight(), buttonImg.getWidth()/2*imgState, 0, buttonImg.getWidth()/2*(imgState+1), buttonImg.getHeight(), null);
//        g.drawOval(x, y, 5, 5);
//        g.drawOval(x+this.getImgWidth(), y+this.getImgHeight(), 5, 5);
        //畫出按鈕label
        if(label !=null){
            Font fontBit = Fontes.getBitFont(buttonImg.getWidth()/40);//40為調整字體的大小參數 越小表示字體越大
            g.setColor(new Color(0,0,0));
            g.setFont(fontBit);
            FontMetrics fm = g.getFontMetrics();
            int sw = fm.stringWidth(label);
            int sa = fm.getAscent();
            g.drawString(label, x+width/2-sw/2-imgState*3+3, y+height/2-sa/2+height/3+imgState*5);
        }
        
    }
    //for img with only one type
    public void paint2(Graphics g) {
      g.drawImage(buttonImg, x, y, this.width, this.height, null);
        
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

    public boolean isIsClicked() {
        return isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    public void action(){
        callback.doSomthing();
    }

}
