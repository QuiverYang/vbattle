/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.event.*;
import javax.swing.*;
 
public class JFrameResizing extends JFrame implements ComponentListener {
 
    JLabel label;

    JFrameResizing(){
      label = new JLabel();
      getContentPane().add(label);
      getContentPane().addComponentListener(this);
    }

    @Override
    public void componentHidden(ComponentEvent ce) {};
    @Override
    public void componentShown(ComponentEvent ce) {};
    @Override
    public void componentMoved(ComponentEvent ce) { };
   @Override
    public void componentResized(ComponentEvent ce) {
      int width = this.getWidth();
      int height = width*9/12;
      Resource.SCREEN_WIDTH = width;
      Resource.SCREEN_HEIGHT = height;
      this.setSize(width,height); 
      
    }
}
  

