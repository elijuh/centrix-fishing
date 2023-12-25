package dev.elijuh.fishing.menu.impl;

import dev.elijuh.fishing.fish.FishRarity;
import dev.elijuh.fishing.menu.Menu;
import dev.elijuh.fishing.utils.Text;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * @author elijuh
 */
@Getter
public class RarityInfoMenu extends Menu {
    private static final short[] DYE_COLOR = {8, 10, 12, 5, 14, 11};
    @Getter
    private static final RarityInfoMenu instance = new RarityInfoMenu();
    private final Inventory inventory;

    private RarityInfoMenu() {
        this.inventory = Bukkit.createInventory(this, 27, "Rarity Info");

        fill();
        int index = 0, slot = 10;
        for (FishRarity rarity : FishRarity.values()) {
            if (slot == 13) {
                slot++;
            }
            inventory.setItem(slot++, ItemBuilder.create(Material.INK_SACK)
                .dura(DYE_COLOR[index++])
                .name(rarity.getColor() + rarity.getDisplay())
                .lore("&8┃ &7Average Worth: &a$" + Text.getCurrencyFormat().format(rarity.getAverageFishPrice()))
                .lore("&8┃ &7Token Reward: &b" + rarity.getTokenValue() + "⛁")
                .lore("&8┃ &7Catch Chance: &f" + rarity.getBaseChance() + "x")
                .build()
            );
        }
    }

    @Override
    public void onClickEvent(InventoryClickEvent e) {
        e.setCancelled(true);
    }
}
