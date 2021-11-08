package com.example.footyapp;

import com.example.footyapp.otherApiClasses.PlayerDetailsOtherApi;
import com.example.footyapp.otherApiClasses.PlayersOtherApi;
import com.example.footyapp.otherApiClasses.TeamsOtherApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ApiHandlerInterfaceOtherApi {

    //url = "https://api-football-v1.p.rapidapi.com/v2/players/player/1467/2020-2021"

    /*headers = {
        'x-rapidapi-host': "api-football-v1.p.rapidapi.com",
        'x-rapidapi-key': "37ade4c2c3msha1d663fb3586373p1d8e5ajsnce9df7b9a45e"
    }*/


    @Headers("x-rapidapi-key: 37ade4c2c3msha1d663fb3586373p1d8e5ajsnce9df7b9a45e")
    @GET("teams/league/{league_id}")
    Call<TeamsOtherApi> getTeamsOtherApi(@Path("league_id") int league_id);

    //"https://api-football-v1.p.rapidapi.com/v2/teams/league/{league_id}"


    @Headers("x-rapidapi-key: 37ade4c2c3msha1d663fb3586373p1d8e5ajsnce9df7b9a45e")
    @GET("players/squad/{team_id}/2020-2021")
    Call<PlayersOtherApi> getPlayersOtherApi(@Path("team_id") int team_id);

    //"https://api-football-v1.p.rapidapi.com/v2/players/squad/{team_id}/2020-2021"


    @Headers("x-rapidapi-key: 37ade4c2c3msha1d663fb3586373p1d8e5ajsnce9df7b9a45e")
    @GET("players/player/{player_id}/2020-2021")
    Call<PlayerDetailsOtherApi> getPlayerInfoOtherApi(@Path("player_id") int player_id);

    //"https://api-football-v1.p.rapidapi.com/v2/players/player/{player_id}/2020-2021"

}
