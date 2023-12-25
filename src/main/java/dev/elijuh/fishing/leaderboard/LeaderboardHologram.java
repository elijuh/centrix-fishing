package dev.elijuh.fishing.leaderboard;

import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.utils.Text;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.HologramLines;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author elijuh
 */
public class LeaderboardHologram {
    private final String title;
    private final int size;
    private final Hologram hologram;

    public LeaderboardHologram(String title, int size, Location location) {
        this.title = Text.color(title);
        this.size = size;
        if (location == null) {
            hologram = null;
            return;
        }
        hologram = HolographicDisplaysAPI.get(Core.i()).createHologram(location);
    }

    public void refresh(LinkedHashMap<String, Integer> entries) {
        if (hologram == null) return;

        HologramLines lines = hologram.getLines();
        lines.clear();
        lines.appendText(title);

        ArrayList<Map.Entry<String, Integer>> indexed = new ArrayList<>(entries.entrySet());

        for (int i = 0; i < size; i++) {
            int pos = i + 1;
            if (i >= indexed.size()) {
                lines.appendText(Text.color("&6#" + pos + " &8┃ &7Empty"));
                continue;
            }
            Map.Entry<String, Integer> entry = indexed.get(i);
            String name = entry.getKey() == null ? "N/A" : entry.getKey();
            lines.appendText(Text.color("&6#" + pos + " &8┃ &e" + name + " &8» &f" + Text.getFormat().format(entry.getValue())));
        }
    }
}
