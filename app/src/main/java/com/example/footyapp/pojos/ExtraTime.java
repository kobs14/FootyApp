
package com.example.footyapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtraTime {

    @SerializedName("homeTeam")
    @Expose
    private String homeTeam;
    @SerializedName("awayTeam")
    @Expose
    private String awayTeam;

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

}
