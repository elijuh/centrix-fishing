package dev.elijuh.fishing.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author elijuh
 */
@UtilityClass
public class Text {
    @Getter
    private final NumberFormat decimalFormat = new DecimalFormat("0.#");
    @Getter
    private final NumberFormat currencyFormat = new DecimalFormat("0.00");
    @Getter
    private final NumberFormat format = NumberFormat.getInstance(Locale.US);

    private final String prefix = color("&6&lFishing &8┃ &a");

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String prefixed(String s) {
        return prefix + color(s);
    }

    public String formatGrams(int grams) {
        if (grams < 1e+3) return grams + "g";

        return decimalFormat.format(grams / 1e+3) + "kg";
    }
}
