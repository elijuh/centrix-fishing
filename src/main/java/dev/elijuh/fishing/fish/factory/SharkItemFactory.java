package dev.elijuh.fishing.fish.factory;

import dev.elijuh.fishing.fish.shark.SharkType;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import dev.elijuh.fishing.utils.item.PlayerSkullBuilder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
public class SharkItemFactory {
    @Getter
    private static final SharkItemFactory instance = new SharkItemFactory();
    private final Map<SharkType, ItemBuilder> byType = new HashMap<>();

    private SharkItemFactory() {
        register(SharkType.HAMMERHEAD_SHARK, PlayerSkullBuilder.create().textureUrl("e6e6fb66bfe9737a33ba53f3d5a8b9fdba1c65a5d2760bcd9a6e54928527103b"));
        register(SharkType.GREAT_WHITE_SHARK, PlayerSkullBuilder.create().textureUrl("a94ae433b301c7fb7c68cba625b0bd36b0b14190f20e34a7c8ee0d9de06d53b9"));
    }

    private void register(SharkType type, ItemBuilder fish) {
        byType.put(type, fish);
    }

    private final PlayerSkullBuilder def = PlayerSkullBuilder.create().textureUrl("a94ae433b301c7fb7c68cba625b0bd36b0b14190f20e34a7c8ee0d9de06d53b9");

    public ItemBuilder fromType(SharkType type) {
        return byType.getOrDefault(type, def).clone();
    }
}
