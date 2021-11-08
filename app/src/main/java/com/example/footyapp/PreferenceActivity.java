package com.example.footyapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.footyapp.ui.Favorites.FavoritesFragment;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_preferences);
        //setContentView(R.layout.activity_settings); // your layout XML here

        // Insert the settings fragment in the FrameLayout we added earlier
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new FavoritesFragment())
                .commit();
    }
}
