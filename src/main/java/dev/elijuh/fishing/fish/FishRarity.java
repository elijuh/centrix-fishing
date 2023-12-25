package dev.elijuh.fishing.fish;

import dev.elijuh.fishing.animations.impl.sound.NoteAnimation;
import dev.elijuh.fishing.animations.impl.sound.impl.*;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

/**
 * @author elijuh
 */

@Getter
public enum FishRarity {
    COMMON("&7", "Common", 1f, 1f, 1, FishCatchSoundCommon.class),
    UNCOMMON("&a", "Uncommon", 0.25f, 2.5f, 2, FishCatchSoundUncommon.class),
    RARE("&b", "Rare", 0.1f, 5f, 5, FishCatchSoundRare.class),
    EPIC("&5", "Epic", 0.05f, 10f, 10, FishCatchSoundEpic.class),
    LEGENDARY("&6", "Legendary", 0.02f, 25f, 20, FishCatchSoundLegendary.class),
    MYTHICAL("&e&l", "Mythical", 0.01f, 50f, 30, FishCatchSoundMythical.class);

    private final String color, display;
    private final float baseChance, averageFishPrice;
    private final int tokenValue;
    private final Constructor<? extends NoteAnimation> sound;

    FishRarity(String color, String display, float baseChance, float averageFishPrice, int tokenValue, Class<? extends NoteAnimation> sound) {
        this.color = color;
        this.display = display;
        this.baseChance = baseChance;
        this.averageFishPrice = averageFishPrice;
        this.tokenValue = tokenValue;
        try {
            this.sound = sound.getConstructor(Player.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void playCatchSound(Player p) {
        try {
            sound.newInstance(p).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
