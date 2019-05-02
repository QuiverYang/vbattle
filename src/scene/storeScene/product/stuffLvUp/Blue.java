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
public class Blue extends StuffLevel{
    public Blue(){
        super("/resources/tinyCharacters.jpg");
        level = 1;
        this.price = 250;
        this.name = "ＮＫ細胞";
        this.info = "ＮＫ細胞 level+";
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(img, x, y, x+this.width, y+this.height,128,32,161,64, null);
    }
}
