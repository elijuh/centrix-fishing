package dev.elijuh.fishing.utils;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.fishing.fish.Fish;
import dev.elijuh.fishing.fish.FishType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author elijuh
 */

@UtilityClass
public class PlayerUtil {

    public List<Fish> getFishesInInventory(Player p) {
        List<Fish> fishes = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null) continue;
            NBTItem nbt = new NBTItem(item);
            if (!nbt.hasTag("fish.type") || !nbt.hasTag("fish.weight")) continue;
            Integer typeId = nbt.getInteger("fish.type");
            Integer grams = nbt.getInteger("fish.weight");
            fishes.add(new Fish(FishType.byId(typeId), grams));
        }
        return fishes;
    }

    public int getLegacyFishesInInventory(Player p) {
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null || item.getType() != Material.RAW_FISH || item.hasItemMeta()) continue;
            amount += item.getAmount();
        }
        return amount;
    }
}
