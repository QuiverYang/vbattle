/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene.storeScene.product;

import vbattle.ItemOnScreen;


/**
 *
 * @author menglinyang
 */
public class Product extends ItemOnScreen{
    protected int price;
    
    protected String info;
    
    public Product(){
        
    }
    
    public Product(String fileName){
        super(fileName);
    }

    public Product(String fileName, String name, int price, String info) {
        super(fileName);
        this.info = info;
        this.name = name;
        this.price = price;
    }
    
    public Product(String fileName, int x, int y, int width, int height){
        super(fileName, x, y, width, height);
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

    
     
    
}
