package dev.elijuh.fishing.utils.item;

import dev.elijuh.fishing.utils.Text;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author elijuh
 */
public class ItemBuilder implements Cloneable {
    private ItemStack item;
    @Getter
    private ItemMeta meta;

    public static ItemBuilder create(Material material) {
        return new ItemBuilder(material);
    }

    ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder material(Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilder dura(int dura) {
        this.item.setDurability((short) dura);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.item.setAmount(Math.min(amount, 64));
        return this;
    }

    public ItemBuilder name(String name) {
        this.meta.setDisplayName(Text.color(name));
        return this;
    }

    public ItemBuilder lore(String line) {
        if (line.isEmpty()) return this;

        List<String> lore = this.meta.getLore() != null ? this.meta.getLore() : new ArrayList<>();
        lore.add(Text.color("&r" + line));
        this.meta.setLore(lore);
        return this;
    }

    public ItemBuilder lore(Collection<String> lines) {
        for (String line : lines) {
            this.lore(line);
        }
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        this.meta.addEnchant(enchant, level, true);
        return this;
    }

    public ItemBuilder flag(ItemFlag flag) {
        meta.addItemFlags(flag);
        return this;
    }

    public ItemBuilder color(int hex) {
        LeatherArmorMeta lMeta = (LeatherArmorMeta) meta;
        lMeta.setColor(Color.fromRGB(hex));
        return this;
    }

    public ItemBuilder unbreakable(boolean hide) {
        meta.spigot().setUnbreakable(true);
        if (hide) {
            flag(ItemFlag.HIDE_UNBREAKABLE);
        }
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return item;
    }

    public ItemBuilder clone() {
        try {
            ItemBuilder itemBuilder = (ItemBuilder) super.clone();
            if (this.item != null) {
                itemBuilder.item = this.item.clone();
            }
            if (this.meta != null) {
                itemBuilder.meta = this.meta.clone();
            }

            return itemBuilder;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}