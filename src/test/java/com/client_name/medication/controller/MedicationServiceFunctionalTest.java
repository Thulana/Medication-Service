package com.client_name.medication.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.annotation.PostConstruct;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MedicationServiceFunctionalTest {

    @Value("${auth.user.name}")
    String userName;

    @Value("${user.password}")
    String userPassword;

    private String uri;

    @LocalServerPort
    private int port;

    @PostConstruct
    void init() {
        uri = String.format("http://localhost:%s", port);
    }

    @BeforeEach
    void setup() {
    }

    @Test
    void shouldReturnErrorForWrongPassword() {
        RestAssured
                .given()
                .queryParam("search_term", "cancer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().basic(userName, "wrongpass")
                .when()
                .get(String.format("%s%s", uri, "/medications"))
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void shouldReturnErrorWhenMedicationsNotFound() {
        RestAssured
                .given()
                .queryParam("search_term", "invalidsearchterm")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().basic(userName, userPassword)
                .when()
                .get(String.format("%s%s", uri, "/medications"))
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnErrorWhenSearchTermIsEmpty() {
        RestAssured
                .given()
                .queryParam("search_term", "")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().basic(userName, userPassword)
                .when()
                .get(String.format("%s%s", uri, "/medications"))
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnErrorWhenSearchTermNotProvided() {
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().basic(userName, userPassword)
                .when()
                .get(String.format("%s%s", uri, "/medications"))
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnResponseForValidSearchTerm() {

        Response response = RestAssured
                .given()
                .queryParam("search_term", "Folic Acid")
                .auth().basic(userName, userPassword)
                .when()
                .get(String.format("%s%s", uri, "/medications"));

        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

}
