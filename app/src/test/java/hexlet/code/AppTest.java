package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.database.UrlsRepository;
import hexlet.code.model.Url;
import hexlet.code.util.Routes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.sql.SQLException;

class AppTest {
    Javalin app;

    @BeforeEach
    public final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(Routes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            String responseBody = response.body().string();
            assertThat(responseBody.contains("<form"));
            assertThat(responseBody.contains("name=\"url\""));
        });
    }

    @Test
    public void testEmptyUrlsPage() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get(Routes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        }));
    }

    @ParameterizedTest
    @ValueSource(strings = {"github.com", "www.github.com", "https://github.com", "https://www.github.com"})
    public void testCreateUrl(String url) {
        JavalinTest.test(app, ((server, client) -> {
            var requestBody = "url=" + url;
            var response = client.post(Routes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            var responseBody = response.body().string();
            assertThat(responseBody.contains("https://github.com"));
        }));
    }

    @ParameterizedTest
    @ValueSource(strings = {"github.com:8000", "www.github.com:8000", "https://github.com:8000",
                            "https://www.github.com:8000"})
    public void testCreateUrlWithPort(String url) {
        JavalinTest.test(app, ((server, client) -> {
            var requestBody = "url=" + url;
            var response = client.post(Routes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            var responseBody = response.body().string();
            assertThat(responseBody.contains("https://github.com:8000"));
        }));
    }

    @Test
    public void testCreateInvalidUrl() {
        JavalinTest.test(app, (server, client) -> {
            client.post(Routes.urlsPath(), "url=google1.com");
            client.post(Routes.urlsPath(), "url=google2.com");
            client.post(Routes.urlsPath(), "url=google3.com");

            client.post(Routes.urlsPath(), "url=github");

            var responseBody = client.get(Routes.urlsPath()).body().string();
            Document document = Jsoup.parse(responseBody);
            Elements ulElements = document.select("ul");

            boolean containsInvalidUrl = ulElements.stream()
                    .anyMatch(ul -> ul.text().contains("github"));

            assertThat(containsInvalidUrl).isFalse();
        });
    }

    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url("https://github.com");
        UrlsRepository.save(url);
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get(Routes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
        }));
    }

    @Test
    public void testInvalidUrlPage() {
        JavalinTest.test(app, ((server, client) -> {
            var response = client.get(Routes.urlPath("1234"));
            assertThat(response.code()).isEqualTo(404);
        }));
    }
}
