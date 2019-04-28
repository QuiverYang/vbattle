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
public class TV extends Food{
    public TV(){
        super("/resources/tv.jpg");
        this.name = "電視";
        this.price = 400;
        this.mp =20;
        this.hp = 0;
        this.info = "電視:MP+20";
    }
}
