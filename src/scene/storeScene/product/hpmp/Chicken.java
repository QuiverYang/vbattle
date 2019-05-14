/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene.product.hpmp;

/**
 *
 * @author menglinyang
 */
public class Chicken extends Food{
    public Chicken(){
        super("/resources/chiken.png");
        this.price = 300;
        this.mp =0;
        this.hp = 40;
        this.info = "雞腿:HP+40";
    }
}
