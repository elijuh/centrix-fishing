package dev.elijuh.fishing.fish.factory;

import dev.elijuh.fishing.fish.FishType;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import dev.elijuh.fishing.utils.item.PlayerSkullBuilder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
public class FishItemFactory {
    @Getter
    private static final FishItemFactory instance = new FishItemFactory();
    private final Map<FishType, ItemBuilder> byType = new HashMap<>();

    private FishItemFactory() {
        register(FishType.BASS, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.COD, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.SALMON, PlayerSkullBuilder.create().textureUrl("cc6ea39435818cc10c984f1935dc6a54df1e547eede3789cfaafd203c9fa6d13"));
        register(FishType.TROUT, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.SARDINE, PlayerSkullBuilder.create().textureUrl("7667dab0279be55e8f71b90627e97edd89aa6dd57e62ae8371322729d1070480"));
        register(FishType.TUNA, PlayerSkullBuilder.create().textureUrl("7667dab0279be55e8f71b90627e97edd89aa6dd57e62ae8371322729d1070480"));
        register(FishType.CATFISH, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.PUFFERFISH, PlayerSkullBuilder.create().textureUrl("2dd4b96726cfd36015af3778336c5226ae12fe80ca8afeee763f4542a883c282"));
        register(FishType.PERCH, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.SNAPPER, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.CLOWNFISH, PlayerSkullBuilder.create().textureUrl("d6dd5e6addb56acbc694ea4ba5923b1b25688178feffa72290299e2505c97281"));
        register(FishType.HALIBUT, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.FLOUNDER, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.SWORDFISH, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.CARP, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.PIKE, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.GROUPER, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.EEL, PlayerSkullBuilder.create().textureUrl("7667dab0279be55e8f71b90627e97edd89aa6dd57e62ae8371322729d1070480"));
        register(FishType.REDFISH, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.TILEFISH, PlayerSkullBuilder.create().textureUrl("221253df5d455aa3e8de80a0a1b6c29dc9893089762156ee6b2b48c40adc521c"));
    }

    private void register(FishType type, ItemBuilder fish) {
        byType.put(type, fish);
    }

    public ItemBuilder fromType(FishType type) {
        return byType.get(type).clone();
    }
}
