/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vbattle;

import java.awt.Font;

/**
 *
 * @author menglinyang
 */
public class Fontes {

    public static Font getBitFont(int size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, Fontes.class.getResourceAsStream("/jackeyfont.ttf"));
            font = font.deriveFont(Font.BOLD, size);
            return font;
        } catch (Exception ex) {
        }
        return new Font(Font.MONOSPACED, Font.BOLD, size);
    }
}
