package dev.elijuh.fishing.rod;

import com.google.common.base.Preconditions;
import dev.elijuh.fishing.fish.FishRarity;
import dev.elijuh.fishing.fish.RarityChanceModifier;
import dev.elijuh.fishing.utils.item.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
@RequiredArgsConstructor
public class RodData {
    private static final RarityChanceModifier LUCK_MODIFIER_BASE = RarityChanceModifier.builder()
        .withModifier(FishRarity.UNCOMMON, 2f)
        .withModifier(FishRarity.RARE, 2f)
        .withModifier(FishRarity.EPIC, 3f)
        .withModifier(FishRarity.LEGENDARY, 4f)
        .withModifier(FishRarity.MYTHICAL, 4f)
        .build();

    private final Map<RodUpgrade, Integer> upgrades = new HashMap<>();
    private final String name;

    public int getUpgradeLevel(RodUpgrade upgrade) {
        return upgrades.getOrDefault(upgrade, 0);
    }

    public void setUpgradeLevel(RodUpgrade upgrade, int level) {
        upgrades.put(upgrade, Math.min(Math.max(level, 0), upgrade.getMaxValue()));
    }

    public RarityChanceModifier getModifier() {
        int luck = getUpgradeLevel(RodUpgrade.INCREASED_LUCK);
        if (luck < 1) return RarityChanceModifier.empty();

        return RarityChanceModifier.allOf(LUCK_MODIFIER_BASE,
            RarityChanceModifier.builder()
                .withModifier(FishRarity.values(), luck)
                .build()
        );
    }

    public ItemStack toItem() {
        ItemBuilder builder = ItemBuilder.create(Material.FISHING_ROD)
            .name(name)
            .flag(ItemFlag.HIDE_ENCHANTS)
            .unbreakable(true)
            .lore(" ");

        if (!upgrades.isEmpty()) {
            builder.lore("&6&lUpgrades");
            for (Map.Entry<RodUpgrade, Integer> entry : upgrades.entrySet()) {
                RodUpgrade upgrade = entry.getKey();
                builder.enchant(upgrade.getVanillaBase(), entry.getValue());
                builder.lore("&6┃ &7" + upgrade.getDisplay() + " &a" + entry.getValue() * 20);
            }
        }

        return builder.build();
    }

    public static RodData fromItem(ItemStack item) {
        Preconditions.checkArgument(item.getType() == Material.FISHING_ROD);

        RodData data = new RodData(item.getItemMeta().getDisplayName());

        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            RodUpgrade upgrade = RodUpgrade.fromVanillaBase(entry.getKey());
            if (upgrade != null) {
                data.setUpgradeLevel(upgrade, entry.getValue());
            }
        }

        return data;
    }
}
