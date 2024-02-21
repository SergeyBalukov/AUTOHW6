package velislava;


import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
//HW5
public class VelislavaTest extends VelislavaAbstractTest {
    private static final Logger logger
            = LoggerFactory.getLogger(VelislavaAbstractTest.class);

    @Test
    @DisplayName("Проверка при ответе 401 с мокированием метод GET")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Бакшанская Алеся")
    @Epic(value = "Velislava")
    void testVelislavaGet401Response() throws URISyntaxException, IOException {
        logger.info("Тест VelislavaGet401Response запущен");

        logger.debug("Формирование мока для GET /product/sakura-yaponskaya");
        stubFor(get(urlPathEqualTo("/product/sakura-yaponskaya"))
                .withQueryParam("key", equalTo("6516831"))
                .withHeader("Content-Language", equalTo("ru"))
//                .withRequestBody(containing("\"title\": \"Sakura\""))
                .willReturn(aResponse()
                        .withStatus(401).withBody("Unauthorized")));

        logger.debug("Отправка запроса на /product/sakura-yaponskaya");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseUrl() + "/product/sakura-yaponskaya");
        request.addHeader("Content-Language", "ru");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("key", "6516831")
                .build();
        request.setURI(uri);

//        request.setEntity(new StringEntity("{" +
//                "\"title\": \"Sakura japanese\"" +
//                "}"));
        logger.debug("http клиент создан");

        HttpResponse response = httpClient.execute(request);

        verify(getRequestedFor(urlPathEqualTo("/product/sakura-yaponskaya"))
                .withQueryParam("key", equalTo("6516831"))
                .withHeader("Content-Language", equalTo("ru")));
        assertEquals(401, response.getStatusLine().getStatusCode());
        assertEquals("Unauthorized", convertResponseToString(response));
        logger.info("Тест VelislavaGet401Response завершен");
    }

    @Test
    @DisplayName("Проверка при ответе 401 с мокированием етод POST")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Бакшанская Алеся")
    @Epic(value = "Velislava")
    void testVelislavaGet401ResponsePost() throws URISyntaxException, IOException {
        logger.info("Тест testVelislavaGet401ResponsePost запущен");

        logger.debug("Формирование мока для POST /product/sakura-yaponskaya");
        stubFor(post(urlPathEqualTo("/product/sakura-yaponskaya"))
                .withHeader("Content-Language", equalTo("ru"))
                .withRequestBody(equalTo("\"title\": \"Sakura\""))
                .willReturn(aResponse()
                        .withStatus(401).withBody("Unauthorized")));

        logger.debug("Отправка запроса на /product/sakura-yaponskaya");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(getBaseUrl() + "/product/sakura-yaponskaya");
        request.addHeader("Content-Language", "ru");

        request.setEntity(new StringEntity(
                "\"title\": \"Sakura\""));
        logger.debug("http клиент создан");

        HttpResponse response = httpClient.execute(request);

        verify(postRequestedFor(urlPathEqualTo("/product/sakura-yaponskaya"))
                .withHeader("Content-Language", equalTo("ru")));
        assertEquals(401, response.getStatusLine().getStatusCode());
        assertEquals("Unauthorized", convertResponseToString(response));
        logger.info("Тест testVelislavaGet401ResponsePost завершен");
    }
}
