package dev.elijuh.fishing.menu.impl;

import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.menu.Menu;
import dev.elijuh.fishing.rod.RodData;
import dev.elijuh.fishing.rod.RodUpgrade;
import dev.elijuh.fishing.user.User;
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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
@Getter
public class RodUpgradeMenu extends Menu {
    private final Inventory inventory;

    private ItemStack item;
    private RodData data;

    public RodUpgradeMenu() {
        this.inventory = Bukkit.createInventory(this, 36, "Rod Upgrades");

        fill();
        inventory.setItem(13, null);
        inventory.setItem(14, ItemBuilder.create(Material.ANVIL).name("&a⇦ &7Insert Fishing Rod").build());
    }

    @Override
    public void onClickEvent(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() instanceof PlayerInventory) {
            if (this.item != null) {
                deselect(p);
            }

            ItemStack item = e.getClickedInventory().getItem(e.getSlot());
            if (item.getType() != Material.FISHING_ROD) return;

            p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
            select(item);
            p.getInventory().setItem(e.getSlot(), null);
        } else if (e.getRawSlot() == 13) {
            p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
            deselect(p);
        } else {
            RodUpgrade upgrade = upgradeButtonSlots.get(e.getRawSlot());
            if (upgrade == null) return;
            User user = Core.i().getUser(p);
            int current = data.getUpgradeLevel(upgrade);
            int priceTokens = upgrade.getPriceForLevel(current + 1);

            if (current >= upgrade.getMaxValue() ||
                user.getUserData().getStatistic("tokens") < priceTokens) {
                p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                return;
            }
            user.getUserData().incrementStatistic("tokens", -priceTokens);
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1f, 2f);
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
            data.setUpgradeLevel(upgrade, current + 1);
            select(data.toItem());
        }
    }

    private void select(ItemStack item) {
        this.item = item;
        inventory.setItem(13, item);
        this.data = RodData.fromItem(item);
        showButtons();
    }

    private void deselect(Player p) {
        if (item == null) return;

        //TODO: add to stash on overflow
        p.getInventory().addItem(item).forEach((index, item) ->
            p.getWorld().dropItem(p.getLocation(), item)
        );

        item = null;
        this.inventory.setItem(13, null);
        hideButtons();
    }

    private final Map<Integer, RodUpgrade> upgradeButtonSlots = new HashMap<>();

    private void showButtons() {
        int slot = 19;
        for (RodUpgrade upgrade : RodUpgrade.values()) {
            int level = data.getUpgradeLevel(upgrade);
            upgradeButtonSlots.put(slot, upgrade);
            inventory.setItem(slot++, ItemBuilder.create(Material.ENCHANTED_BOOK)
                .name("&a" + upgrade.getDisplay())
                .lore("&8┃ &7" + upgrade.getDescription())
                .lore("&8┃ &7Level: &a" + level + "&7/&a" + upgrade.getMaxValue())
                .lore("&8┃")
                .lore("&8┃ &7Upgrade Price: &b" + (level >= upgrade.getMaxValue() ? "&cN/A &7(Maxed)" :
                    NumberFormat.getInstance().format(upgrade.getPriceForLevel(level + 1)) + "⛁"))
                .lore("&8┃ &7Left-Click to &aUpgrade")
                .build());
        }
    }

    private void hideButtons() {
        for (Integer slot : upgradeButtonSlots.keySet()) {
            inventory.setItem(slot, FILLER);
        }
        upgradeButtonSlots.clear();
    }
}
