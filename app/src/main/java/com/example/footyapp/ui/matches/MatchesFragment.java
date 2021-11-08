package com.example.footyapp.ui.matches;

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

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import com.example.footyapp.MainActivity;
import com.example.footyapp.R;
import com.example.footyapp.SittingsActivity;
import com.example.footyapp.VideoViewActivity;
import com.example.footyapp.pojos.AllLeagueMatches;
import com.example.footyapp.pojos.Match;
import com.example.footyapp.pojos.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatchesFragment extends Fragment {

    private View view;
    private ListView mListView;

    private MatchesAdapter matchesAdapter;
    public static AllLeagueMatches allLeagueMatches = null;
    static boolean someFlag = true;
    static Random random = new Random();
    static ArrayList<String> gifsList = new ArrayList<>();

    public static boolean isFavoriteChanged = false;

    public static List<String> videosHighLightsListPL = new ArrayList<>();
    public static List<String> videosHighLightsListPD = new ArrayList<>();
    public static List<String> videosHighLightsListBL1 = new ArrayList<>();
    public static List<String> videosHighLightsListSA = new ArrayList<>();
    public static List<String> videosHighLightsListFL1 = new ArrayList<>();

    String someVideoLink = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_matches,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        mListView = (ListView)view.findViewById(R.id.listView_matches);
        ArrayList<Match> matches = new ArrayList<>();

        if(someFlag){
            gifsList.add("https://i.gifer.com/L8Cr.gif");
            gifsList.add("https://i.gifer.com/9hv2.gif");
            gifsList.add("https://i.gifer.com/BjCh.gif");
            gifsList.add("https://i.gifer.com/MzHY.gif");
            gifsList.add("https://i.gifer.com/28Qa.gif");
            gifsList.add("https://i.gifer.com/Rnhm.gif");
            gifsList.add("https://i.gifer.com/1QYN.gif");
            gifsList.add("https://i.gifer.com/AUjH.gif");
            gifsList.add("https://i.gifer.com/L8Cn.gif");
            gifsList.add("https://i.gifer.com/AUj6.gif");
            gifsList.add("https://i.gifer.com/A5pN.gif");

            addSomeVideosHighLightsToTheList();

            someFlag = !someFlag;
        }

        if(isFavoriteChanged){
            mListView.setVisibility(View.GONE);

            int randomGif = Math.abs(random.nextInt()) % gifsList.size();
            Glide.with(this).load(gifsList.get(randomGif)).into((ImageView) getView().findViewById(R.id.ivGif));
            Glide.with(this).load(gifsList.get(randomGif)).into((ImageView) getView().findViewById(R.id.ivGifTwo));

            ImageView imageView = getView().findViewById(R.id.league_crest);
            imageView.setBackground(getResources().getDrawable(R.drawable.top_europe_leagues_image));

            ((TextView)getView().findViewById(R.id.league_name)).setText("");
            getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.loadingTextView).setVisibility(View.VISIBLE);

            isFavoriteChanged = false;

            return;
        }

        //gifs - https://gifer.com
        int randomGif = Math.abs(random.nextInt()) % gifsList.size();
        Glide.with(this).load(gifsList.get(randomGif)).into((ImageView) getView().findViewById(R.id.ivGif));
        Glide.with(this).load(gifsList.get(randomGif)).into((ImageView) getView().findViewById(R.id.ivGifTwo));

        if(allLeagueMatches != null){

            setMatchesTitleWithSomeImage();
            ((TextView)getView().findViewById(R.id.league_name)).setText(allLeagueMatches.getCompetition().getName());
            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
            getView().findViewById(R.id.loadingTextView).setVisibility(View.GONE);

            for(Match m : allLeagueMatches.getMatches())
            {
                matches.add(m);
            }

            matchesAdapter = new MatchesAdapter(getContext(),R.layout.row_match,matches,allLeagueMatches.getMatches().get(0).getSeason().getCurrentMatchday());
            mListView.setAdapter(matchesAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Match selectedMatch = (Match) parent.getItemAtPosition(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialog);

                    ArrayList<String> matchDetails = new ArrayList<>();

                    matchDetails.add("Date: " + selectedMatch.getUtcDate().substring(0,10));
                    matchDetails.add("Time: " + selectedMatch.getUtcDate().substring(11,selectedMatch.getUtcDate().length()-1));
                    matchDetails.add("Status: " + selectedMatch.getStatus());

                    String matchStadium = "";

                    for(Team team : SittingsActivity.teamsOfLeague_selected.getTeams()){
                        if(selectedMatch.getHomeTeam().getName().equals(team.getName()))
                            matchStadium = team.getVenue();
                    }

                    matchDetails.add("Stadium: " + matchStadium);

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
                        matchDetails.add("Winner: " + winnerDrawOrNone);
                    }

                    matchDetails.add("(current match day: " + selectedMatch.getSeason().getCurrentMatchday().toString() + ")");

                    if(selectedMatch.getStatus().toUpperCase().equals("FINISHED")){
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

                        matchDetails.add("Watch Highlights(At net): " + Html.fromHtml("<a href=\"" + someVideoLink + "\">" + someVideoLink + "</a>"));
                        matchDetails.add("Watch At App");
                    }

                    //SpannableString title = new SpannableString(selectedMatch.getHomeTeam().getName() + " vs " +
                            //selectedMatch.getAwayTeam().getName());

                    //title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);

                    String title = selectedMatch.getHomeTeam().getName() + " vs " +
                            selectedMatch.getAwayTeam().getName();

                    builder.setTitle(title);

                    ListAdapter adapter = new ArrayAdapter<String>(
                            getContext(), R.layout.dialod_matches, matchDetails) {

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

                            holder.rowDetails.setText(getItem(position));

                            //holder.rowDetails.setMovementMethod(LinkMovementMethod.getInstance());
                            Linkify.addLinks((TextView) holder.rowDetails, Linkify.ALL);

                            if(getItem(position).equals("Watch At App")){
                                holder.rowDetails.setTextColor(getResources().getColor(R.color.colorDarkRed));
                            }

                            if(!(getItem(position).equals("Watch At App"))){
                                holder.rowDetails.setTextColor(getResources().getColor(R.color.colorDialogBody));
                            }

                            holder.icon.setImageDrawable(drawable);

                            return convertView;
                        }
                    };

                    /*builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int item) {
                                }
                            });*/

                    builder.setAdapter(adapter,null);

                    builder.setNeutralButton("\t\t\t\t\t\t\tok - good to know", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog dialog = builder.create();

                    if(selectedMatch.getHomeTeam().getName().equals(SittingsActivity.teamSelected) ||
                            selectedMatch.getAwayTeam().getName().equals(SittingsActivity.teamSelected)){

                        dialog.setIcon(R.drawable.ic_some_star);
                    }

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

                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);

                    if(MainActivity.changeMode)
                        window.setBackgroundDrawableResource(R.color.greyyy);
                }
            });
        }
    }

    private void setMatchesTitleWithSomeImage(){
        String favoriteLeague = SittingsActivity.teamsOfLeague_selected.getCompetition().getCode();
        ImageView imageView = getView().findViewById(R.id.league_crest);

        switch (favoriteLeague){
            case "FL1":
                imageView.setBackground(getResources().getDrawable(R.drawable.league1logo));
                break;

            case "SA":
                imageView.setBackground(getResources().getDrawable(R.drawable.seriealogo));
                break;

            case "BL1":
                imageView.setBackground(getResources().getDrawable(R.drawable.bl1logo));
                break;

            case "PD":
                imageView.setBackground(getResources().getDrawable(R.drawable.la_liga_logo));
                break;

            default:
                imageView.setBackground(getResources().getDrawable(R.drawable.ic_icons8_the_premier_league));
                break;
        }
    }

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