package com.spotify.api.applicationApi;

import com.spotify.api.RestResource;
import com.spotify.pojos.Playlists;
import com.spotify.utils.ConfigLoader;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.spotify.api.Route.PLAYLISTS;
import static com.spotify.api.Route.USERS;
import static com.spotify.api.TokenManager.getToken;

public class PlaylistApi {

    @Step
    public static Response post(Playlists playlistsRequests){

        return RestResource.post(USERS+"/"+ConfigLoader.getInstance().getUserId()+PLAYLISTS, getToken(),playlistsRequests);

    }

    public static Response post(String token,Playlists playlistsRequests){

        return RestResource.post(USERS+"/"+ConfigLoader.getInstance().getUserId()+PLAYLISTS,token,playlistsRequests);

    }
    public static  Response get(String playlistId){

        return RestResource.get(PLAYLISTS+"/"+ playlistId,getToken());

    }
    public static Response update(String playlistId,Playlists playlistsRequests){

        return RestResource.update(PLAYLISTS+"/"+ playlistId,getToken(),playlistsRequests);
    }
}
