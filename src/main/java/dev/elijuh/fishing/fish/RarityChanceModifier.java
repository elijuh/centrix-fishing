package dev.elijuh.fishing.fish;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
public class RarityChanceModifier {
    private static final RarityChanceModifier EMPTY = new RarityChanceModifier(ImmutableMap.of());
    private final Map<FishRarity, Float> modifiers;

    private RarityChanceModifier(Map<FishRarity, Float> modifiers) {
        this.modifiers = modifiers;
    }

    public float modify(FishRarity rarity, float chance) {
        Float mod = modifiers.get(rarity);
        return mod == null ? chance : chance * mod;
    }

    public float get(FishRarity rarity) {
        return modifiers.getOrDefault(rarity, 1f);
    }

    public static RarityChanceModifier allOf(RarityChanceModifier... modifiers) {
        Map<FishRarity, Float> map = new HashMap<>();
        for (RarityChanceModifier modifier : modifiers) {
            for (FishRarity rarity : FishRarity.values()) {
                float chance = map.getOrDefault(rarity, rarity.getBaseChance());
                map.put(rarity, modifier.modify(rarity, chance));
            }
        }
        return new RarityChanceModifier(map);
    }

    public static RarityChanceModifier empty() {
        return EMPTY;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<FishRarity, Float> modifiers = new HashMap<>();

        private Builder() {
        }

        public Builder withModifier(FishRarity rarity, float modifier) {
            this.modifiers.put(rarity, modifier);
            return this;
        }

        public Builder withModifier(FishRarity[] rarities, float modifier) {
            for (FishRarity rarity : rarities) {
                this.modifiers.put(rarity, modifier);
            }
            return this;
        }

        public RarityChanceModifier build() {
            return new RarityChanceModifier(modifiers);
        }
    }
}
