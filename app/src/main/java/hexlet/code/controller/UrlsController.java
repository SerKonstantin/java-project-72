package hexlet.code.controller;

import hexlet.code.database.UrlsRepository;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.util.Routes;
import io.javalin.http.Context;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collections;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        var urls = UrlsRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var userInput = ctx.formParam("url");
        try {
            URI uri = new URI(userInput);
            var scheme = uri.getScheme();
            scheme = scheme == null ? "https" : scheme;
            var host = uri.getHost();
            var port = uri.getPort();
            if (host != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(scheme).append("://").append(host);

                if (port != -1) {
                    sb.append(":").append(port);
                }

                var url = new Url(sb.toString());
                UrlsRepository.save(url);
                ctx.sessionAttribute("flash", "TODO success message 1");
                ctx.sessionAttribute("flashType", "success");
                ctx.redirect(Routes.urlsPath());
            } else {
                throw new URISyntaxException(userInput, "TODO Invalid url message 1");
            }
        } catch (URISyntaxException e) {
            ctx.sessionAttribute("flash", "TODO fail message 2");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(Routes.rootPath());
        }

    }

    public static void show(Context ctx) {
        // TODO
    }

}
