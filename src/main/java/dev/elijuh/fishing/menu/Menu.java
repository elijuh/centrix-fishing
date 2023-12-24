package dev.elijuh.fishing.menu;

import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;

/**
 * @author elijuh
 */
public abstract class Menu implements InventoryHolder {
    protected final ItemStack FILLER = ItemBuilder.create(Material.STAINED_GLASS_PANE).name(" ").dura(15).build();

    protected void fill() {
        Inventory inv = getInventory();

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, FILLER);
        }
    }

    protected void fillEdges() {
        Inventory inv = getInventory();
        int rows = inv.getSize() / 9;

        for (int i = 0; i < rows; i++) {
            if (i == 0 || i == rows - 1) {
                for (int j = 0; j < 9; j++) {
                    inv.setItem(i * 9 + j, FILLER);
                }
            } else {
                inv.setItem(i * 9, FILLER);
                inv.setItem(i * 9 + 8, FILLER);
            }
        }
    }

    protected int[] getCenterSlots() {
        int size = getInventory().getSize();
        if (size < 27) return new int[0];

        int[] slots = new int[size - 18 - ((size / 9 - 2) * 2)];
        int slotIndex = 0;
        for (int i = 1; i < size / 9 - 1; i++) {
            for (int j = 1; j < 8; j++) {
                slots[slotIndex++] = i * 9 + j;
            }
        }
        return slots;
    }

    public abstract void onClickEvent(InventoryClickEvent e);

    public void open(Player p) {
        Inventory inv = getInventory();
        if (p.getOpenInventory().getTopInventory() != inv) {
            p.openInventory(inv);
        }
    }

    public void onDragEvent(InventoryDragEvent e) {
        e.setCancelled(true);
    }

    public void onCloseEvent(InventoryCloseEvent e) {

    }

    public static class Handler implements Listener {

        @EventHandler
        public void on(InventoryClickEvent e) {
            Inventory inv = e.getClickedInventory();
            if (inv == null) return;
            if (inv.getHolder() instanceof Menu) {
                Menu menu = (Menu) inv.getHolder();
                menu.onClickEvent(e);
            }
        }

        @EventHandler
        public void on(InventoryDragEvent e) {
            if (e.getInventory().getHolder() instanceof Menu) {
                Menu menu = (Menu) e.getInventory().getHolder();
                menu.onDragEvent(e);
            }
        }

        @EventHandler
        public void on(InventoryCloseEvent e) {
            if (e.getInventory().getHolder() instanceof Menu) {
                Menu menu = (Menu) e.getInventory().getHolder();
                menu.onCloseEvent(e);
            }
        }
    }
}
