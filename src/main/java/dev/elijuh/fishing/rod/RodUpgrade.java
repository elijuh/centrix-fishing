package dev.elijuh.fishing.rod;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
@RequiredArgsConstructor
@Getter
public enum RodUpgrade {
    FISHING_SPEED("Fishing Speed", "Shorten the duration between catching fish.", 8, 2500, Enchantment.LURE),
    INCREASED_LUCK("Increased Luck", "Higher chance of catching rarer fish and treasure.", 10, 1000, Enchantment.LUCK);

    private final String display, description;
    private final int maxValue, tokensPerLevel;
    private final Enchantment vanillaBase;

    private static final Map<Enchantment, RodUpgrade> fromVanilla = new HashMap<>();

    static {
        for (RodUpgrade upgrade : values()) {
            fromVanilla.put(upgrade.getVanillaBase(), upgrade);
        }
    }

    public static RodUpgrade fromVanillaBase(Enchantment enchantment) {
        return fromVanilla.get(enchantment);
    }

    public int getPriceForLevel(int level) {
        return tokensPerLevel * level;
    }
}
