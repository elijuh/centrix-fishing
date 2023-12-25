package dev.elijuh.fishing.user;

import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author elijuh
 */
@Getter
public class UserData {
    private final Map<String, Integer> stats = new HashMap<>();
    private final Set<String> updatedKeys = new HashSet<>();

    public void setStatistic(String key, int value) {
        stats.put(key, value);
        updatedKeys.add(key);
    }

    public void incrementStatistic(String key, int value) {
        stats.put(key, getStatistic(key) + value);
        updatedKeys.add(key);
    }

    public int getStatistic(String key) {
        return stats.getOrDefault(key, 0);
    }

    public Map<String, Integer> getUpdatedStats() {
        Map<String, Integer> updatedStats = new HashMap<>(updatedKeys.size(), 1f);
        for (String key : updatedKeys) {
            updatedStats.put(key, getStatistic(key));
        }
        return updatedStats;
    }
}
