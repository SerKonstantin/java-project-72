package hexlet.code.database;

import hexlet.code.model.UrlCheck;
import hexlet.code.util.TimestampFormatter;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UrlChecksRepository extends BaseRepository {
    public static List<UrlCheck> getEntities(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id DESC";
        try (var conn = dataSource.getConnection(); var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var results = stmt.executeQuery();
            var urlChecks = new ArrayList<UrlCheck>();
            while (results.next()) {
                var id = results.getLong("id");
                var statusCode = results.getInt("status_code");
                var title = results.getString("title");
                var h1 = results.getString("h1");
                var description = results.getString("description");
                var createdAt = results.getTimestamp("created_at");

                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(TimestampFormatter.getString(createdAt));

                urlChecks.add(urlCheck);
            }
            return urlChecks;
        }
    }

    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = "INSERT INTO url_checks (status_code, title, h1, description, url_id) VALUES (?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, urlCheck.getStatusCode());
            stmt.setString(2, urlCheck.getTitle());
            stmt.setString(3, urlCheck.getH1());
            stmt.setString(4, urlCheck.getDescription());
            stmt.setLong(5, urlCheck.getUrlId());
            stmt.executeUpdate();

            var generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong("id"));
                var sqlTimestamp = generatedKeys.getTimestamp("created_at");
                urlCheck.setCreatedAt(TimestampFormatter.getString(sqlTimestamp));
            } else {
                throw new SQLException("DB have not returned an id or timestamp after saving an entity");
            }
        }
    }
}
