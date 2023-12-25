package dev.elijuh.fishing.commands.impl;

import com.google.common.collect.ImmutableList;
import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.commands.Command;
import dev.elijuh.fishing.user.User;
import dev.elijuh.fishing.user.UserData;
import dev.elijuh.fishing.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.List;

/**
 * @author elijuh
 */
public class SetFishingStatCommand extends Command {

    public SetFishingStatCommand() {
        super("setfishingstat", ImmutableList.of(), "fishing.admin");
        setUsage(Text.color("&cUsage: /setfishingstat <key> <amount> <+,-,=> [player]"));
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage(getUsage());
            return;
        }
        Player target = args.length > 3 ? Bukkit.getPlayerExact(args[3]) : p;
        if (args.length > 3 && target == null) {
            p.sendMessage(Text.color("&c") + "That player is not online.");
            return;
        }
        String key = args[0];
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            p.sendMessage(Text.color("&cInvalid integer: &7") + args[1]);
            return;
        }
        char op = args[2].charAt(0);
        User user = Core.i().getUser(target);
        if (!operation(user.getUserData(), key, amount, op)) {
            p.sendMessage(Text.color("&cInvalid operation: &7" + op));
        }
        p.sendMessage(Text.prefixed("&7You have set &a" + target.getName() + "'s &e" + key + " &7to &a" +
            NumberFormat.getInstance().format(user.getUserData().getStatistic(key))));
    }

    private boolean operation(UserData data, String key, int amount, char c) {
        switch (c) {
            case '+': {
                data.incrementStatistic(key, amount);
                break;
            }
            case '-': {
                data.incrementStatistic(key, -amount);
                break;
            }
            case '=': {
                data.setStatistic(key, amount);
                break;
            }
            default: return false;
        }
        return true;
    }
}
