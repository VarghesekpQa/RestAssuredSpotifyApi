package com.spotify.api;

import com.spotify.pojos.Playlists;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.spotify.api.Route.API;
import static com.spotify.api.Route.TOKEN;
import static com.spotify.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestResource {

    public static Response post(String path,String token ,Object playlistsRequests){
        return given(getRequestSpecification())
                .body(playlistsRequests)
                .auth().oauth2(token)
                .when()
                .post(path)
                .then().spec(getResponseSpecification())
                .extract()
                .response();
    }

    public static  Response get(String path,String token){
        return given(getRequestSpecification())
                .auth().oauth2(token)
                .when()
                .get(path)
                .then().spec(getResponseSpecification())
                .extract()
                .response();
    }
    public static Response update(String path,String token,Object playlistsRequests){
        return given(getRequestSpecification())
                .auth().oauth2(token)
                .body(playlistsRequests)
                .when()
                .put(path)
                .then().spec(getResponseSpecification())
                .extract()
                .response();
    }

    public static Response postAccount(HashMap<String,String> formData ){
        return given(getAccountRequestSpecification())
                .formParams(formData)
                .when()
                .post(API+TOKEN)
                .then().spec(getResponseSpecification())
                .extract()
                .response();
    }

}
