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
public class MainPanel extends javax.swing.JPanel {

    private BufferedImage img;
    private ImgResource rc;
    Button btn;
     private boolean clickState;
     private BufferedImage introImg;

    class MouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getX() >= 100
                    && e.getX() <= 200 && e.getY() >= 100 && e.getY() <= 200) {
                btn.setClickState(true);
                clickState = true;
                System.out.println(btn.getClickState());
                System.out.println("ddd");
            }
        }

    }

    public MainPanel() {
        clickState = false;
        rc = ImgResource.getInstance();
        introImg =  rc.tryGetImage("/resources/BlueSky.png");
        btn = new Button("/resources/1ILL.jpg", 100, 100);
        btn.setX(100);
        btn.setY(100);
        
        this.addMouseListener(new MouseListener());
        this.addMouseMotionListener(new MouseListener());

    }

    @Override
    public void paintComponent(Graphics g) {
        btn.paint(g);
        if(this.clickState){
            System.out.println("ccc");
            g.drawImage(this.introImg, 500, 500, 200, 200, null);
            this.clickState = false;
        }
    }
    
    public boolean getClickState(){
        return this.clickState;
    }

}