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
public class Ghost extends ItemOnScreen{
    int xf,singleImgWidth,counter,change;
    private int rateX, rateY;
    
    private static AudioClip ghostSound;
    
    public static void ghostClicked(URL url){
        
        ghostSound = Applet.newAudioClip(url);
        ghostSound.play();
    }
    
    public Ghost(int x, int y, int width, int height){
        super("/resources/devil_1.png",x,y,width,height);
        singleImgWidth = img.getWidth()/5;
        rateX = x/Resource.SCREEN_WIDTH;
        rateY = y/Resource.SCREEN_HEIGHT;
        ghostSound = Applet.newAudioClip(getClass().getResource("/resources/pop.wav"));
    }
    
    public static boolean isOnGhost(MouseEvent e, Ghost ghost) {
        if (e.getX() >= ghost.getX()
                && e.getX() <= ghost.getX() + ghost.getWidth() && e.getY() >= ghost.getY() && e.getY() <= ghost.getY() + ghost.getHeight()) {
            return true;
        }
        return false;
    }
    
    @Override
    public void paint(Graphics g) {
        if(counter % 3 == 1){
            xf = singleImgWidth * (change++%5);
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
