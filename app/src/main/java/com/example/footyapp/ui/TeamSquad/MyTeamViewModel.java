package com.example.footyapp.ui.TeamSquad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyTeamViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyTeamViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is MyTeam fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}