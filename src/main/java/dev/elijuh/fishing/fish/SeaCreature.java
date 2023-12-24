package dev.elijuh.fishing.fish;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * @author elijuh
 */

@Getter
public abstract class SeaCreature {
    protected final int weight;

    public SeaCreature(int weight) {
        this.weight = weight;
    }

    public abstract ItemStack getItem();
}
