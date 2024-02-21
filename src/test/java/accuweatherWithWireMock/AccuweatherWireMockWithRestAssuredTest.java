package accuweatherWithWireMock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weather.ErrorUnauthorized;
import weather.Weather;



import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.*;
//HW6
public class AccuweatherWireMockWithRestAssuredTest extends AccuweatherWireMockWithRestAssuredAbstractTest {
    private static final Logger logger
            = LoggerFactory.getLogger(AccuweatherWireMockWithRestAssuredTest.class);

    @Test
    @DisplayName("Проверка ответа на запрос прогноза на 1 день с мокированием")
    @Description("Метод GET, авторизация пройдена")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Бакшанская Алеся")
    @Epic(value = "Accuweather")
    void testGetResponseForecasts1Days() {
        logger.info("Тест GetResponseForecasts1Days запущен");
        logger.debug("Формирование мока GET-запроса \"/forecasts/v1/daily/1day\"");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/1day"))
                .withQueryParam("apikey", equalTo("235689"))
                .withHeader("Cache-Control", equalTo("public"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Cache-Control", "public")
                        .withBody("Massage")));

        logger.debug("Формирование GET-запроса \"/forecasts/v1/daily/1day\"");
        Response response = given()
                .queryParam("apikey", "235689")
                .header("Cache-Control", "public")
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/1day");
        int statusCode = response.getStatusCode();
        String header = response.getHeader("Cache-Control");
        String body = response.getBody().asString();

        Assertions.assertEquals(200, statusCode);
        Assertions.assertEquals("public", header);
        Assertions.assertEquals("Massage", body);
        logger.info("Тест GetResponseForecasts1Days завершен");
    }

    @Test
    @DisplayName("Проверка тела ответа на запрос прогноза на 1 день с мокированием")
    @Description("Метод GET, авторизация пройдена")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Бакшанская Алеся")
    @Epic(value = "Accuweather")
    void testBodyForecast1Days() throws JsonProcessingException {
        logger.info("Тест testBodyForecast1Days запущен");
        logger.debug("Формирование мока GET-запроса \"/forecasts/v1/daily/1day\"");

        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/1day"))
                .withQueryParam("apikey", equalTo("235689"))
                .willReturn(aResponse().withStatus(200)
                        .withBody("{\n" +
                                "  \"Headline\": {\n" +
                                "    \"EffectiveDate\": \"2023-12-23T01:00:00-07:00\",\n" +
                                "    \"EffectiveEpochDate\": 1703318400,\n" +
                                "    \"Severity\": 7,\n" +
                                "    \"Text\": \"Breezy from late Friday night into Saturday morning\",\n" +
                                "    \"Category\": \"wind\",\n" +
                                "    \"EndDate\": \"2023-12-23T13:00:00-07:00\",\n" +
                                "    \"EndEpochDate\": 1703361600,\n" +
                                "    \"MobileLink\": \"http://www.accuweather.com/en/ca/athabasca/t9s/daily-weather-forecast/50?lang=en-us\",\n" +
                                "    \"Link\": \"http://www.accuweather.com/en/ca/athabasca/t9s/daily-weather-forecast/50?lang=en-us\"\n" +
                                "  },\n" +
                                "  \"DailyForecasts\": [\n" +
                                "    {\n" +
                                "      \"Date\": \"2023-12-20T07:00:00-07:00\",\n" +
                                "      \"EpochDate\": 1703080800,\n" +
                                "      \"Temperature\": {\n" +
                                "        \"Minimum\": {\n" +
                                "          \"Value\": 22,\n" +
                                "          \"Unit\": \"F\",\n" +
                                "          \"UnitType\": 18\n" +
                                "        },\n" +
                                "        \"Maximum\": {\n" +
                                "          \"Value\": 34,\n" +
                                "          \"Unit\": \"F\",\n" +
                                "          \"UnitType\": 18\n" +
                                "        }\n" +
                                "      },\n" +
                                "      \"Day\": {\n" +
                                "        \"Icon\": 4,\n" +
                                "        \"IconPhrase\": \"Intermittent clouds\",\n" +
                                "        \"HasPrecipitation\": false\n" +
                                "      },\n" +
                                "      \"Night\": {\n" +
                                "        \"Icon\": 35,\n" +
                                "        \"IconPhrase\": \"Partly cloudy\",\n" +
                                "        \"HasPrecipitation\": false\n" +
                                "      },\n" +
                                "      \"Sources\": [\n" +
                                "        \"AccuWeather\"\n" +
                                "      ],\n" +
                                "      \"MobileLink\": \"http://www.accuweather.com/en/ca/athabasca/t9s/daily-weather-forecast/50?day=1&lang=en-us\",\n" +
                                "      \"Link\": \"http://www.accuweather.com/en/ca/athabasca/t9s/daily-weather-forecast/50?day=1&lang=en-us\"\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}")));
        String response = given().queryParam("apikey", "235689")
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/1day")
                .then().statusCode(200).extract().body().asString();
        logger.debug("Формирование объекта weather по запросу /forecasts/v1/daily/1day");
        Weather weather = new ObjectMapper().readValue(response, Weather.class);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, weather.getDailyForecasts().size()),
                () -> Assertions.assertEquals(34, weather.getDailyForecasts()
                        .get(0).getTemperature().getMaximum().getValue()));
        logger.info("Тест testBodyForecast1Days завершен");
    }

    @Test
    @DisplayName("Проверка тела ответа на запрос прогноза на 10 дней с мокированием")
    @Description("Метод GET, авторизация не пройдена")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Бакшанская Алеся")
    @Epic(value = "Accuweather")
    void testForecast10DaysWithStatus401() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        logger.info("Тест Forecast10DaysWithStatus401 запущен");
        logger.debug("Формирование объекта ErrorUnauthorized");
        ErrorUnauthorized errorUnauthorized = new ErrorUnauthorized();
        errorUnauthorized.setCode("Unauthorized");
        errorUnauthorized.setMessage("Api Authorization failed");
        errorUnauthorized.setReference("/forecasts/v1/daily/10day/50?apikey=l9IVz7e0QE5Q4yTLUDkXU6O5wnzCPivB");

        logger.debug("Формирование мока GET-запроса \"/forecasts/v1/daily/10day\"");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/10day"))
                .withQueryParam("apikey", equalTo("235689"))
                .willReturn(aResponse().withStatus(401)
                        .withHeader("Unauthorized", "keep-alive")
                        .withHeader("Content-Length", "143")
                        .withBody(mapper.writeValueAsString(errorUnauthorized))));

        String response = given().queryParam("apikey", "235689")
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/10day")
                .then().statusCode(401)
                .assertThat().header("Unauthorized", "keep-alive")
                .assertThat().header("Content-Length", "143")
                .extract().body().asString();
        logger.debug("Формирование объекта errorUnauthorized по запросу /forecasts/v1/daily/10day");
        ErrorUnauthorized eUFromResponse = mapper.readValue(response, ErrorUnauthorized.class);
        Assertions.assertAll(
                () -> Assertions.assertEquals("Unauthorized", eUFromResponse.getCode()),
                () -> Assertions.assertEquals("Api Authorization failed", eUFromResponse.getMessage()));
        logger.info("Тест Forecast10DaysWithStatus401 завершен");
    }


}
