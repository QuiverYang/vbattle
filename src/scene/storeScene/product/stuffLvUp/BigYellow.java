/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene.product.stuffLvUp;

import java.awt.Graphics;

/**
 *
 * @author menglinyang
 */
public class BigYellow extends StuffLevel{
    public BigYellow(){
        super("/resources/bigCharacters.jpg");
        level = 1;
        this.price = 1000;
        this.name = "巨噬細胞";
        this.info = "巨噬細胞 level+";
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(img, x, y, x+this.width, y+this.height,256,0,321,64, null);
    }
}
