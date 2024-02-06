package hexlet.code.controller;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import hexlet.code.database.UrlChecksRepository;
import hexlet.code.database.UrlsRepository;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.util.Routes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.jsoup.Jsoup;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collections;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        var urls = UrlsRepository.getEntities();
        var latestChecks = UrlChecksRepository.getLatestChecks();
        var page = new UrlsPage(urls, latestChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        var userInput = ctx.formParam("url");
        if (userInput != null) {
            userInput = userInput.trim().toLowerCase();
        }

        try {
            assert userInput != null;
            var url = normalizeUrl(userInput);

            if (UrlsRepository.findByName(url.getName()).isEmpty()) {
                UrlsRepository.save(url);
                ctx.sessionAttribute("flash", "URL added successfully");
                ctx.sessionAttribute("flashType", "success");
            } else {
                ctx.sessionAttribute("flash", "URL page already exists");
                ctx.sessionAttribute("flashType", "info");
            }
            ctx.redirect(Routes.urlsPath());

        } catch (AssertionError | URISyntaxException e) {
            ctx.sessionAttribute("flash", "Invalid URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(Routes.rootPath());
        }
    }

    private static Url normalizeUrl(String userInput) throws URISyntaxException {
        if (!userInput.startsWith("http://") && !userInput.startsWith("https://")) {
            userInput = "https://" + userInput;
        }

        URI uri = new URI(userInput);
        var scheme = uri.getScheme();
        var host = uri.getHost();
        var port = uri.getPort();

        if (host == null || !host.contains(".")) {
            throw new URISyntaxException(userInput, "Invalid URL");
        }

        if (host.startsWith("www.")) {
            host = host.substring(4);
        }

        var urlBuilder = new StringBuilder();
        urlBuilder.append(scheme).append("://").append(host);
        if (port != -1) {
            urlBuilder.append(":").append(port);
        }

        return new Url(urlBuilder.toString());
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        var url = UrlsRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Page not found"));
        var urlChecks = UrlChecksRepository.getEntities(url.getId());
        var page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void check(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        var url = UrlsRepository.findById(urlId)
                .orElseThrow(() -> new NotFoundResponse("Page not found"));

        try {
            var response = Unirest.get(url.getName()).asString();
            var code = response.getStatus();

            var doc = Jsoup.parse(response.getBody());
            var title = doc != null ? doc.title() : "";

            var h1 = "";
            if (doc != null && doc.selectFirst("h1") != null) {
                h1 = doc.selectFirst("h1").text();
            }

            var description = "";
            if (doc != null && doc.selectFirst("meta[name=description]") != null) {
                description = doc.selectFirst("meta[name=description]").attr("content");
            }

            var urlCheck = new UrlCheck(code, title, h1, description, urlId);
            UrlChecksRepository.save(urlCheck);
            ctx.sessionAttribute("flash", "Check performed successfully");
            ctx.sessionAttribute("flashType", "success");

        } catch (UnirestException | NullPointerException e) {
            ctx.sessionAttribute("flash", "Failed to perform a check");
            ctx.sessionAttribute("flashType", "danger");
        }

        ctx.redirect(Routes.urlPath(urlId));
    }
}
