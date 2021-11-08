package com.example.footyapp.ui.League;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.footyapp.ApiClientSynchronise;
import com.example.footyapp.ApiHandlerInterfaceSynchronise;
import com.example.footyapp.MainActivity;
import com.example.footyapp.R;
import com.example.footyapp.SittingsActivity;
import com.example.footyapp.pojos.ActiveCompetition;
import com.example.footyapp.pojos.LeagueStanding;
import com.example.footyapp.pojos.Scorer;
import com.example.footyapp.pojos.Squad;
import com.example.footyapp.pojos.Table;
import com.example.footyapp.pojos.Team;
import com.example.footyapp.pojos.TeamSquad;
import com.example.footyapp.pojos.TopScorers;
import com.example.footyapp.ui.TeamSquad.MyTeamFragment;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeagueTableFragment//.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeagueTableFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeagueTableFragment extends Fragment {

    private View view;
    private ListView mListView;
    private TextView league_title;
    private LeagueTableAdapter tableAdapter;
    public static LeagueStanding leagueStanding = null;
    private ArrayList<Table> league_table = new ArrayList<>();

    ApiHandlerInterfaceSynchronise apiHandlerInterfaceSynchronise = null;
    Call<TeamSquad> call = null;
    TeamSquad teamInfoPlusSquad = null;

    Call<TopScorers> callTopScorers = null;
    static TopScorers topScorers = null;

    String theActiveCompetitions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.league_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mListView = (ListView)view.findViewById(R.id.listView_league_table);

        if(apiHandlerInterfaceSynchronise == null)
            apiHandlerInterfaceSynchronise = ApiClientSynchronise.createService(ApiHandlerInterfaceSynchronise.class);

        if(callTopScorers == null)
            callTopScorers = apiHandlerInterfaceSynchronise.getTopScorersSynchronise(SittingsActivity.leagueSelected);

        if (leagueStanding != null) {

            setMatchesTitleWithSomeImage();
            String leagueFullName = leagueStanding.getCompetition().getName();

            league_title = (TextView)getActivity().findViewById(R.id.league_tbl_title);
            league_title.setText(leagueFullName);

            //TextView txtViewTitle = getView().findViewById(R.id.league_tbl_title);
            ImageView imgViewTopScorrer = getView().findViewById(R.id.image_top_scorer);
            imgViewTopScorrer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Thread someThread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try
                            {
                                topScorers = callTopScorers.execute().body();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

                    someThread.start();

                    try
                    {
                        someThread.join();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    if(topScorers != null){

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialog);

                        String season = topScorers.getSeason().getStartDate().substring(0,4) + "-" +
                                            topScorers.getSeason().getEndDate().substring(0,4);

                        //SpannableString title = new SpannableString("*TOP " + topScorers.getCount().toString() + " SCORERS*" +
                                                //"\n(Season " + season + ")" +
                                               // "\n\nPLAYER\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTEAM\t\t\t\t\tGOALS");
                        //title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);

                        //builder.setTitle(title);

                        TextView txtView = new TextView(getContext());
                        txtView.setText("\t\t\t\t\t\t\t\t\t\t\t*TOP " + topScorers.getCount().toString() + " SCORERS*" +
                                "\n\t\t\t\t\t\t\t\t\t\t\t(Season " + season + ")" +
                                "\n\n\t\t\tPLAYER\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTEAM\t\t\t\t\t\tGOALS");
                        txtView.setGravity(Gravity.LEFT);
                        txtView.setTextSize(20);
                        txtView.setTextColor(getResources().getColor(R.color.colorPurple));
                        txtView.setTypeface(txtView.getTypeface(), Typeface.BOLD);

                        //Drawable img = getContext().getResources().getDrawable(R.drawable.top_scorer_img);
                        //img.setBounds(0, 0, 60, 60);
                        //txtView.setCompoundDrawables(img, null, null, null);

                        builder.setCustomTitle(txtView);

                        //final String[] theScorers = new String[topScorers.getCount()];

                        //int i = 0;

                        //for(Scorer scorer : topScorers.getScorers()){
                            //Player player = scorer.getPlayer();
                            //String teamShortName = giveMeTeamShoreName(scorer.getTeam().getName());

                            //theScorers[i++] = player.getName()  + teamShortName +
                                             //scorer.getNumberOfGoals().toString();

                        //}


                        final ArrayList<Scorer> theScorers = new ArrayList<>();

                        for(Scorer scorer : topScorers.getScorers()){
                            theScorers.add(scorer);
                        }

                        ListAdapter adapter = new ArrayAdapter<Scorer>(
                                getContext(), R.layout.list_row_top_scorers, theScorers) {

                            ViewHolder holder;
                            Drawable icon;

                            class ViewHolder {
                                ImageView icon;
                                TextView playerName;
                                TextView playerTeam;
                                TextView playerGoals;
                            }

                            public View getView(int position, View convertView,
                                                ViewGroup parent) {
                                final LayoutInflater inflater = (LayoutInflater) getContext()
                                        .getSystemService(
                                                Context.LAYOUT_INFLATER_SERVICE);

                                if (convertView == null) {
                                    convertView = inflater.inflate(
                                            R.layout.list_row_top_scorers, null);

                                    holder = new ViewHolder();
                                    holder.icon = (ImageView) convertView
                                            .findViewById(R.id.icon);
                                    holder.playerName = (TextView) convertView
                                            .findViewById(R.id.player_nameee);
                                    holder.playerTeam = (TextView) convertView
                                            .findViewById(R.id.player_teammm);
                                    holder.playerGoals = (TextView) convertView
                                            .findViewById(R.id.player_goalsss);

                                    convertView.setTag(holder);
                                } else {
                                    holder = (ViewHolder) convertView.getTag();
                                }

                                Drawable drawable = getResources().getDrawable(R.drawable.top_scorer_img);
                                //holder.playerName.setText(theScorers[position]);
                                holder.playerName.setText(getItem(position).getPlayer().getName());
                                holder.playerTeam.setText(giveMeTeamShoreName(getItem(position).getTeam().getName()));
                                holder.playerGoals.setText(getItem(position).getNumberOfGoals().toString());

                                holder.playerName.setMovementMethod(LinkMovementMethod.getInstance());
                                Linkify.addLinks((TextView) holder.playerName, Linkify.ALL);
                                holder.playerTeam.setMovementMethod(LinkMovementMethod.getInstance());
                                Linkify.addLinks((TextView) holder.playerTeam, Linkify.ALL);
                                holder.playerGoals.setMovementMethod(LinkMovementMethod.getInstance());
                                Linkify.addLinks((TextView) holder.playerGoals, Linkify.ALL);

                                holder.icon.setImageDrawable(drawable);

                                return convertView;
                            }
                        };

                        builder.setAdapter(adapter,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int item) {
                                        //Toast.makeText(MyApp.this, "You selected: " + items[item],Toast.LENGTH_LONG).show();
                                        //dialog.dismiss();
                                    }
                                });


                        //builder.setItems(theScorers, new DialogInterface.OnClickListener() {
                            //@Override
                            //public void onClick(DialogInterface dialogInterface, int which) {

                            //}
                        //});

                        //builder.setMessage("lala");


                        builder.setNeutralButton("\t\t\t\t\t\t\t\t\t\t\t\t\tOK,Nice", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        AlertDialog dialog = builder.create();

                        //dialog.setIcon(R.drawable.top_scorer_img);

                        dialog.show();

                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorGreen));
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTypeface(dialog.getButton(DialogInterface.BUTTON_NEUTRAL).getTypeface(), Typeface.BOLD);
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextSize(20);


                        /*Window window = dialog.getWindow();
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.CENTER);*/


                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = 1600;
                        lp.height = lp.height;
                        //lp.x=-170;
                        //lp.y=100;
                        dialog.getWindow().setAttributes(lp);


                        if(MainActivity.changeMode)
                            dialog.getWindow().setBackgroundDrawableResource(R.color.greyyy);


                        //TextView textViewTitle = (TextView) dialog.findViewById(android.R.id.title);
                        //textViewTitle.setTextSize(20);
                        //textViewTitle.setTextColor(getResources().getColor(R.color.colorDarkBlue));
                        //textViewTitle.setTypeface(textViewTitle.getTypeface(), Typeface.BOLD);

                        //dialog.getWindow().setBackgroundDrawableResource(R.color.colorBackgroundGrey);
                        //dialog.getWindow().setLayout(2000, 2000);
                    }
                }
            });


            for (Table t : leagueStanding.getStandings().get(0).getTable()) {
                league_table.add(t);
            }

            tableAdapter = new LeagueTableAdapter(getContext(),R.layout.row_table,league_table);
            mListView.setAdapter(tableAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Table teamSelected =  (Table)parent.getItemAtPosition(position);
                    int teamId = teamSelected.getTeam().getId();

                    call = apiHandlerInterfaceSynchronise.getTeamSquadSynchronise(teamId);

                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try
                            {
                                teamInfoPlusSquad = call.execute().body();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialog); //R.style.AlertDialogCustom);

                    //String message = "";

                    ArrayList<String> teamDetails = new ArrayList<>();

                    if(teamInfoPlusSquad != null) {
                        //message += "\n#Founded: " + teamInfoPlusSquad.getFounded().toString() + "<br>" +
                                //"#Stadium: " + teamInfoPlusSquad.getVenue() + "<br>" +
                                //"#Club Colors: " + teamInfoPlusSquad.getClubColors() + "<br>" +
                                //"#Current Coach: ";

                        teamDetails.add("Founded: " + teamInfoPlusSquad.getFounded().toString());
                        teamDetails.add("Stadium: " + teamInfoPlusSquad.getVenue());
                        teamDetails.add("Club Colors: " + teamInfoPlusSquad.getClubColors());

                        String coach = "";

                        for(int i = teamInfoPlusSquad.getSquad().size()-1; i>=0; i--){
                            Squad squad = teamInfoPlusSquad.getSquad().get(i);

                            if(squad.getRole().equals("COACH")){
                                coach = squad.getName();
                                break;
                            }
                        }

                        //message += coach + "<br><br>#Active Competitions: ";

                        teamDetails.add("Current Coach: " + coach + "\n");
                        teamDetails.add("\t\t\t\t\t\t\t\t\t\t*Active Competitions*");

                        theActiveCompetitions = "";

                        for (ActiveCompetition ac : teamInfoPlusSquad.getActiveCompetitions()) {
                            //message += ac.getName() + ",";
                            theActiveCompetitions += ac.getName() + ",";
                        }

                        //message = message.substring(0, message.length() - 1) + "\n";


                        theActiveCompetitions = theActiveCompetitions.substring(0, theActiveCompetitions.length() - 1) + "\n";
                        teamDetails.add(theActiveCompetitions + "\n");


                        //message += "<br><br>\t\t\t\t\t\t\t\t\t\t\t\t*contact*<br>#Phone: " + teamInfoPlusSquad.getPhone() +
                                //"<br>#Adress: " + teamInfoPlusSquad.getAddress().substring(0,teamInfoPlusSquad.getAddress().length()-6); //+
                                //"<br>#Website: " + teamInfoPlusSquad.getWebsite();


                        teamDetails.add("\t\t\t\t\t\t\t\t\t\t\t\t\t\t*Contact*");
                        teamDetails.add("Phone: " + teamInfoPlusSquad.getPhone());
                        teamDetails.add("Address: " + teamInfoPlusSquad.getAddress().substring(0,teamInfoPlusSquad.getAddress().length()-6));


                        if(teamInfoPlusSquad.getEmail() != null){
                            //message +="<br>#Email: " + teamInfoPlusSquad.getEmail();
                            teamDetails.add("#Email: " + teamInfoPlusSquad.getEmail());
                        }


                        //message += "<br>#Website: ";

                        //SpannableString title = new SpannableString(teamInfoPlusSquad.getName());
                        //title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);


                        teamDetails.add("#Website: " + Html.fromHtml("<a href=\"" + teamInfoPlusSquad.getWebsite() + "\">" + teamInfoPlusSquad.getWebsite() + "</a>"));
                        teamDetails.add("\n");

                        String title = teamInfoPlusSquad.getName();

                        builder//.setMessage(Html.fromHtml(message + "<a href=\"" + teamInfoPlusSquad.getWebsite() + "\">" + teamInfoPlusSquad.getWebsite() + "</a>"))
                                .setTitle(title);


                        ListAdapter adapter = new ArrayAdapter<String>(
                                getContext(), R.layout.dialod_matches, teamDetails) {

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

                                holder.rowDetails.setMovementMethod(LinkMovementMethod.getInstance());
                                Linkify.addLinks((TextView) holder.rowDetails, Linkify.ALL);

                                //holder.rowDetails.setMovementMethod(LinkMovementMethod.getInstance());
                                //holder.playerTeam.setText(giveMeTeamShoreName(getItem(position).getTeam().getName()));
                                //holder.playerGoals.setText(getItem(position).getNumberOfGoals().toString());

                                if(getItem(position).equals("\t\t\t\t\t\t\t\t\t\t*Active Competitions*") ||
                                        getItem(position).equals("\t\t\t\t\t\t\t\t\t\t\t\t\t\t*Contact*")){
                                    holder.rowDetails.setTextColor(getResources().getColor(R.color.colorBlack));
                                }

                                if(getItem(position).equals("Founded: " + teamInfoPlusSquad.getFounded().toString())){
                                    holder.rowDetails.setTextColor(getResources().getColor(R.color.colorDialogBody));
                                }

                                if(!(getItem(position).equals("\t\t\t\t\t\t\t\t\t\t*Active Competitions*")) &&
                                        !(getItem(position).equals(theActiveCompetitions + "\n")) &&
                                        !(getItem(position).equals("\n")) &&
                                        !(getItem(position).equals("\t\t\t\t\t\t\t\t\t\t\t\t\t\t*Contact*"))){
                                    holder.icon.setImageDrawable(drawable);
                                }

                                return convertView;
                            }
                        };

                        builder.setAdapter(adapter,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int item) {
                                        //Toast.makeText(MyApp.this, "You selected: " + items[item],Toast.LENGTH_LONG).show();
                                        //dialog.dismiss();
                                    }
                                });


                        builder.setNeutralButton("\t\t\t\t\t\t\tok - good to know", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        final View customLayoutView = getLayoutInflater().inflate(R.layout.alert_dialog, null);

                        URI uri = null;
                        try {
                            uri = new URI(teamInfoPlusSquad.getCrestUrl());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        GlideToVectorYou.justLoadImageAsBackground((MainActivity)getActivity(),android.net.Uri.parse(uri.toString()), (ImageView)customLayoutView.findViewById(R.id.someImage));
                        builder.setView(customLayoutView);

                        AlertDialog dialog = builder.create();

                        if(teamSelected.getTeam().getName().equals(MyTeamFragment.teamSquad.getName()))
                            dialog.setIcon(R.drawable.ic_favotite_heart);

                        dialog.show();

                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorGreen));
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTypeface(dialog.getButton(DialogInterface.BUTTON_NEUTRAL).getTypeface(), Typeface.BOLD);
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextSize(20);



                        //dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorNavIcon));
                        //TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        //textView.setMovementMethod(LinkMovementMethod.getInstance());
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

                        /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = 1600;
                        lp.height = lp.height;
                        //lp.x=-170;
                        //lp.y=100;
                        dialog.getWindow().setAttributes(lp);*/
                    }

                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View customLayoutView = getLayoutInflater().inflate(R.layout.dialog_custom_league_table, null);

                    if(teamInfoPlusSquad != null){

                        ((TextView) customLayoutView.findViewById(R.id.textView_title)).setText(teamInfoPlusSquad.getName());
                        ((TextView) customLayoutView.findViewById(R.id.textViewFonded)).setText(teamInfoPlusSquad.getFounded().toString());
                        ((TextView) customLayoutView.findViewById(R.id.textViewStadium)).setText(teamInfoPlusSquad.getVenue());
                        ((TextView) customLayoutView.findViewById(R.id.textViewClubColors)).setText(teamInfoPlusSquad.getClubColors());

                        String coach = "", activeC = "";

                        for(int i = teamInfoPlusSquad.getSquad().size()-1; i>=0; i--){
                            Squad squad = teamInfoPlusSquad.getSquad().get(i);

                            if(squad.getRole().equals("COACH")){
                                coach = squad.getName();
                                break;
                            }
                        }

                        ((TextView) customLayoutView.findViewById(R.id.textViewCurrentCoach)).setText(coach);

                        for (ActiveCompetition ac : teamInfoPlusSquad.getActiveCompetitions()) {
                            activeC += ac.getName() + ",";
                        }

                        activeC = activeC.substring(0, activeC.length() - 1);

                        ((TextView) customLayoutView.findViewById(R.id.textView_ac)).setText(activeC);
                        ((TextView) customLayoutView.findViewById(R.id.textView_phone)).setText(teamInfoPlusSquad.getPhone());
                        ((TextView) customLayoutView.findViewById(R.id.textView_adress)).setText(teamInfoPlusSquad.getAddress());
                        ((TextView) customLayoutView.findViewById(R.id.textView_website)).setText(teamInfoPlusSquad.getWebsite());
                        ((TextView) customLayoutView.findViewById(R.id.textView_email)).setText(teamInfoPlusSquad.getEmail());

                        builder.setMessage("").setTitle("");

                        builder.setNeutralButton("\t\t\t\t\t\t\tok - good to know", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        URI uri = null;

                        try {
                            uri = new URI(teamInfoPlusSquad.getCrestUrl());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        GlideToVectorYou.justLoadImageAsBackground((MainActivity)getActivity(),android.net.Uri.parse(uri.toString()), (ImageView)customLayoutView.findViewById(R.id.imageViewCrestOne));
                        GlideToVectorYou.justLoadImageAsBackground((MainActivity)getActivity(),android.net.Uri.parse(uri.toString()), (ImageView)customLayoutView.findViewById(R.id.imageViewCrestTwo));

                        builder.setView(customLayoutView);
                        AlertDialog dialog = builder.create();

                        Window window = dialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        window.setGravity(Gravity.CENTER);

                        if(teamSelected.getTeam().getName().equals(MyTeamFragment.teamSquad.getName()))
                            dialog.setIcon(R.drawable.ic_favotite_heart);

                        dialog.show();

                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorNavIcon));
                    }
                    */
                }
            });
        }
    }

    private void setMatchesTitleWithSomeImage() {
        String favoriteLeague = SittingsActivity.teamsOfLeague_selected.getCompetition().getCode();
        TextView textView = getView().findViewById(R.id.league_tbl_title);
        Drawable img;

        switch (favoriteLeague) {

            case "FL1":
                img = getContext().getResources().getDrawable(R.drawable.league1logo);
                break;

            case "SA":
                img = getContext().getResources().getDrawable(R.drawable.seriealogo);
                break;

            case "BL1":
                img = getContext().getResources().getDrawable(R.drawable.bl1logo);
                break;

            case "PD":
                img = getContext().getResources().getDrawable(R.drawable.la_liga_logo);
                break;

            default:
                img = getContext().getResources().getDrawable(R.drawable.ic_icons8_the_premier_league);
        }

        img.setBounds(0, 0, 130, 130);
        textView.setCompoundDrawables(img, null, null, null);
    }

    private String giveMeTeamShoreName(String teamFullName){
        String teamShorName = null;

        for(Team team : SittingsActivity.teamsOfLeague_selected.getTeams())
        {
            if(teamFullName.equals(team.getName()))
                teamShorName = team.getShortName();

            if(teamShorName != null)
                break;
        }

        return teamShorName;
    }
}
