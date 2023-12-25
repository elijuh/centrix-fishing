package dev.elijuh.fishing.leaderboard;

import dev.elijuh.fishing.Core;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author elijuh
 */
@Getter
public class Leaderboard {
    private final String title, key;
    private final int size;
    private final LeaderboardHologram hologram;

    public Leaderboard(String title, String key, int size) {
        this.title = title;
        this.key = key;
        this.size = size;
        this.hologram = new LeaderboardHologram(title, size, Core.i().getLocations().getLocation("hologram-" + key));
    }

    public void update() {
        LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();

        Map<UUID, Integer> lb = Core.i().getStorage().getDao().getLeaderboard(key, size).join();
        Map<UUID, String> names = Core.i().getStorage().getDao().getNames(lb.keySet()).join();
        lb.forEach((uuid, value) -> stats.put(names.get(uuid), value));

        Bukkit.getScheduler().runTask(Core.i(), () -> hologram.refresh(stats));
    }
}
