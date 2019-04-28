/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene.product.hpmp;

import scene.storeScene.product.Product;

/**
 *
 * @author menglinyang
 */
public class Food extends Product{
    protected int hp,mp;
    
    public Food(String fileName){
        super(fileName);
    }
    
    
    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }
    
}
