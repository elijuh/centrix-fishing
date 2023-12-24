package dev.elijuh.fishing.user;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author elijuh
 */
@Getter
public class UserData {
    private final Map<String, Integer> stats = new HashMap<>();

    public void setStatistic(String key, int value) {
        stats.put(key, value);
    }

    public void incrementStatistic(String key, int value) {
        stats.put(key, getStatistic(key) + value);
    }

    public int getStatistic(String key) {
        return stats.getOrDefault(key, 0);
    }
}
