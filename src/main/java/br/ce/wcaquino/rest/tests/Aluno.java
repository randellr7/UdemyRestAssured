package br.ce.wcaquino.rest.tests;

import io.restassured.RestAssured;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class Aluno {

    @Test
    public void RetornoMsg(){


        given()

                .when()
                .get("http://restapi.wcaquino.me/ola")
                .then()
                .statusCode(200)
        ;

    }

}
