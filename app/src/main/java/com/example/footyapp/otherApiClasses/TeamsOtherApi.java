
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamsOtherApi {

    @SerializedName("api")
    @Expose
    private ApiTeams api;

    public ApiTeams getApi() {
        return api;
    }

    public void setApi(ApiTeams api) {
        this.api = api;
    }

}
