package accuweather;


import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.Method;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import weather.Weather;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
//hw6
public class ForecastsAnaLocationTest extends AccuweatherAbstractTest {


    @Test
    @DisplayName("Проверка ответа на запрос прогноза на 1 день")
    @Description("Метод GET, авторизация пройдена")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Бакшанская Алеся")
    @Epic(value = "Accuweather")
    void testGetResponseForecasts1Days() {
//        Weather weather = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
//                .when().get(getBaseUrl() + "/forecasts/v1/daily/1day/{locationKey}")
//                .then().statusCode(200).time(lessThan(2000L))
//                .extract().response().body().as(Weather.class);
//        Assertions.assertEquals(1, weather.getDailyForecasts().size());

        Response response = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/1day/{locationKey}");
        String headerAm = response.getHeader("am");
        int sizeWeatherForecast = response.body().as(Weather.class).getDailyForecasts().size();
        int statusCode = response.getStatusCode();
        Assertions.assertAll(() -> Assertions.assertEquals(1, sizeWeatherForecast),
                () -> Assertions.assertEquals("true", headerAm),
                () -> Assertions.assertEquals(200, statusCode));
    }
    @Test
    @DisplayName("Проверка ответа на запрос прогноза на 10 дней")
    @Description("Метод GET, авторизация не пройдена")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Бакшанская Алеся")
    @Epic(value = "Accuweather")
    void testGetResponseForecasts10Days() {
        String code = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/10day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Code");

        String message = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/10day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Message");

        Assertions.assertAll(() -> Assertions.assertEquals("Unauthorized", code),
                () -> Assertions.assertEquals("Api Authorization failed", message));
    }
    @Test
    @DisplayName("Проверка ответа на запрос прогноза на 15 дней")
    @Description("Метод GET, авторизация не пройдена")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Бакшанская Алеся")
    @Epic(value = "Accuweather")
    void testGetResponseForecasts15Days() {

//        String code = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
//                .when()
//                .request(Method.GET,getBaseUrl()+"/forecasts/v1/daily/15day/{locationKey}")
//                .then().extract()
//                .jsonPath()
//                .getString("Code");
        String code = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/15day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Code");

        String message = given().queryParam("apikey", getApiKey()).pathParam("locationKey", 50)
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/15day/{locationKey}")
                .then().statusCode(401).extract()
                .jsonPath()
                .getString("Message");

        Assertions.assertAll(() -> Assertions.assertEquals("Unauthorized", code),
                () -> Assertions.assertEquals("Api Authorization failed", message));

    }





    @Test
    @DisplayName("Проверка ответа на запрос автозаполнения поиска")
    @Description("Метод GET, авторизация пройдена")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Бакшанская Алеся")
    @Epic(value = "Accuweather")
    void testResponseDateAutocompleteSearch() {
        //вариант через assertThat() (но после первого бага проверка прекращается)
        given().queryParam("apikey", getApiKey()).queryParam("q", "Moscow")
                .when().request(Method.GET,getBaseUrl() + "/locations/v1/cities/autocomplete")
                .then().assertThat().statusCode(200).time(lessThan(2000L))
                .statusLine("HTTP/1.1 200 OK")
                .header("Content-Encoding", "gzip")
                .body("[0].LocalizedName", equalTo("Moscow"))
                .body("[0].Key", equalTo("294021"));
        //вариант через assertAll() (выводит все баги)
        JsonPath response = given().queryParam("apikey", getApiKey()).queryParam("q", "Moscow")
                .when().request(Method.GET,getBaseUrl() + "/locations/v1/cities/autocomplete")
                .body().jsonPath();
        Assertions.assertAll(() -> Assertions.assertEquals("Moscow", response.get("[0].LocalizedName")),
                () -> Assertions.assertEquals("294021", response.get("[0].Key")));


    }
}
