
package com.example.footyapp.otherApiClasses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiTeams {

    @SerializedName("results")
    @Expose
    private Integer results;
    @SerializedName("teams")
    @Expose
    private List<Team> teams = null;

    public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

}
