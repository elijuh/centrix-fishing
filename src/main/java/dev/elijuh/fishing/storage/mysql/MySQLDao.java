package dev.elijuh.fishing.storage.mysql;

import dev.elijuh.fishing.storage.Dao;
import dev.elijuh.fishing.user.UserData;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author elijuh
 */
@RequiredArgsConstructor
public class MySQLDao implements Dao {
    private static final String
        INSERT_STATS = "INSERT INTO `user_stats`(`uuid`, `key`, `value`) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE `%s` = %d;",
        SELECT_STATS = "SELECT * FROM `user_stats` WHERE `uuid` = ?;";
    private final MySQLStorage storage;

    private byte[] toBytes(UUID uuid) {
        return ByteBuffer.wrap(new byte[16])
            .putLong(uuid.getMostSignificantBits())
            .putLong(uuid.getLeastSignificantBits())
            .array();
    }

    public CompletableFuture<Void> save(UUID uuid, UserData data) {
        return CompletableFuture.completedFuture(null);
        /*
        byte[] bytes = toBytes(uuid);
        try (Connection connection = storage.getDataSource().getConnection()) {
            return CompletableFuture.allOf(data.getStats().entrySet().stream().map(entry -> {
                String key = entry.getKey();
                int value = entry.getValue();
                return CompletableFuture.runAsync(() -> {
                    try {
                        PreparedStatement statement = connection.prepareStatement(String.format(INSERT_STATS, key, value));
                        statement.setBytes(1, bytes);
                        statement.setString(2, key);
                        statement.setInt(3, value);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }).toArray(CompletableFuture[]::new));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);

         */
    }

    public CompletableFuture<UserData> getUserData(UUID uuid) {
        return CompletableFuture.completedFuture(null);
        /*
        return CompletableFuture.supplyAsync(() -> {
            UserData data = new UserData();
            storage.use(connection -> {
                PreparedStatement statement = connection.prepareStatement(SELECT_STATS);
                statement.setBytes(1, toBytes(uuid));
                ResultSet row = statement.executeQuery();
                while (row.next()) {
                    data.setStatistic(row.getString(2), row.getInt(3));
                }
            });
            return data;
        });

         */
    }
}
