package dev.elijuh.fishing.storage.mysql;

import dev.elijuh.fishing.storage.Dao;
import dev.elijuh.fishing.user.UserData;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author elijuh
 */
@RequiredArgsConstructor
public class MySQLDao implements Dao {
    private static final String
        INSERT_STATS = "INSERT INTO `user_stats`(`uuid`, `key`, `value`) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE `value` = ?;",
        INSERT_NAME = "INSERT INTO `names`(`uuid`, `name`) VALUES(?, ?) ON DUPLICATE KEY UPDATE `name` = ?;",
        SELECT_STATS = "SELECT * FROM `user_stats` WHERE `uuid` = ?;",
        SELECT_LEADERBOARD = "SELECT `uuid`, `value` FROM `user_stats` WHERE `key` = ? ORDER BY `value` DESC LIMIT %d;",
        SELECT_NAME = "SELECT `name` FROM `names` WHERE `uuid` = ?";
    private final MySQLStorage storage;

    private byte[] toBytes(UUID uuid) {
        return ByteBuffer.wrap(new byte[16])
            .putLong(uuid.getMostSignificantBits())
            .putLong(uuid.getLeastSignificantBits())
            .array();
    }

    private UUID fromBytes(byte[] bytes) {
        if (bytes.length != 16) throw new IllegalArgumentException("bytes must have a length of 16");
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        return new UUID(wrap.getLong(), wrap.getLong());
    }

    public CompletableFuture<Void> save(UUID uuid, UserData data) {
        byte[] bytes = toBytes(uuid);
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = storage.getDataSource().getConnection()) {
                CompletableFuture.allOf(data.getUpdatedStats().entrySet().stream().map(entry -> {
                    String key = entry.getKey();
                    int value = entry.getValue();
                    return CompletableFuture.runAsync(() -> {
                        try {
                            PreparedStatement statement = connection.prepareStatement(INSERT_STATS);
                            statement.setBytes(1, bytes);
                            statement.setString(2, key);
                            statement.setInt(3, value);
                            statement.setInt(4, value);
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                }).toArray(CompletableFuture[]::new)).join();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<UserData> getUserData(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            UserData data = new UserData();
            storage.use(connection -> {
                PreparedStatement statement = connection.prepareStatement(SELECT_STATS);
                statement.setBytes(1, toBytes(uuid));
                ResultSet row = statement.executeQuery();
                while (row.next()) {
                    String key = row.getString(2);
                    int value = row.getInt(3);
                    data.setStatistic(key, value);
                }
            });
            return data;
        });
    }

    @Override
    public CompletableFuture<Map<UUID, Integer>> getLeaderboard(String key, int size) {
        return CompletableFuture.supplyAsync(() -> {
            LinkedHashMap<UUID, Integer> map = new LinkedHashMap<>();
            storage.use(connection -> {
                PreparedStatement statement = connection.prepareStatement(String.format(SELECT_LEADERBOARD, size));
                statement.setString(1, key);
                ResultSet row = statement.executeQuery();
                while (row.next()) {
                    map.put(fromBytes(row.getBytes(1)), row.getInt(2));
                }
            });
            return map;
        });
    }

    @Override
    public CompletableFuture<Void> saveName(UUID uuid, String name) {
        return CompletableFuture.runAsync(() -> storage.use(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_NAME);
            statement.setBytes(1, toBytes(uuid));
            statement.setString(2, name);
            statement.setString(3, name);
            statement.executeUpdate();
        }));
    }

    @Override
    public CompletableFuture<Map<UUID, String>> getNames(Collection<UUID> uuids) {
        return CompletableFuture.supplyAsync(() -> {
            Map<UUID, String> names = new ConcurrentHashMap<>();
            storage.use(connection -> CompletableFuture.allOf(
                uuids.stream().map(uuid ->
                    CompletableFuture.runAsync(() -> {
                        try (PreparedStatement statement = connection.prepareStatement(SELECT_NAME)) {
                            statement.setBytes(1, toBytes(uuid));
                            ResultSet row = statement.executeQuery();
                            if (row.next()) {
                                names.put(uuid, row.getString(1));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    })
                ).toArray(CompletableFuture[]::new)
            ).join());
            return names;
        });
    }
}
