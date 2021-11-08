package com.example.footyapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.footyapp.ImagesApiClasses.Player;
import com.example.footyapp.ImagesApiClasses.PlayerImagesAndMore;
import com.example.footyapp.otherApiClasses.PlayersOtherApi;
import com.example.footyapp.otherApiClasses.Team;
import com.example.footyapp.otherApiClasses.TeamsOtherApi;
import com.example.footyapp.pojos.Match;
import com.example.footyapp.pojos.Squad;
import com.example.footyapp.pojos.Table;
import com.example.footyapp.ui.Favorites.FavoritesFragment;
import com.example.footyapp.ui.League.LeagueTableFragment;
import com.example.footyapp.ui.TeamSquad.MyTeamFragment;
import com.example.footyapp.ui.matches.MatchesFragment;

import java.util.ArrayList;

import retrofit2.Call;

//AppCompatActivity
public class TriviaActivity extends FragmentActivity {

    private Context mContext= this;

    public static boolean isUserChangedFavorite = false;

    ApiHandlerInterfaceOtherApi apiHandlerInterfaceOtherApi = null;
    int leagueIdOtherApi = -1;
    Call<TeamsOtherApi> call = null;
    static TeamsOtherApi teamsOtherApi = null;
    Call<PlayersOtherApi> callPlayersOtherApi = null;
    static PlayersOtherApi playersOtherApi = null;

    ApiHandlerInterfaceImages apiHandlerInterfaceImages = null;
    Call<PlayerImagesAndMore> callPlayerAndImages;
    PlayerImagesAndMore playerImagesAndMore;
    Player mostExpansivePlayer = null;
    String mostExpansivePlayerName;
    String mostExpansivePlayerStringSigning;

    String favoriteLeague;
    String favoriteTeam;

    TextView textViewTitle;

    Button oldestClubQuestion;
    static boolean oldestClubOpen = false;
    EditText oldestClubAnswer;

    Button newestClubQuestion;
    static boolean newestClubOpen = false;
    EditText newestClubAnswer;

    Button biggerStadiumQuestion;
    static boolean biggerStadiumOpen = false;
    EditText biggerStadiumAnswer;

    Button expansivePlayerQuestion;
    static boolean expansivePlayerOpen = false;
    EditText expansivePlayerAnswer;

    Button teamScoreMostGoalsQuestion;
    static boolean teamScoreMostGoalsOpen = false;
    EditText teamScoreMostGoalsAnswer;

    Button yourTeamScoreAndSufferedQuestion;
    static boolean yourTeamScoreAndSufferedOpen = false;
    EditText yourTeamScoreAndSufferedAnswer;

    Button theYoungestPlayerInTeamQuestion;
    static boolean theYoungestPlayerInTeamOpen = false;
    EditText theYoungestPlayerInTeamAnswer;

    Button playerAgeAverageQuestion;
    static boolean playerAgeAverageOpen = false;
    EditText playerAgeAverageAnswer;

    Button tallestPlayerQuestion;
    static boolean tallestPlayerOpen = false;
    EditText tallestPlayerAnswer;

    Button gameWithMostGoalsQuestion;
    static boolean gameWithMostGoalsOpen = false;
    EditText gameWithMostGoalsAnswer;

