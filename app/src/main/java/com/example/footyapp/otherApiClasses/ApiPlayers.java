
package com.example.footyapp.otherApiClasses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiPlayers {

    @SerializedName("results")
    @Expose
    private Integer results;
    @SerializedName("players")
    @Expose
    private List<Player> players = null;

    public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}
