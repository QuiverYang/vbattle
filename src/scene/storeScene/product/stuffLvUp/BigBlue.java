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
public class BigBlue extends StuffLevel{
    public BigBlue(){
        super("/resources/bigBlue.png");
        level = 1;
        this.price = 1500;
        this.name = "樹突細胞";
        this.info = "樹突細胞 level+";
    }
    
}