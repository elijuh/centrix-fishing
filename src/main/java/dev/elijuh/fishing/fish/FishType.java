package dev.elijuh.fishing.fish;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author elijuh
 */
@Getter
public enum FishType {
    BASS(0, "Bass", FishRarity.COMMON, 1500, 3000),
    COD(1, "Cod", FishRarity.COMMON, 1000, 3500),
    SALMON(2, "Salmon", FishRarity.COMMON, 2000, 5000),
    TROUT(3, "Trout", FishRarity.COMMON, 500, 3500),
    SARDINE(4, "Sardine", FishRarity.COMMON, 100, 250),
    TUNA(5, "Tuna", FishRarity.UNCOMMON, 10000, 30000),
    CATFISH(6, "Catfish", FishRarity.UNCOMMON, 500, 5000),
    PUFFERFISH(7, "Pufferfish", FishRarity.UNCOMMON, 2000, 5000),
    PERCH(8, "Perch", FishRarity.RARE, 200, 1000),
    SNAPPER(9, "Snapper", FishRarity.RARE, 2500, 7500),
    CLOWNFISH(10, "Clownfish", FishRarity.RARE, 100, 500),
    HALIBUT(11, "Halibut", FishRarity.EPIC, 10000, 20000),
    FLOUNDER(12, "Flounder", FishRarity.EPIC, 200, 2000),
    SWORDFISH(13, "Swordfish", FishRarity.EPIC, 50000, 65000),
    CARP(14, "Carp", FishRarity.LEGENDARY, 2000, 10000),
    PIKE(15, "Pike", FishRarity.LEGENDARY, 1000, 5000),
    GROUPER(16, "Grouper", FishRarity.LEGENDARY, 2000, 10000),
    EEL(17, "Eel", FishRarity.MYTHICAL, 1000, 5000),
    REDFISH(18, "Redfish", FishRarity.MYTHICAL, 500, 2500),
    TILEFISH(19, "Tilefish", FishRarity.MYTHICAL, 7500, 15000),
    ;

    private final int id;
    private final String display;
    private final FishRarity rarity;
    private final int minWeight, maxWeight;
    private final float pricePerGram;

    FishType(int id, String display, FishRarity rarity, int minWeight, int maxWeight) {
        this.id = id;
        this.display = display;
        this.rarity = rarity;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.pricePerGram = rarity.getAverageFishPrice() / ((minWeight + maxWeight) / 2f);
    }

    public static FishType getDefault() {
        return COD;
    }

    private static final Map<Integer, FishType> ID_MAP = new HashMap<>();

    static {
        for (FishType type : values()) {
            ID_MAP.put(type.id, type);
        }
    }

    public static FishType byId(int id) {
        return ID_MAP.get(id);
    }

    public int randomWeight() {
        return ThreadLocalRandom.current().nextInt(minWeight, maxWeight);
    }
}
