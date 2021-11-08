package com.example.footyapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.footyapp.pojos.ActiveCompetition;
import com.example.footyapp.pojos.AllLeagueMatches;
import com.example.footyapp.pojos.FavoriteTeamMatches;
import com.example.footyapp.pojos.LeagueStanding;
import com.example.footyapp.pojos.Team;
import com.example.footyapp.pojos.TeamSquad;
import com.example.footyapp.pojos.TeamsOfLeague;
import com.example.footyapp.ui.Favorites.FavoritesFragment;
import com.example.footyapp.ui.League.LeagueTableFragment;
import com.example.footyapp.ui.TeamSquad.MyTeamFragment;
import com.example.footyapp.ui.matches.MatchesFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//AppCompatActivity
public class SittingsActivity extends FragmentActivity {

    private Context mContext= this;
    DatabaseHelper dbHelper;
    ApiHandlerInterface apiHandlerInterface;

    Button submitBtn;
    public static String leagueSelected = null;
    public static String teamSelected = null;
    String teamID = null;

    private AllLeagueMatches allLeagueMatches = null;
    public static TeamsOfLeague teamsOfLeague_selected = null;
    private TeamSquad teamInfo;
    private LeagueStanding leagueStanding;

    ApiHandlerInterfaceSynchronise apiHandlerInterfaceSynchronise;
    Call<LeagueStanding> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sitting_activity);

        dbHelper = new DatabaseHelper(this);
        apiHandlerInterface = ApiClient.getClient().create(ApiHandlerInterface.class);

        CreateLeagueSpinner();

        submitBtn = (Button)findViewById(R.id.submitBtn);

        apiHandlerInterfaceSynchronise = ApiClientSynchronise.createService(ApiHandlerInterfaceSynchronise.class);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (leagueSelected != null && teamSelected != null && leagueSelected != "None" && teamSelected != "None") {

                    teamID = getTeamId().toString();

                    call = apiHandlerInterfaceSynchronise.getLeagueStandingSynchronise(leagueSelected);

                    Thread thread;

                    thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try
                            {
                                LeagueTableFragment.leagueStanding = call.execute().body();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();

                    try
                    {
                        thread.join();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }


                    getTeamInfo(teamID);
                    getFavoriteMatches();
                    getAllMatches();

                    if (dbHelper.insertUserChoice(leagueSelected, teamSelected, teamID)){
                        finish();
                    }else {
                        Toast.makeText(mContext,"Faild to insert data.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void CreateLeagueSpinner(){
        Spinner league_spinner = (Spinner)findViewById(R.id.spinner_league);
        league_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String LeagueAddChoise = parent.getItemAtPosition(position).toString();
                CreateTeamsSpinner(LeagueAddChoise);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void CreateTeamsSpinner (String chosenLeague){
        switch (chosenLeague) {
            case "Premier League":
                sendNetworkRequestTeamsList("PL");
                leagueSelected = "PL";
                break;
            case "Ligue 1":
                sendNetworkRequestTeamsList("FL1");
                leagueSelected = "FL1";
                break;

            case "Série A":
                sendNetworkRequestTeamsList("SA");
                leagueSelected = "SA";
                break;

            case "Bundesliga":
                sendNetworkRequestTeamsList("BL1");
                leagueSelected = "BL1";
                break;
            case "Primera División":
                sendNetworkRequestTeamsList("PD");
                leagueSelected = "PD";
                break;
            case "None":
                AdjustTeamsSpinner(null);
                leagueSelected = "None";
                break;
        }
    }

    public void AdjustTeamsSpinner(TeamsOfLeague teamsOfLeague){
        ArrayList<String> teams_list = new ArrayList<>();
        Spinner spinner = (Spinner) findViewById(R.id.spinner_teams);
        teams_list.add("None");

        if(teamsOfLeague != null)
        {
            for (Team t : teamsOfLeague.getTeams())
            {
                teams_list.add(t.getName());
            }
        }

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,teams_list);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerArrayAdapter.notifyDataSetChanged();
    }

    private void sendNetworkRequestTeamsList(String league_filter){
        getTeams(league_filter);
    }

    private Integer getTeamId(){
        Integer id = 0;
        for(Team t : teamsOfLeague_selected.getTeams()){
            if (teamSelected.equals(t.getName())){
                id = t.getId();
            }
        }

        return id;
    }

    private void insertTeamInfoToDataBase(TeamSquad teamInfo){

        if(teamInfo == null)
            return;

        String c1 = "", c2 = "", c3 = "", c4 = "", c5 = "";
        int i = 0;
        for (ActiveCompetition comp : teamInfo.getActiveCompetitions()){
            switch (i){
                case 0:
                    c1 = comp.getName();
                    break;
                case 1:
                    c2 = comp.getName();
                    break;
                case 2:
                    c3 = comp.getName();
                    break;
                case 3:
                    c4 = comp.getName();
                    break;
                case 4:
                    c5 = comp.getName();
                    break;
                default:
                    break;
            }

            i++;
        }

        if (dbHelper.insertTeamInfoData(teamInfo.getName(), teamInfo.getId().toString(), teamInfo.getVenue(), teamInfo.getCrestUrl(), teamInfo.getAddress(),
                teamInfo.getWebsite(), teamInfo.getFounded().toString(), teamInfo.getClubColors(),c1, c2, c3, c4, c5)){
        }
        else {
            Toast.makeText(mContext,"Faild to insert data.",Toast.LENGTH_SHORT).show();
        }
    }



    /*Api Calls*/
    private void getAllMatches() {
        Call<AllLeagueMatches> call = apiHandlerInterface.getTheMatches(leagueSelected);
        call.enqueue(new Callback<AllLeagueMatches>() {
            @Override
            public void onResponse(Call<AllLeagueMatches> call, Response<AllLeagueMatches> response) {
                allLeagueMatches = response.body();
                MatchesFragment.allLeagueMatches = allLeagueMatches;
            }

            @Override
            public void onFailure(Call<AllLeagueMatches> call, Throwable t) {
                Log.e("in enqueue", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getTeams(String league_name) {
        Call<TeamsOfLeague> call = apiHandlerInterface.getTeamsOfLeague(league_name);
        call.enqueue(new Callback<TeamsOfLeague>() {
            @Override
            public void onResponse(Call<TeamsOfLeague> call, Response<TeamsOfLeague> response) {
                Log.e("in enqueue", "onREspone: " + response.body());
                teamsOfLeague_selected = response.body();
                AdjustTeamsSpinner(teamsOfLeague_selected);
            }

            @Override
            public void onFailure(Call<TeamsOfLeague> call, Throwable t) {
                Log.e("in enqueue", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getLeagueStanding(String league_filter){
        Call<LeagueStanding> call = apiHandlerInterface.getLeagueStanding(league_filter);
        call.enqueue(new Callback<LeagueStanding>() {
            @Override
            public void onResponse(Call<LeagueStanding> call, Response<LeagueStanding> response) {
                leagueStanding = response.body();
                LeagueTableFragment.leagueStanding = leagueStanding;
            }

            @Override
            public void onFailure(Call<LeagueStanding> call, Throwable t) {
                Log.e("in enqueue", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getTeamInfo(String id) {
        Call<TeamSquad> call = apiHandlerInterface.getTeamSquad(Integer.parseInt(id));
        call.enqueue(new Callback<TeamSquad>() {
            @Override
            public void onResponse(Call<TeamSquad> call, Response<TeamSquad> response) {
                teamInfo = response.body();
                MyTeamFragment.teamSquad = teamInfo;
                insertTeamInfoToDataBase(teamInfo);
            }

            @Override
            public void onFailure(Call<TeamSquad> call, Throwable t) {
                Log.e("in enqueue", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void getFavoriteMatches() {
        Call<FavoriteTeamMatches> call = apiHandlerInterface.getFavoriteTeamMatches(teamID);
        call.enqueue(new Callback<FavoriteTeamMatches>() {
            @Override
            public void onResponse(Call<FavoriteTeamMatches> call, Response<FavoriteTeamMatches> response) {
                FavoritesFragment.favoriteTeamMatches = response.body();
            }

            @Override
            public void onFailure(Call<FavoriteTeamMatches> call, Throwable t) {
                Log.e("in enqueue", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
    /*API calls*/
}
