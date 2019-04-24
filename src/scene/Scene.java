/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import vbattle.MainPanel.GameStatusChangeListener;

/**
 *
 * @author menglinyang
 */
public abstract class Scene {
    //介面
    public GameStatusChangeListener gsChangeListener;
    //建構子裡放一個介面 可以讓介面回傳點擊事件後需要轉場至下一個畫面的資訊
    public Scene(GameStatusChangeListener gsChangeListener){
        this.gsChangeListener = gsChangeListener;
    }
    
    abstract public MouseAdapter genMouseAdapter();
    abstract public void paint(Graphics g);
    abstract public void logicEvent();
    
}
