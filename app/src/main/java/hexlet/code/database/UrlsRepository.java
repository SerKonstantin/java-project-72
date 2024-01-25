package hexlet.code.database;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlsRepository extends BaseRepository {
    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT * FROM urls";
        try (var conn = dataSource.getConnection(); var stmt = conn.prepareStatement(sql)) {
            var results = stmt.executeQuery();
            var urls = new ArrayList<Url>();
            while (results.next()) {
                var id = results.getLong("id");
                var name = results.getString("name");
                var createdAt = results.getTimestamp("created_at");
                var url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                urls.add(url);
            }
            return urls;
        }
    }

    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name) VALUES (?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, url.getName());
            stmt.executeUpdate();
            var generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
                url.setCreatedAt(generatedKeys.getTimestamp(2));
            } else {
                throw new SQLException("DB have not returned an id or timestamp after saving an entity");
            }
        }
    }

    public static Optional<Url> findByName(String term) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, term);
            var results = stmt.executeQuery();
            if (results.next()) {
                var id = results.getLong("id");
                var name = results.getString("name");
                var createdAt = results.getTimestamp("created_at");
                var url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                return Optional.of(url);
            } else {
                return Optional.empty();
            }
        }
    }
}
