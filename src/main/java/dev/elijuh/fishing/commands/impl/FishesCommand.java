package dev.elijuh.fishing.commands.impl;

import com.google.common.collect.ImmutableList;
import dev.elijuh.fishing.commands.Command;
import dev.elijuh.fishing.menu.impl.FishInfoMenu;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author elijuh
 */
public class FishesCommand extends Command {

    public FishesCommand() {
        super("fishes", ImmutableList.of("fishinfo"), null);
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        FishInfoMenu.getInstances().open(p);
    }
}
