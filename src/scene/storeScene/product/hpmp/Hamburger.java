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
public class Hamburger extends Food{
    
    public Hamburger(){
        super("/resources/hamburger.jpg");
        this.name = "漢堡";
        this.price = 100;
        this.mp =10;
        this.hp = 20;
        this.info = "漢堡:HP+20,MP+10";
    }
}
