package com.example.footyapp.ui.Favorites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.footyapp.ApiClientSynchronise;
import com.example.footyapp.ApiHandlerInterfaceSynchronise;
import com.example.footyapp.MainActivity;
import com.example.footyapp.R;
import com.example.footyapp.SittingsActivity;
import com.example.footyapp.VideoViewActivity;
import com.example.footyapp.pojos.FavoriteTeamMatches;
import com.example.footyapp.pojos.HeadToHead;
import com.example.footyapp.pojos.Match;
import com.example.footyapp.pojos.Team;
import com.example.footyapp.ui.matches.MatchesAdapter;
import com.example.footyapp.ui.matches.MatchesViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;


public class FavoritesFragment extends Fragment {

    private View view;
    private ListView mListView;
    private MatchesViewModel matchesViewModel;

    //ApiHandlerInterface apiHandlerInterface = null;

    private MatchesAdapter matchesAdapter;
    public static FavoriteTeamMatches favoriteTeamMatches = null;
    //private static HeadToHead headTohead = null;

    static Random random = new Random();
    static boolean someFlag = true;
    String someVideoLink = "";

    /***************************************************/
    ApiHandlerInterfaceSynchronise apiHandlerInterfaceSynchronise = null;
    Call<HeadToHead> call = null;
    HeadToHead h2h = null;
    /***************************************************/

    public static List<String> videosHighLightsListPL = new ArrayList<>();
    public static List<String> videosHighLightsListPD = new ArrayList<>();
    public static List<String> videosHighLightsListBL1 = new ArrayList<>();
    public static List<String> videosHighLightsListSA = new ArrayList<>();
    public static List<String> videosHighLightsListFL1 = new ArrayList<>();

    Match someMatch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_favorites, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        mListView = (ListView)view.findViewById(R.id.listView_favorite_matches);
        ArrayList<Match> matches = new ArrayList<>();

        //if(apiHandlerInterface == null)
            //apiHandlerInterface = ApiClient.getClient().create(ApiHandlerInterface.class);

        if(apiHandlerInterfaceSynchronise == null)
            apiHandlerInterfaceSynchronise = ApiClientSynchronise.createService(ApiHandlerInterfaceSynchronise.class);

        if(someFlag){
            addSomeVideosHighLightsToTheList();
            someFlag = !someFlag;
        }

