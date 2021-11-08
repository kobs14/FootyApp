
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayersOtherApi {

    @SerializedName("api")
    @Expose
    private ApiPlayers api;

    public ApiPlayers getApi() {
        return api;
    }

    public void setApi(ApiPlayers api) {
        this.api = api;
    }

}
