package com.example.footyapp.ui.TeamSquad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
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

import com.example.footyapp.ApiClient;
import com.example.footyapp.ApiClientImages;
import com.example.footyapp.ApiClientImagesNotSynchronise;
import com.example.footyapp.ApiClientOtherApi;
import com.example.footyapp.ApiHandlerInterface;
import com.example.footyapp.ApiHandlerInterfaceImages;
import com.example.footyapp.ApiHandlerInterfaceImagesNotSynchronise;
import com.example.footyapp.ApiHandlerInterfaceOtherApi;
import com.example.footyapp.ImagesApiClasses.PlayerImagesAndMore;
import com.example.footyapp.MainActivity;
import com.example.footyapp.R;
import com.example.footyapp.SittingsActivity;
import com.example.footyapp.otherApiClasses.Player;
import com.example.footyapp.otherApiClasses.PlayerDetailsOtherApi;
import com.example.footyapp.otherApiClasses.PlayerInformation;
import com.example.footyapp.otherApiClasses.PlayersOtherApi;
import com.example.footyapp.otherApiClasses.Team;
import com.example.footyapp.otherApiClasses.TeamsOtherApi;
import com.example.footyapp.pojos.AllLeagueMatches;
import com.example.footyapp.pojos.Squad;
import com.example.footyapp.pojos.TeamSquad;

