package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.UrlsController;
import hexlet.code.database.BaseRepository;
import hexlet.code.dto.BasePage;
import hexlet.code.util.Routes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Collections;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws SQLException, IOException {
        var app = getApp();
        app.start(getPort());
    }

    public static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    public static String getJdbcUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL",
                "jdbc:h2:mem:project;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;");
    }

    private static String readResourceFile(String filename) throws IOException {
        try (var inputStream = App.class.getClassLoader().getResourceAsStream(filename);
             var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public static Javalin getApp() throws SQLException, IOException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getJdbcUrl());

        var dataSource = new HikariDataSource(hikariConfig);
        var resourceFileName = "schema.sql";
        var sql = readResourceFile(resourceFileName);
        try (var conn = dataSource.getConnection(); var stmt = conn.createStatement()) {
            stmt.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        JavalinJte.init(createTemplateEngine());
        var app = Javalin.create(config -> config.plugins.enableDevLogging());

        app.get(Routes.rootPath(), ctx -> {
            var page = new BasePage();
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            page.setFlashType(ctx.consumeSessionAttribute("flashType"));
            ctx.render("index.jte", Collections.singletonMap("page", page));
        });
        app.get(Routes.urlsPath(), UrlsController::index);
        app.get(Routes.urlPath("{id}"), UrlsController::show);
        app.post(Routes.urlsPath(), UrlsController::create);

        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
}
