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
public class Noodle extends Food{
    
    public Noodle(){
        super("/resources/noodle.png");
        this.name = "麵食";
        this.price = 200;
        this.mp =0;
        this.hp = 30;
        this.info = "麵食:HP+30";
    }
}
