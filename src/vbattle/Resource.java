/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

/**
 *
 * @author anny
 */
public class Resource {
    public static int SCREEN_WIDTH = 1200;
    public static int SCREEN_HEIGHT = 900;
    public static int FPS = 50;

    public static int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public static void setSCREEN_WIDTH(int SCREEN_WIDTH) {
        Resource.SCREEN_WIDTH = SCREEN_WIDTH;
    }

    public static int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public static void setSCREEN_HEIGHT(int SCREEN_HEIGHT) {
        Resource.SCREEN_HEIGHT = SCREEN_HEIGHT;
    }

    public static int getFPS() {
        return FPS;
    }

    public static void setFPS(int FPS) {
        Resource.FPS = FPS;
    }
}
