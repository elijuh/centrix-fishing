package dev.elijuh.fishing.commands.impl;

import com.google.common.collect.ImmutableList;
import dev.elijuh.fishing.commands.Command;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.List;

/**
 * @author elijuh
 */
public class MaxRodCommand extends Command {
    public MaxRodCommand() {
        super("maxrod", ImmutableList.of(), "fishing.admin");
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        p.getInventory().addItem(ItemBuilder.create(Material.FISHING_ROD)
            .name("&6&lLegendary &8┃ &fAquastrike Rod")
            .lore(" ")
            .lore("&6┃ &7Fishing Speed &a160")
            .lore(" ")
            .lore("&8(&bInfinite &7Uses Left&8)")
            .enchant(Enchantment.LURE, 8)
            .flag(ItemFlag.HIDE_ENCHANTS)
            .unbreakable(true)
            .build()
        );
    }
}
