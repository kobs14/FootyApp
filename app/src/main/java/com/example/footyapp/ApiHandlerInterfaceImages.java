package com.example.footyapp;

import com.example.footyapp.ImagesApiClasses.PlayerImagesAndMore;
import com.example.footyapp.pojos.HeadToHead;
import com.example.footyapp.pojos.LeagueStanding;
import com.example.footyapp.pojos.PlayerInfo;
import com.example.footyapp.pojos.TeamSquad;
import com.example.footyapp.pojos.TopScorers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiHandlerInterfaceImages {

    //@Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    //@GET("api/v1/json/1/searchplayers.php?t={TeamName}&p={Playername}")
    @GET("api/v1/json/1/searchplayers.php")
    Call<PlayerImagesAndMore> getPlayerImagesAndMore(@Query("t") String teamName,@Query("p") String playerName);

}
