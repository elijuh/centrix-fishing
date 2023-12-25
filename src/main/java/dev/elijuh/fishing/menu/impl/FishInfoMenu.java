package dev.elijuh.fishing.menu.impl;

import dev.elijuh.fishing.fish.FishRarity;
import dev.elijuh.fishing.fish.FishType;
import dev.elijuh.fishing.fish.factory.FishItemFactory;
import dev.elijuh.fishing.menu.Menu;
import dev.elijuh.fishing.utils.Text;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author elijuh
 */
@Getter
public class FishInfoMenu extends Menu {
    @Getter
    private static final FishInfoMenu instances = new FishInfoMenu();
    private final Inventory inventory;

    private FishInfoMenu() {
        this.inventory = Bukkit.createInventory(this, 54, "Fishes");

        fillEdges();

        int[] slots = getCenterSlots();
        int slotIndex = 0;
        for (FishType type : FishType.values()) {
            if (slotIndex >= slots.length) break;

            FishRarity rarity = type.getRarity();
            ItemStack item = FishItemFactory.getInstance().fromType(type)
                .name(rarity.getColor() + rarity.getDisplay() + " &8┃ &f" + type.getDisplay())
                .lore(" ")
                .lore("&8┃ &7Weight Range: &f" + Text.formatGrams(type.getMinWeight()) + " - " + Text.formatGrams(type.getMaxWeight()))
                .lore("&8┃ &7Value per kg: &a$" + Text.getCurrencyFormat().format(type.getPricePerGram() * 1000))
                .build();

            inventory.setItem(slots[slotIndex++], item);
        }

        inventory.setItem(53, ItemBuilder.create(Material.NETHER_STAR)
            .name("&aRarity Info")
            .lore(" ")
            .lore("&7Left-Click &8┃ &aOpen")
            .build());
    }

    @Override
    public void onClickEvent(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getRawSlot() == 53) {
            RarityInfoMenu.getInstance().open((Player) e.getWhoClicked());
        }
    }
}
