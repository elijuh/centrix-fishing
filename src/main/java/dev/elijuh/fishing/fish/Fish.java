package dev.elijuh.fishing.fish;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.fishing.fish.factory.FishItemFactory;
import dev.elijuh.fishing.utils.Text;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * @author elijuh
 */

@Getter
public class Fish extends SeaCreature {
    public static final double LEGACY_FISH_VALUE = 2.00;
    private final FishType type;

    public Fish(FishType type, int weight) {
        super(weight);
        this.type = type;
    }

    @Override
    public ItemStack getItem() {
        ItemBuilder builder = FishItemFactory.getInstance().fromType(type);

        FishRarity rarity = type.getRarity();
        String color = rarity.getColor();
        builder.name(color + rarity.getDisplay() + " &8┃ &f" + type.getDisplay())
            .lore(" ")
            .lore("&6┃ &7Weighs &e" + Text.formatGrams(weight))
            .lore("&6┃ &7Worth &a$" + Text.getCurrencyFormat().format(type.getPricePerGram() * weight))
            .lore(" ")
            .lore("&7Take this to the &eFishers Market &7to sell.");

        NBTItem nbt = new NBTItem(builder.build());
        nbt.setInteger("fish.weight", weight);
        nbt.setInteger("fish.type", type.getId());
        return nbt.getItem();
    }
}
