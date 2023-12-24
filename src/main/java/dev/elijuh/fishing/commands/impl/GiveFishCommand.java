package dev.elijuh.fishing.commands.impl;

import com.google.common.collect.ImmutableList;
import dev.elijuh.fishing.commands.Command;
import dev.elijuh.fishing.fish.Fish;
import dev.elijuh.fishing.fish.FishType;
import dev.elijuh.fishing.utils.NumberUtil;
import dev.elijuh.fishing.utils.Text;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author elijuh
 */
public class GiveFishCommand extends Command {
    public GiveFishCommand() {
        super("givefish", ImmutableList.of(), "fishing.admin");
        setUsage(Text.color("&cUsage: /givefish <type> [amount]"));
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return args.length == 1 ? Arrays.stream(FishType.values())
            .map(Enum::name)
            .filter(name -> StringUtil.startsWithIgnoreCase(name, args[0]))
            .collect(Collectors.toList())
            : ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(getUsage());
            return;
        }
        FishType type;
        try {
            type = FishType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            p.sendMessage(Text.color("&cInvalid fish type: &7") + args[0]);
            return;
        }
        int amount = args.length > 1 ? Math.min(Math.max(NumberUtil.parseInt(args[1], 1), 1), 64) : 1;

        ItemStack item = new Fish(type, type.getMaxWeight()).getItem();
        item.setAmount(amount);
        p.getInventory().addItem(item);
        p.sendMessage(Text.prefixed("&aYou have been given &7" + amount + "x &r" + item.getItemMeta().getDisplayName() + "&a."));
    }
}
