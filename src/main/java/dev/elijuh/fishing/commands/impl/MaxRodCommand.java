package dev.elijuh.fishing.commands.impl;

import com.google.common.collect.ImmutableList;
import dev.elijuh.fishing.commands.Command;
import dev.elijuh.fishing.rod.RodData;
import dev.elijuh.fishing.rod.RodUpgrade;
import dev.elijuh.fishing.utils.Text;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author elijuh
 */
public class MaxRodCommand extends Command {
    public MaxRodCommand() {
        super("maxrod", ImmutableList.of(), "fishing.admin");
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        RodData data = new RodData(Text.color("&6Maxed &fFishing Rod"));
        for (RodUpgrade upgrade : RodUpgrade.values()) {
            data.setUpgradeLevel(upgrade, upgrade.getMaxValue());
        }
        p.getInventory().addItem(data.toItem());
    }
}
