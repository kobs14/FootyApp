
package com.example.footyapp.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeadToHead {

    @SerializedName("head2head")
    @Expose
    private Head2head head2head;
    @SerializedName("match")
    @Expose
    private Match match;

    public Head2head getHead2head() {
        return head2head;
    }

    public void setHead2head(Head2head head2head) {
        this.head2head = head2head;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

}
