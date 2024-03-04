package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.database.UrlsRepository;
import hexlet.code.model.Url;
import hexlet.code.util.Routes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

class AppTest {
    private static Javalin app;
    private static MockWebServer mockServer;
    private static String mockServerUrl;
    private static String fixturesFolderPath = "src/test/resources/fixtures/";

    public String readFixture(String fileName) throws IOException {
        String pathName = fixturesFolderPath + fileName;
        return Files.readString(Path.of(pathName));
    }

    @BeforeAll
    public static void initializeMockWebServer() throws IOException {
        mockServer = new MockWebServer();
        mockServerUrl = mockServer.url("/").toString();
    }

    @BeforeEach
    public void initializeApp() throws SQLException, IOException {
        app = App.getApp();
    }

    @AfterAll
    public static void shutdownMockWebServer() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(Routes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            String responseBody = response.body().string();
            assertThat(responseBody.contains("Website analyzer"));
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
    @ValueSource(strings = {"github.com", "www.github.com", "https://github.com", "https://www.github.com",
                            "github.com/SerKonstantin/java-project-72"})
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
                            "https://www.github.com:8000", "github.com:8080/SerKonstantin/java-project-72"})
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
            client.post(Routes.urlsPath(), "url=valid1.com");
            client.post(Routes.urlsPath(), "url=valid2.com");
            client.post(Routes.urlsPath(), "url=valid3.com");

            client.post(Routes.urlsPath(), "url=invalid");

            var response = client.get(Routes.urlsPath());
            String responseBody = response.body().string();
            assertThat(!responseBody.contains("invalid"));
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

    @Test
    public void testUrlCheck() throws IOException {
        var mockResponse = new MockResponse();
        var mockContent = readFixture("mockPage.html");
        mockResponse.setBody(mockContent);
        mockServer.enqueue(mockResponse);

        JavalinTest.test(app, ((server, client) -> {
            var url = new Url(mockServerUrl);
            UrlsRepository.save(url);
            var id = url.getId();

            var response = client.post(Routes.checkUrlPath(id));
            assertThat(response.code()).isEqualTo(200);
            var responseBody = response.body().string();
            assertThat(responseBody.contains("200"));
            assertThat(responseBody.contains("some title"));
            assertThat(responseBody.contains("some header"));
            assertThat(responseBody.contains("some description"));

            assertThat(responseBody.contains("Check performed successfully"));
        }));
    }

    @Test
    public void testUrlWithNoAttrCheck() throws IOException {
        var mockResponse = new MockResponse();
        var mockContent = readFixture("mockPageWithNoAttr.html");
        mockResponse.setBody(mockContent);
        mockServer.enqueue(mockResponse);

        JavalinTest.test(app, ((server, client) -> {
            var url = new Url(mockServerUrl);
            UrlsRepository.save(url);
            var id = url.getId();

            var response = client.post(Routes.checkUrlPath(id));
            assertThat(response.code()).isEqualTo(200);
            var responseBody = response.body().string();
            assertThat(responseBody.contains("200"));

            assertThat(responseBody.contains("Check performed successfully"));
        }));
    }
}
