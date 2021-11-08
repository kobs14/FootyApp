
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Games {

    @SerializedName("appearences")
    @Expose
    private Integer appearences;
    @SerializedName("minutes_played")
    @Expose
    private Integer minutesPlayed;
    @SerializedName("lineups")
    @Expose
    private Integer lineups;

    public Integer getAppearences() {
        return appearences;
    }

    public void setAppearences(Integer appearences) {
        this.appearences = appearences;
    }

    public Integer getMinutesPlayed() {
        return minutesPlayed;
    }

    public void setMinutesPlayed(Integer minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }

    public Integer getLineups() {
        return lineups;
    }

    public void setLineups(Integer lineups) {
        this.lineups = lineups;
    }

}
