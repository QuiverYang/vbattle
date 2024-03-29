package vbattle;

import scene.storeScene.product.finProduct.FinProduct;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
public class Player {

    private int inventory, stage;
    private int hp, mp, cash,hpMax,mpMax; //體力 快樂度
    private ArrayList<FinProduct> fp;
    private int unlock[] = new int[5];//資源存量,破關進度,解鎖腳色
    private String savePath;
    private static Player player;
    private String playerName;
    private String[] playerNameList;
    private ArrayList<String[]> playerInfo; //儲存所有玩家
    private int playerIndex; //若為 -1 -->新玩家 , 若不等於-1 -->舊玩家於檔案內的行數位置
    public static boolean loadCheck;
    
    private boolean saveCheck;
    //GETTER SETTER

    public static Player getPlayerInstane() {
        if (player == null) {
            player = new Player();
        }
        return player;
    }
    
    public Player() { //建立新玩家
        this.inventory = 0;
        this.stage = 0;
        int[] temp = {1,1,0,0,0};
        unlock = temp;
        playerNameList = new String[6];
        savePath = "Playertest";
        playerIndex = -1;
        playerInfo = new ArrayList();
        fp = new ArrayList();
        saveCheck = loadCheck = false;
        
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


    public ArrayList<FinProduct> getFp() {
        return fp;
    }

    public void setFp(ArrayList<FinProduct> fp) {
        this.fp = fp;
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
    
    public void addUnlockContent(int index){
        unlock[index]++;
    }

    public void setUnlock(int[] unlock) {
        this.unlock = unlock;
    }

    public int getHpMax() {
        return hpMax;
    }

    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }

    public int getMpMax() {
        return mpMax;
    }

    public void setMpMax(int mpMax) {
        this.mpMax = mpMax;
    }
    
    
    
    //GETTER SETTER
    
    public void increaseMpMax(int num){
        if(num<0 && this.mpMax+num<0){
            this.mpMax = 0;
        }else{
            this.mpMax += num;
        }
    }
    public void increaseHpMax(int num){
        if(num<0 && this.hpMax+num<0){
            this.hpMax = 0;
        }else {
             this.hpMax += num;
        }
       
    }
    
    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void increaseInventory(int money) {
        this.inventory += money;
    }

    public void increaseCash(int money) {
        this.cash += money;
    }

    public void increaseHp(int hpUp) {
        this.hp += hpUp;
    }

    public void increaseMp(int mpUp) {
        this.mp += mpUp;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void save() throws IOException {  //存檔方法
        //(playerName,inventory,stage,onlock0,onlock1,onlock2,onlock3,onlock4,hp,mp,cash,hpmax.mpmax,fp...)
        //(0         ,-        ,1    ,2      ,3      ,4      ,5      ,6      ,7 ,8 ,9   ,10   ,11   ,12...)

        //暫存當前玩家資料
        for (int i = 0; i < unlock.length; i++) {
            if(this.stage >=1 && this.stage<4  && this.unlock[stage+1]==0){ //設定stage與unlock間的關係
                this.unlock[this.stage+1] = 1;
                
            }
            System.out.println(unlock[i]);
        }
        int countFp = 12;
        String[] info = new String[12 + this.fp.size() * 3];
        info[0] = this.playerName;
//        info[1] = String.valueOf(this.inventory);
        info[1] = String.valueOf(this.stage);
        for (int k = 0; k < 5; k++) {
            info[k + 2] = String.valueOf(this.unlock[k]);
        }
        info[7] = String.valueOf(this.hp);
        info[8] = String.valueOf(this.mp);
        
        info[9] = String.valueOf(this.cash);
        info[10] = String.valueOf(this.hpMax);
        info[11] = String.valueOf(this.mpMax);
        
        for (int i = 0; i < this.fp.size(); i++) {
            info[countFp++] = String.valueOf(this.fp.get(i).getName());
            info[countFp++] = String.valueOf(this.fp.get(i).getPlusHp());
            info[countFp++] = String.valueOf(this.fp.get(i).getPlusMp());
        }
        //判斷是否為新玩家
        if (this.playerIndex == -1 && this.playerInfo.size() >= 6) { //若為新玩家且當前玩家數>=6，則刪除最舊玩家，新增新玩家
            this.playerInfo.remove(0);
            this.playerInfo.add(info);
            this.playerIndex = this.playerInfo.size()-1;
        } else if (this.playerIndex == -1 && this.playerInfo.size() < 6) {  //若為新玩家且當前玩家數<6，則存入陣列
            this.playerInfo.add(info);
            this.playerIndex = this.playerInfo.size()-1;
        } else if (this.playerIndex != -1) {
            this.playerInfo.set(this.playerIndex, info); //若為舊玩家，則修改其資料
        }

        //存檔
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/" + savePath + ".txt"));
        for (int i = 0; i < this.playerInfo.size(); i++) {
            String[] tmp = this.playerInfo.get(i);
            for (int j = 0; j < tmp.length - 1; j++) {
                bw.write(tmp[j] + ",");
            }
            bw.write(tmp[tmp.length - 1]);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    public void setPlayerIndex(int i) {
        this.playerIndex = i;
    }

    //設定當前玩家資料
    public void loadPlayer(int index) throws IOException { //載入方法
        //(playerName,inventory,stage,onlock0,onlock1,onlock2,onlock3,onlock4,hp,mp,cash,hpmax.mpmax,fp...)
        //(0         ,-        ,1    ,2      ,3      ,4      ,5      ,6      ,7 ,8 ,9   ,10   ,11   ,12...)
        playerIndex = index;
        
        String status[] = playerInfo.get(index);
        this.playerName = status[0];
        
        this.stage = Integer.parseInt(status[1]);
        for (int i = 0; i < unlock.length; i++) {
            this.unlock[i] = Integer.parseInt(status[i + 2]);
        }
        this.hp = Integer.parseInt(status[7]);
        this.mp = Integer.parseInt(status[8]);
        this.cash = Integer.parseInt(status[9]);
//        this.inventory = this.cash;
        this.hpMax = Integer.parseInt(status[10]);
        this.mpMax = Integer.parseInt(status[11]);
        this.fp.clear(); //先刪除fp list內所有物件（以防後面讀存重複加入）
        
        //如果玩家有金融商品
        if (status.length > 12 ) {
            int countFp = 12;
            int indexFp = 0;
            int indexOfRisk, indexOfProfit,profit;
            double risk;
            for (int i = 0; i < (status.length - 12) / 3; i++) {
                
                switch (status[countFp++]) {
                    case "抗生藥品":
                        String stockInfo = FinProduct.PRODUCT_STOCK_INFO;
                        indexOfRisk = stockInfo.indexOf("風險");
                        indexOfProfit = stockInfo.indexOf("利潤");
                        risk = Double.parseDouble(stockInfo.substring(indexOfRisk + 2, indexOfRisk + 4));
                        profit = Integer.parseInt(stockInfo.substring(indexOfProfit + 2, indexOfProfit + 4));
                        
                        this.fp.add(new FinProduct(FinProduct.PRODUCT_STOCK_PATH, "抗生藥品", FinProduct.PRODUCT_FUTURES_PRICE, risk, profit, stockInfo,Integer.parseInt(status[countFp++]),Integer.parseInt(status[countFp++])));

                        break;
                        
                    case "健康食品":
                        String fundInfo = FinProduct.PRODUCT_FUND_INFO;
                        indexOfRisk = fundInfo.indexOf("風險");
                        indexOfProfit = fundInfo.indexOf("利潤");
                        risk = Double.parseDouble(fundInfo.substring(indexOfRisk + 2, indexOfRisk + 4));
                        profit = Integer.parseInt(fundInfo.substring(indexOfProfit + 2, indexOfProfit + 4));
                        this.fp.add(new FinProduct(FinProduct.PRODUCT_FUND_PATH, "健康食品", FinProduct.PRODUCT_FUND_PRICE, risk, profit, fundInfo,Integer.parseInt(status[countFp++]),Integer.parseInt(status[countFp++])));
                        break;
                        
                    case "生化元素":
                        String futureInfo = FinProduct.PRODUCT_FUTURES_INFO;
                        indexOfRisk = futureInfo.indexOf("風險");
                        indexOfProfit = futureInfo.indexOf("利潤");
                        risk = Double.parseDouble(futureInfo.substring(indexOfRisk + 2, indexOfRisk + 4));
                        profit = Integer.parseInt(futureInfo.substring(indexOfProfit + 2, indexOfProfit + 4));
                        this.fp.add(new FinProduct(FinProduct.PRODUCT_FUTURES_PATH, "生化元素", FinProduct.PRODUCT_FUTURES_PRICE, risk, profit, futureInfo,Integer.parseInt(status[countFp++]),Integer.parseInt(status[countFp++])));
                        break;
                }
                
            }
        }
        
    }


    //初始化玩家數值
    public void defaultPlayer() {
        this.hp = this.hpMax = 100;
        this.mp = this.mpMax = 100;
        this.inventory = this.cash = 6000;
        this.unlock = new int[]{1,1,0,0,0};
        this.fp.clear();
        this.stage = 0;
    }

    //儲存所有玩家資料，方便後續作儲存
    public void loadTotal() throws IOException {
        if (this.loadCheck == false) {  //確認整個遊戲只有load過一次
            BufferedReader br = new BufferedReader(new FileReader("src/" + this.savePath + ".txt"));
            while (br.ready() && this.playerInfo.size() < 6) {
                playerInfo.add(br.readLine().split(","));
            }
            this.loadCheck = true;
        }

    }

    //回傳所有玩家名字
    public String[] getPlayerList() {
        String[] NameList = new String[this.playerInfo.size()];
        for (int i = 0; i < this.playerInfo.size(); i++) {
            String[] tmp = this.playerInfo.get(i);
            NameList[i] = tmp[0];
        }
        return NameList;
    }

    public void test() {
        System.out.println(this.playerName);
        System.out.println(String.valueOf(this.inventory));
        System.out.println(this.stage);
        for (int i = 0; i < unlock.length; i++) {
            System.out.println(this.unlock[i]);
        }
    }

    public void printPlayerList() {
        for (int i = 0; i < 6; i++) {
            System.out.println(playerNameList[i]);
        }
    }
    
//    public void printFp(){
//        for(int i=0; i<fp.size(); i++){
//            System.out.println(i+" ):"+fp.get(i).getName()+" :(hp)"+fp.get(i).getPlusHp()+ "(mp):" + fp.get(i).getPlusMp());
//        }
//    }
    
   

}
