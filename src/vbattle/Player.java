package vbattle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private int inventory,stage;
    private int hp,mp,cash; //體力 快樂度
    private ArrayList<FinancialProduct> fp;
    private int unlock[] = new int[5];//資源存量,破關進度,解鎖腳色
    private String savePath;
    private static Player player;
    private String playerName;
    private String[] playerNameList;
    //GETTER SETTER
    
    public static Player getPlayerInstane(){
        if(player == null){
            player = new Player();
        }
            return player;
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

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
    

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
        playerNameList = new String[6];
        savePath = "Playertest";
    }
    
    public void setPlayerName(String name){
        this.playerName = name;
    }
    
    public void increaseInventory(int money){
        this.inventory+=money;
    }
    public void increaseCash(int money){
        this.cash+=money;
    }
    public String getPlayerName(){
        return this.playerName;
    }
    
    
//    public Player(String loadPath)throws IOException{ //載入存檔
//        load(loadPath);
//    }
    
    public void save()throws IOException{  //存檔方法
        //(playerName,inventory,stage,onlock0,onlock1,onlock2,onlock3,onlock4,hp,mp,cash)
        //(0         ,1        ,2    ,3      ,4      ,5      ,6      ,7      ,8 ,9 ,10)
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/"+savePath+".txt"));
        bw.write(playerName+",");
        bw.write(String.valueOf(this.inventory));
        bw.write(","+String.valueOf(this.stage));
        for (int i = 0; i < 5; i++) {
            bw.write(String.valueOf(","+this.unlock[i]));
        }
        bw.write(","+hp);
        bw.write(","+mp);
        bw.write(","+cash);
        bw.newLine();
        bw.flush();
        bw.close();
    }
    
    public void load(String loadPath, int index)throws IOException{ //載入方法
        BufferedReader br = new BufferedReader(new FileReader("src/"+loadPath+".txt"));
        for(int i=0; i<index; i++){
            br.readLine();
        }
        String status[] = br.readLine().split(",");
        this.playerName = status[0];
        this.inventory = Integer.parseInt(status[1]);
        this.stage = Integer.parseInt(status[2]);
        for (int i = 0; i < unlock.length; i++) {
            this.unlock[i] = Integer.parseInt(status[i+3]);
        }
        this.hp = Integer.parseInt(status[8]);
        this.mp = Integer.parseInt(status[9]);
        this.cash = Integer.parseInt(status[10]);
        br.close();
    }
    
    public void loadPlayerList(String path)throws IOException{
        int i=0;
        BufferedReader br = new BufferedReader(new FileReader("src/"+path+".txt"));
        while(br.ready()){
            String tmp[] = br.readLine().split(",");
            playerNameList[i++] = tmp[0];
        }
    }
    
    public String[] getPlayerList(){
        return this.playerNameList;
    }
    
    public void test(){
        System.out.println(this.playerName);
        System.out.println(String.valueOf(this.inventory));
        System.out.println(this.stage);
        for (int i = 0; i < unlock.length; i++) {
            System.out.println(this.unlock[i]);
        }
    }
    
    public void printPlayerList(){
        for(int i=0; i<6; i++){
             System.out.println(playerNameList[i]);
        }
    }
}

    
