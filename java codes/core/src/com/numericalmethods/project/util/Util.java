package com.numericalmethods.project.util;

/**
 * Created by asus-pc on 12/5/2016.
 */
public class Util {
    public static Float multiplyToWorldFactor(Float number) {
        return number * Constants.WORLD_FACTOR;
    }

    public static Float multiplyToWorldFactor(Integer number) {
        return number * Constants.WORLD_FACTOR;
    }
}
