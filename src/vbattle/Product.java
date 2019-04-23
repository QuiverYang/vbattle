/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.image.BufferedImage;

/**
 *
 * @author menglinyang
 */
public class Product {
    String name;
    private int width;
    private int height;
    private int x;
    private int y;
    private BufferedImage img;
    private ImgResource rc;
    private boolean isShown;
    private int price,hp,mp;
    private int value;
    
    
    public Product(String fileName){
        rc = ImgResource.getInstance();
        img = rc.tryGetImage(fileName);
    }
    
}
