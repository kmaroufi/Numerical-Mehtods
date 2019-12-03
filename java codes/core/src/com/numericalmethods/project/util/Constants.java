package com.numericalmethods.project.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by asus-pc on 12/1/2016.
 */
public class Constants {
    public static final Vector2 SCREEN_SIZE;
    public static final Vector2 MAIN_MENU_SCREEN_TABLE_SIZE = new Vector2(800, 450);
    public static final Vector2 TABLE_OFFSET = new Vector2(30, 30);
    public static final Float WORLD_FACTOR;

    static {
        SCREEN_SIZE = new Vector2((float)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                (float)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        WORLD_FACTOR = SCREEN_SIZE.x / 2560;
    }
}
