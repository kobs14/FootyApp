package com.example.footyapp;

import com.example.footyapp.pojos.HeadToHead;
import com.example.footyapp.pojos.LeagueStanding;
import com.example.footyapp.pojos.PlayerInfo;
import com.example.footyapp.pojos.TeamSquad;
import com.example.footyapp.pojos.TopScorers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ApiHandlerInterfaceSynchronise {

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("matches/{match_id}")
    Call<HeadToHead> getMatchHeadToHeadSynchronise(@Path("match_id") String matchId);

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("competitions/{name}/standings")
    Call<LeagueStanding> getLeagueStandingSynchronise(@Path("name") String name);

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("teams/{id}")
    Call<TeamSquad> getTeamSquadSynchronise(@Path("id") int id);

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("players/{player_id}")
    Call<PlayerInfo> getPlayerInfoSynchronise(@Path("player_id") int player_id);

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("competitions/{league_filter}/scorers")
    Call<TopScorers> getTopScorersSynchronise(@Path("league_filter") String league_filter);

}
