/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import scene.storeScene.SellScene;
import scene.storeScene.BuyScene;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import scene.Scene;
import scene.*;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.Timer;
import scene.IntroScene;
import scene.LoadGameScene;
import scene.StageScene;

/**
 *
 * @author anny
 */
public class MainPanel extends JPanel {

    //設定各個場景代號
    public static final int MENU_SCENE = 0;
    public static final int INTRO_SCENE = 1;
    public static final int NEW_GAME_SCENE = 2;
    public static final int LOAD_GAME_SCENE = 3;
    public static final int STAGE_SCENE = 4;
    public static final int STORE_SCENE = 5;
    public static final int SELL_SCENE = 6;
    public static final int SAVE_SCENE = 7;
    public static final int DISPLAY_SCENE = 8;
    public static final int STUFFLIST_SCENE = 9;


    private StageScene stageScene; //暫存stage實體（for暫停使用）
    private BuyScene buyScene;  //暫存stage實體 （for暫停使用）
    private DisplayScene displayScene;//暫存stage實體 （for暫停使用）
    public static AudioClip backgroundSound;
    
    public interface GameStatusChangeListener {

        void changeScene(int sceneId);
    }

    MouseAdapter mouseAdapter;
    Scene currentScene;
    GameStatusChangeListener gsChangeListener;

    public MainPanel() {
        gsChangeListener = new GameStatusChangeListener() {
            @Override
            public void changeScene(int sceneId) {
                changeCurrentScene(genSceneById(sceneId));
            }
        };

        this.setBackground(Color.red);

//        changeCurrentScene(genSceneById(DISPLAY_SCENE));
        changeCurrentScene(genSceneById(MENU_SCENE));

        Timer t1 = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentScene != null) {
                    currentScene.logicEvent();
                }
            }
        });
        t1.start();
        
        backgroundSound = Applet.newAudioClip(getClass().getResource("/resources/storeMusic.wav"));
        backgroundSound.loop();
    }

    private void changeCurrentScene(Scene scene) {
        if (currentScene != null) {
            this.removeMouseMotionListener(mouseAdapter);
            this.removeMouseListener(mouseAdapter);
        }
        
        currentScene = scene;
        mouseAdapter = scene.genMouseAdapter();

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        if (MenuScene.newGameCheck == false) {
            this.addKeyListener(MenuScene.genKeyAdapter());
            this.setFocusable(true);
        }

    }

    private Scene genSceneById(int id) {
        //設定切換場景

        switch (id) {
            case MENU_SCENE:
                return new MenuScene(gsChangeListener);
            case INTRO_SCENE:
                return new IntroScene(gsChangeListener);
            case STORE_SCENE:
                if(SaveScene.saveSceneCheck){  //如果使用到儲存畫面，則回傳上一刻的實體
                    SaveScene.saveSceneCheck = false;
                    return this.buyScene;
                }
                return this.buyScene = new BuyScene(gsChangeListener);  
            case LOAD_GAME_SCENE: {
                try {
                    return new LoadGameScene(gsChangeListener);
                } catch (IOException ex) {
                    ex.getStackTrace();
                }
            }
            case STAGE_SCENE:
                if(SaveScene.saveSceneCheck){  //如果使用到儲存畫面，則回傳上一刻的實體
                    SaveScene.saveSceneCheck = false;
                     return this.stageScene;
                }
                return  this.stageScene = new StageScene(gsChangeListener);

            case SELL_SCENE:
                return new SellScene(gsChangeListener);
            case SAVE_SCENE:
                return new SaveScene(gsChangeListener);
            case DISPLAY_SCENE:
                return new DisplayScene(gsChangeListener);
            case STUFFLIST_SCENE:
                return new StuffListScene(gsChangeListener);
        }
        return null;
    }

    @Override
    public void paintComponent(Graphics g) {
       
            currentScene.paint(g);
        
    }

}
