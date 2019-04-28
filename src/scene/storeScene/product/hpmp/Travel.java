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
public class Travel extends Food{
    public Travel(){
        super("/resources/travel.jpg");
        this.name = "旅遊";
        this.price = 400;
        this.mp =30;
        this.hp = 0;
        this.info = "旅遊:MP+30";
    }
}
