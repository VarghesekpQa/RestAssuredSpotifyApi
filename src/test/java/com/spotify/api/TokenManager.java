package com.spotify.api;

import com.spotify.utils.ConfigLoader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.spotify.api.RestResource.postAccount;
import static com.spotify.api.SpecBuilder.getResponseSpecification;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.time.Instant;
import java.util.HashMap;

public class TokenManager {

    private static String access_token;
    private static Instant expire_time;

    public synchronized static String getToken() {

        try {
            if (access_token == null || Instant.now().isAfter(expire_time)) {

                System.out.println("Renewing token...");

                Response response = renewToken();
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expire_time = Instant.now().plusSeconds(expiryDurationInSeconds);
            }else {
                System.out.println("Token is good to use...");
            }
        } catch (Exception e) {
            throw new RuntimeException("Abort!!! failed to get token!");
        }
        return access_token;
    }

    private static Response renewToken() {

        HashMap<String, String> formData = new HashMap<String, String>();

        formData.put("grant_type", ConfigLoader.getInstance().getGrantType());
        formData.put("refresh_token",ConfigLoader.getInstance().getRefreshToken() );
        formData.put("client_id", ConfigLoader.getInstance().getClientId());
        formData.put("client_secret", ConfigLoader.getInstance().getClientSecret());

        Response response = postAccount(formData);

        if (response.statusCode() != 200) {
            throw new RuntimeException("Abort!!! Renew token failed!");
        } else {
            return response;
        }
    }
}
