package dev.elijuh.fishing.fish.services;

import dev.elijuh.fishing.fish.Bait;
import dev.elijuh.fishing.fish.FishRarity;
import dev.elijuh.fishing.fish.RarityChanceModifier;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
public class BaitService {
    @Getter
    private static final BaitService instance = new BaitService();

    private final Map<Bait, RarityChanceModifier> chances = new HashMap<>();

    public BaitService() {
        chances.put(Bait.WORM, RarityChanceModifier.builder()
            .withModifier(FishRarity.UNCOMMON, 2f).build()
        );
    }

    public RarityChanceModifier getModifier(Bait bait) {
        return chances.getOrDefault(bait, RarityChanceModifier.empty());
    }
}
