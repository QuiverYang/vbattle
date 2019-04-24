package scene;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import vbattle.Actor;
import vbattle.MainPanel;



public class StageScene extends Scene{
    private Actor actor1;
    private Actor actor2;
    private int timeCount = 0; //倍數計時器：初始化
    private int eventTime = 2; // eventListener時間週期：大於0的常數
    private int battleAreaY[] ={500,400,300};

    public StageScene(MainPanel.GameStatusChangeListener gsChangeListener) throws IOException{
        super(gsChangeListener);
        actor1 = new Actor(1, 100, battleAreaY[1] , 128, 128, 0, "actor1");  //int type(1:我方角 or 2:敵人) , int x0, int y0, int imgWidth, int imgHeight, int actorIndex(角色圖片), String txtpath(角色資訊)
        actor2 = new Actor(-1, 800, battleAreaY[1] , 128, 128, 3, "actor2");
    }

    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {
           
        };
    }

    @Override
    public void paint(Graphics g) {
        actor1.paint(g);
        actor2.paint(g);
    }

    @Override
    public void logicEvent() {
        timeCount ++;//倍數計時器：FPS為底的時間倍數
        actor1.refreshCd();
        actor2.refreshCd();
        
        if(timeCount == eventTime){ // 計時器重置：取所有事件的最小公倍數
            timeCount = 0;
        }
        
        if(timeCount%eventTime == 0 ){
            eventlistener();
        }
    }
    
    public void eventlistener(){
        //任一角色沒命就停止
//        System.out.println(actor1.getHp());
        if(actor1.getHp() <= 0 || actor2.getHp() <= 0){
            System.out.println("die");
            return;
        }
        
        //我方角 
        //如沒有碰撞就呼叫走路方法
        if (actor1.collisionCheck(actor2) == false) {
            tmpmovemethod(actor1);
        }
        //如果碰撞到就呼叫攻擊方法
        if (actor1.collisionCheck(actor2)) {
            tmpattckmethod(actor1,actor2);
            
        }
      
        
        //敵方角
        if (actor2.collisionCheck(actor1) == false) {
            tmpmovemethod(actor2);
        }
        if (actor2.collisionCheck(actor1)) {
            tmpattckmethod(actor2,actor1);
        }
    }
    
    public void tmpmovemethod(Actor actor){
        actor.walk();
        actor.move();
    }
    
    public void tmpattckmethod(Actor actor1,Actor actor2){
        actor1.attack(actor2);  
    }

//    private BufferedImage bg;
//    private int amount;
//    private Stuff[] enemy;
//    private ArrayList<Stuff> movingPlayerStuff = new ArrayList<>();
//    private ArrayList<Stuff> movingEnemyStuff = new ArrayList<>();
//    private Stuff drag;
//    //icon屬性
//    private BufferedImage[] icon= new BufferedImage[5];
//    private int[] iconX = new int[5];
//    private int[] iconY = new int[5];
//    private int[] iconX1 = new int[5];
//    private int[] iconY1 = new int[5];
//    private boolean[] iconable = new boolean[5];
//    //icon屬性
//
//
//
//    public StageScene(MainPanel.GameStatusChangeListener gsChangeListener) throws IOException {
//
//        for (int i = 0; i < 5; i++) {
//            try {
//                icon[i] = ImageIO.read(getClass().getResource("/Animal.png"));
//                iconX[i] = (int)(Resource.SCREEN_WIDTH *(0.1f * i)+Resource.SCREEN_WIDTH *(0.4f));
//                iconY[i] = (int)(Resource.SCREEN_HEIGHT * 0.7f);
//                iconX1[i] = iconX[i] +100;
//                iconY1[i] = iconY[i] +100;
//            } catch (IOException ex) {
//                
//            }
//            
//        }
//    }
//    
//
//    @Override
//    public MouseAdapter genMouseAdapter() {
//        return new MouseAdapter() {
//
//            public void isOnIcon(MouseEvent e) {
//                for (int i = 0; i < 5; i++) {
//                    if (e.getX() >= iconX[i]
//                        && e.getX() <= iconX[i] + iconX1[i]
//                        && e.getY() >= iconY[i]
//                        && e.getY() <= iconY[i] + iconY1[i]) 
//                    iconable[i] = true;
//                }
//            }
//            
//            @Override
//            public void mousePressed(MouseEvent e){
//                this.isOnIcon(e);
//                if(e.getButton() == MouseEvent.BUTTON1){
//                    for (int i = 0; i < 5; i++) {
//                        if (iconable[i]){
//                            try {
//                                drag = new Stuff(e.getX()-50,e.getY()-50,100,100,"test");
//                            } catch (IOException ex) {
//                            }
//
//                        }
//                    }
//                }
//            }
//            
//            @Override
//            public void mouseDragged(MouseEvent e){
//                if(drag != null){
//                    drag.setX0(e.getX()-50);
//                    drag.setY0(e.getY()-50);
//                    drag.setX1(drag.getX0()+100);
//                    drag.setY1(drag.getY0()+100);
//                }
//            }
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                for (int i = 0; i < 5; i++) {
//                    iconable[i] = false;
//                }
//                if(drag != null && e.getY() >= 450){
//                    try {
//                        movingPlayerStuff.add(new Stuff(drag.getX0(),drag.getY0(),100,100,"test"));
//                    } catch (IOException ex) {
//                        Logger.getLogger(StageScene.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                    drag = null;
//                
//            }
//        };
//
//
//    }
//
//    @Override
//    public MouseAdapter genMouseAdapter() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//
//    }
//
//    @Override
//    public void paint(Graphics g) {
//
//        for (int i = 0; i < 5; i++) {
//            g.drawImage(icon[i], iconX[i], iconY[i],iconX[i]+100,iconY[i]+100,0,64,32,96,null);
//        }
//        for (int i = 0; i < this.movingPlayerStuff.size(); i++) {
//            this.movingPlayerStuff.get(i).paint(g);
//        }
//        for (int i = 0; i < this.movingEnemyStuff.size(); i++) {
//        }
//        if(drag != null)
//            drag.paint(g);
//    }
//    
//
//        
//    }
//
//
//    @Override
//    public void logicEvent() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
}
