package dev.elijuh.fishing.storage;

import dev.elijuh.fishing.user.UserData;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author elijuh
 */
public interface Dao {
    CompletableFuture<Void> save(UUID uuid, UserData data);

    CompletableFuture<UserData> getUserData(UUID uuid);

    CompletableFuture<Map<UUID, Integer>> getLeaderboard(String key, int size);

    CompletableFuture<Void> saveName(UUID uuid, String name);

    CompletableFuture<Map<UUID, String>> getNames(Collection<UUID> uuids);
}
