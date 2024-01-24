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
        var userInput = ctx.formParam("url").trim().toLowerCase();

        try {
            if (!userInput.startsWith("http://") && !userInput.startsWith("https://")) {
                userInput = "https://" + userInput;
            }

            URI uri = new URI(userInput);
            var scheme = uri.getScheme();
            var host = uri.getHost();
            var port = uri.getPort();

            if (host != null && host.contains(".")) {
                if (host.startsWith("www.")) {
                    host = host.substring(4);
                }

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
                throw new URISyntaxException(userInput, "Invalid URL");
            }
        } catch (URISyntaxException e) {
            ctx.sessionAttribute("flash", "Invalid URL. Please try again with a valid URL.");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(Routes.rootPath());
        }
    }

    public static void show(Context ctx) {
        // TODO
    }

}
