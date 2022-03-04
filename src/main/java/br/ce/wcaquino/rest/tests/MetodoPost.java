package br.ce.wcaquino.rest.tests;

import br.ce.wcaquino.rest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MetodoPost extends BaseTest {
    String url = "https://restapi.wcaquino.me/users";

    @Test
    public void inserirUsuario(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{ \"name\": \"Jose\", \"age\": 55 }")
        .when()
                .post(url)
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Jose"))
                .body("age", is(55))
                ;
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{ \"age\": 55 }")
                .when()
                .post(url)
                .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void alterarUsuario(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Jose edit\", \"age\": 80 }")
                .when()
                .put(url+"/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Jose edit"))
                .body("age", is(80))
        ;
    }

    @Test
    public void autenticacao(){
        given()
                .log().all()
                .when()
                .auth().basic("admin", "senha")
                .get("https://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)

        ;
    }

    @Test
    public void autenticacao2(){
        given()
                .log().all()
                .when()
                .auth().preemptive().basic("admin", "senha")
                .get("https://restapi.wcaquino.me/basicauth2")
                .then()
                .log().all()
                .statusCode(200)

        ;
    }

}


