/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import scene.Scene;
import scene.MenuScene;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import scene.IntroScene;

/**
 *
 * @author anny
 */
public class MainPanel extends JPanel{
    
    //設定各個場景代號
    public static final int MENU_SCENE = 0;
    public static final int INTRO_SCENE = 1;
    public static final int NEW_GAME_SCENE = 2;
    public static final int LOAD_GAME_SCENE = 3;
    
    
    public interface GameStatusChangeListener{
        void changeScene(int sceneId);
    }
    
    MouseListener mouseListener;
    Scene currentScene;
    GameStatusChangeListener gsChangeListener;
    
    public MainPanel(){
        gsChangeListener = new GameStatusChangeListener(){
            @Override
            public void changeScene(int sceneId){
                changeCurrentScene(genSceneById(sceneId));
            }
        };
        
        changeCurrentScene(genSceneById(MENU_SCENE));
        
        Timer t1 = new Timer(25, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(currentScene != null){
                   currentScene.logicEvent();
                }
            }
        });
        t1.start();
    }
    
    
    
    private void changeCurrentScene(Scene scene){
        if(currentScene != null){
            this.removeMouseListener(mouseListener);
        }
        currentScene = scene;
        mouseListener = scene.genMouseListener();
        this.addMouseListener(mouseListener);
    }
    
    private Scene genSceneById(int id){
        //設定切換場景
        
        switch(id){
            case MENU_SCENE:
                return new MenuScene(gsChangeListener);
            case INTRO_SCENE:
                return new IntroScene(gsChangeListener);
//            case NEW_GAME_SCENE:
//                return new NewGameScene(gsChangeListener);
//            case LOAD_GAME_SCENE:
//                return LoadGameScene(gsChangeListener);
        }
        return null;
    }
    
    @Override
    public void paintComponent(Graphics g){
        if(currentScene != null){
            currentScene.paint(g);
        }
    }
    
}
