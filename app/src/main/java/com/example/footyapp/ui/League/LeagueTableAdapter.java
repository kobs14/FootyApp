package com.example.footyapp.ui.League;

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
import com.example.footyapp.pojos.Table;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.net.URI;
import java.util.ArrayList;

public class LeagueTableAdapter extends ArrayAdapter<Table> {

    private static final String TAG = "LeagueListAdapter";
    private Context mContext;
    private int mResource;

    public LeagueTableAdapter(@NonNull Context context, int resource, ArrayList<Table> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_table,parent,false);
        }

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            Cursor cursor = dbHelper.getUserChoice();
            cursor.moveToLast();
            String favoriteTeam = cursor.getString(2);

            ((TextView) convertView.findViewById(R.id.team_position)).setText(getItem(position).getPosition().toString());
            ((TextView) convertView.findViewById(R.id.team_name)).setText(getItem(position).getTeam().getName());
            ((TextView) convertView.findViewById(R.id.matches_played)).setText(getItem(position).getPlayedGames().toString());
            ((TextView) convertView.findViewById(R.id.matches_won)).setText(getItem(position).getWon().toString());
            ((TextView) convertView.findViewById(R.id.matches_drawn)).setText(getItem(position).getDraw().toString());
            ((TextView) convertView.findViewById(R.id.matches_lost)).setText(getItem(position).getLost().toString());
            ((TextView) convertView.findViewById(R.id.team_points)).setText(getItem(position).getPoints().toString());


            if(favoriteTeam.equals(getItem(position).getTeam().getName())){
                ((TextView) convertView.findViewById(R.id.team_name)).setTypeface(null, Typeface.BOLD_ITALIC);
                ((TextView) convertView.findViewById(R.id.team_name)).setTextColor(getContext().getResources().getColor(R.color.turkeyz));

                ((TextView) convertView.findViewById(R.id.team_position)).setTextColor(getContext().getResources().getColor(R.color.lightRed));
                ((TextView) convertView.findViewById(R.id.matches_played)).setTextColor(getContext().getResources().getColor(R.color.lightRed));
                ((TextView) convertView.findViewById(R.id.team_points)).setTextColor(getContext().getResources().getColor(R.color.lightRed));
            }

            if(!(favoriteTeam.equals(getItem(position).getTeam().getName()))){
                ((TextView) convertView.findViewById(R.id.team_name)).setTypeface(null, Typeface.BOLD);
                ((TextView) convertView.findViewById(R.id.matches_played)).setTextColor(getContext().getResources().getColor(R.color.turkeyz));
                ((TextView) convertView.findViewById(R.id.team_points)).setTextColor(getContext().getResources().getColor(R.color.turkeyz));

                if(MainActivity.changeMode){
                    ((TextView) convertView.findViewById(R.id.team_name)).setTextColor(getContext().getResources().getColor(R.color.white));
                    ((TextView) convertView.findViewById(R.id.team_position)).setTextColor(getContext().getResources().getColor(R.color.white));

                }
                else{
                    ((TextView) convertView.findViewById(R.id.team_name)).setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                    ((TextView) convertView.findViewById(R.id.team_position)).setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                }
            }

            URI uri = new URI(getItem(position).getTeam().getCrestUrl());
            GlideToVectorYou.justLoadImage((MainActivity)(parent.getContext()),android.net.Uri.parse(uri.toString()), (ImageView)convertView.findViewById(R.id.team_crest));

        }
        catch (Exception e){
            Log.e("Adapter","error in adapter");
        }

        return convertView;
    }
}
