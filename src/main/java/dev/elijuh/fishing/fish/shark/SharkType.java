package dev.elijuh.fishing.fish.shark;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
@RequiredArgsConstructor
@Getter
public enum SharkType {
    GREAT_WHITE_SHARK(0, 1000000, 2500000, 0.01f),
    HAMMERHEAD_SHARK(1, 200000, 500000, 0.01f),
    TIGER_SHARK(2, 500000, 1000000, 0.01f),
    BULL_SHARK(3, 300000, 600000, 0.01f),
    MAKO_SHARK(4, 100000, 300000, 0.01f),
    BLUE_SHARK(5, 50000, 150000, 0.01f),
    LEMON_SHARK(6, 100000, 200000, 0.01f),
    NURSE_SHARK(7, 50000, 150000, 0.01f),
    BLACKTIP_SHARK(8, 40000, 80000, 0.01f),
    OCEANIC_WHITETIP_SHARK(9, 70000, 130000, 0.01f),
    GALAPAGOS_SHARK(10, 80000, 150000, 0.01f),
    SILKY_SHARK(11, 60000, 110000, 0.01f),
    THRESHER_SHARK(12, 200000, 500000, 0.01f),
    SANDBAR_SHARK(13, 100000, 200000, 0.01f),
    GOBLIN_SHARK(14, 50000, 100000, 0.01f),
    PORBEAGLE_SHARK(15, 90000, 180000, 0.01f),
    SHORTFIN_MAKO_SHARK(16, 100000, 250000, 0.01f),
    SIXGILL_SHARK(17, 200000, 600000, 0.01f),
    LEOPARD_SHARK(18, 5000, 20000, 0.01f),
    SOUPFIN_SHARK(19, 10000, 30000, 0.01f),
    ANGEL_SHARK(20, 10000, 30000, 0.01f),
    ZEBRA_SHARK(21, 5000, 15000, 0.01f),
    MEGAMOUTH_SHARK(22, 1500000, 3000000, 0.01f),
    GREENLAND_SHARK(23, 400000, 800000, 0.01f),
    COW_SHARK(24, 100000, 300000, 0.01f),
    BLACKTIP_REEF_SHARK(25, 30000, 80000, 0.01f),
    ;

    private final int id;
    private final float minWeight, maxWeight, pricePerKg;

    public static SharkType getDefault() {
        return HAMMERHEAD_SHARK;
    }

    private static final Map<Integer, SharkType> ID_MAP = new HashMap<>();

    static {
        for (SharkType type : values()) {
            ID_MAP.put(type.id, type);
        }
    }

    public static SharkType byId(int id) {
        return ID_MAP.get(id);
    }
}
