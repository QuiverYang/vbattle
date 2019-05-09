package vbattle;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author anny,LC
 */
public class Stuff {

    //腳色屬性
    private String name;
    private int maxHp, maxMp, atk, lv, exp;//物件HP,ATK,LV,EXP
    private String txtpath;//參數txt路徑
    private String imgpath;//圖片路徑...存入參數txt檔
    private BufferedImage img;//角色圖片
    private BufferedImage throwimg;//投擲
    private int hpRate, atkRate, hpBase, atkBase;//基礎參數...存入參數txt檔
    private float speed = 1 / 16f; //角色移動速度：測試速度1/16f 角色寬
    private int cdTime;//CD時間：單位是FPS倍數週期
    private int attackedTime;//CD時間:被攻擊後的無敵時間
    private int type; // 判斷正反角 (1-->我方角 , -1-->敵方)
    private int range;
    private int imgSize;
    private BombA a;

    //腳色變動屬性
//    private boolean clickState;
    private int hp, mp;

    //系統屬性
    private int x0, y0, imgWidth, imgHeight, x1, y1;//起始座標,圖片尺寸,物件範圍(起始座標+圖片尺寸)
    private ImgResource rc;//共用圖片資源loader
    private float frame;  //第 int frame 偵：紀錄載入圖片X座標
    private int characterNumY0, characterNumY1;//角色編號：紀錄載入圖片Y座標
    private boolean attackCd = true;//CD狀態：初始化true
    private boolean attackedCd = true;
    private int cdCounter = 0;//CD計數器：單位是FPS倍數週期
    private int attackedCdCounter = 0;
    private static AudioClip hit;
    private boolean clickState;

    private BufferedImage ghostImg;
    private BufferedImage devilImg;
    private int price;

    public static final int ACTOR1_PRICE = 1;
    public static final int ACTOR2_PRICE = 1;
    public static final int ACTOR3_PRICE = 1;
    public static final int ACTOR4_PRICE = 1;
    public static final int ACTOR5_PRICE = 1;

    public Stuff(int type, int x0, int y0, int imgWidth, int imgHeight, int characterNum, String txtpath) throws IOException {
        //設定建構子參數
        this.type = type;
        this.x0 = x0;
        this.y0 = y0;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.x1 = x0 + imgWidth;
        this.y1 = y0 + imgHeight;
        this.characterNumY0 = characterNum * 32;
        this.characterNumY1 = characterNumY0 + 32;
        this.cdTime = 40;
        this.attackedTime = 5;
        this.txtpath = txtpath;
        //設定建構子參數
        //讀取參數txt檔
        rc = ImgResource.getInstance();
        BufferedReader br = new BufferedReader(new FileReader("src/" + txtpath + ".txt"));
        String status[] = br.readLine().split(",");
        img = rc.tryGetImage(status[0]);
        ghostImg = rc.tryGetImage("/resources/ghost.png"); //ghost pic
        devilImg = rc.tryGetImage("/resources/devil_1.png"); //ghost pic
        this.hpRate = Integer.parseInt(status[1]);
        this.atkRate = Integer.parseInt(status[2]);
        this.hpBase = Integer.parseInt(status[3]);
        this.atkBase = Integer.parseInt(status[4]);
        this.range = Integer.parseInt(status[5]);
        if (this.range > 0) {
            br.close();
        }
        //讀取參數txt檔
        //初始化腳色
        this.lv = 1;
        this.maxHp = this.hp = hpRate * lv + this.hpBase;
        this.atk = atkRate * lv + this.atkBase;
        //初始化腳色

        try {
            hit = Applet.newAudioClip(getClass().getResource("/resources/Damage3.wav"));
        } catch (Exception ex) {
        }
        clickState = false;
    }

    //內建GETTER SETTER
    public int getX0() {
        return x0;
    }

    public void setX0(int x0) {
        this.x0 = x0;
    }

    public int getY0() {
        return y0;
    }

