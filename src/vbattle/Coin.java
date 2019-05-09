/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 *
 * @author menglinyang
 */
public class Coin extends ItemOnScreen{
    int xf,singleImgWidth,counter,change;
    private int rateX, rateY;
    
    private static AudioClip coinSound;
    
    public static void CoinClicked(URL url){
        
        coinSound = Applet.newAudioClip(url);
        coinSound.play();
    }
    
    public Coin(int x, int y, int width, int height){
        super("/resources/coin.png",x,y,width,height);
        singleImgWidth = img.getWidth()/6;
        rateX = x/Resource.SCREEN_WIDTH;
        rateY = y/Resource.SCREEN_HEIGHT;
        coinSound = Applet.newAudioClip(getClass().getResource("/resources/coin.wav"));
    }
    
    public static boolean isOnCoin(MouseEvent e, Coin coin) {
        if (e.getX() >= coin.getX()
                && e.getX() <= coin.getX() + coin.getWidth() && e.getY() >= coin.getY() && e.getY() <= coin.getY() + coin.getHeight()) {
            return true;
        }
        return false;
    }
    
    @Override
    public void paint(Graphics g) {
        if(counter % 3 == 1){
            xf = singleImgWidth * (change++%6);
        }
        counter++;
        
        g.drawImage(img, x, y, x+this.width, y+this.height,xf,0,xf+singleImgWidth,singleImgWidth, null);
    }
    
    public void setWidth(int width){
        this.width = width;
    }
    
    public void setHeight(int height){
        this.height = height;
    }
    
    
}
