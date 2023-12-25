package dev.elijuh.fishing.fish.services;

import dev.elijuh.fishing.fish.Bait;
import dev.elijuh.fishing.fish.Fish;
import dev.elijuh.fishing.fish.FishType;
import dev.elijuh.fishing.fish.RarityChanceModifier;
import dev.elijuh.fishing.rod.RodData;

import java.util.Arrays;

/**
 * @author elijuh
 */
public class FishService {

    public Fish randomCatch(Bait bait, RodData rod) {
        RarityChanceModifier baitMod = BaitService.getInstance().getModifier(bait);

        FishType type = randomType(RarityChanceModifier.allOf(baitMod, rod.getModifier()));
        int grams = type.randomWeight();

        return new Fish(type, grams);
    }

    public FishType randomType(RarityChanceModifier modifier) {
        FishType[] types = FishType.values();
        double bound = Arrays.stream(types).mapToDouble(type -> modifier.get(type.getRarity())).sum();
        double i = bound * Math.random(), j = 0;
        FishType last = FishType.getDefault();
        for (FishType type : types) {
            if (j <= i) {
                last = type;
            } else break;
            j += modifier.get(type.getRarity());
        }
        return last;
    }
}
