/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author anny
 */
public class ImgResource {
      private int count;
    private String[] paths;
    private BufferedImage[] imgs;
    
    private static ImgResource ImgResource;
    
    public static  ImgResource getInstance(){
        if(ImgResource == null){
            ImgResource = new ImgResource();
            
        }
            return ImgResource;
        
    }
    
    private ImgResource(){
        this.count = 0;
        this.imgs = new BufferedImage[2];
        this.paths = new String[2];
    }
    
    public BufferedImage tryGetImage(String path){
        int index = this.findExisted(path);
        if(index == -1){
            return addImage(path);
        }
        return imgs[index];
    }
    
    public int findExisted(String path){
        for(int i=0; i<this.count; i++){
            if(this.paths[i].equals(path)){
                return i;
            }
        }
        return -1;
    }
    
    private BufferedImage addImage(String path){
        try {
            BufferedImage img  = ImageIO.read(getClass().getResource(path));
            if(count == imgs.length){
                doubleArr();
            }
            imgs[count] = img;
            paths[count++] = path;
            return img;
        } catch (IOException ex) {
            Logger.getLogger(ImgResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
        
    }

    private void doubleArr() {
       BufferedImage[] tmp1 = new BufferedImage[this.imgs.length*2];
       String[] tmp2 = new String[this.paths.length*2];
       for(int i=0; i<this.imgs.length; i++){
           tmp1[i] = this.imgs[i];
           tmp2[i] = this.paths[i];
       }
       imgs = tmp1;
       paths = tmp2;
    }
    
    
}