    public void setY0(int y0) {
        this.y0 = y0;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public int getX1() {
        return x0 + imgWidth;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getExp() {
        return exp;
    }

    public int getType() {
        return this.type;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getTxtpath() {
        return txtpath;
    }

    public void setTxtpath(String txtpath) {
        this.txtpath = txtpath;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public int getCdTime() {
        return cdTime;
    }

    public void setCdTime(int cdTime) {
        this.cdTime = cdTime;
    }

    //內建GETTER SETTER
    public void setCoordinate(int x, int y) {
        this.x0 = x - imgWidth / 2;
        this.y0 = y - imgHeight / 2;
        this.x1 = x0 + imgWidth;
        this.y1 = y0 + imgHeight;
    }

    public void setClickState(boolean a) {
        this.clickState = a;
    }

    public boolean getClickState() {
        return this.clickState;
    }

    //腳色方法
    public void walkFrame() {
        //控制更新範圍：0~3
        frame++;
        if (frame == 3) {
            frame = 0;
        }
    }

    public void move() {
        walkFrame();
        //移動座標
        this.x0 = x0 + (int) (imgWidth * speed * type);
        this.x1 = x0 + imgWidth;
    }

    public void attack(Stuff attacked) {
        if (attackCd) {//cd中不進攻擊狀態
            if (frame < 3 || frame > 6) { //進入攻擊狀態：防止重複初始化frame;
                frame = 3;

                if (range > 0) {
                    a = new BombA(x0, y0, 32, Math.abs(x0 - attacked.x0));

                }
            }

            frame += 1 / 4f;//播放動畫

            if (frame >= 5) {//動畫完成觸發攻擊效果
                if (range < 1) {
                    attacked.back(this);
                } else {
                }
                frame = 0;
                attackCd = false;
            }

        } else {
            walkFrame();
        }
    }

    public BombA animation() {
        if (a != null) {
            a.move();
            return a;
        }
        return null;
    }

    public void back(Stuff attacker) {
        if (attackCd) {
            hp = hp - attacker.atk;
            frame = 0;
            x0 = x0 - 100 * type;
            this.x1 = x0 + imgWidth;
            hit.play();
            attackCd = false;
        }
    }

    public void die() {
        frame += 1 / 2f;

        y0 -= 15;
        y1 -= 15;

        if (frame >= 5) {
            frame = 3;
        }
    }

    public void refreshCd() {
        if (!attackCd) {
            cdCounter++;
            if (cdCounter == cdTime) {
                attackCd = true;
                cdCounter = 0;
            }
        }
        if (!attackedCd) {
            attackedCdCounter++;
            if (attackedCdCounter == attackedTime) {
                attackedCd = true;
                attackedCdCounter = 0;
            }
        }
    }

    public Stuff collisionCheck(ArrayList<Stuff> actor) {
        for (int i = 0; i < actor.size(); i++) {
            if (type == 1 && this.x0 < actor.get(i).x1 && this.x1 + (range * type) > actor.get(i).x0) {
                return actor.get(i);
            }
            if (type == -1 && this.x0 + (range * type) < actor.get(i).x1 && this.x1 + (range * imgWidth * type) > actor.get(i).x0) {
                return actor.get(i);
            }
        }
        return null;
    }

    public void lvup() {//商店購買等級使用
        this.lv += 1;
    }

    public float getHpPercent() {
        return (float) this.hp / (float) this.maxHp;
    }

    public void paint(Graphics g) {
        //HP顯示
        if (this.hp > 0) {
            g.setColor(Color.red);
            g.fillRect(x0 + (int) (imgWidth * 1 / 4f), this.getY0() - (int) (Resource.SCREEN_HEIGHT * 0.0056), (int) (this.getImgWidth() * this.getHpPercent() * (1 / 2f)), (int) (Resource.SCREEN_HEIGHT * 0.0056));
            //角色顯示
            g.drawImage(img, x0, y0, x1, y1, (int) frame * 32, characterNumY0, ((int) frame + 1) * 32, characterNumY1, null);
        } else if (this.hp <= 0) {
            if (this.type == 1) {
                g.drawImage(this.ghostImg, x0, y0, x0 + imgWidth, y1, (int) frame * 32, 0, (((int) frame + 1) * 32), this.ghostImg.getHeight(), null);
            } else {
                g.drawImage(this.devilImg, x0, y0, x0 + imgWidth, y1, (int) frame * 32, 0, (((int) frame + 1) * 32), this.devilImg.getHeight(), null);
            }
        }

        if (a != null) {
            a.paint(g);
        }
    }

    public void resize() {
        imgWidth = imgHeight = Resource.SCREEN_WIDTH / 12;
        this.x1 = x0 + imgWidth;
        this.y1 = y0 + imgHeight;

    }
}
