package dev.elijuh.fishing.utils;

import lombok.experimental.UtilityClass;

/**
 * @author elijuh
 */

@UtilityClass
public class NumberUtil {

    public int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }
}
