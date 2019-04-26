/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;


/**
 *
 * @author anny
 */
public class Button extends ItemOnScreen{


    private int labelSize;
    private int imgState;
    private String label;
    private boolean clickState;
    private boolean isClicked;
    private int intData;
    private Callback callback;
    AudioClip clickSound;
    Font fontBit;
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
    
    public void changeIcon(String iconName){
        img = rc.tryGetImage(iconName);
    }
    
    public Button(String iconName){
        super(iconName);
        fontBit = Fontes.getBitFont(labelSize/4);//4為調整字體的大小參數 越小表示字體越大
        try{
            clickSound = Applet.newAudioClip(getClass().getResource("/resources/Cursor2.wav"));
        }catch(Exception ex){
            ex.getStackTrace();
        }
    }

    public Button(String iconName, int x, int y, int width, int height) {
        super(iconName,x,y,width,height);
        labelSize = width;
        fontBit = Fontes.getBitFont(labelSize/4);//4為調整字體的大小參數 越小表示字體越大
        imgState = 0;
        clickState = false;
        
        try{
            clickSound = Applet.newAudioClip(getClass().getResource("/resources/Cursor2.wav"));
        }catch(Exception ex){
            ex.getStackTrace();
        }
        
    }
    
    public void setFontBit(int width){
        fontBit =  Fontes.getBitFont(width/4);
    }
    
    public AudioClip getSound(){
        return this.clickSound;
    } 
    

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public int getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(int labelSize) {
        this.labelSize = labelSize;
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

    public int getImgHeight() {
        return this.height;
    }

    //for img with 2 types
    public void paintBtn(Graphics g) {
        
        
        g.drawImage(img, x, y, x+this.getImgWidth(), y+this.getImgHeight(), img.getWidth()/2*imgState, 0, img.getWidth()/2*(imgState+1), img.getHeight(), null);
//        g.drawOval(x, y, 5, 5);
//        g.drawOval(x+this.getImgWidth(), y+this.getImgHeight(), 5, 5);
        //畫出按鈕label
        if(label !=null){
            g.setColor(Color.BLACK);
            g.setFont(fontBit);
            FontMetrics fm = g.getFontMetrics();
            int sw = fm.stringWidth(label);
            int sa = fm.getAscent();
            g.drawString(label, x+width/2-sw/2-imgState*3+3, y+height/2-sa/2+height/3+imgState*5);
        }
        
    }
    
    public void setImgState(int x){
        this.imgState = x;
    }

    public void setClickState(boolean a) {
        if(a==true){
            this.clickSound.play();
        }
        this.clickState = a;
    }

    public boolean getClickState() {
        return this.clickState;
    }

    public boolean isClicked() {
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
    @Override
    public void reset(int x, int y, int width, int height){
        super.reset(x,y,width,height);
        fontBit =  Fontes.getBitFont(width/4);
    }

}
