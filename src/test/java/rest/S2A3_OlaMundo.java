package rest;


import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class S2A3_OlaMundo {

    public static void main(String[] args) {
        //String tete = "git repositorio";
        String url = "https://restapi.wcaquino.me/ola";
        Response response = RestAssured.request(Method.GET, url);

        System.out.println(response.getBody().asString());





    }



}
