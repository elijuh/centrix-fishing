package dev.elijuh.fishing.leaderboard;

import dev.elijuh.fishing.Core;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author elijuh
 */
public class LeaderboardManager {
    private final List<Leaderboard> leaderboards = Collections.synchronizedList(new ArrayList<>());

    public LeaderboardManager() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Core.i(), () ->
            leaderboards.forEach(Leaderboard::update), 1200L, 1200L
        );
    }

    public void register(Leaderboard... leaderboards) {
        this.leaderboards.addAll(Arrays.asList(leaderboards));
        for (Leaderboard leaderboard : leaderboards) {
            leaderboard.update();
        }
    }

    public void shutdown() {
        if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            HolographicDisplaysAPI.get(Core.i()).deleteHolograms();
        }
        leaderboards.clear();
    }
}