import com.example.footyapp.ui.matches.MatchesFragment;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTeamFragment extends Fragment {

    private View view;
    private MyTeamSquadAdapter squadAdapter;
    private ListView mListView;
    public static TeamSquad teamSquad = null;

    ApiHandlerInterfaceOtherApi apiHandlerInterfaceOtherApi = null;
    ApiHandlerInterfaceImages apiHandlerInterfaceImages = null;

    Call<TeamsOtherApi> call = null;
    TeamsOtherApi teamsOtherApi = null;

    Call<PlayersOtherApi> callTwo = null;
    PlayersOtherApi playersOtherApi = null;

    Call<PlayerDetailsOtherApi> callThree = null;
    PlayerDetailsOtherApi playerDetailsOtherApi = null;

    Call<PlayerImagesAndMore> callPlayerAndImages = null;
    PlayerImagesAndMore playerImagesAndMore = null;

    //ApiHandlerInterfaceImagesNotSynchronise apiHandlerInterfaceImagesNotSynchronise;
    //PlayerImagesAndMore pIaM = null;

    ArrayList<String> playerDialogInfo = new ArrayList<>();
    PlayerInformation pi = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        //apiHandlerInterfaceImagesNotSynchronise = ApiClientImagesNotSynchronise.getClient().create(ApiHandlerInterfaceImagesNotSynchronise.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_squad,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        mListView = (ListView)view.findViewById(R.id.listView_team_squad);

        if(apiHandlerInterfaceOtherApi == null)
            apiHandlerInterfaceOtherApi = ApiClientOtherApi.createService(ApiHandlerInterfaceOtherApi.class);

        if(apiHandlerInterfaceImages == null)
            apiHandlerInterfaceImages = ApiClientImages.createService(ApiHandlerInterfaceImages.class);

        if(teamSquad != null){

            ArrayList<Squad> squad_list = new ArrayList<>();

            for (Squad s : teamSquad.getSquad()) {
                squad_list.add(s);
            }

            try
            {
                URI uri = new URI(teamSquad.getCrestUrl());
                GlideToVectorYou.justLoadImage((MainActivity)(getContext()),android.net.Uri.parse(uri.toString()), (ImageView)getActivity().findViewById(R.id.team_logo));

                ((TextView) getActivity().findViewById(R.id.team_name)).setText(teamSquad.getName());
            }
            catch (Exception e){

            }

            squadAdapter = new MyTeamSquadAdapter(getContext(),R.layout.row_squad,squad_list);
            mListView.setAdapter(squadAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Squad selectedPlayer = (Squad) parent.getItemAtPosition(position);

                    //getPlayerBioAndImages(teamSquad.getShortName(),selectedPlayer.getName());

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialog);

                    playerDialogInfo = new ArrayList<>();
                    playerDialogInfo.add("\t\t\t\t\t\t\t\t\t\t\t*Personal Details*");
                    playerDialogInfo.add("Date Of Birth: " + selectedPlayer.getDateOfBirth().substring(0,10));

                    String playerImageUrl = null;
                    String teamShortName = teamSquad.getShortName();

                    if(teamShortName.equals("Barça"))
                        teamShortName = "Barcelona";

                    callPlayerAndImages = apiHandlerInterfaceImages.getPlayerImagesAndMore(teamShortName,selectedPlayer.getName());

                    Thread threadi;

                    threadi = new Thread(new Runnable() {

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

                    threadi.start();

                    try
                    {
                        threadi.join();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    if(playerImagesAndMore != null){
                        if(playerImagesAndMore.getPlayer() != null){
                            playerImageUrl = playerImagesAndMore.getPlayer().get(0).getStrThumb();
                        }
                    }

                    int leagueIdOtherApi;
                    String leagueName = SittingsActivity.teamsOfLeague_selected.getCompetition().getName();

                    switch(leagueName){
                        case "Premier League":
                            leagueIdOtherApi = 2;
                            break;
                        case "Ligue 1":
                            leagueIdOtherApi = 4;
                            break;
                        case "Serie A":
                            leagueIdOtherApi = 28;
                            break;
                        case "Bundesliga":
                            leagueIdOtherApi = 8;
                            break;
                        case "Primera Division":
                            leagueIdOtherApi = 30;
                            break;
                        default:
                            leagueIdOtherApi = 0;
                    }

                    call = apiHandlerInterfaceOtherApi.getTeamsOtherApi(leagueIdOtherApi);

                    Thread thread;

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
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
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

                    if(teamIdOtherApi != -1)
                    {
                        callTwo = apiHandlerInterfaceOtherApi.getPlayersOtherApi(teamIdOtherApi);

                        Thread threadTwo;

                        threadTwo = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try
                                {
                                    playersOtherApi = callTwo.execute().body();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });

                        threadTwo.start();

                        try
                        {
                            threadTwo.join();
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        int playerIdOtherApi = -1;

                        if(playersOtherApi != null)
                        {
                            for(Player player : playersOtherApi.getApi().getPlayers())
                            {
                                if(player.getLastname().equals("Messi Cuccittini"))
                                    player.setFirstname("Messi");

                                if(selectedPlayer.getName().contains(player.getFirstname()))
                                {
                                    playerIdOtherApi = player.getPlayerId();
                                    break;
                                }
                            }

                            if(playerIdOtherApi != -1)
                            {
                                callThree = apiHandlerInterfaceOtherApi.getPlayerInfoOtherApi(playerIdOtherApi);

                                Thread threadThree;

                                threadThree = new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        try
                                        {
                                            playerDetailsOtherApi = callThree.execute().body();
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                threadThree.start();

                                try
                                {
                                    threadThree.join();
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                                if(playerDetailsOtherApi != null)
                                {
                                    //PlayerInformation pi = playerDetailsOtherApi.getApi().getPlayers().get(0);

                                    pi = playerDetailsOtherApi.getApi().getPlayers().get(0);

                                    playerDialogInfo.add("Age: " + pi.getAge().toString());
                                    playerDialogInfo.add("Birth place: " + pi.getBirthPlace() + ", " + pi.getBirthCountry());
                                    playerDialogInfo.add("Height: " + pi.getHeight() + "\t\t#Weight: " + pi.getWeight());

                                    if(pi.getRating() != null){
                                        playerDialogInfo.add("Rating: " + String.format("%.2f",Float.parseFloat(pi.getRating())));
                                    }

                                    //maybe add :
                                    //shirtNumber, date signed, amount signing, facebook, instegram, twitter..

                                    playerDialogInfo.add("Statics...");
                                    playerDialogInfo.add("Biography...");
                                    playerDialogInfo.add("\n");

                                    /*playerDialogInfo.add("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t*Statics*");
                                    playerDialogInfo.add("Captain: " + pi.getCaptain().toString() + "\t\t\tShots: " +
                                                        pi.getShots().getTotal().toString() + " (on target: " + pi.getShots().getOn().toString() + ")");

                                    playerDialogInfo.add("Goals: " + pi.getGoals().getTotal().toString() + "\t\t\tAssists: " + pi.getGoals().getAssists().toString());


                                    if(selectedPlayer.getPosition().equals("Goalkeeper")){
                                        //message += "\t\t\t#Saves: " + pi.getGoals().getSaves().toString();

                                        playerDialogInfo.add("Saves: " + pi.getGoals().getSaves().toString());
                                    }*/

                                    /*playerDialogInfo.add("Passes: " + pi.getPasses().getTotal().toString() +
                                            " (key: " + pi.getPasses().getKey().toString() +
                                            ", acur: " + pi.getPasses().getAccuracy().toString() + ")");

                                    playerDialogInfo.add("Tackles: " + pi.getTackles().getTotal().toString() +
                                            " (blocks: " + pi.getTackles().getBlocks().toString() +
                                            ", interceptions: " + pi.getTackles().getInterceptions().toString() + ")" );

                                    playerDialogInfo.add("Duels: " + pi.getDuels().getTotal().toString() +
                                            " (won duels: " + pi.getDuels().getWon().toString() + ")");

                                    playerDialogInfo.add("Dribbles: " + (new Integer(pi.getDribbles().getAttempts().intValue() + pi.getDribbles().getSuccess().intValue())).toString() +
                                            " (attempts: " + pi.getDribbles().getAttempts().toString() + ", success: " + pi.getDribbles().getSuccess().toString() + ")");

                                    playerDialogInfo.add("Fouls: " + (new Integer(pi.getFouls().getDrawn().intValue() + pi.getFouls().getCommitted().intValue())).toString() +
                                            " (drawn: " + pi.getFouls().getDrawn().toString() + ", committed: " + pi.getFouls().getCommitted().toString() + ")");

                                    playerDialogInfo.add("Cards: " + (new Integer(pi.getCards().getYellow().intValue() + pi.getCards().getRed().intValue())).toString() +
                                            " (yellow: " + pi.getCards().getYellow().toString() + ", red: " + pi.getCards().getRed().toString() + ")");

                                    playerDialogInfo.add("Penalty: " + "(won:" + pi.getPenalty().getWon().toString() + ", commited:" + pi.getPenalty().getCommited().toString() +
                                            ", success:" + pi.getPenalty().getSuccess().toString() + ", missed:" + pi.getPenalty().getMissed().toString() + ", saved:" + pi.getPenalty().getSaved().toString() + ")");

                                    playerDialogInfo.add("Games: " + "(appearences:" + pi.getGames().getAppearences().toString() + ", minutes played:" +
                                            pi.getGames().getMinutesPlayed().toString() + ", lineups:" + pi.getGames().getLineups().toString() + ")");

                                    playerDialogInfo.add("Substitutes: " + "(In:" + pi.getSubstitutes().getIn().toString() +
                                            ", Out:" + pi.getSubstitutes().getOut().toString() + ", Bench:" + pi.getSubstitutes().getBench().toString() + ")");

                                     */
                                }
                            }
                        }
                    }

                    SpannableString title = new SpannableString(selectedPlayer.getName());
                    title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);

                    builder.setTitle(title);

                    ListAdapter adapter = new ArrayAdapter<String>(
                            getContext(), R.layout.dialod_matches, playerDialogInfo) {

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
                            holder.icon.setImageDrawable(drawable);

                            //Linkify.addLinks((TextView) holder.rowDetails, Linkify.ALL);
                            //holder.rowDetails.setMovementMethod(LinkMovementMethod.getInstance());

                            if(getItem(position).equals("Statics...") || getItem(position).equals("\n") ||
                                    getItem(position).equals("Biography...")){
                                holder.rowDetails.setTextColor(getResources().getColor(R.color.colorDarkRed));
                            }

                            if(getItem(position).equals("\t\t\t\t\t\t\t\t\t\t\t*Personal Details*")){
                                holder.rowDetails.setTextColor(getResources().getColor(R.color.colorBlack));
                            }

                            if(getItem(position).equals("\t\t\t\t\t\t\t\t\t\t\t*Personal Details*") ||
                                    getItem(position).equals("\n")){
                                holder.icon.setVisibility(View.GONE);
                            }

                            return convertView;
                        }
                    };


                    builder.setAdapter(adapter,null);
                    //////////////////////////////////////////////////
                    /*
                    builder.setAdapter(adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int item) {
                                    if(playerDialogInfo.get(item).equals("Biography...") || playerDialogInfo.get(item).equals("Statics...")) {
                                        AlertDialog.Builder someBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialog);//AlertDialogCustom);

                                        ArrayList<String> playerBioStatics = new ArrayList<>();

                                        ArrayList<String> playerBioImages = new ArrayList<>();
                                        ArrayList<String> playerStaticsImages = new ArrayList<>();

                                        if (playerDialogInfo.get(item).equals("Biography...")) {
                                            playerBioStatics.add("Bio:");

                                            if (playerImagesAndMore != null) {
                                                if (playerImagesAndMore.getPlayer() != null) {
                                                    playerBioStatics.add(playerImagesAndMore.getPlayer().get(0).getStrDescriptionEN());

                                                    playerBioImages.add(playerImagesAndMore.getPlayer().get(0).getStrCutout());
                                                    playerBioImages.add(playerImagesAndMore.getPlayer().get(0).getStrRender());
                                                }

                                            }

                                        }
                                        else//Statics...
                                        {
                                            playerBioStatics.add("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t*Statics*");

                                            playerBioStatics.add("Captain: " + pi.getCaptain().toString() + "\t\t\tShots: " +
                                                    pi.getShots().getTotal().toString() + " (on target: " + pi.getShots().getOn().toString() + ")");

                                            playerBioStatics.add("Goals: " + pi.getGoals().getTotal().toString() + "\t\t\tAssists: " + pi.getGoals().getAssists().toString());

                                            playerBioStatics.add("Saves: " + pi.getGoals().getSaves().toString());

                                            playerBioStatics.add("Passes: " + pi.getPasses().getTotal().toString() +
                                                    " (key: " + pi.getPasses().getKey().toString() +
                                                    ", acur: " + pi.getPasses().getAccuracy().toString() + ")");

                                            playerBioStatics.add("Tackles: " + pi.getTackles().getTotal().toString() +
                                                    " (blocks: " + pi.getTackles().getBlocks().toString() +
                                                    ", interceptions: " + pi.getTackles().getInterceptions().toString() + ")");

                                            playerBioStatics.add("Duels: " + pi.getDuels().getTotal().toString() +
                                                    " (won duels: " + pi.getDuels().getWon().toString() + ")");

                                            playerBioStatics.add("Dribbles: " + (new Integer(pi.getDribbles().getAttempts().intValue() + pi.getDribbles().getSuccess().intValue())).toString() +
                                                    " (attempts: " + pi.getDribbles().getAttempts().toString() + ", success: " + pi.getDribbles().getSuccess().toString() + ")");

                                            playerBioStatics.add("Fouls: " + (new Integer(pi.getFouls().getDrawn().intValue() + pi.getFouls().getCommitted().intValue())).toString() +
                                                    " (drawn: " + pi.getFouls().getDrawn().toString() + ", committed: " + pi.getFouls().getCommitted().toString() + ")");

                                            playerBioStatics.add("Cards: " + (new Integer(pi.getCards().getYellow().intValue() + pi.getCards().getRed().intValue())).toString() +
                                                    " (yellow: " + pi.getCards().getYellow().toString() + ", red: " + pi.getCards().getRed().toString() + ")");

                                            playerBioStatics.add("Penalty: " + "(won:" + pi.getPenalty().getWon().toString() + ", commited:" + pi.getPenalty().getCommited().toString() +
                                                    ", success:" + pi.getPenalty().getSuccess().toString() + ", missed:" + pi.getPenalty().getMissed().toString() + ", saved:" + pi.getPenalty().getSaved().toString() + ")");

                                            playerBioStatics.add("Games: " + "(appearences:" + pi.getGames().getAppearences().toString() + ", minutes played:" +
                                                    pi.getGames().getMinutesPlayed().toString() + ", lineups:" + pi.getGames().getLineups().toString() + ")");

                                            playerBioStatics.add("Substitutes: " + "(In:" + pi.getSubstitutes().getIn().toString() +
                                                    ", Out:" + pi.getSubstitutes().getOut().toString() + ", Bench:" + pi.getSubstitutes().getBench().toString() + ")");

                                            if (playerImagesAndMore != null) {
                                                if (playerImagesAndMore.getPlayer() != null) {
                                                    playerStaticsImages.add(playerImagesAndMore.getPlayer().get(0).getStrFanart1());
                                                    playerStaticsImages.add(playerImagesAndMore.getPlayer().get(0).getStrFanart2());
                                                    playerStaticsImages.add(playerImagesAndMore.getPlayer().get(0).getStrFanart3());
                                                    playerStaticsImages.add(playerImagesAndMore.getPlayer().get(0).getStrFanart4());
                                                }
                                            }
                                        }

                                        playerBioStatics.add("\n");

                                        SpannableString titleee = new SpannableString(pi.getPlayerName());
                                        titleee.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, titleee.length(), 0);

                                        someBuilder.setTitle(titleee);

                                        ListAdapter adapterrrr = new ArrayAdapter<String>(
                                                getContext(), R.layout.dialod_matches, playerBioStatics) {

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
                                                //Linkify.addLinks((TextView) holder.rowDetails, Linkify.ALL);

                                                holder.icon.setImageDrawable(drawable);

                                                if((getItem(position).equals("Bio:")) || (getItem(position).equals("\n")) ||
                                                        (getItem(position).equals("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t*Statics*"))){
                                                    holder.icon.setVisibility(View.GONE);
                                                    holder.rowDetails.setTextColor(getResources().getColor(R.color.colorDarkRed));

                                                }

                                                return convertView;
                                            }
                                        };*/
                                        ////////////////////////////////////////////////////

                                        /*someBuilder.setAdapter(adapterrrr,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int item) {
                                                    }
                                                });*/


                                        ///////////////////////////////////////////////////////////////////////
                                        /*
                                        someBuilder.setAdapter(adapterrrr,null);

                                        someBuilder.setNeutralButton("\t\t\t\t\t\t\tok - good to know", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                        String someImageee = null;

                                        if(playerBioImages.size() > 0 || playerStaticsImages.size() > 0){

                                            if(playerDialogInfo.get(item).equals("Biography...")){
                                                if(playerBioImages.size() > 0){
                                                    someImageee = playerBioImages.get(0);
                                                }
                                            }
                                            else{
                                                if(playerStaticsImages.size() > 0){
                                                    someImageee = playerStaticsImages.get(0);
                                                }
                                            }
                                        }

                                        if(someImageee != null){
                                            final View customLayoutViewww = getLayoutInflater().inflate(R.layout.dialog_player_image, null);
                                            URI uri = null;

                                            try {
                                                uri = new URI(someImageee);
                                            } catch (URISyntaxException e) {
                                                e.printStackTrace();
                                            }

                                            GlideToVectorYou.justLoadImageAsBackground((MainActivity)getActivity(),android.net.Uri.parse(uri.toString()), (ImageView)customLayoutViewww.findViewById(R.id.imageViewPlayer));
                                            someBuilder.setView(customLayoutViewww);
                                        }


                                        AlertDialog dialoggg = someBuilder.create();

                                        dialoggg.getListView().setOnItemClickListener(
                                                new AdapterView.OnItemClickListener() {
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        //String selectedString = (String) parent.getItemAtPosition(position);
                                                        //Toast.makeText(getContext(), selectedString,Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        dialoggg.show();


                                        dialoggg.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorGreen));
                                        dialoggg.getButton(DialogInterface.BUTTON_NEUTRAL).setTypeface(dialoggg.getButton(DialogInterface.BUTTON_NEUTRAL).getTypeface(), Typeface.BOLD);
                                        dialoggg.getButton(DialogInterface.BUTTON_NEUTRAL).setTextSize(20);

                                        Window window = dialoggg.getWindow();
                                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                        window.setGravity(Gravity.CENTER);

                                        //Toast.makeText(MyApp.this, "You selected: " + items[item],Toast.LENGTH_LONG).show();
                                        //dialog.dismiss();

                                    }
                                }
                            });*/
                            ///////////////////////////////////////////////////////////////////////

                    builder.setNeutralButton("\t\t\t\t\t\t\tok - good to know", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    if(playerImageUrl != null){
                        final View customLayoutView = getLayoutInflater().inflate(R.layout.dialog_player_image, null);
                        URI uri = null;

                        try {
                            uri = new URI(playerImageUrl);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        GlideToVectorYou.justLoadImageAsBackground((MainActivity)getActivity(),android.net.Uri.parse(uri.toString()), (ImageView)customLayoutView.findViewById(R.id.imageViewPlayer));
                        builder.setView(customLayoutView);
                    }

                    AlertDialog dialog = builder.create();

                    dialog.getListView().setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedString = (String) parent.getItemAtPosition(position);

                                    if(selectedString.equals("Biography...") || selectedString.equals("Statics...")) {
                                        AlertDialog.Builder someBuilder = new AlertDialog.Builder(getActivity(), R.style.MyDialog);

                                        ArrayList<String> playerBioStatics = new ArrayList<>();

                                        ArrayList<String> playerBioImages = new ArrayList<>();
                                        ArrayList<String> playerStaticsImages = new ArrayList<>();

                                        if (selectedString.equals("Biography...")) {
                                            playerBioStatics.add("Bio:");

                                            if (playerImagesAndMore != null) {
                                                if (playerImagesAndMore.getPlayer() != null) {
                                                    playerBioStatics.add(playerImagesAndMore.getPlayer().get(0).getStrDescriptionEN());

                                                    playerBioImages.add(playerImagesAndMore.getPlayer().get(0).getStrCutout());
                                                    playerBioImages.add(playerImagesAndMore.getPlayer().get(0).getStrRender());
                                                }
                                            }
                                        }
                                        else//Statics...
                                        {
                                            playerBioStatics.add("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t*Statics*");

                                            playerBioStatics.add("Captain: " + pi.getCaptain().toString() + "\t\t\tShots: " +
                                                    pi.getShots().getTotal().toString() + " (on target: " + pi.getShots().getOn().toString() + ")");

                                            playerBioStatics.add("Goals: " + pi.getGoals().getTotal().toString() + "\t\t\tAssists: " + pi.getGoals().getAssists().toString() +
                                                    "\t\t\tSaves: " + pi.getGoals().getSaves().toString());

                                            playerBioStatics.add("Passes: " + pi.getPasses().getTotal().toString() +
                                                    " (key: " + pi.getPasses().getKey().toString() +
                                                    ", acur: " + pi.getPasses().getAccuracy().toString() + ")");

                                            playerBioStatics.add("Tackles: " + pi.getTackles().getTotal().toString() +
                                                    " (blocks: " + pi.getTackles().getBlocks().toString() +
                                                    ", interceptions: " + pi.getTackles().getInterceptions().toString() + ")");

                                            //playerBioStatics.add("Duels: " + pi.getDuels().getTotal().toString() +
                                                    //" (won duels: " + pi.getDuels().getWon().toString() + ")");

                                            playerBioStatics.add("Dribbles: " + (new Integer(pi.getDribbles().getAttempts().intValue() + pi.getDribbles().getSuccess().intValue())).toString() +
                                                    " (attempts: " + pi.getDribbles().getAttempts().toString() + ", success: " + pi.getDribbles().getSuccess().toString() + ")");

                                            playerBioStatics.add("Fouls: " + (new Integer(pi.getFouls().getDrawn().intValue() + pi.getFouls().getCommitted().intValue())).toString() +
                                                    " (drawn: " + pi.getFouls().getDrawn().toString() + ", committed: " + pi.getFouls().getCommitted().toString() + ")");

                                            playerBioStatics.add("Cards: " + (new Integer(pi.getCards().getYellow().intValue() + pi.getCards().getRed().intValue())).toString() +
                                                    " (yellow: " + pi.getCards().getYellow().toString() + ", red: " + pi.getCards().getRed().toString() + ")");

                                            playerBioStatics.add("Penalty: " + "(won:" + pi.getPenalty().getWon().toString() + ", commited:" + pi.getPenalty().getCommited().toString() +
                                                    ", success:" + pi.getPenalty().getSuccess().toString() + ", missed:" + pi.getPenalty().getMissed().toString() + ", saved:" + pi.getPenalty().getSaved().toString() + ")");

                                            playerBioStatics.add("Games: " + "(appearences:" + pi.getGames().getAppearences().toString() + ", minutes played:" +
                                                    pi.getGames().getMinutesPlayed().toString() + ", lineups:" + pi.getGames().getLineups().toString() + ")");

                                            //playerBioStatics.add("Substitutes: " + "(In:" + pi.getSubstitutes().getIn().toString() +
                                                    //", Out:" + pi.getSubstitutes().getOut().toString() + ", Bench:" + pi.getSubstitutes().getBench().toString() + ")");

                                            if (playerImagesAndMore != null) {
                                                if (playerImagesAndMore.getPlayer() != null) {
                                                    playerStaticsImages.add(playerImagesAndMore.getPlayer().get(0).getStrFanart1());
                                                    playerStaticsImages.add(playerImagesAndMore.getPlayer().get(0).getStrFanart2());
                                                    playerStaticsImages.add(playerImagesAndMore.getPlayer().get(0).getStrFanart3());
                                                    playerStaticsImages.add(playerImagesAndMore.getPlayer().get(0).getStrFanart4());
                                                }
                                            }
                                        }

                                        playerBioStatics.add("\n");

                                        SpannableString titleee = new SpannableString(pi.getPlayerName());
                                        titleee.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, titleee.length(), 0);

                                        someBuilder.setTitle(titleee);

                                        ListAdapter adapterrrr = new ArrayAdapter<String>(
                                                getContext(), R.layout.dialod_matches, playerBioStatics) {

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
                                                holder.icon.setImageDrawable(drawable);

                                                if((getItem(position).equals("Bio:")) || (getItem(position).equals("\n")) ||
                                                        (getItem(position).equals("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t*Statics*"))){
                                                    holder.icon.setVisibility(View.GONE);
                                                    holder.rowDetails.setTextColor(getResources().getColor(R.color.colorBlack));

                                                }

                                                return convertView;
                                            }
                                        };

                                        someBuilder.setAdapter(adapterrrr,null);

                                        someBuilder.setNeutralButton("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tOK,Nice", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                                        String someImageee = null;

                                        //maybe change to random player images
                                        if(playerBioImages.size() > 0 || playerStaticsImages.size() > 0){

                                            if(selectedString.equals("Biography...")){
                                                if(playerBioImages.size() > 0){
                                                    someImageee = playerBioImages.get(0);
                                                }
                                            }
                                            else{
                                                if(playerStaticsImages.size() > 0){
                                                    someImageee = playerStaticsImages.get(0);
                                                }
                                            }
                                        }

                                        if(someImageee != null){
                                            final View customLayoutViewww = getLayoutInflater().inflate(R.layout.dialog_player_image, null);
                                            URI uri = null;

                                            try {
                                                uri = new URI(someImageee);
                                            } catch (URISyntaxException e) {
                                                e.printStackTrace();
                                            }

                                            GlideToVectorYou.justLoadImageAsBackground((MainActivity)getActivity(),android.net.Uri.parse(uri.toString()), (ImageView)customLayoutViewww.findViewById(R.id.imageViewPlayer));
                                            someBuilder.setView(customLayoutViewww);
                                        }


                                        AlertDialog dialoggg = someBuilder.create();

                                        dialoggg.getListView().setOnItemClickListener(
                                                new AdapterView.OnItemClickListener() {
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        //String selectedString = (String) parent.getItemAtPosition(position);
                                                        //Toast.makeText(getContext(), selectedString,Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        dialoggg.show();

                                        dialoggg.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorGreen));
                                        dialoggg.getButton(DialogInterface.BUTTON_NEUTRAL).setTypeface(dialoggg.getButton(DialogInterface.BUTTON_NEUTRAL).getTypeface(), Typeface.BOLD);
                                        dialoggg.getButton(DialogInterface.BUTTON_NEUTRAL).setTextSize(20);

                                        Window window = dialoggg.getWindow();
                                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                        window.setGravity(Gravity.CENTER);

                                        if(MainActivity.changeMode)
                                            window.setBackgroundDrawableResource(R.color.greyyy);
                                    }
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

    /*
    private void getPlayerBioAndImages(String theTeamName,String thePlayerName){
        Call<PlayerImagesAndMore> call = apiHandlerInterfaceImagesNotSynchronise.getPlayerImagesAndMore(theTeamName,thePlayerName);
        call.enqueue(new Callback<PlayerImagesAndMore>() {
            @Override
            public void onResponse(Call<PlayerImagesAndMore> call, Response<PlayerImagesAndMore> response) {
                pIaM = response.body();

                if(pIaM.getPlayer() != null){
                    playerImageUrl = pIaM.getPlayer().get(0).getStrThumb();
                }
            }

            @Override
            public void onFailure(Call<PlayerImagesAndMore> call, Throwable t) {
                Log.e("in enqueue", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }*/
}