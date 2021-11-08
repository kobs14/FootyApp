
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Penalty {

    @SerializedName("won")
    @Expose
    private Integer won;
    @SerializedName("commited")
    @Expose
    private Integer commited;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("missed")
    @Expose
    private Integer missed;
    @SerializedName("saved")
    @Expose
    private Integer saved;

    public Integer getWon() {
        return won;
    }

    public void setWon(Integer won) {
        this.won = won;
    }

    public Integer getCommited() {
        return commited;
    }

    public void setCommited(Integer commited) {
        this.commited = commited;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getMissed() {
        return missed;
    }

    public void setMissed(Integer missed) {
        this.missed = missed;
    }

    public Integer getSaved() {
        return saved;
    }

    public void setSaved(Integer saved) {
        this.saved = saved;
    }

}
