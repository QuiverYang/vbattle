/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import vbattle.Button;
import vbattle.ImgResource;
import vbattle.MainPanel;
import vbattle.Player;
import vbattle.Resource;

/**
 *
 * @author annyhung
 */
public class StuffListScene extends Scene {

    private Button returnBtn;
    private Button[] stuffBtn;
    private Player player;

    private BufferedImage tinyImg, bigImg, background;
    private ImgResource rc;
    private int[] unlock;
    private int[] iconX, iconY;
    private int currentStuff;
    private String name, hp, atk, info;
    private String[] infoArr;
    private Font font, fontTiny;

    private int SCREEN_WIDTH = Resource.SCREEN_WIDTH;
    private int SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;

    public StuffListScene(MainPanel.GameStatusChangeListener gsChangeListener) {
        super(gsChangeListener);
        player = Player.getPlayerInstane();
        unlock = player.getUnlock();
        rc = ImgResource.getInstance();
        name = hp = atk = info = "";
        currentStuff = -1;
        infoArr = new String[10];
        initInfoArr();  //初始化角色介紹
        
        //img
        tinyImg = rc.tryGetImage("/resources/tinyCharacters.png");
        bigImg = rc.tryGetImage("/resources/bigCharacters.png");
        background = rc.tryGetImage("/resources/almanacBg.png");

        //icon座標
        iconX = new int[10];
        iconY = new int[10];
        for (int i = 0; i < 5; i++) {
            iconX[i] = (int) (Resource.SCREEN_WIDTH * (0.1f * i) + Resource.SCREEN_WIDTH * (0.05f));
            iconY[i] = (int) (Resource.SCREEN_HEIGHT * 0.40f);
            System.out.println(i + " " + iconX[i] + " y:" + iconY[i]);
        }
        for (int i = 5; i < 10; i++) {
            iconX[i] = (int) (Resource.SCREEN_WIDTH * (0.1f * (i - 5)) + Resource.SCREEN_WIDTH * (0.05f));
            iconY[i] = (int) (Resource.SCREEN_HEIGHT * 0.7f);
        }
        
        //font
        font = new Font("Courier", Font.BOLD, Resource.SCREEN_WIDTH / 35);
        fontTiny = new Font("Courier", Font.BOLD, Resource.SCREEN_WIDTH / 45);
        
        //btn
        for (int i = 0; i < 10; i++) {
            stuffBtn = new Button[10];
        }
        for (int i = 0; i < unlock.length; i++) {
            stuffBtn[i] = new Button("/resources/btnIcon.png", iconX[i], iconY[i], Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
            stuffBtn[i + 5] = new Button("/resources/btnIcon.png", iconX[i + 5], iconY[i + 5], Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
        }
        returnBtn = new Button("/resources/return_blue.png", 20, 20, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);returnBtn = new Button("/resources/return_blue.png", 20, 20, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);

    }
    //初始化角色介紹
    public void initInfoArr() {
        infoArr[0] = "B細胞則在骨髓或肝臟、脾\n臟內繼續發育成熟，其接觸\n到某種抗原之後，才會有抗\n體的製造";
        infoArr[1] = "人體免疫系統中對抗癌化、\n老化及受病毒感染等細胞的\n第一道防線，又可稱為自然\n殺手細胞";
        infoArr[2] = "在胸腺內分化成熟，成熟後\n移居於周圍淋巴組織中，可\n消滅受感染的細胞";
        infoArr[3] = "存在於血液和暴露於環境中\n的組織中，將抗原處理後展\n示給免疫系統的其他白細胞\n，故是一種抗原呈現細胞";
        infoArr[4] = "以固定細胞或游離細胞的形\n式，對死亡細胞、細胞殘片\n及病原體進行噬菌作用，並\n活化淋巴球或其他免疫細胞\n，加快其對病原體作出反應\n的時間";
        infoArr[5] = "是一種令免疫受損的機會性\n感染病原，一般影響肺部及\n泌尿道，或造成燒傷、傷口\n及其他血液感染，如敗血病";
        infoArr[6] = "會引起不同程度的化膿性炎\n症擴散疾病，如癤、癰、中\n耳炎、鼻竇炎、骨髓炎和膿\n毒病等";
        infoArr[7] = "在食品衛生檢驗上，將一般\n棲息在人體上的大腸桿菌作\n為衛生指標菌，但有些菌株\n可經由食品媒介引起人類腹\n瀉";
        infoArr[8] = "可引致多種疾病，常見的包\n括有中耳炎和肺炎。此細菌\n亦可引致侵入性肺炎球菌病\n，如入侵腦膜和血液，導致\n嚴重甚至致命的疾病";
        infoArr[9] = "指細胞不正常增生，且這些\n增生的細胞可能侵犯身體的\n其他部分，還會局部侵入週\n遭正常組織甚至經由體內循\n環系統或淋巴系統轉移到身\n體其他部分";
    }

    @Override
    public MouseAdapter genMouseAdapter() {
        return new MouseAdapter() {
            public boolean isOnBtn(MouseEvent e, Button btn) {
                if (e.getX() >= btn.getX()
                        && e.getX() <= btn.getX() + btn.getImgWidth() && e.getY() >= btn.getY() && e.getY() <= btn.getY() + btn.getImgHeight()) {
                    return true;
                }
                return false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < stuffBtn.length; i++) {
                    if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, stuffBtn[i]) && unlock[i%5] !=0) {
                        System.out.println(i + "ss");
                        stuffBtn[i].setClickState(true);
                        stuffBtn[i].setImgState(1);
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, returnBtn)) {
                    returnBtn.setClickState(true);
                    returnBtn.setImgState(1);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (int i = 0; i < stuffBtn.length; i++) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        stuffBtn[i].setImgState(0);

                        returnBtn.setImgState(0);
                    }
                }
                for (int i = 0; i < stuffBtn.length; i++) {
                    if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, stuffBtn[i]) && stuffBtn[i].getClickState()) {
                        currentStuff = i;
                        load(i);
                        stuffBtn[i].setClickState(false);
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && isOnBtn(e, returnBtn) && returnBtn.getClickState()) {
                    returnBtn.setClickState(false);
                    gsChangeListener.changeScene(MainPanel.STORE_SCENE);
                }
            }
        };
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, Resource.SCREEN_WIDTH, Resource.SCREEN_HEIGHT, 0, 0, this.background.getWidth(), this.background.getHeight(), null);
        returnBtn.paintBtn(g);
        
        //繪製按鈕上角色圖案
        for (int i = 0; i < unlock.length; i++) {
            if (unlock[i] != 0) {
                stuffBtn[i].paintBtn(g);
                stuffBtn[i + 5].paintBtn(g);
                if (i < 2) {
                    g.drawImage(tinyImg, iconX[i + 5] + (int) (Resource.SCREEN_WIDTH * 0.0083), iconY[i + 5] + (int) (Resource.SCREEN_WIDTH * 0.0083), iconX[i + 5] + (int) (Resource.SCREEN_HEIGHT * 0.078) + (int) (Resource.SCREEN_WIDTH * 0.0083), iconY[i + 5] + (int) (Resource.SCREEN_HEIGHT * 0.078) + (int) (Resource.SCREEN_WIDTH * 0.0083), 0, (i + 3) * 32, 32, (i + 3 + 1) * 32, null);
                }
                if (i >= 2) {
                    g.drawImage(bigImg, iconX[i + 5] + (int) (Resource.SCREEN_WIDTH * 0.0083), iconY[i + 5] + (int) (Resource.SCREEN_WIDTH * 0.0083), iconX[i + 5] + (int) (Resource.SCREEN_HEIGHT * 0.089) + (int) (Resource.SCREEN_WIDTH * 0.0083), iconY[i + 5] + (int) (Resource.SCREEN_HEIGHT * 0.078) + (int) (Resource.SCREEN_WIDTH * 0.0083), 0, (i) * 64, 64, (i + 1) * 64, null);
                }
                if (i < 3) {
                    g.drawImage(tinyImg, iconX[i] + (int) (Resource.SCREEN_WIDTH * 0.0083), iconY[i] + (int) (Resource.SCREEN_WIDTH * 0.0083), iconX[i] + (int) (Resource.SCREEN_HEIGHT * 0.078) + (int) (Resource.SCREEN_WIDTH * 0.0083), iconY[i] + (int) (Resource.SCREEN_HEIGHT * 0.078) + (int) (Resource.SCREEN_WIDTH * 0.0083), 0, i * 32, 32, (i + 1) * 32, null);
                }
                if (i > 2) {
                    g.drawImage(bigImg, iconX[i] + (int) (Resource.SCREEN_WIDTH * 0.0083), iconY[i] + (int) (Resource.SCREEN_WIDTH * 0.0083), iconX[i] + (int) (Resource.SCREEN_HEIGHT * 0.078) + (int) (Resource.SCREEN_WIDTH * 0.0083), iconY[i] + (int) (Resource.SCREEN_HEIGHT * 0.078) + (int) (Resource.SCREEN_WIDTH * 0.0083), 0, (i - 3) * 64, 64, (i - 3 + 1) * 64, null);
                }
            }

        }
        
        //點擊顯示
        if (currentStuff != -1) {
            if (currentStuff < 3) {
                g.drawImage(tinyImg, Resource.SCREEN_WIDTH / 12 * 9, Resource.SCREEN_HEIGHT / 9 * 3, (int) (Resource.SCREEN_WIDTH * 0.875), Resource.SCREEN_HEIGHT / 2, 0, currentStuff * 32, 32, (currentStuff + 1) * 32, null);
            } else if (currentStuff > 2 && currentStuff < 5) {
                g.drawImage(bigImg, Resource.SCREEN_WIDTH / 12 * 9, Resource.SCREEN_HEIGHT / 9 * 3, (int) (Resource.SCREEN_WIDTH * 0.916), Resource.SCREEN_HEIGHT / 9 * 5, 0, (currentStuff - 3) * 64, 64, (currentStuff - 3 + 1) * 64, null);
            } else if (currentStuff > 4 && currentStuff < 7) {
                g.drawImage(tinyImg, Resource.SCREEN_WIDTH / 12 * 9, Resource.SCREEN_HEIGHT / 9 * 3, (int) (Resource.SCREEN_WIDTH * 0.875), Resource.SCREEN_HEIGHT / 2, 0, (currentStuff - 2) * 32, 32, (currentStuff - 2 + 1) * 32, null);
            } else if (currentStuff > 6) {
                g.drawImage(bigImg, Resource.SCREEN_WIDTH / 12 * 9, Resource.SCREEN_HEIGHT / 9 * 3, (int) (Resource.SCREEN_WIDTH * 0.917), Resource.SCREEN_HEIGHT / 9 * 5, 0, (currentStuff - 5) * 64, 64, (currentStuff - 5 + 1) * 64, null);
            }
            g.setFont(font);
            g.setColor(Color.white);
            FontMetrics fm = g.getFontMetrics();
            int sw = fm.stringWidth(name);
            g.drawString(name, (int) (Resource.SCREEN_WIDTH * 0.658) + (int) (Resource.SCREEN_WIDTH * 0.317) / 2 - sw / 2, (int) (Resource.SCREEN_HEIGHT * 0.578));
            g.drawString("HP:" + hp, (int) (Resource.SCREEN_WIDTH * 0.775), (int) (Resource.SCREEN_HEIGHT * 0.611));
            g.drawString("ATK:" + atk, (int) (Resource.SCREEN_WIDTH * 0.775), (int) (Resource.SCREEN_HEIGHT * 0.644));

            g.setFont(fontTiny);
            drawString(g, infoArr[currentStuff], (int) (Resource.SCREEN_WIDTH * 0.692), (int) (Resource.SCREEN_HEIGHT * 0.678));

        }

    }

    private void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n")) {
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
        }
    }

    private void load(int i) {
        try {
            BufferedReader br;
            String[] status = null;
            int lv = 1;
            if (i < 5) { //讀取我方腳
                br = new BufferedReader(new FileReader("src/" + "test" + i + ".txt"));
                status = br.readLine().split(",");
                lv = unlock[i];
            } else if (i > 4) { //讀取敵方腳
                br = new BufferedReader(new FileReader("src/" + "actor" + (i - 5) + ".txt"));
                status = br.readLine().split(",");
            }

            int hpRate = Integer.parseInt(status[1]);
            int atkRate = Integer.parseInt(status[2]);
            int hpBase = Integer.parseInt(status[3]);
            int atkBase = Integer.parseInt(status[4]);
            this.name = status[12];

            this.hp = (hpRate * lv + hpBase) + "";
            this.atk = (atkRate * lv + atkBase) + "";

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException from almanac");
        } catch (IOException ex) {
            System.out.println("IOException from almanac");
        }
    }

    @Override
    public void logicEvent() {
        resize();
    }

    private void resize() {
        if (SCREEN_WIDTH != Resource.SCREEN_WIDTH || SCREEN_HEIGHT != Resource.SCREEN_HEIGHT) {
            SCREEN_WIDTH = Resource.SCREEN_WIDTH;
            SCREEN_HEIGHT = Resource.SCREEN_HEIGHT;
            
            //icon座標
            for (int i = 0; i < 5; i++) {
                iconX[i] = (int) (Resource.SCREEN_WIDTH * (0.1f * i) + Resource.SCREEN_WIDTH * (0.05f));
                iconY[i] = (int) (Resource.SCREEN_HEIGHT * 0.40f);
                System.out.println(i + " " + iconX[i] + " y:" + iconY[i]);
            }
            for (int i = 5; i < 10; i++) {
                iconX[i] = (int) (Resource.SCREEN_WIDTH * (0.1f * (i - 5)) + Resource.SCREEN_WIDTH * (0.05f));
                iconY[i] = (int) (Resource.SCREEN_HEIGHT * 0.7f);
            }
            //btn
            for (int i = 0; i < unlock.length; i++) {
                stuffBtn[i].reset(iconX[i], iconY[i], Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
                stuffBtn[i + 5].reset(iconX[i + 5], iconY[i + 5], Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
            }
            returnBtn.reset( 20, 20, Resource.SCREEN_WIDTH / 12, Resource.SCREEN_HEIGHT / 9);
            //font
            font = new Font("Courier", Font.BOLD, Resource.SCREEN_WIDTH / 35);
            fontTiny = new Font("Courier", Font.BOLD, Resource.SCREEN_WIDTH / 45);
        }
    }

}
