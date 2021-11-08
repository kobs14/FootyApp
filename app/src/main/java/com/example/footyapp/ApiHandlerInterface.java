package com.example.footyapp;

import com.example.footyapp.pojos.AllLeagueMatches;
import com.example.footyapp.pojos.FavoriteTeamMatches;
import com.example.footyapp.pojos.HeadToHead;
import com.example.footyapp.pojos.LeagueStanding;
import com.example.footyapp.pojos.TeamSquad;
import com.example.footyapp.pojos.TeamsOfLeague;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ApiHandlerInterface {

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("competitions/{name}/teams")
    Call<TeamsOfLeague> getTeamsOfLeague(@Path("name") String name);

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("competitions/{name}/standings")
    Call<LeagueStanding> getLeagueStanding(@Path("name") String name);

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("teams/{id}")
    Call<TeamSquad> getTeamSquad(@Path("id") int id);

    /*
    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("competitions/{name}/matches")
    Call<MatchDay> getMatchDay(@Path("name") String name,@Query("matchday") int matchNumber);
    */

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("competitions/{name}/matches")
    Call<AllLeagueMatches> getTheMatches(@Path("name") String name);

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("teams/{team_id}/matches")
    Call<FavoriteTeamMatches> getFavoriteTeamMatches(@Path("team_id") String teamId);

    @Headers("X-Auth-Token: 7cb45770c8794bdf82b00c120660a2ff")
    @GET("matches/{match_id}")
    Call<HeadToHead> getMatchHeadToHead(@Path("match_id") String matchId);

}
