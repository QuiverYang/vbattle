/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import vbattle.Button;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.MainPanel.GameStatusChangeListener;


//每個scene都繼承至 Abstract Class Scene
public class MenuScene extends Scene{

    private BufferedImage img;
    private ImgResource rc;
    private Button introBtn;
    private Button loadBtn;
    private Button newGameBtn;
    private boolean clickState;
    private BufferedImage introImg;

     
     
    public MenuScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        
        clickState = false;
        rc = ImgResource.getInstance();
        introImg =  rc.tryGetImage("/resources/BlueSky.png");
        introBtn = new Button("/resources/1ILL.jpg",100,600, 200, 100);
        
        newGameBtn = new Button("/resources/1ILL.jpg",500,600, 200, 100);
             
        loadBtn =  new Button("/resources/1ILL.jpg",900,600, 200, 100);

        

    }

    @Override
    public void paint(Graphics g) {
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

    @Override
    public MouseListener genMouseListener() {
        return new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                //判斷哪一個btn被點擊到
                int ex = e.getX();
                int ey = e.getY();
                if(ex>100&&ex<300&&ey>600&&ey<700){
                    gsChangeListener.changeScene(MainPanel.INTRO_SCENE);
                }
            }
        };
    }

    @Override
    public void logicEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}