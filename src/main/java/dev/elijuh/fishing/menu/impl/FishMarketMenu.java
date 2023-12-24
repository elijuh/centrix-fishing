package dev.elijuh.fishing.menu.impl;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.fishing.fish.Fish;
import dev.elijuh.fishing.fish.FishType;
import dev.elijuh.fishing.menu.Menu;
import dev.elijuh.fishing.utils.PlayerUtil;
import dev.elijuh.fishing.utils.Text;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author elijuh
 */
@Getter
public class FishMarketMenu extends Menu {
    private final Inventory inventory;

    public FishMarketMenu(Player p) {
        this.inventory = Bukkit.createInventory(this, 27, "Fish Market");

        fillEdges();

        List<Fish> fishes = PlayerUtil.getFishesInInventory(p);
        float sumWorth = (float) fishes.stream().mapToDouble(fish -> fish.getType().getPricePerGram() * fish.getWeight()).sum();

        this.inventory.setItem(10, ItemBuilder.create(Material.WATER_BUCKET).name("&eSell All Fish")
            .enchant(Enchantment.LUCK, 1)
            .flag(ItemFlag.HIDE_ENCHANTS)
            .lore("&7Sells all of the fish in your inventory.")
            .lore(" ")
            .lore("&8┃ &7Fishes: " + fishes.size())
            .lore("&8┃ &7Worth: &a$" + Text.getCurrencyFormat().format(sumWorth))
                .lore(" ")
                .lore("&7Left-Click &8┃ &aSell All")
            .build()
        );
    }

    @Override
    public void onClickEvent(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getRawSlot() == 10) {
            PlayerInventory inv = e.getWhoClicked().getInventory();
            int count = 0;
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < 36; i++) {
                ItemStack item = inv.getItem(i);
                if (item == null) continue;
                NBTItem nbt = new NBTItem(item);
                if (!nbt.hasTag("fish.type") || !nbt.hasTag("fish.weight")) continue;
                Integer typeId = nbt.getInteger("fish.type");
                Integer grams = nbt.getInteger("fish.weight");
                total = total.add(BigDecimal.valueOf(FishType.byId(typeId).getPricePerGram() * grams));
                count++;
                inv.setItem(i, null);
            }
            if (count > 0) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + e.getWhoClicked().getName() + " " + total);
                e.getWhoClicked().sendMessage(Text.prefixed("&7You have sold &a" + count + " &7fish for &a$" +
                    Text.getCurrencyFormat().format(total.doubleValue())));
                e.getWhoClicked().closeInventory();
            } else {
                e.getWhoClicked().sendMessage(Text.prefixed("&7You don't have any fish to sell."));
            }
        }
    }
}
