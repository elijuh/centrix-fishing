package dev.elijuh.fishing.storage;

import dev.elijuh.fishing.user.UserData;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author elijuh
 */
public interface Dao {
    CompletableFuture<Void> save(UUID uuid, UserData data);

    CompletableFuture<UserData> getUserData(UUID uuid);
}
