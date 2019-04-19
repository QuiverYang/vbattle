package scene;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import vbattle.Button;
import vbattle.MainPanel.GameStatusChangeListener;
import vbattle.Resource;
import vbattle.Stuff;

/**
 *
 * @author USER
 */
public class StageScene extends Scene{
    private BufferedImage bg;
    private int amount;
    private Stuff[] enemy;
    private ArrayList<Stuff> movingPlayerStuff = new ArrayList<>();
    private ArrayList<Stuff> movingEnemyStuff = new ArrayList<>();
    private Stuff drag;
    //icon屬性
    private BufferedImage[] icon= new BufferedImage[5];
    private int[] iconX = new int[5];
    private int[] iconY = new int[5];
    private int[] iconX1 = new int[5];
    private int[] iconY1 = new int[5];
    private boolean[] iconable = new boolean[5];
    //icon屬性

    public StageScene(GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        for (int i = 0; i < 5; i++) {
            try {
                icon[i] = ImageIO.read(getClass().getResource("/Animal.png"));
                iconX[i] = (int)(Resource.SCREEN_WIDTH *(0.1f * i)+Resource.SCREEN_WIDTH *(0.4f));
                iconY[i] = (int)(Resource.SCREEN_HEIGHT * 0.7f);
                iconX1[i] = iconX[i] +100;
                iconY1[i] = iconY[i] +100;
            } catch (IOException ex) {
                
            }
            
        }
            
//                this.player[i] = new Stuff((int) (Resource.SCREEN_WIDTH * (0.1f * i)+200), (int) (Resource.SCREEN_HEIGHT * 0.7f), 100, 100, "test");
    }

    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {

            public void isOnIcon(MouseEvent e) {
                for (int i = 0; i < 5; i++) {
                    if (e.getX() >= iconX[i]
                        && e.getX() <= iconX[i] + iconX1[i]
                        && e.getY() >= iconY[i]
                        && e.getY() <= iconY[i] + iconY1[i]) 
                    iconable[i] = true;
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e){
                this.isOnIcon(e);
                if(e.getButton() == MouseEvent.BUTTON1){
                    for (int i = 0; i < 5; i++) {
                        if (iconable[i]){
                            try {
                                drag = new Stuff(e.getX()-50,e.getY()-50,100,100,"test");
                            } catch (IOException ex) {
                            }

                        }
                    }
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e){
                if(drag != null){
                    drag.setX0(e.getX()-50);
                    drag.setY0(e.getY()-50);
                    drag.setX1(drag.getX0()+100);
                    drag.setY1(drag.getY0()+100);
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                for (int i = 0; i < 5; i++) {
                    iconable[i] = false;
                }
                if(drag != null && e.getY() >= 450){
                    try {
                        movingPlayerStuff.add(new Stuff(drag.getX0(),drag.getY0(),100,100,"test"));
                    } catch (IOException ex) {
                        Logger.getLogger(StageScene.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    drag = null;
                
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < 5; i++) {
            g.drawImage(icon[i], iconX[i], iconY[i],iconX[i]+100,iconY[i]+100,0,64,32,96,null);
        }
        for (int i = 0; i < this.movingPlayerStuff.size(); i++) {
            this.movingPlayerStuff.get(i).paint(g);
        }
        for (int i = 0; i < this.movingEnemyStuff.size(); i++) {
        }
        if(drag != null)
            drag.paint(g);
    }
    
    @Override
    public void logicEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
