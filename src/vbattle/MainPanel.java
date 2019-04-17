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
import vbattle.Button.InterfaceBtn;

/**
 *
 * @author anny
 */
public class MainPanel extends javax.swing.JPanel {

    private BufferedImage img;
    private ImgResource rc;
    private Button introBtn;
    private Button loadBtn;
    private Button newGameBtn;
     private boolean clickState;
     private BufferedImage introImg;

//    class MouseListener extends MouseAdapter {
//
//        @Override
//        public void mousePressed(MouseEvent e) {
//            if (e.getButton() == MouseEvent.BUTTON1 && e.getX() >= 100
//                    && e.getX() <= 200 && e.getY() >= 100 && e.getY() <= 200) {
//                btn.setClickState(true);
//                clickState = true;
//                System.out.println(btn.getClickState());
//                System.out.println("ddd");
//            }
//        }
//
//    }
     
     

    public MainPanel() {
        clickState = false;
        rc = ImgResource.getInstance();
        introImg =  rc.tryGetImage("/resources/BlueSky.png");
        introBtn = new Button("/resources/1ILL.jpg", 100, 100, new InterfaceBtn(){
            
            @Override
            public void doSomething(){
                 System.out.println("jijjd");
                 introBtn.setClickState(true);
            }
            
        });
        introBtn.setX(100);
        introBtn.setY(100);
        
        this.addMouseListener(new MouseListenerDragStuff(introBtn));
        this.addMouseMotionListener(new MouseListenerDragStuff(introBtn));

    }

    @Override
    public void paintComponent(Graphics g) {
        introBtn.paint(g);
        if(this.introBtn.getClickState()){
            System.out.println("ccc");
            g.drawImage(this.introImg, 100, 100, 200, 200, null);
           
        }
    }
    
    public boolean getClickState(){
        return this.clickState;
    }

}