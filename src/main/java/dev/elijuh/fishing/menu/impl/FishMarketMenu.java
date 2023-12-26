package dev.elijuh.fishing.menu.impl;

import com.google.common.collect.ImmutableSet;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.animations.impl.sound.impl.FishSellSound;
import dev.elijuh.fishing.fish.Fish;
import dev.elijuh.fishing.fish.FishType;
import dev.elijuh.fishing.menu.Menu;
import dev.elijuh.fishing.user.User;
import dev.elijuh.fishing.utils.Text;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author elijuh
 */
@Getter
public class FishMarketMenu extends Menu {
    private final Inventory inventory;

    public FishMarketMenu(Player p) {
        this.inventory = Bukkit.createInventory(this, 27, "Fish Market");

        fill();

        User user = Core.i().getUser(p);

        Map<Fish, Integer> fishes = getFishesInInventory(p);
        float sumWorth = (float) fishes.entrySet().stream().mapToDouble(entry -> {
            Fish fish = entry.getKey();
            return fish.getType().getPricePerGram() * fish.getWeight() * entry.getValue();
        }).sum();

        this.inventory.setItem(10, ItemBuilder.create(fishes.isEmpty() ? Material.BUCKET : Material.WATER_BUCKET)
            .name("&eSell All Fish")
            .lore("&7Sells all of the fish in your inventory.")
            .lore(" ")
            .lore("&8┃ &7Fishes: &6" + fishes.size())
            .lore("&8┃ &7Worth: &a$" + Text.getCurrencyFormat().format(sumWorth))
            .lore(" ")
            .lore("&7Left-Click &8┃ &aSell All")
            .build()
        );

        int fishesLegacy = getLegacyFishesInInventory(p);

        this.inventory.setItem(11, ItemBuilder.create(Material.RAW_FISH).name("&bSell All Legacy Fish")
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

        int junk = getFishingJunkInInventory(p);

        this.inventory.setItem(12, ItemBuilder.create(Material.BOWL).name("&bSell All Junk")
            .lore("&7Not having the best of luck?")
            .lore("&7I'll buy your &esaddles&7, &ebowls&7,")
            .lore("&estring&7, &etripwire hooks&7, &eink sacs&7,")
            .lore("&ebones&7, &eleather&7, &elily pads&7,")
            .lore("&erotten flesh&7, and &enametags&7!")
            .lore(" ")
            .lore("&8┃ &7Junk: &6" + junk)
            .lore("&8┃ &7Worth: &a$" + Text.getCurrencyFormat().format(junk * Fish.JUNK_VALUE))
            .lore(" ")
            .lore("&7Left-Click &8┃ &aSell All")
            .build()
        );

        this.inventory.setItem(13, ItemBuilder.create(Material.FISHING_ROD).name("&aFishing Rod Upgrader")
            .lore("&7Increase the fun of fishing by upgrading")
            .lore("&7your &eFishing Rod &7for the fastest catches")
            .lore("&7and catching rarer fish species!")
            .lore(" ")
            .lore("&7Left-Click &8┃ &aOpen")
            .build()
        );

        this.inventory.setItem(14, ItemBuilder.create(Material.BOOK).name("&6Fish Information")
            .lore("&7View the information for each fish")
            .lore("&7to see how much they're worth")
            .lore("&7and how much they can weigh.")
            .lore(" ")
            .lore("&7Left-Click &8┃ &aOpen")
            .build()
        );

        NumberFormat nf = Text.getFormat();
        this.inventory.setItem(16, ItemBuilder.create(Material.PAPER).name("&6Your Statistics")
            .lore("&8┃ &7Fish Caught: &f" + nf.format(user.getUserData().getStatistic("fishCaught")))
            .lore("&8┃ &7Junk Caught: &f" + nf.format(user.getUserData().getStatistic("junkCaught")))
            .lore("&8┃ &7Treasure Caught: &f" + nf.format(user.getUserData().getStatistic("treasureCaught")))
            .lore("&8┃")
            .lore("&8┃ &7Heaviest Catch: &f" + Text.formatGrams(user.getUserData().getStatistic("heaviestCatch")))
            .lore("&8┃ &7Tokens: &b" + nf.format(user.getTokens()) + "⛁")
            .build()
        );
    }

    @Override
    public void onClickEvent(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if (e.getRawSlot() == 10) {
            sellAllFromInventory(p, "fish", item -> {
                NBTItem nbt = new NBTItem(item);
                if (!nbt.hasTag("fish.type") || !nbt.hasTag("fish.weight")) return null;
                Integer typeId = nbt.getInteger("fish.type");
                Integer grams = nbt.getInteger("fish.weight");
                return BigDecimal.valueOf(FishType.byId(typeId).getPricePerGram() * grams);
            });
        } else if (e.getRawSlot() == 11) {
            sellAllFromInventory(p, "legacy fish", item ->
                item.getType() == Material.RAW_FISH && !item.hasItemMeta() ?
                    BigDecimal.valueOf(Fish.LEGACY_FISH_VALUE) : null
            );
        } else if (e.getRawSlot() == 12) {
            sellAllFromInventory(p, "junk items", item ->
                JUNK_TYPES.contains(item.getType()) && !item.hasItemMeta() &&  item.getDurability() == 0 ?
                    BigDecimal.valueOf(Fish.JUNK_VALUE) : null
            );
        } else if (e.getRawSlot() == 13) {
            new RodUpgradeMenu(p).open(p);
        } else if (e.getRawSlot() == 14) {
            FishInfoMenu.getInstances().open(p);
        }
    }

    private void sellAllFromInventory(Player p, String called, Function<ItemStack, BigDecimal> supplier) {
        PlayerInventory pInv = p.getInventory();
        BigDecimal total = BigDecimal.ZERO;
        int count = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack item = pInv.getItem(i);
            if (item == null) continue;
            BigDecimal worth = supplier.apply(item);
            if (worth == null) continue;
            total = total.add(worth).multiply(BigDecimal.valueOf(item.getAmount()));
            count += item.getAmount();
            pInv.setItem(i, null);
        }
        if (count == 0) {
            p.sendMessage(Text.prefixed("&7You don't have any legacy fish to sell."));
            return;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + p.getName() + " " + total);
        p.sendMessage(Text.prefixed("&7You have sold &a" + Text.getFormat().format(count)
            + " &7" + called + " for &a$" + Text.getCurrencyFormat().format(total)));
        p.closeInventory();
        p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 1f, 1f);
        new FishSellSound(p).start();
    }

    private Map<Fish, Integer> getFishesInInventory(Player p) {
        Map<Fish, Integer> fishes = new HashMap<>();
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null) continue;
            NBTItem nbt = new NBTItem(item);
            if (!nbt.hasTag("fish.type") || !nbt.hasTag("fish.weight")) continue;
            Integer typeId = nbt.getInteger("fish.type");
            Integer grams = nbt.getInteger("fish.weight");
            fishes.put(new Fish(FishType.byId(typeId), grams), item.getAmount());
        }
        return fishes;
    }

    private int getLegacyFishesInInventory(Player p) {
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null || item.getType() != Material.RAW_FISH || item.hasItemMeta()) continue;
            amount += item.getAmount();
        }
        return amount;
    }
    private static final Set<Material> JUNK_TYPES = ImmutableSet.of(
        Material.SADDLE, Material.BOWL, Material.STRING, Material.TRIPWIRE_HOOK, Material.NAME_TAG,
        Material.INK_SACK, Material.BONE, Material.LEATHER, Material.ROTTEN_FLESH, Material.WATER_LILY
    );

    private int getFishingJunkInInventory(Player p) {
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null || !JUNK_TYPES.contains(item.getType()) || item.getDurability() != 0 || item.hasItemMeta()) continue;
            amount += item.getAmount();
        }
        return amount;
    }
}
