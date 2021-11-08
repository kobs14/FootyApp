package com.example.footyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.blongho.country_data.World;
import com.example.footyapp.ui.Favorites.FavoritesFragment;
import com.example.footyapp.ui.League.LeagueTableFragment;
import com.example.footyapp.ui.Preferences.PreferencesFragment;
import com.example.footyapp.ui.matches.MatchesFragment;
import com.example.footyapp.ui.TeamSquad.MyTeamFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

//AppCompatActivity
public class MainActivity extends FragmentActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    DatabaseHelper dbHelper;
    public static boolean changeMode = false;
    public static boolean isModeNotChanged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(!changeMode){
            setTheme(R.style.LightTheme);
        }
        else{
            setTheme(R.style.DarkTheme);
        }

        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        World.init(getApplicationContext());

        //this.deleteDatabase("dataBaseManager");

        if(isModeNotChanged){
            Intent intent = new Intent(MainActivity.this,SittingsActivity.class);
            startActivity(intent);
        }

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_matches);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_matches:
                fragment = new MatchesFragment();
                break;

            case R.id.navigation_league:
                fragment = new LeagueTableFragment();
                break;

            case R.id.navigation_myteam:
                fragment = new MyTeamFragment();
                break;

            case R.id.navigation_favorites:
                fragment = new FavoritesFragment();
                break;

            case R.id.navigation_preferences:
                fragment = new PreferencesFragment();
                break;
        }

        return loadFragment(fragment);
    }

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();

            return true;
        }

        return false;
    }
}
