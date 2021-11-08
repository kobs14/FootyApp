package com.example.footyapp.ui.League;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeagueViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LeagueViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is League Table fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}