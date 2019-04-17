package vbattle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Player {
    private int inventory,stage;
    private int unlock[] = new int[5];//資源存量,破關進度,解鎖腳色
    //GETTER SETTER

    public int getInventory() {
        return inventory;
    }
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
    public int getStage() {
        return stage;
    }
    public void setStage(int stage) {
        this.stage = stage;
    }
    public int[] getUnlock() {
        return unlock;
    }
    public void setUnlock(int[] unlock) {
        this.unlock = unlock;
    }
    //GETTER SETTER
    
    
    public Player(){ //建立新玩家
        this.inventory = 0;
        this.stage = 0;
        for (int i = 0; i < unlock.length; i++) {
            this.unlock[i] = 0;
        }
    }
    public Player(String loadPath)throws IOException{ //載入存檔
        load(loadPath);
    }
    
    public void save(String savePath)throws IOException{  //存檔方法
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/"+savePath+".txt"));
        bw.write(String.valueOf(this.inventory));
        bw.write(","+String.valueOf(this.stage));
        for (int i = 0; i < 5; i++) {
            bw.write(String.valueOf(","+this.unlock[i]));
        }
        bw.flush();
        bw.close();
    }
    
    public void load(String loadPath)throws IOException{ //載入方法
        BufferedReader br = new BufferedReader(new FileReader(loadPath+".txt"));
        String status[] = br.readLine().split(",");
        this.inventory = Integer.parseInt(status[0]);
        this.stage = Integer.parseInt(status[1]);
        for (int i = 0; i < unlock.length; i++) {
            this.unlock[i] = Integer.parseInt(status[i+2]);
        }
        br.close();
    }
    
    public void test(){
        System.out.println(String.valueOf(this.inventory));
        System.out.println(this.stage);
        for (int i = 0; i < unlock.length; i++) {
            System.out.println(this.unlock[i]);
        }
    }
}

    
