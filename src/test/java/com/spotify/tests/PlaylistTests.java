package com.spotify.tests;

import com.spotify.api.StatusCode;
import com.spotify.api.applicationApi.PlaylistApi;
import com.spotify.pojos.Error;
import com.spotify.pojos.Playlists;

import com.spotify.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.spotify.utils.FakerUtils.generateDescription;
import static com.spotify.utils.FakerUtils.generateName;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@Epic("Spotify OAuth 2.0")
@Feature("Playlist API")
public class PlaylistTests extends BaseTest {

    @Step
    public Playlists PlaylistBuilder(String name, String description, boolean _public) {

        return  Playlists.builder()
                .name(name)
                .description(description)
                ._public(_public)
                .build();
    }

    @Step
    public void AssertPlaylistsEqual(Playlists playlistsRequest, Playlists playlistResponse) {

        assertThat(playlistsRequest.getName(), equalTo(playlistResponse.getName()));
        assertThat(playlistsRequest.getDescription(), equalTo(playlistResponse.getDescription()));
        assertThat(playlistsRequest.get_public(), equalTo(playlistResponse.get_public()));
    }

    @Step
    public void AssertStatusCode(int actualStatusCode, int expectedStatusCode) {
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }

    @Step
    public void AssertError(Error responseErr, int expectedStatusCode, String expectedMsg) {
        assertThat(responseErr.getError().getStatus(), equalTo(expectedStatusCode));
        assertThat(responseErr.getError().getMessage(), equalTo(expectedMsg));
    }

    @Story("Create a playlist story")
    @Link("https://example.org")
    @Link(name = "allure", type = "mylink")
    @Issue("12345")
    @TmsLink("1234567")
    @Description("This is the create playlist description")
    @Test(description = "Should be able to create playlist")
    public void CreatePlaylist() {
        Playlists playlistsRequests = PlaylistBuilder(generateName(), generateDescription(), false);

        Response response = PlaylistApi.post(playlistsRequests);
        AssertStatusCode(response.statusCode(), StatusCode.CODE_201.getCode());

        AssertPlaylistsEqual(playlistsRequests, response.as(Playlists.class));
    }

    @Test
    public void GetPlaylist() {

        Playlists playlistsRequests = PlaylistBuilder("Playlist Wd8_-_Fu_6", "Description pW_0_634_m__2QR--1-j", false);

        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());

        AssertStatusCode(response.statusCode(),  StatusCode.CODE_200.getCode());

        AssertPlaylistsEqual(playlistsRequests, response.as(Playlists.class));

    }

    @Test
    public void UpdatePlaylist() {
        Playlists playlistsRequests = PlaylistBuilder(generateName(), generateDescription(), false);

        Response response = PlaylistApi.update(DataLoader.getInstance().getUpdatePlaylistId(), playlistsRequests);

        AssertStatusCode(response.statusCode(),  StatusCode.CODE_200.getCode());
    }

    @Story("Create a playlist story")
    @Test
    public void CreatePlaylistWithoutName() {

        Playlists playlistsRequests = PlaylistBuilder("", generateDescription(), false);
        Response response = PlaylistApi.post(playlistsRequests);
        AssertStatusCode(response.statusCode(), 400);

        Error errorResponse = response.as(Error.class);

        AssertError(response.as(Error.class),  StatusCode.CODE_400.getCode(),  StatusCode.CODE_400.getMsg());
    }

    @Story("Create a playlist story")
    @Test
    public void CreatePlaylistWithExpiredToken() {

        Playlists playlistsRequests = PlaylistBuilder(generateName(), generateDescription(), false);

        Response response = PlaylistApi.post("xjsghkjfsfasucfckwjgc", playlistsRequests);
        AssertStatusCode(response.statusCode(), StatusCode.CODE_401.getCode());

        AssertError(response.as(Error.class),  StatusCode.CODE_401.getCode(),  StatusCode.CODE_401.getMsg());

    }

}