        if(favoriteTeamMatches != null){

            setMatchesTitleWithSomeImage();

            for(Match match : favoriteTeamMatches.getMatches())
            {
                matches.add(match);
            }

            matchesAdapter = new MatchesAdapter(getContext(), R.layout.row_match,matches,favoriteTeamMatches.getMatches().get(0).getSeason().getCurrentMatchday());
            mListView.setAdapter(matchesAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Match selectedMatch = (Match) parent.getItemAtPosition(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialog);//R.style.AlertDialogCustom);

                    ArrayList<String> favoriteMatchDetails = new ArrayList<>();

                    //String message = "\t\t\t\t\t\t\t#date: " + selectedMatch.getUtcDate().substring(0,10) + "\n" +
                            //"\t\t\t\t\t\t\t#time: " + selectedMatch.getUtcDate().substring(11,selectedMatch.getUtcDate().length()-1) + "\n" +
                            //"\t\t\t\t\t\t\t#status: " + selectedMatch.getStatus() + "\n" +
                            //"\t\t\t\t\t\t\t#stadium: ";


                    favoriteMatchDetails.add("Date: " + selectedMatch.getUtcDate().substring(0,10));
                    favoriteMatchDetails.add("Time: " + selectedMatch.getUtcDate().substring(11,selectedMatch.getUtcDate().length()-1));
                    favoriteMatchDetails.add("Status: " + selectedMatch.getStatus());


                    String matchStadium = "";

                    for(Team team : SittingsActivity.teamsOfLeague_selected.getTeams()){
                        if(selectedMatch.getHomeTeam().getName().equals(team.getName()))
                        {
                            matchStadium = team.getVenue();
                            break;
                        }
                    }

                    //message += matchStadium;

                    favoriteMatchDetails.add("Stadium: " + matchStadium);

                    String winnerDrawOrNone = selectedMatch.getScore().getWinner();

                    if(winnerDrawOrNone != null)
                    {
                        if(winnerDrawOrNone.equals("HOME_TEAM"))
                            winnerDrawOrNone = selectedMatch.getHomeTeam().getName();
                        else if(winnerDrawOrNone.equals("AWAY_TEAM"))
                            winnerDrawOrNone = selectedMatch.getAwayTeam().getName();
                        else
                            winnerDrawOrNone = "draw";
                    }

                    if(selectedMatch.getStatus().toUpperCase().equals("FINISHED")){
                        //message += "\n\t\t\t\t\t\t\t#winner: " + winnerDrawOrNone;
                        //message += "\n\t\t\t\t\t\t\t(current match day: " + selectedMatch.getSeason().getCurrentMatchday().toString() + ")";

                        favoriteMatchDetails.add("Winner: " + winnerDrawOrNone);
                        favoriteMatchDetails.add("(current match day: " + selectedMatch.getSeason().getCurrentMatchday().toString() + ")");

                        switch(SittingsActivity.leagueSelected){
                            case "PL":
                                someVideoLink = videosHighLightsListPL.get(Math.abs(random.nextInt()) % videosHighLightsListPL.size());
                                break;
                            case "PD":
                                someVideoLink = videosHighLightsListPD.get(Math.abs(random.nextInt()) % videosHighLightsListPD.size());
                                break;
                            case "BL1":
                                someVideoLink = videosHighLightsListBL1.get(Math.abs(random.nextInt()) % videosHighLightsListBL1.size());
                                break;
                            case "SA":
                                someVideoLink = videosHighLightsListSA.get(Math.abs(random.nextInt()) % videosHighLightsListSA.size());
                                break;
                            case "FL1":
                                someVideoLink = videosHighLightsListFL1.get(Math.abs(random.nextInt()) % videosHighLightsListFL1.size());
                                break;
                            default:
                                break;
                        }

                        favoriteMatchDetails.add("Watch Highlights: " + Html.fromHtml("<a href=\"" + someVideoLink + "\">" + someVideoLink + "</a>"));
                        favoriteMatchDetails.add("Watch At App");
                    }
                    else
                    {
                        //message += "\n\t\t\t\t(current match day: " + selectedMatch.getSeason().getCurrentMatchday().toString() + ")";

                        favoriteMatchDetails.add("(current match day: " + selectedMatch.getSeason().getCurrentMatchday().toString() + ")\n");

                        call = apiHandlerInterfaceSynchronise.getMatchHeadToHeadSynchronise(selectedMatch.getId().toString());

                        Thread thread;

                        thread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try
                                    {
                                        h2h = call.execute().body();
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

                        if(h2h != null)
                        {
                            ArrayList<String> teamsShortNames = getTeamsShortNames(h2h.getHead2head().getHomeTeam().getName(),h2h.getHead2head().getAwayTeam().getName());

                            //message += "\n\n\t\t\t\t\t\t\t\t*head 2 head*\n";
                            //message += "#number of matches: " + h2h.getHead2head().getNumberOfMatches().toString() + "\n" +
                                    //"#total goals: " + h2h.getHead2head().getTotalGoals().toString() + "\n\n" +
                                    //teamsShortNames.get(0) + ":  #wins: " + h2h.getHead2head().getHomeTeam().getWins().toString() +
                                    //", draws: " + h2h.getHead2head().getHomeTeam().getDraws().toString() + ", loses: " + h2h.getHead2head().getHomeTeam().getLosses().toString() + "\n" +
                                    //"\n" + teamsShortNames.get(1) + ":  #wins: " + h2h.getHead2head().getAwayTeam().getWins().toString() +
                                    //", draws: " + h2h.getHead2head().getAwayTeam().getDraws().toString() + ", loses: " + h2h.getHead2head().getAwayTeam().getLosses().toString();


                            favoriteMatchDetails.add("\t\t\t\t\t\t\t\t\t\t\t\t*Head 2 Head*");
                            favoriteMatchDetails.add("Number of matches: " + h2h.getHead2head().getNumberOfMatches().toString());
                            favoriteMatchDetails.add("Total goals: " + h2h.getHead2head().getTotalGoals().toString() + "\n");


                            favoriteMatchDetails.add(teamsShortNames.get(0) + ":   #W: " + h2h.getHead2head().getHomeTeam().getWins().toString() +
                                                    ",    #D: " + h2h.getHead2head().getHomeTeam().getDraws().toString() +
                                                    ",    #L: " + h2h.getHead2head().getHomeTeam().getLosses().toString());

                            favoriteMatchDetails.add(teamsShortNames.get(1) + ":   #W: " + h2h.getHead2head().getAwayTeam().getWins().toString() +
                                    ",    #D: " + h2h.getHead2head().getAwayTeam().getDraws().toString()  +
                                    ",    #L: " + h2h.getHead2head().getAwayTeam().getLosses().toString() + "\n");


                            if(h2h.getHead2head().getNumberOfMatches() != 0)
                            {
                                //message += "\n\n\t\t\t\t\t\t\t\t*success ratio*\n" + h2h.getHead2head().getHomeTeam().getName() + ": " +
                                       //String.format("%.2f",h2h.getHead2head().getHomeTeam().getWins().floatValue() / h2h.getHead2head().getNumberOfMatches().floatValue() * 100.0) +
                                        //"%\n\n" + h2h.getHead2head().getAwayTeam().getName() + ": " +
                                        //String.format("%.2f",h2h.getHead2head().getAwayTeam().getWins().floatValue() / h2h.getHead2head().getNumberOfMatches().floatValue() * 100.0) + "%";

                                favoriteMatchDetails.add("\t\t\t\t\t\t\t\t\t\t\t\t*Success Ratio*");
                                favoriteMatchDetails.add(h2h.getHead2head().getHomeTeam().getName() + ": " +
                                        String.format("%.2f",h2h.getHead2head().getHomeTeam().getWins().floatValue() / h2h.getHead2head().getNumberOfMatches().floatValue() * 100.0) + "%");

                                favoriteMatchDetails.add(h2h.getHead2head().getAwayTeam().getName() + ": " +
                                        String.format("%.2f",h2h.getHead2head().getAwayTeam().getWins().floatValue() / h2h.getHead2head().getNumberOfMatches().floatValue() * 100.0) + "%");

                            }

                            h2h = null;
                        }
                    }
                    /*else
                    {
                        //get head2head
                        if(headTohead == null)
                            getHeadToHead(selectedMatch.getId().toString());

                        if(headTohead != null)
                        {
                            if(selectedMatch.getId().equals(headTohead.getMatch().getId()))
                            {
                                ArrayList<String> teamsShortNames = getTeamsShortNames(headTohead.getHead2head().getHomeTeam().getName(),headTohead.getHead2head().getAwayTeam().getName() );

                                message += "\n\n\t\t\t\t\t\t\t\t*head 2 head*\n";
                                message += "#number of matches: " + headTohead.getHead2head().getNumberOfMatches().toString() + "\n" +
                                        "#total goals: " + headTohead.getHead2head().getTotalGoals().toString() + "\n\n" +
                                        teamsShortNames.get(0) + ":  #wins: " + headTohead.getHead2head().getHomeTeam().getWins().toString() +
                                        ", draws: " + headTohead.getHead2head().getHomeTeam().getDraws().toString() + ", loses: " + headTohead.getHead2head().getHomeTeam().getLosses().toString() + "\n" +
                                        "\n" + teamsShortNames.get(1) + ":  #wins: " + headTohead.getHead2head().getAwayTeam().getWins().toString() +
                                        ", draws: " + headTohead.getHead2head().getAwayTeam().getDraws().toString() + ", loses: " + headTohead.getHead2head().getAwayTeam().getLosses().toString();

                                if(headTohead.getHead2head().getNumberOfMatches() != 0)
                                {
                                    message += "\n\n\t\t\t\t\t\t\t\t*success ratio*\n" + headTohead.getHead2head().getHomeTeam().getName() + ": " +
                                            String.format("%.2f",headTohead.getHead2head().getHomeTeam().getWins().floatValue() / headTohead.getHead2head().getNumberOfMatches().floatValue() * 100.0) +
                                            "%\n\n" + headTohead.getHead2head().getAwayTeam().getName() + ": " +
                                            String.format("%.2f",headTohead.getHead2head().getAwayTeam().getWins().floatValue() / headTohead.getHead2head().getNumberOfMatches().floatValue() * 100.0) + "%";
                                }

                                headTohead = null;
                            }
                        }
                    }*/

                    //SpannableString title = new SpannableString(selectedMatch.getHomeTeam().getName() + " vs " +
                            //selectedMatch.getAwayTeam().getName());

                    //title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);


                    String title = selectedMatch.getHomeTeam().getName() + " vs " +
                            selectedMatch.getAwayTeam().getName();


                    builder//.setMessage(message)
                            .setTitle(title);

                    someMatch = selectedMatch;

                    ListAdapter adapter = new ArrayAdapter<String>(
                            getContext(), R.layout.dialod_matches, favoriteMatchDetails) {

                        ViewHolder holder;
                        Drawable icon;

                        class ViewHolder {
                            ImageView icon;
                            TextView rowDetails;
                        }

                        public View getView(int position, View convertView,
                                            ViewGroup parent) {
                            final LayoutInflater inflater = (LayoutInflater) getContext()
                                    .getSystemService(
                                            Context.LAYOUT_INFLATER_SERVICE);

                            if (convertView == null) {
                                convertView = inflater.inflate(
                                        R.layout.dialod_matches, null);

                                holder = new ViewHolder();
                                holder.icon = (ImageView) convertView
                                        .findViewById(R.id.iconMatchDetails);
                                holder.rowDetails = (TextView) convertView
                                        .findViewById(R.id.txtViewRowDetails);

                                convertView.setTag(holder);
                            } else {
                                holder = (ViewHolder) convertView.getTag();
                            }

                            Drawable drawable = getResources().getDrawable(R.drawable.ic_some_arrow);
                            //holder.playerName.setText(theScorers[position]);
                            holder.rowDetails.setText(getItem(position));

                            //holder.rowDetails.setMovementMethod(LinkMovementMethod.getInstance());
                            Linkify.addLinks((TextView) holder.rowDetails, Linkify.ALL);

                            //holder.playerTeam.setText(giveMeTeamShoreName(getItem(position).getTeam().getName()));
                            //holder.playerGoals.setText(getItem(position).getNumberOfGoals().toString());

                            if(getItem(position).equals("\t\t\t\t\t\t\t\t\t\t\t\t*Head 2 Head*") ||
                                    getItem(position).equals("\t\t\t\t\t\t\t\t\t\t\t\t*Success Ratio*")){
                                holder.rowDetails.setTextColor(getResources().getColor(R.color.colorBlack));
                            }

                            if(getItem(position).equals("Watch At App")){
                                holder.rowDetails.setTextColor(getResources().getColor(R.color.colorDarkRed));
                            }

                            if(getItem(position).equals("Date: " + someMatch.getUtcDate().substring(0,10))){
                                holder.rowDetails.setTextColor(getResources().getColor(R.color.colorDialogBody));
                            }

                            if(!(getItem(position).equals("\t\t\t\t\t\t\t\t\t\t\t\t*Head 2 Head*")) &&
                                    !(getItem(position).equals("\n")) &&
                                    !(getItem(position).equals("\t\t\t\t\t\t\t\t\t\t\t\t*Success Ratio*"))){
                                holder.icon.setImageDrawable(drawable);
                            }

                            return convertView;
                        }
                    };

                    /*builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int item) {
                                    //Toast.makeText(MyApp.this, "You selected: " + items[item],Toast.LENGTH_LONG).show();
                                    //dialog.dismiss();
                                }
                            });*/

                    builder.setAdapter(adapter,null);

                    builder.setNeutralButton("\t\t\t\t\t\t\tok - good to know", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog dialog = builder.create();

                    dialog.getListView().setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedString = (String) parent.getItemAtPosition(position);

                                    if(selectedString.equals("Watch At App")){
                                        Intent intent = new Intent(getActivity(), VideoViewActivity.class);
                                        startActivity(intent);
                                    }

                                    //Toast.makeText(getContext(), selectedString,Toast.LENGTH_SHORT).show();
                                }
                            });

                    dialog.show();

                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorGreen));
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTypeface(dialog.getButton(DialogInterface.BUTTON_NEUTRAL).getTypeface(), Typeface.BOLD);
                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextSize(20);


                    //dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorNavIcon));
                    //TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    //textView.setTextSize(20);
                    //textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    //textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);

                    //dialog.getWindow().setBackgroundDrawableResource(R.color.colorBackgroundGrey);
                    //dialog.getWindow().setLayout(2000, 2000);

                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);

                    if(MainActivity.changeMode)
                        window.setBackgroundDrawableResource(R.color.greyyy);

                    /*
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());
                    lp.width = 1600;
                    lp.height = lp.height;
                    //lp.x=-170;
                    //lp.y=100;
                    dialog.getWindow().setAttributes(lp);*/
                }
            });
        }
    }

    private void setMatchesTitleWithSomeImage(){
        String favoriteLeague = SittingsActivity.teamsOfLeague_selected.getCompetition().getCode();
        TableLayout tableLayout = getView().findViewById(R.id.league_crest);

        switch (favoriteLeague){
            case "FL1":
                tableLayout.setBackground(getResources().getDrawable(R.drawable.league1logo));
                break;

            case "SA":
                tableLayout.setBackground(getResources().getDrawable(R.drawable.seriealogo));
                break;

            case "BL1":
                tableLayout.setBackground(getResources().getDrawable(R.drawable.bl1logo));
                break;

            case "PD":
                tableLayout.setBackground(getResources().getDrawable(R.drawable.la_liga_logo));
                break;

            default:
                break;
        }
    }

    private ArrayList<String> getTeamsShortNames(String homeTeamName, String awayTeamName){

        String homeTeamShortName = null;
        String awayTeamShortName = null;

        ArrayList<String> shortNames = new ArrayList<>();

        for(Team team : SittingsActivity.teamsOfLeague_selected.getTeams())
        {
            if(homeTeamName.equals(team.getName()))
                homeTeamShortName = team.getShortName();

            if(awayTeamName.equals(team.getName()))
                awayTeamShortName = team.getShortName();

            if(homeTeamShortName != null && awayTeamShortName != null)
                break;
        }

        shortNames.add(homeTeamShortName);
        shortNames.add(awayTeamShortName);

        return shortNames;
    }

    /*private void getHeadToHead(String matchId) {

        Call<HeadToHead> call = apiHandlerInterface.getMatchHeadToHead(matchId);
        call.enqueue(new Callback<HeadToHead>() {
            @Override
            public void onResponse(Call<HeadToHead> call, Response<HeadToHead> response) {
                headTohead = response.body();
            }

            @Override
            public void onFailure(Call<HeadToHead> call, Throwable t) {
                Log.e("in enqueue", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }*/

    private void addSomeVideosHighLightsToTheList(){

        //PL
        videosHighLightsListPL.add("https://www.scorebat.com/embed/g/960517/");
        videosHighLightsListPL.add("https://www.scorebat.com/embed/g/960522/");
        videosHighLightsListPL.add("https://www.scorebat.com/embed/g/960516/");
        videosHighLightsListPL.add("https://www.scorebat.com/embed/g/960511/");
        videosHighLightsListPL.add("https://www.scorebat.com/embed/g/960506/");
        videosHighLightsListPL.add("https://www.scorebat.com/embed/g/960502/");
        videosHighLightsListPL.add("https://www.scorebat.com/embed/g/960499/");

        //PD
        videosHighLightsListPD.add("https://www.scorebat.com/embed/g/972373/");
        videosHighLightsListPD.add("https://www.scorebat.com/embed/g/972372/");
        videosHighLightsListPD.add("https://www.scorebat.com/embed/g/972362/");
        videosHighLightsListPD.add("https://www.scorebat.com/embed/g/972367/");

        //BL1
        videosHighLightsListBL1.add("https://www.scorebat.com/embed/g/950241/");
        videosHighLightsListBL1.add("https://www.scorebat.com/embed/g/950242/");
        videosHighLightsListBL1.add("https://www.scorebat.com/embed/g/950247/");
        videosHighLightsListBL1.add("https://www.scorebat.com/embed/g/950234/");

        //SA
        videosHighLightsListSA.add("https://www.scorebat.com/embed/g/970701/");
        videosHighLightsListSA.add("https://www.scorebat.com/embed/g/970699/");
        videosHighLightsListSA.add("https://www.scorebat.com/embed/g/970680/");
        videosHighLightsListSA.add("https://www.scorebat.com/embed/g/970688/");
        videosHighLightsListSA.add("https://www.scorebat.com/embed/g/970686/");
        videosHighLightsListSA.add("https://www.scorebat.com/embed/g/970678/");

        //FL1
        videosHighLightsListFL1.add("https://www.scorebat.com/embed/g/953316/");
        videosHighLightsListFL1.add("https://www.scorebat.com/embed/g/935764/");
        videosHighLightsListFL1.add("https://www.scorebat.com/embed/g/953312/");
        videosHighLightsListFL1.add("https://www.scorebat.com/embed/g/935758/");
    }
}
