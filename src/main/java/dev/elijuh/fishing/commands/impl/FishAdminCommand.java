package dev.elijuh.fishing.commands.impl;

import com.google.common.collect.ImmutableList;
import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.commands.Command;
import dev.elijuh.fishing.utils.Text;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author elijuh
 */
public class FishAdminCommand extends Command {

    public FishAdminCommand() {
        super("fishadmin", ImmutableList.of(), "fishing.admin");
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (args.length < 1) {
            return;
        }
        if (args[0].equalsIgnoreCase("setlocation") && args.length > 1) {
            Core.i().getLocations().setLocation(args[1], p.getLocation());
            Core.i().getLocations().save();
            p.sendMessage(Text.prefixed("&e" + args[1] + " &7has been set to your location."));
        }
    }
}
