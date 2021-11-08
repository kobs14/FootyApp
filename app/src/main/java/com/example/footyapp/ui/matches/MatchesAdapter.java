package com.example.footyapp.ui.matches;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.footyapp.DatabaseHelper;
import com.example.footyapp.MainActivity;
import com.example.footyapp.R;
import com.example.footyapp.SittingsActivity;
import com.example.footyapp.pojos.Match;
import com.example.footyapp.pojos.Team;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.net.URI;
import java.util.ArrayList;

public class MatchesAdapter extends ArrayAdapter<Match> {

    private Context mContext;
    private Integer currentMatchDay;

    public MatchesAdapter(@NonNull Context context, int resource, ArrayList<Match> objects, Integer currentMatchDay) {
        super(context, resource, objects);
        mContext = context;

        this.currentMatchDay = currentMatchDay;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_match,parent,false);
        }

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            Cursor cursor = dbHelper.getUserChoice();
            cursor.moveToLast();
            String favoriteTeam = cursor.getString(2);

            Integer scoreHomeTeam = getItem(position).getScore().getFullTime().getHomeTeam();
            Integer scoreAwayTeam = getItem(position).getScore().getFullTime().getAwayTeam();

            String finalScore =  scoreHomeTeam == null || scoreAwayTeam == null ?
                            " --- " : " " + scoreHomeTeam.toString() + "-" + scoreAwayTeam.toString() + " ";

            String homeTeamName = getItem(position).getHomeTeam().getName();
            String awayTeamName = getItem(position).getAwayTeam().getName();

            String homeTeamShortName = null;
            String awayTeamShortName = null;

            for(Team team : SittingsActivity.teamsOfLeague_selected.getTeams())
            {
                if(homeTeamName.equals(team.getName()))
                    homeTeamShortName = team.getShortName();

                if(awayTeamName.equals(team.getName()))
                    awayTeamShortName = team.getShortName();

                if(homeTeamShortName != null && awayTeamShortName != null)
                    break;
            }

            Integer matchDayThing = getItem(position).getMatchday();

            ((TextView) convertView.findViewById(R.id.textViewMatchDay)).setText(getItem(position).getMatchday().toString());
            ((TextView) convertView.findViewById(R.id.teamA_name)).setText(homeTeamShortName);
            ((TextView) convertView.findViewById(R.id.score_textView)).setText(finalScore);
            ((TextView) convertView.findViewById(R.id.teamB_name)).setText(awayTeamShortName);

            if(favoriteTeam.equals(homeTeamName) || favoriteTeam.equals(awayTeamName)){
                if(favoriteTeam.equals(homeTeamName)){
                    ((TextView) convertView.findViewById(R.id.teamA_name)).setTypeface(null, Typeface.BOLD_ITALIC);
                    ((TextView) convertView.findViewById(R.id.teamA_name)).setTextColor(getContext().getResources().getColor(R.color.turkeyz));
                }
                else{
                    ((TextView) convertView.findViewById(R.id.teamB_name)).setTypeface(null, Typeface.BOLD_ITALIC);
                    ((TextView) convertView.findViewById(R.id.teamB_name)).setTextColor(getContext().getResources().getColor(R.color.turkeyz));
                }
            }

            if(currentMatchDay.equals(matchDayThing)){
                ((TextView) convertView.findViewById(R.id.textViewMatchDay)).setTextColor(getContext().getResources().getColor(R.color.lightRed));
            }

            if(!favoriteTeam.equals(homeTeamName)){
                ((TextView) convertView.findViewById(R.id.teamA_name)).setTypeface(null, Typeface.BOLD);

                if(MainActivity.changeMode){
                    ((TextView) convertView.findViewById(R.id.teamA_name)).setTextColor(getContext().getResources().getColor(R.color.white));
                }
                else{
                    ((TextView) convertView.findViewById(R.id.teamA_name)).setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                }
            }

            if(!favoriteTeam.equals(awayTeamName)){
                ((TextView) convertView.findViewById(R.id.teamB_name)).setTypeface(null, Typeface.BOLD);

                if(MainActivity.changeMode){
                    ((TextView) convertView.findViewById(R.id.teamB_name)).setTextColor(getContext().getResources().getColor(R.color.white));
                }
                else{
                    ((TextView) convertView.findViewById(R.id.teamB_name)).setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                }
            }

            if(!currentMatchDay.equals(matchDayThing)){
                if(MainActivity.changeMode){
                    ((TextView) convertView.findViewById(R.id.textViewMatchDay)).setTextColor(getContext().getResources().getColor(R.color.white));
                }
                else{
                    ((TextView) convertView.findViewById(R.id.textViewMatchDay)).setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                }
            }

            String homeTeamUri = null, awayTeamUri = null;

            for(Team team : SittingsActivity.teamsOfLeague_selected.getTeams())
            {
                if(homeTeamName.equals(team.getName()))
                    homeTeamUri = team.getCrestUrl();

                if(awayTeamName.equals(team.getName()))
                    awayTeamUri = team.getCrestUrl();

                if(homeTeamUri != null && awayTeamUri != null)
                    break;
            }

            URI uriHome = new URI(homeTeamUri);
            URI uriAway = new URI(awayTeamUri);

            GlideToVectorYou.justLoadImage((MainActivity)(parent.getContext()),android.net.Uri.parse(uriHome.toString()), (ImageView)convertView.findViewById(R.id.team_A_crest));
            GlideToVectorYou.justLoadImage((MainActivity)(parent.getContext()),android.net.Uri.parse(uriAway.toString()), (ImageView)convertView.findViewById(R.id.team_B_crest));
        }
        catch (Exception e){
            Log.e("Adapter","error in adapter");
        }

        return convertView;
    }
}
