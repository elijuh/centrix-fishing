package dev.elijuh.fishing.storage.mysql;

import com.zaxxer.hikari.HikariDataSource;
import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.storage.Storage;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * @author elijuh
 */
@Getter
public class MySQLStorage implements Storage {
    private final HikariDataSource dataSource;
    private final MySQLDao dao;

    public MySQLStorage() {
        dataSource = new HikariDataSource(new MySQLConfiguration().toHikariConfig());
        dao = new MySQLDao(this);

        String path = "schema.sql";
        use(connection -> {
            InputStream in = Core.i().getResource(path);
            if (in == null) {
                throw new RuntimeException(path + " is null");
            }
            try {
                Statement statement = connection.createStatement();
                for (String sql : readStatements(in)) {
                    statement.executeUpdate(sql);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public void use(ConnectionConsumer consumer) {
        if (dataSource == null || dataSource.isClosed()) {
            throw new IllegalStateException("data source is not open");
        }

        try (Connection connection = dataSource.getConnection()) {
            consumer.apply(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FunctionalInterface
    public interface ConnectionConsumer {
        void apply(Connection connection) throws SQLException;
    }

    private List<String> readStatements(InputStream is) throws IOException {
        List<String> queries = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);

                if (line.endsWith(";")) {
                    queries.add(sb.toString().trim());
                    sb = new StringBuilder();
                }
            }
        }

        return queries;
    }
}
