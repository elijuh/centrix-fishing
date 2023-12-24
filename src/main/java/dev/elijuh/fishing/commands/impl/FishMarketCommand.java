package dev.elijuh.fishing.commands.impl;

import com.google.common.collect.ImmutableList;
import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.commands.Command;
import dev.elijuh.fishing.menu.impl.FishMarketMenu;
import dev.elijuh.fishing.utils.Text;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author elijuh
 */
public class FishMarketCommand extends Command {
    public FishMarketCommand() {
        super("fishmarket", ImmutableList.of(), null);
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (!Core.getTestingWhitelist().contains(p.getName())) {
            p.sendMessage(Text.prefixed("&7This feature hasn't been released to public yet!"));
            return;
        }
        new FishMarketMenu(p).open(p);
    }
}
