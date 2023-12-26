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
        if (args.length < 1) return;

        if (args[0].equalsIgnoreCase("setlocation") && args.length > 1) {
            Core.i().getLocations().setLocation(args[1], p.getLocation());
            Core.i().getLocations().save();
            p.sendMessage(Text.prefixed("&e" + args[1] + " &7has been set to your location."));
        } else if (args[0].equalsIgnoreCase("maintenance") && args.length > 1) {
            if (args[1].equalsIgnoreCase("toggle")) {
                Core.i().getMaintenanceManager().toggle();
                boolean enabled = Core.i().getMaintenanceManager().isEnabled();
                p.sendMessage(Text.prefixed("&7Maintenance mode has been " + (enabled ? "&aEnabled" : "&cDisabled")));
            } else if (args[1].equalsIgnoreCase("add") && args.length > 2) {
                Core.i().getMaintenanceManager().whitelist(args[2]);
                p.sendMessage(Text.prefixed("&e" + args[2] + " &7has been added to the whitelist (case-sensitive)."));
            } else if (args[1].equalsIgnoreCase("remove") && args.length > 2) {
                Core.i().getMaintenanceManager().unwhitelist(args[2]);
                p.sendMessage(Text.prefixed("&e" + args[2] + " &7has been removed from the whitelist (case-sensitive)."));
            }
        }
    }
}
