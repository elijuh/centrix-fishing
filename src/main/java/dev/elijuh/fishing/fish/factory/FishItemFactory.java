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
        register(FishType.CATFISH, PlayerSkullBuilder.create().textureUrl("cc81b4c0d8aff95399fcf0d08c2763112451141586528957de5f56dbc9d45702"));
        register(FishType.PUFFERFISH, PlayerSkullBuilder.create().textureUrl("2dd4b96726cfd36015af3778336c5226ae12fe80ca8afeee763f4542a883c282"));
        register(FishType.PERCH, PlayerSkullBuilder.create().textureUrl("36b98c5101bd4e3b4d64ad86bdcb950276713bc140c262a3cf59dcece6c93215"));
        register(FishType.SNAPPER, PlayerSkullBuilder.create().textureUrl("2755380d63688562d115b880afaf0ca274a3ef3d815aada5935bafc43905086d"));
        register(FishType.CLOWNFISH, PlayerSkullBuilder.create().textureUrl("d6dd5e6addb56acbc694ea4ba5923b1b25688178feffa72290299e2505c97281"));
        register(FishType.HALIBUT, PlayerSkullBuilder.create().textureUrl("658dc1f5f3f47cc27a31f20b9908e818681a7dc27d16a770b97ba5281f5a330"));
        register(FishType.FLOUNDER, PlayerSkullBuilder.create().textureUrl("524fb39c7d7945981acc336187a9fbb21f9f683f46c82972e9f964fd8f897c4"));
        register(FishType.SWORDFISH, PlayerSkullBuilder.create().textureUrl("f8ad56a1ac57cda6a4ec84fd99789998feec1fa74736c6cc4297f7751932a44f"));
        register(FishType.CARP, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.PIKE, PlayerSkullBuilder.create().textureUrl("79bf2ee421a8da44c58bcd6ccfb1ef9152fd7c9d83f76d3e5747eb10cb10eb5b"));
        register(FishType.GROUPER, PlayerSkullBuilder.create().textureUrl("b4857004faed252e54242a2ac0239814e02abb1a087ea95410e9e46fcf9427d2"));
        register(FishType.EEL, PlayerSkullBuilder.create().textureUrl("7667dab0279be55e8f71b90627e97edd89aa6dd57e62ae8371322729d1070480"));
        register(FishType.REDFISH, PlayerSkullBuilder.create().textureUrl("8bdc2a726b8fa580b617ef8c8065414f13364dfd7e4bc94ade4642bc0ed19c30"));
        register(FishType.TILEFISH, PlayerSkullBuilder.create().textureUrl("221253df5d455aa3e8de80a0a1b6c29dc9893089762156ee6b2b48c40adc521c"));
    }

    private void register(FishType type, ItemBuilder fish) {
        byType.put(type, fish);
    }

    public ItemBuilder fromType(FishType type) {
        return byType.get(type).clone();
    }
}
