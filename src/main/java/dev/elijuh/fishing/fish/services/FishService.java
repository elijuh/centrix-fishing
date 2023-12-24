package dev.elijuh.fishing.fish.services;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.fishing.fish.Bait;
import dev.elijuh.fishing.fish.Fish;
import dev.elijuh.fishing.fish.FishRarity;
import dev.elijuh.fishing.fish.FishType;
import dev.elijuh.fishing.fish.factory.FishItemFactory;
import dev.elijuh.fishing.utils.Text;
import dev.elijuh.fishing.utils.item.ItemBuilder;

/**
 * @author elijuh
 */
public class FishService {

    public Fish randomCatch(Bait bait) {
        FishType type = BaitService.getInstance().catchType(bait);
        int grams = type.randomWeight();

        return new Fish(type, grams);
    }
}
