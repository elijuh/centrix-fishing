package dev.elijuh.fishing.fish.services;

import dev.elijuh.fishing.fish.Bait;
import dev.elijuh.fishing.fish.FishType;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
public class BaitService {
    @Getter
    private static final BaitService instance = new BaitService();

    private final Map<Bait, Map<FishType, Float>> chances = new HashMap<>();

    public BaitService() {
        Map<FishType, Float> wormChances = new HashMap<>();
        wormChances.put(FishType.COD, 20f);
        chances.put(Bait.WORM, wormChances);
    }

    private float getChance(Bait bait, FishType type) {
        if (!chances.containsKey(bait)) return type.getRarity().getBaseChance();
        return chances.get(bait).getOrDefault(type, type.getRarity().getBaseChance());
    }

    public FishType catchType(Bait bait) {
        FishType[] types = FishType.values();
        double bound = Arrays.stream(types).mapToDouble(type -> getChance(bait, type)).sum();
        double i = bound * Math.random(), j = 0;
        FishType last = FishType.getDefault();
        for (FishType type : types) {
            if (j <= i) {
                last = type;
            } else break;
            j += getChance(bait, type);
        }
        return last;
    }
}
