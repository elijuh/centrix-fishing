package dev.elijuh.fishing.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author elijuh
 */

@UtilityClass
public class DataUtil {

    public <K, V extends Comparable<?>> LinkedHashMap<K, V> sortMap(Map<K, V> map) {
        return sortMap(map, map.size());
    }

    public <K, V extends Comparable<?>> LinkedHashMap<K, V> sortMap(Map<K, V> map, int limit) {
        LinkedHashMap<K, V> newMap = new LinkedHashMap<>();
        ArrayList<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());
        entries.sort(null);
        for (int i = entries.size() - 1; i >= 0; i--) {
            if (limit-- < 1) break;

            Map.Entry<K, V> entry = entries.get(i);
            newMap.put(entry.getKey(), entry.getValue());
        }
        return newMap;
    }
}