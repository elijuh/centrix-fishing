package dev.elijuh.fishing.fish.shark;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.fishing.fish.SeaCreature;
import dev.elijuh.fishing.fish.factory.SharkItemFactory;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * @author elijuh
 */
@Getter
public class Shark extends SeaCreature {
    private final SharkType type;
    private final ItemStack item;

    public Shark(SharkType type, int weight) {
        super(weight);
        this.type = type;

        ItemBuilder builder = SharkItemFactory.getInstance().fromType(type);

        NBTItem nbt = new NBTItem(builder.build());

        item = nbt.getItem();
    }
}
