package hexlet.code.database;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                urlCheck.setCreatedAt(createdAt);
                urlChecks.add(urlCheck);
            }
            return urlChecks;
        }
    }

    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = "INSERT INTO url_checks (status_code, title, h1, description, url_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, urlCheck.getStatusCode());
            stmt.setString(2, urlCheck.getTitle());
            stmt.setString(3, urlCheck.getH1());
            stmt.setString(4, urlCheck.getDescription());
            stmt.setLong(5, urlCheck.getUrlId());

            var createdAt = new Timestamp(new Date().getTime());
            stmt.setTimestamp(6, createdAt);

            stmt.executeUpdate();

            var generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
                urlCheck.setCreatedAt(createdAt);
            } else {
                throw new SQLException("DB have not returned an id or timestamp after saving an entity");
            }
        }
    }

    public static Map<Long, UrlCheck> getLatestChecks() throws SQLException {
        var sql = "SELECT DISTINCT ON (url_id) * from url_checks ORDER BY url_id, id DESC";
        try (var conn = dataSource.getConnection(); var stmt = conn.prepareStatement(sql)) {
            var results = stmt.executeQuery();
            var latestChecks = new HashMap<Long, UrlCheck>();
            while (results.next()) {
                var id = results.getLong("id");
                var statusCode = results.getInt("status_code");
                var title = results.getString("title");
                var h1 = results.getString("h1");
                var description = results.getString("description");
                var urlId = results.getLong("url_id");
                var createdAt = results.getTimestamp("created_at");

                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);
                latestChecks.put(urlId, urlCheck);
            }
            return latestChecks;
        }
    }
}
