/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene;

import vbattle.ItemOnScreen;


/**
 *
 * @author menglinyang
 */
public class Product extends ItemOnScreen{
    private int price;
    private int hp,mp;
    private String info;

    public Product(String fileName, String name, int price, String info) {
        super(fileName);
        this.info = info;
        this.name = name;
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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