    Button biggestWinQuestion;
    static boolean biggestWinOpen = false;
    EditText biggestWinAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!MainActivity.changeMode){
            setTheme(R.style.LightTheme);
        }
        else{
            setTheme(R.style.DarkTheme);
        }

        setContentView(R.layout.activity_trivia);

        if(isUserChangedFavorite){
            teamsOtherApi = null;
            leagueIdOtherApi = -1;

            playersOtherApi = null;

            mostExpansivePlayer = null;

            isUserChangedFavorite = false;
        }

        if(apiHandlerInterfaceOtherApi == null)
            apiHandlerInterfaceOtherApi = ApiClientOtherApi.createService(ApiHandlerInterfaceOtherApi.class);

        if(leagueIdOtherApi == -1)
            leagueIdOtherApi = giveMeLeagueIdOfOtherApi(SittingsActivity.teamsOfLeague_selected.getCompetition().getName());

        if(call == null)
            call = apiHandlerInterfaceOtherApi.getTeamsOtherApi(leagueIdOtherApi);

        if(apiHandlerInterfaceImages == null)
            apiHandlerInterfaceImages = ApiClientImages.createService(ApiHandlerInterfaceImages.class);

        /*nice question mark gifs*/
        //https://i.gifer.com/XlO9.gif
        //https://i.gifer.com/72gi.gif
        //https://i.gifer.com/4wd8.gif
        //https://i.gifer.com/fyhz.gif
        //https://i.gifer.com/4Jo1.gif

        Glide.with(this).load("https://i.gifer.com/72gi.gif").into((ImageView)findViewById(R.id.triviaGif));
        Glide.with(this).load("https://i.gifer.com/72gi.gif").into((ImageView)findViewById(R.id.triviaGifTwo));

        assignFavoriteLeagueAndFavoriteTeam();

        textViewTitle = findViewById(R.id.txtViewTriviaTitleLeagueAndTeam);
        textViewTitle.setText("-The " + favoriteLeague + " And " + favoriteTeam + "-");

        oldestClubQuestion = findViewById(R.id.question_whoIsTheOldestClub);
        oldestClubQuestion.setText("Who is the oldest club in the " + favoriteLeague + " ?");
        oldestClubAnswer = findViewById(R.id.answer_whoIsTheOldestClub);
        oldestClubAnswer.setVisibility(View.GONE);

        newestClubQuestion = findViewById(R.id.question_whoIsTheNewestClub);
        newestClubQuestion.setText("Who is the newest club in the " + favoriteLeague + " ?");
        newestClubAnswer = findViewById(R.id.answer_whoIsTheNewestClub);
        newestClubAnswer.setVisibility(View.GONE);

        biggerStadiumQuestion = findViewById(R.id.question_whichStadiumInThePremierLeagueIsTheBigger);
        biggerStadiumQuestion.setText("Which Stadium in the " + favoriteLeague + " is the bigger ?");
        biggerStadiumAnswer = findViewById(R.id.answer_whichStadiumInThePremierLeagueIsTheBigger);
        biggerStadiumAnswer.setVisibility(View.GONE);

        expansivePlayerQuestion = findViewById(R.id.question_whichPlayerInYourTeamMostExpansiveSigningWise);
        expansivePlayerQuestion.setText("Which player in "  + favoriteTeam + " is the Most Expansive(Signing Wise) ?");
        expansivePlayerAnswer = findViewById(R.id.answer_whichPlayerInYourTeamMostExpansiveSigningWise);
        expansivePlayerAnswer.setVisibility(View.GONE);

        teamScoreMostGoalsQuestion = findViewById(R.id.question_whichTeamScoreMostGoalsThisSeason);
        teamScoreMostGoalsAnswer = findViewById(R.id.answer_whichTeamScoreMostGoalsThisSeason);
        teamScoreMostGoalsAnswer.setVisibility(View.GONE);

        yourTeamScoreAndSufferedQuestion = findViewById(R.id.question_howManyGoalsYourTeamScoreAndSuffered);
        yourTeamScoreAndSufferedQuestion.setText("How Many Goals " + favoriteTeam + " Score ? How many Goals " + favoriteTeam + " Suffered ?");
        yourTeamScoreAndSufferedAnswer = findViewById(R.id.answer_howManyGoalsYourTeamScoreAndSuffered);
        yourTeamScoreAndSufferedAnswer.setVisibility(View.GONE);

        theYoungestPlayerInTeamQuestion = findViewById(R.id.question_whoIsTheYoungestPlayerInYourTeam);
        theYoungestPlayerInTeamQuestion.setText("Who is The youngest player in " + favoriteTeam + " ?");
        theYoungestPlayerInTeamAnswer = findViewById(R.id.answer_whoIsTheYoungestPlayerInYourTeam);
        theYoungestPlayerInTeamAnswer.setVisibility(View.GONE);

        playerAgeAverageQuestion = findViewById(R.id.question_whatIsTheAgeAverageInYourTeam);
        playerAgeAverageQuestion.setText("What Is The player's Age Average In " + favoriteTeam + " ?");
        playerAgeAverageAnswer = findViewById(R.id.answer_whatIsTheAgeAverageInYourTeam);
        playerAgeAverageAnswer.setVisibility(View.GONE);

        tallestPlayerQuestion = findViewById(R.id.question_whoIsRheTallestPlayer);
        tallestPlayerQuestion.setText("Who is the tallest player In " + favoriteTeam + " ?");
        tallestPlayerAnswer = findViewById(R.id.answer_whoIsRheTallestPlayer);
        tallestPlayerAnswer.setVisibility(View.GONE);

        gameWithMostGoalsQuestion = findViewById(R.id.question_inWhichGameMostGoalsHasScored);
        gameWithMostGoalsAnswer = findViewById(R.id.answer_inWhichGameMostGoalsHasScored);
        gameWithMostGoalsAnswer.setVisibility(View.GONE);

        biggestWinQuestion = findViewById(R.id.question_biggestWin);
        biggestWinQuestion.setText("Biggest Win of " + favoriteTeam + " this season ?");
        biggestWinAnswer = findViewById(R.id.answer_biggestWin);
        biggestWinAnswer.setVisibility(View.GONE);

        oldestClubQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The oldest club in the " + favoriteLeague + " is: ";

                Thread thread;

                if(teamsOtherApi == null){
                    thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try
                            {
                                teamsOtherApi = call.execute().body();
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
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Team oldestClub = teamsOtherApi.getApi().getTeams().get(0);
                int clubFounded = oldestClub.getFounded();

                for(Team team : teamsOtherApi.getApi().getTeams()){
                    if(team.getFounded() <  clubFounded){
                        oldestClub = team;
                        clubFounded = team.getFounded();
                    }
                }

                answer += oldestClub.getName() + ".";
                answer += "\nThe club founded at " + clubFounded + ".";

                oldestClubAnswer.setText(answer);

                if(!oldestClubOpen){
                    oldestClubAnswer.setVisibility(View.VISIBLE);
                    oldestClubQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    oldestClubAnswer.setVisibility(View.GONE);
                    oldestClubQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                oldestClubOpen = !oldestClubOpen;
            }
        });


        newestClubQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The newest club in the " + favoriteLeague + " is: ";

                Thread thread;

                if(teamsOtherApi == null){
                    thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try
                            {
                                teamsOtherApi = call.execute().body();
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
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Team newestClub = teamsOtherApi.getApi().getTeams().get(0);
                int clubFounded = newestClub.getFounded();

                for(Team team : teamsOtherApi.getApi().getTeams()){
                    if(team.getFounded() >  clubFounded){
                        newestClub = team;
                        clubFounded = team.getFounded();
                    }
                }

                answer += newestClub.getName() + ".";
                answer += "\nThe club founded at " + clubFounded + ".";

                newestClubAnswer.setText(answer);

                if(!newestClubOpen){
                    newestClubAnswer.setVisibility(View.VISIBLE);
                    newestClubQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    newestClubAnswer.setVisibility(View.GONE);
                    newestClubQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                newestClubOpen = !newestClubOpen;
            }
        });


        biggerStadiumQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The Bigger Stadium in the " + favoriteLeague + " is: ";

                Thread thread;

                if(teamsOtherApi == null){
                    thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try
                            {
                                teamsOtherApi = call.execute().body();
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
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Team teamThatHaveTheBiggerStadium = teamsOtherApi.getApi().getTeams().get(0);
                String theStadium = teamThatHaveTheBiggerStadium.getVenueName();
                int stadiumCapacity = teamThatHaveTheBiggerStadium.getVenueCapacity();

                for(Team team : teamsOtherApi.getApi().getTeams()){
                    if(team.getVenueCapacity() > stadiumCapacity){
                        teamThatHaveTheBiggerStadium = team;
                        theStadium = team.getVenueName();
                        stadiumCapacity = team.getVenueCapacity();
                    }
                }

                answer += theStadium + ".";
                answer += "\nThe team that play in this stadium is: " + teamThatHaveTheBiggerStadium.getName() + ".";
                answer += "\nThe Stadium Capacity is: " + stadiumCapacity + " people.";

                biggerStadiumAnswer.setText(answer);

                if(!biggerStadiumOpen){
                    biggerStadiumAnswer.setVisibility(View.VISIBLE);
                    biggerStadiumQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    biggerStadiumAnswer.setVisibility(View.GONE);
                    biggerStadiumQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                biggerStadiumOpen = !biggerStadiumOpen;
            }
        });


        expansivePlayerQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The Most Expansive player(signing wise) in your" +
                        " Favorite Team(" + favoriteTeam + ") is: ";

                if(mostExpansivePlayer == null){
                    mostExpansivePlayerName = "";
                    mostExpansivePlayerStringSigning = "";

                    float signingMoney = 0;

                    String teamShortName = MyTeamFragment.teamSquad.getShortName();

                    if(teamShortName.equals("Barça"))
                        teamShortName = "Barcelona";

                    for(Squad player : MyTeamFragment.teamSquad.getSquad()){
                        callPlayerAndImages = apiHandlerInterfaceImages.getPlayerImagesAndMore(teamShortName,player.getName());

                        Thread thread;

                        thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try
                                {
                                    playerImagesAndMore = callPlayerAndImages.execute().body();
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

                        if(playerImagesAndMore != null){
                            if(playerImagesAndMore.getPlayer() != null){
                               if(playerImagesAndMore.getPlayer().get(0).getStrSigning() != null){
                                   String notParseSigningMoney = playerImagesAndMore.getPlayer().get(0).getStrSigning();

                                   if(notParseSigningMoney.equals(""))
                                       continue;

                                   if(!Character.isDigit(notParseSigningMoney.charAt(0)))
                                       continue;

                                   String parseSigningMoney = "";
                                   int lala = 0;

                                   for(int i = 0; i <= notParseSigningMoney.length()-1; i++){
                                       if(notParseSigningMoney.charAt(i) == ' ' || notParseSigningMoney.charAt(i) == 'M'){
                                           break;
                                       }

                                       if(notParseSigningMoney.charAt(i) == ','){
                                           lala++;

                                           if(lala == 2)
                                               break;

                                           parseSigningMoney += ".";
                                       }
                                       else{
                                           parseSigningMoney += notParseSigningMoney.charAt(i);
                                       }
                                   }

                                   if(Float.parseFloat(parseSigningMoney) > signingMoney){
                                       mostExpansivePlayer = playerImagesAndMore.getPlayer().get(0);
                                       signingMoney = Float.parseFloat(parseSigningMoney);
                                       mostExpansivePlayerName = player.getName();
                                       mostExpansivePlayerStringSigning = playerImagesAndMore.getPlayer().get(0).getStrSigning();
                                   }
                               }
                            }
                        }
                    }
                }

                answer += mostExpansivePlayerName + ".";
                answer += "\nThe Transfer to " + favoriteTeam + " coast to " + favoriteTeam + ": "
                            + mostExpansivePlayerStringSigning;

                expansivePlayerAnswer.setText(answer);

                if(!expansivePlayerOpen){
                    expansivePlayerAnswer.setVisibility(View.VISIBLE);
                    expansivePlayerQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    expansivePlayerAnswer.setVisibility(View.GONE);
                    expansivePlayerQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                expansivePlayerOpen = !expansivePlayerOpen;
            }
        });


        teamScoreMostGoalsQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The team that score most GOALS this season(so far) is: ";

                String teamThatScoreMostName = "";
                int teamGoals = 0;

                if(LeagueTableFragment.leagueStanding != null){
                    for(Table table : LeagueTableFragment.leagueStanding.getStandings().get(0).getTable()){
                        if(table.getGoalsFor() > teamGoals){
                            teamThatScoreMostName = table.getTeam().getName();
                            teamGoals = table.getGoalsFor();
                        }
                    }
                }

                answer += teamThatScoreMostName + ".";
                answer += "\nThe team scored so far " + teamGoals + " Goals.";

                teamScoreMostGoalsAnswer.setText(answer);

                if(!teamScoreMostGoalsOpen){
                    teamScoreMostGoalsAnswer.setVisibility(View.VISIBLE);
                    teamScoreMostGoalsQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    teamScoreMostGoalsAnswer.setVisibility(View.GONE);
                    teamScoreMostGoalsQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                teamScoreMostGoalsOpen = !teamScoreMostGoalsOpen;
            }
        });


        yourTeamScoreAndSufferedQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = favoriteTeam + " Scored this season so far ";

                int goalsFor = 0;
                int goalsAgainst = 0;

                if(LeagueTableFragment.leagueStanding != null){
                    for(Table table : LeagueTableFragment.leagueStanding.getStandings().get(0).getTable()){
                        if(!(SittingsActivity.teamSelected.equals(table.getTeam().getName())))
                            continue;

                        goalsFor = table.getGoalsFor();
                        goalsAgainst = table.getGoalsAgainst();
                        break;
                    }
                }

                answer += goalsFor + " Goals.";
                answer += "\nAnd " + favoriteTeam + " suffered so far " + goalsAgainst + " Goals.";

                yourTeamScoreAndSufferedAnswer.setText(answer);

                if(!yourTeamScoreAndSufferedOpen){
                    yourTeamScoreAndSufferedAnswer.setVisibility(View.VISIBLE);
                    yourTeamScoreAndSufferedQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    yourTeamScoreAndSufferedAnswer.setVisibility(View.GONE);
                    yourTeamScoreAndSufferedQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                yourTeamScoreAndSufferedOpen = !yourTeamScoreAndSufferedOpen;
            }
        });


        theYoungestPlayerInTeamQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The Youngest player in " + favoriteTeam + " is: ";

                Thread thread;

                if(playersOtherApi == null){
                    if(teamsOtherApi == null){
                        thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try
                                {
                                    teamsOtherApi = call.execute().body();
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
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int teamIdOtherApi = -1;

                    if(teamsOtherApi != null)
                    {
                        for(Team team : teamsOtherApi.getApi().getTeams())
                        {
                            if(team.getName().equals("Bayern Munich"))
                                team.setName("Bayern");

                            if(team.getName().equals("Borussia Dortmund"))
                                team.setName("Borussia");

                            if(team.getName().equals("Bayer Leverkusen"))
                                team.setName("Leverkusen");

                            if(team.getName().equals("Paris Saint Germain"))
                                team.setName("Paris");

                            if(SittingsActivity.teamSelected.contains(team.getName()))
                            {
                                teamIdOtherApi = team.getTeamId();
                                break;
                            }
                        }
                    }

                    if(teamIdOtherApi != -1){
                        callPlayersOtherApi = apiHandlerInterfaceOtherApi.getPlayersOtherApi(teamIdOtherApi);

                        thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try
                                {
                                    playersOtherApi = callPlayersOtherApi.execute().body();
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
                    }
                }

                int playerYoungestAge = 100;
                String playerYoungestName = "";

                if(playersOtherApi.getApi() != null){
                    if(playersOtherApi.getApi().getPlayers() != null){
                        for(com.example.footyapp.otherApiClasses.Player p : playersOtherApi.getApi().getPlayers()){
                            if(p.getAge() < playerYoungestAge){
                                playerYoungestAge = p.getAge();
                                playerYoungestName = p.getPlayerName();
                            }
                        }
                    }
                }

                answer += playerYoungestName + ".";
                answer += "\nThe age of " + playerYoungestName + " is: " + playerYoungestAge + ".";

                theYoungestPlayerInTeamAnswer.setText(answer);

                if(!theYoungestPlayerInTeamOpen){
                    theYoungestPlayerInTeamAnswer.setVisibility(View.VISIBLE);
                    theYoungestPlayerInTeamQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    theYoungestPlayerInTeamAnswer.setVisibility(View.GONE);
                    theYoungestPlayerInTeamQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                theYoungestPlayerInTeamOpen = !theYoungestPlayerInTeamOpen;
            }
        });


        playerAgeAverageQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The player's age average in " + favoriteTeam + " is ";

                Thread thread;

                if(playersOtherApi == null){
                    if(teamsOtherApi == null){
                        thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try
                                {
                                    teamsOtherApi = call.execute().body();
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
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int teamIdOtherApi = -1;

                    if(teamsOtherApi != null)
                    {
                        for(Team team : teamsOtherApi.getApi().getTeams())
                        {
                            if(team.getName().equals("Bayern Munich"))
                                team.setName("Bayern");

                            if(team.getName().equals("Borussia Dortmund"))
                                team.setName("Borussia");

                            if(team.getName().equals("Bayer Leverkusen"))
                                team.setName("Leverkusen");

                            if(team.getName().equals("Paris Saint Germain"))
                                team.setName("Paris");

                            if(SittingsActivity.teamSelected.contains(team.getName()))
                            {
                                teamIdOtherApi = team.getTeamId();
                                break;
                            }
                        }
                    }

                    if(teamIdOtherApi != -1){
                        callPlayersOtherApi = apiHandlerInterfaceOtherApi.getPlayersOtherApi(teamIdOtherApi);

                        thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try
                                {
                                    playersOtherApi = callPlayersOtherApi.execute().body();
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
                    }
                }

                int numberOfPlayers = 28;
                float playersAverageAge = 28;

                if(playersOtherApi.getApi() != null){
                    if(playersOtherApi.getApi().getPlayers() != null){
                        numberOfPlayers = playersOtherApi.getApi().getPlayers().size();
                        playersAverageAge = 0;
                        for(com.example.footyapp.otherApiClasses.Player p : playersOtherApi.getApi().getPlayers()){
                            playersAverageAge +=  p.getAge().floatValue();
                        }

                        playersAverageAge /= numberOfPlayers;
                    }
                }

                answer += String.format("%.02f",playersAverageAge) + ".";

                playerAgeAverageAnswer.setText(answer);

                if(!playerAgeAverageOpen){
                    playerAgeAverageAnswer.setVisibility(View.VISIBLE);
                    playerAgeAverageQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    playerAgeAverageAnswer.setVisibility(View.GONE);
                    playerAgeAverageQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                playerAgeAverageOpen = !playerAgeAverageOpen;
            }
        });

        tallestPlayerQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The tallest player in " + favoriteTeam + " is ";

                Thread thread;

                if(playersOtherApi == null){
                    if(teamsOtherApi == null){
                        thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try
                                {
                                    teamsOtherApi = call.execute().body();
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
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int teamIdOtherApi = -1;

                    if(teamsOtherApi != null)
                    {
                        for(Team team : teamsOtherApi.getApi().getTeams())
                        {
                            if(team.getName().equals("Bayern Munich"))
                                team.setName("Bayern");

                            if(team.getName().equals("Borussia Dortmund"))
                                team.setName("Borussia");

                            if(team.getName().equals("Bayer Leverkusen"))
                                team.setName("Leverkusen");

                            if(team.getName().equals("Paris Saint Germain"))
                                team.setName("Paris");

                            if(SittingsActivity.teamSelected.contains(team.getName()))
                            {
                                teamIdOtherApi = team.getTeamId();
                                break;
                            }
                        }
                    }

                    if(teamIdOtherApi != -1){
                        callPlayersOtherApi = apiHandlerInterfaceOtherApi.getPlayersOtherApi(teamIdOtherApi);

                        thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try
                                {
                                    playersOtherApi = callPlayersOtherApi.execute().body();
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
                    }
                }

                String tallestPlayerName = "";
                int tallestPlayerHeight = 187;
                String heightAsString = "";

                if(playersOtherApi.getApi() != null){
                    if(playersOtherApi.getApi().getPlayers() != null){
                        tallestPlayerName = playersOtherApi.getApi().getPlayers().get(0).getPlayerName();

                        if(playersOtherApi.getApi().getPlayers().get(0).getHeight() != null){
                            String tempString = playersOtherApi.getApi().getPlayers().get(0).getHeight();
                            for(int i = 0; i <= tempString.length() - 1; i++){
                                if(!Character.isDigit(tempString.charAt(i)))
                                    break;

                                heightAsString += tempString.charAt(i);
                            }

                            tallestPlayerHeight = Integer.parseInt(heightAsString);
                        }

                        if(heightAsString.equals(""))
                            heightAsString = "187";

                        for(com.example.footyapp.otherApiClasses.Player p : playersOtherApi.getApi().getPlayers()){
                            String tempString = p.getHeight();

                            if(tempString == null)
                                continue;

                            String someString = "";

                            for(int i = 0; i <= tempString.length() - 1; i++){
                                if(!Character.isDigit(tempString.charAt(i)))
                                    break;

                                someString += tempString.charAt(i);
                            }

                            if(someString.equals(""))
                                continue;

                            int tempHeightAsInt = Integer.parseInt(someString);

                            if(tempHeightAsInt > tallestPlayerHeight){
                                tallestPlayerHeight = tempHeightAsInt;
                                tallestPlayerName = p.getPlayerName();
                            }
                        }
                    }
                }

                answer += tallestPlayerName + ".";
                answer += "\nThe Height of " + tallestPlayerName + " is " + tallestPlayerHeight + " cm.";

                tallestPlayerAnswer.setText(answer);

                if(!tallestPlayerOpen){
                    tallestPlayerAnswer.setVisibility(View.VISIBLE);
                    tallestPlayerQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    tallestPlayerAnswer.setVisibility(View.GONE);
                    tallestPlayerQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                tallestPlayerOpen = !tallestPlayerOpen;
            }
        });


        gameWithMostGoalsQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The game in which the most goals were scored were between ";

                String homeTeam = "";
                String awayTeam = "";
                int goalsHomeTeam = 0;
                int goalsAwayTeam = 0;
                int matchDay = 0;

                int mostGoals = 0;

                if(MatchesFragment.allLeagueMatches != null){
                    for(Match match : MatchesFragment.allLeagueMatches.getMatches()){
                        if(match.getMatchday() > match.getSeason().getCurrentMatchday())
                            break;

                        if(!match.getStatus().toUpperCase().equals("FINISHED"))
                            continue;

                        int goalsThatScoredInMatch;

                        if(match.getScore().getFullTime().getHomeTeam() == null ||
                                match.getScore().getFullTime().getAwayTeam() == null){
                            continue;
                        }

                        goalsThatScoredInMatch = match.getScore().getFullTime().getHomeTeam() +
                                        match.getScore().getFullTime().getAwayTeam();

                        if(goalsThatScoredInMatch > mostGoals){
                            mostGoals = goalsThatScoredInMatch;
                            homeTeam = match.getHomeTeam().getName();
                            awayTeam = match.getAwayTeam().getName();
                            matchDay = match.getMatchday();
                            goalsHomeTeam = match.getScore().getFullTime().getHomeTeam();
                            goalsAwayTeam =  match.getScore().getFullTime().getAwayTeam();
                        }
                    }
                }

                answer += homeTeam + " vs " + awayTeam + ".";
                answer += "\nThe number of goals were scored in this game are " + mostGoals + ".";
                answer += "\nFinal score was ";

                if(goalsHomeTeam == goalsAwayTeam){
                    answer += goalsHomeTeam + "-" + goalsAwayTeam;
                }
                else{
                    if(goalsHomeTeam > goalsAwayTeam){
                        answer += goalsHomeTeam + "-" + goalsAwayTeam + " in favor of " + homeTeam;
                    }
                    else{
                        answer += goalsAwayTeam + "-" + goalsHomeTeam + " in favor of " + awayTeam;
                    }
                }

                answer += ".\nThe match took place at Match Day " + matchDay + ".";

                gameWithMostGoalsAnswer.setText(answer);

                if(!gameWithMostGoalsOpen){
                    gameWithMostGoalsAnswer.setVisibility(View.VISIBLE);
                    gameWithMostGoalsQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    gameWithMostGoalsAnswer.setVisibility(View.GONE);
                    gameWithMostGoalsQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                gameWithMostGoalsOpen = !gameWithMostGoalsOpen;
            }
        });


        biggestWinQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = "The Biggest win of " + favoriteTeam + " this season was ";

                String myTeam = SittingsActivity.teamSelected;
                boolean isMyTeamWon = false;
                int teamMostGoals = 0;
                int rivalGoals = 0;
                String againstWho = "";

                if(FavoritesFragment.favoriteTeamMatches != null){
                    for(Match match : FavoritesFragment.favoriteTeamMatches.getMatches()){
                        if(match.getMatchday() > match.getSeason().getCurrentMatchday())
                            break;

                        if(!match.getStatus().toUpperCase().equals("FINISHED"))
                            continue;

                        if(match.getScore().getWinner() == null)
                            continue;

                        if(!match.getScore().getWinner().equals("HOME_TEAM") &&
                                !match.getScore().getWinner().equals("AWAY_TEAM")){
                            continue;
                        }

                       if(match.getScore().getWinner().equals("HOME_TEAM") &&
                                !match.getHomeTeam().getName().equals(myTeam)){
                           continue;
                       }

                       if(match.getScore().getWinner().equals("AWAY_TEAM") &&
                                !match.getAwayTeam().getName().equals(myTeam)){
                            continue;
                        }

                        isMyTeamWon = true;

                        int tempGoalsThing = 0;

                        if(match.getHomeTeam().getName().equals(myTeam)){
                            tempGoalsThing = match.getScore().getFullTime().getHomeTeam();
                        }
                        else{
                            tempGoalsThing = match.getScore().getFullTime().getAwayTeam();
                        }

                        if(tempGoalsThing > teamMostGoals){
                            teamMostGoals = tempGoalsThing;
                            rivalGoals = match.getHomeTeam().getName().equals(myTeam) ? match.getScore().getFullTime().getAwayTeam() :
                                    match.getScore().getFullTime().getHomeTeam();

                            againstWho = match.getHomeTeam().getName().equals(myTeam) ? match.getAwayTeam().getName() :
                                    match.getHomeTeam().getName();
                        }
                    }
                }

                if(!isMyTeamWon){
                    answer = favoriteTeam + " not won games yet this season.";
                }
                else{
                    answer += teamMostGoals + ":" + rivalGoals + " against " + againstWho + ".";
                }

                biggestWinAnswer.setText(answer);

                if(!biggestWinOpen){
                    biggestWinAnswer.setVisibility(View.VISIBLE);
                    biggestWinQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_up, 0);
                }
                else{
                    biggestWinAnswer.setVisibility(View.GONE);
                    biggestWinQuestion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
                }

                biggestWinOpen = !biggestWinOpen;
            }
        });
    }

    private void assignFavoriteLeagueAndFavoriteTeam(){
        switch (SittingsActivity.leagueSelected) {
            case "PL":
                favoriteLeague = "Premier League";
                break;
            case "FL1":
                favoriteLeague = "Ligue 1";
                break;
            case "SA":
                favoriteLeague = "Série A";
                break;
            case "BL1":
                favoriteLeague = "Bundesliga";
                break;
            case "PD":
                favoriteLeague = "Primera División";
                break;
            default:
                favoriteLeague = "";
                break;
        }

        favoriteTeam = null;

        for(com.example.footyapp.pojos.Team team : SittingsActivity.teamsOfLeague_selected.getTeams())
        {
            if(SittingsActivity.teamSelected.equals(team.getName()))
                favoriteTeam = team.getShortName();

            if(favoriteTeam != null)
                break;
        }
    }

    int giveMeLeagueIdOfOtherApi(String leagueName){
        switch(leagueName){
            case "Premier League":
                return 2;
            case "Ligue 1":
                return 4;
            case "Serie A":
                return 28;
            case "Bundesliga":
                return 8;
            case "Primera Division":
                return 30;
            default:
                return 0;
        }
    }
}