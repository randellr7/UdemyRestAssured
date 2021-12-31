package rest;

import static io.restassured.RestAssured.*;

public class S5A23_OlaMundo {

    public static void main(String[] args) {
        baseURI = "http://restapi.wcaquino.me";
        port = 80;
        basePath = "/v2";

        given()
                .log().all()
        .when()
                .get("/users")
                .then()
                .statusCode(200);




    }



}
