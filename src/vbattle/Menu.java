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
public class Menu extends javax.swing.JPanel {

    private BufferedImage img;
    private ImgResource rc;
    private Button introBtn;
    private Button loadBtn;
    private Button newGameBtn;
     private boolean clickState;
     private BufferedImage introImg;

     
     

    public Menu() {
        clickState = false;
        rc = ImgResource.getInstance();
        introImg =  rc.tryGetImage("/resources/BlueSky.png");
        introBtn = new Button("/resources/1ILL.jpg", 200, 100);
        
//        loadBtn = new Button("/resources/1ILL.jpg", 5, 100, );
        introBtn.setX(100);
        introBtn.setY(600);
        
        newGameBtn = new Button("/resources/1ILL.jpg", 200, 100);
        newGameBtn.setX(500);
        newGameBtn.setY(600);
        
        loadBtn =  new Button("/resources/1ILL.jpg", 200, 100);
        loadBtn.setX(900);
        loadBtn.setY(600);
        
        this.addMouseListener(new MouseListenerDragStuff(introBtn));
        this.addMouseMotionListener(new MouseListenerDragStuff(introBtn));

    }

    @Override
    public void paintComponent(Graphics g) {
        introBtn.paint(g);
        newGameBtn.paint(g);
        loadBtn.paint(g);
        if(this.introBtn.getClickState()){
            System.out.println("ccc");
            g.drawImage(this.introImg, 500, 500, 200, 200, null);
           
        }
    }
    
    public boolean getClickState(){
        return this.clickState;
    }

}