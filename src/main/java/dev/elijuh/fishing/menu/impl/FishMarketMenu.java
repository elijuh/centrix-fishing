package dev.elijuh.fishing.menu.impl;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.animations.impl.sound.impl.FishSellSound;
import dev.elijuh.fishing.fish.Fish;
import dev.elijuh.fishing.fish.FishType;
import dev.elijuh.fishing.menu.Menu;
import dev.elijuh.fishing.user.User;
import dev.elijuh.fishing.utils.PlayerUtil;
import dev.elijuh.fishing.utils.Text;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

/**
 * @author elijuh
 */
@Getter
public class FishMarketMenu extends Menu {
    private static final NumberFormat nf = NumberFormat.getInstance();
    private final Inventory inventory;

    public FishMarketMenu(Player p) {
        this.inventory = Bukkit.createInventory(this, 27, "Fish Market");

        fillEdges();

        User user = Core.i().getUser(p);

        List<Fish> fishes = PlayerUtil.getFishesInInventory(p);
        float sumWorth = (float) fishes.stream().mapToDouble(fish -> fish.getType().getPricePerGram() * fish.getWeight()).sum();

        this.inventory.setItem(10, ItemBuilder.create(Material.WATER_BUCKET).name("&eSell All Fish")
            .enchant(Enchantment.LUCK, 1)
            .flag(ItemFlag.HIDE_ENCHANTS)
            .lore("&7Sells all of the fish in your inventory.")
            .lore(" ")
            .lore("&8┃ &7Fishes: &6" + fishes.size())
            .lore("&8┃ &7Worth: &a$" + Text.getCurrencyFormat().format(sumWorth))
            .lore(" ")
            .lore("&7Left-Click &8┃ &aSell All")
            .build()
        );

        int fishesLegacy = PlayerUtil.getLegacyFishesInInventory(p);

        this.inventory.setItem(11, ItemBuilder.create(Material.RAW_FISH).name("&bSell All Legacy Fish")
            .enchant(Enchantment.LUCK, 1)
            .flag(ItemFlag.HIDE_ENCHANTS)
            .lore("&7Have some boring old vanilla fish")
            .lore("&7from fishing before the new update?")
            .lore("&7No worries, you can sell them to me for &a$" + Text.getCurrencyFormat().format(Fish.LEGACY_FISH_VALUE) + " &7each!")
            .lore(" ")
            .lore("&8┃ &7Fishes: &6" + fishesLegacy)
            .lore("&8┃ &7Worth: &a$" + Text.getCurrencyFormat().format(fishesLegacy * Fish.LEGACY_FISH_VALUE))
            .lore(" ")
            .lore("&7Left-Click &8┃ &aSell All")
            .build()
        );

        this.inventory.setItem(12, ItemBuilder.create(Material.FISHING_ROD).name("&bFishing Rod Upgrader")
            .enchant(Enchantment.LUCK, 1)
            .flag(ItemFlag.HIDE_ENCHANTS)
            .lore("&7Increase the fun of fishing by upgrading")
            .lore("&7your &eFishing Rod &7for the fastest catches")
            .lore("&7and catching rarer fish species!")
            .lore(" ")
            .lore("&7Left-Click &8┃ &aOpen")
            .build()
        );

        this.inventory.setItem(16, ItemBuilder.create(Material.BOOK).name("&6Your Statistics")
            .lore("&8┃ &7Fish Caught: &f" + nf.format(user.getUserData().getStatistic("fishCaught")))
            .lore("&8┃ &7Junk Caught: &f" + nf.format(user.getUserData().getStatistic("junkCaught")))
            .lore("&8┃ &7Treasure Caught: &f" + nf.format(user.getUserData().getStatistic("treasureCaught")))
            .lore("&8┃")
            .lore("&8┃ &7Heaviest Catch: &f" + Text.formatGrams(user.getUserData().getStatistic("heaviestCatch")))
            .build()
        );
    }

    @Override
    public void onClickEvent(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if (e.getRawSlot() == 10) {
            PlayerInventory pInv = p.getInventory();
            int count = 0;
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < 36; i++) {
                ItemStack item = pInv.getItem(i);
                if (item == null) continue;
                NBTItem nbt = new NBTItem(item);
                if (!nbt.hasTag("fish.type") || !nbt.hasTag("fish.weight")) continue;
                Integer typeId = nbt.getInteger("fish.type");
                Integer grams = nbt.getInteger("fish.weight");
                total = total.add(BigDecimal.valueOf(FishType.byId(typeId).getPricePerGram() * grams));
                count++;
                pInv.setItem(i, null);
            }
            if (count > 0) {
                onSell(p, count, total);
            } else {
                p.sendMessage(Text.prefixed("&7You don't have any fish to sell."));
            }
        } else if (e.getRawSlot() == 11) {
            PlayerInventory pInv = p.getInventory();
            int count = 0;
            for (int i = 0; i < 36; i++) {
                ItemStack item = pInv.getItem(i);
                if (item == null || item.getType() != Material.RAW_FISH || item.hasItemMeta()) continue;
                count += item.getAmount();
                pInv.setItem(i, null);
            }
            if (count > 0) {
                onSell(p, count, BigDecimal.valueOf(count).multiply(BigDecimal.valueOf(Fish.LEGACY_FISH_VALUE)));
            } else {
                p.sendMessage(Text.prefixed("&7You don't have any legacy fish to sell."));
            }
        } else if (e.getRawSlot() == 12) {
            new RodUpgradeMenu().open(p);
        }
    }

    private void onSell(Player p, int count, Number worth) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + p.getName() + " " + worth);
        p.sendMessage(Text.prefixed("&7You have sold &a" + nf.format(count)
            + " &7fish for &a$" + Text.getCurrencyFormat().format(worth)));
        p.closeInventory();
        p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1f, 1f);
        new FishSellSound(p).start();
    }
}
