package dev.elijuh.fishing.commands.impl;

import com.google.common.collect.ImmutableList;
import dev.elijuh.fishing.commands.Command;
import dev.elijuh.fishing.fish.FishRarity;
import dev.elijuh.fishing.utils.Text;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author elijuh
 */
public class TestCatchSoundCommand extends Command {

    public TestCatchSoundCommand() {
        super("testcatchsound", ImmutableList.of(), "fishing.admin");
        setUsage(Text.color("cUsage: /testcatchsound <rarity>"));
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return args.length == 1 ? Arrays.stream(FishRarity.values())
            .map(Enum::name)
            .filter(name -> StringUtil.startsWithIgnoreCase(name, args[0]))
            .collect(Collectors.toList())
            : ImmutableList.of();
    }

    @Override
    public void onExecute(Player p, String[] args) {
        if (args.length < 1) {
            p.sendMessage(getUsage());
            return;
        }
        FishRarity rarity;
        try {
            rarity = FishRarity.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            p.sendMessage(Text.color("&cInvalid rarity: &7") + args[0]);
            return;
        }
        rarity.playCatchSound(p);
    }
}
