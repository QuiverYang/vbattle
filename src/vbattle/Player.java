package vbattle;

import scene.storeScene.FinProduct;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private int inventory,stage;
    private int hp,mp,cash; //體力 快樂度
    private ArrayList<FinProduct> fp;
    private int unlock[] = new int[5];//資源存量,破關進度,解鎖腳色
    private String savePath;
    private static Player player;
    private String playerName;
    private String[] playerNameList;
    private ArrayList<String[]> playerInfo; //儲存所有玩家
    private int playerIndex; //若為 -1 -->新玩家 , 若不等於-1 -->舊玩家於檔案內的行數位置
    public static boolean loadCheck;
    //GETTER SETTER

    public static Player getPlayerInstane() {
        if (player == null) {
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

    public void setUnlock(int[] unlock) {
        this.unlock = unlock;
    }
    //GETTER SETTER

    public Player() { //建立新玩家
        this.inventory = 0;
        this.stage = 0;
        for (int i = 0; i < unlock.length; i++) {
            this.unlock[i] = 0;
        }
        playerNameList = new String[6];
        savePath = "Playertest";
        playerIndex = -1;
        playerInfo = new ArrayList();
        fp = new ArrayList();
        loadCheck = false;
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
    
    public void increaseHp(int hpUp){
        this.hp += hpUp;
    }
    
    public void increaseMp(int mpUp){
        this.mp += mpUp;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void save() throws IOException {  //存檔方法
        //(playerName,inventory,stage,onlock0,onlock1,onlock2,onlock3,onlock4,hp,mp,cash)
        //(0         ,1        ,2    ,3      ,4      ,5      ,6      ,7      ,8 ,9 ,10)

        //暫存當前玩家資料
        int countFp = 11;
        String[] info = new String[11 + this.fp.size() * 2];
        info[0] = this.playerName;
        info[1] = String.valueOf(this.inventory);
        info[2] = String.valueOf(this.stage);
        for (int k = 0; k < 5; k++) {
            info[k + 3] = String.valueOf(this.unlock[k]);
        }
        info[8] = String.valueOf(this.hp);
        info[9] = String.valueOf(this.mp);
        info[10] = String.valueOf(this.cash);
        for (int i = 0; i < this.fp.size(); i++) {
            info[i + countFp++] = String.valueOf(this.fp.get(i).getName());  
            info[i + countFp++] = String.valueOf(this.fp.get(i).getValue());  
        }

        //判斷是否為新玩家
        if (this.playerIndex == -1 && this.playerInfo.size() >= 6) { //若為新玩家且當前玩家數>=6，則刪除最舊玩家，新增新玩家
            this.playerInfo.remove(0);
            this.playerInfo.add(info);
        } else if (this.playerIndex == -1 && this.playerInfo.size() < 6) {  //若為新玩家且當前玩家數<6，則存入陣列
            this.playerInfo.add(info);
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
            bw.write(tmp[tmp.length - 2]);
            bw.newLine();
        }
        bw.flush();
        bw.close();

//        for (int j = 0; j < i; j++) {
//            if (info[j][0] != null) {
//                for (int k = 0; k < 10; k++) {
//
//                    bw.write(info[j][k] + ",");
//                }
//
//                bw.write(info[j][10]);
//                bw.newLine();
//            }
//
//        }
//        String[][] info = new String[6][11];
//        BufferedReader br = new BufferedReader(new FileReader("src/" + savePath + ".txt"));
//        int i = 0;
//        while (br.ready()) {
//            String status[] = br.readLine().split(",");
//            for (int j = 0; j < 11; j++) {
//                info[i][j] = status[j];
//            }
//            i++;
//        }
//
//        for (int j = 0; j < i; j++) {
//            if (info[j][0].equals(this.playerName)) {
//                checkSame = true;
//                info[j][1] = String.valueOf(this.inventory);
//                info[j][2] = String.valueOf(this.stage);
//                for (int k = 0; k < 5; k++) {
//                    info[j][k + 3] = String.valueOf(this.unlock[k]);
//                }
//                info[j][8] = String.valueOf(this.hp);
//                info[j][9] = String.valueOf(this.mp);
//                info[j][10] = String.valueOf(this.cash);
//
//            }
//        }
//
//        BufferedWriter bw = new BufferedWriter(new FileWriter("src/" + savePath + ".txt"));
//        for (int j = 0; j < i; j++) {
//            if (info[j][0] != null) {
//                for (int k = 0; k < 10; k++) {
//
//                    bw.write(info[j][k] + ",");
//                }
//
//                bw.write(info[j][10]);
//                bw.newLine();
//            }
//
//        }
//        if (!checkSame) {
//            System.out.println(this.playerName);
//            bw.write(this.playerName);
//            bw.write("," + String.valueOf(this.inventory));
//            bw.write("," + String.valueOf(this.stage));
//            for (int j = 0; j < unlock.length; j++) {
//                bw.write(String.valueOf("," + this.unlock[j]));
//            }
//            bw.write("," + hp);
//            bw.write("," + mp);
//            bw.write("," + cash);
//            bw.newLine();
//        }
//        bw.flush();
//        bw.close();
//        br.close();
//        System.out.print("brready" + br.ready());
//        do {
//            System.out.println("origin");
//            String status[] = br.readLine().split(",");
//            System.out.println("origin" + status[0]);
//            if (status[0].equals(this.playerName) == false) { //不相等
//                for (int j = 0; j < 10; j++) {
//                    System.out.println("origin" + status[j]);
//                    bw.write(status[j] + ",");
//                }
//                bw.write(status[10]);
//                bw.newLine();//原本值
//            } else {//相等
//                checkSame = true;
//                bw.write(this.playerName);
//                bw.write(String.valueOf(this.inventory));
//                bw.write("," + String.valueOf(this.stage));
//                for (int j = 0; j < 5; j++) {
//                    bw.write(String.valueOf("," + this.unlock[j]));
//                }
//                bw.write("," + hp);
//                bw.write("," + mp);
//                bw.write("," + cash);
//                bw.newLine();
//            }
//
//        } while (br.ready());
//        if (!checkSame) {
//            System.out.println(this.playerName);
//            bw.write(this.playerName);
//            bw.write("," + String.valueOf(this.inventory));
//            bw.write("," + String.valueOf(this.stage));
//            for (int j = 0; j < unlock.length; j++) {
//                bw.write(String.valueOf("," + this.unlock[j]));
//            }
//            bw.write("," + hp);
//            bw.write("," + mp);
//            bw.write("," + cash);
//            bw.newLine();
//
//        }
//        bw.flush();
//        bw.close();
//        br.close();
//        bw.write(playerName + ",");
//        bw.write(String.valueOf(this.inventory));
//        bw.write("," + String.valueOf(this.stage));
//        for (int i = 0; i < 5; i++) {
//            bw.write(String.valueOf("," + this.unlock[i]));
//        }
//        bw.write("," + hp);
//        bw.write("," + mp);
//        bw.write("," + cash);
//        bw.newLine();
//        bw.flush();
//        bw.close();
    }

    public void setPlayerIndex(int i) {
        this.playerIndex = i;
    }

    //設定當前玩家資料
    public void loadPlayer(int index) throws IOException { //載入方法
        playerIndex = index;
        
        String status[] = playerInfo.get(index);
        this.playerName = status[0];
        this.inventory = Integer.parseInt(status[1]);
        this.stage = Integer.parseInt(status[2]);
        for (int i = 0; i < unlock.length; i++) {
            this.unlock[i] = Integer.parseInt(status[i + 3]);
        }
        this.hp = Integer.parseInt(status[8]);
        this.mp = Integer.parseInt(status[9]);
        this.cash = Integer.parseInt(status[10]);
        
        //如果玩家有金融商品
        if(status.length > 11){
            int countFp =11; 
            for(int i=0; i<(status.length-11)/2; i++){
//                this.fp.add(new FinProduct());
                this.fp.get(i).setValue(Integer.parseInt(status[countFp++]));
            }
        }
    }

    //初始化玩家數值
    public void defaultPlayer() {
        this.hp = 100;
        this.mp = 50;
        this.cash = 6000;
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

}
