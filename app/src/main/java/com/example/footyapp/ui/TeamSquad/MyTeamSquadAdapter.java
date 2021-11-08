package com.example.footyapp.ui.TeamSquad;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blongho.country_data.World;
import com.example.footyapp.R;
import com.example.footyapp.pojos.Squad;

import java.util.ArrayList;

public class MyTeamSquadAdapter extends ArrayAdapter<Squad>{

    private static final String TAG = "SquadListAdapter";
    private Context mContext;
    private int mResource;

    public MyTeamSquadAdapter(@NonNull Context context, int resource, ArrayList<Squad> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_squad,parent,false);
        }

        try {

            ((TextView) convertView.findViewById(R.id.player_name)).setText(getItem(position).getName());
            ((TextView) convertView.findViewById(R.id.player_position)).setText(getItem(position).getPosition());
            //((TextView) convertView.findViewById(R.id.player_shirt_number)).setText(getItem(position).getShirtNumber());

            String nationality = getItem(position).getNationality();

            if(nationality.equals("England") || nationality.equals("Scotland"))
                nationality = "United Kingdom";

            if(nationality.equals("Côte d’Ivoire"))
                nationality = "France";

            int flag_cod = World.getFlagOf(nationality);

            ((ImageView) convertView.findViewById(R.id.player_nationality_logo)).setImageResource(flag_cod);

        }
        catch (Exception e){
            Log.e("Adapter","error in adapter");
        }
        return convertView;

    }


}