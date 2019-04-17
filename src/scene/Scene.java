/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import vbattle.MainPanel.GameStatusChangeListener;

/**
 *
 * @author menglinyang
 */
public abstract class Scene {
    GameStatusChangeListener gsChangeListener;
    
    public Scene(GameStatusChangeListener gsChangeListener){
        this.gsChangeListener = gsChangeListener;
    }
    
    abstract public MouseListener genMouseListener();
    abstract public void paint(Graphics g);
    abstract public void logicEvent();
    
}
