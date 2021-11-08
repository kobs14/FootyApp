
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cards {

    @SerializedName("yellow")
    @Expose
    private Integer yellow;
    @SerializedName("yellowred")
    @Expose
    private Integer yellowred;
    @SerializedName("red")
    @Expose
    private Integer red;

    public Integer getYellow() {
        return yellow;
    }

    public void setYellow(Integer yellow) {
        this.yellow = yellow;
    }

    public Integer getYellowred() {
        return yellowred;
    }

    public void setYellowred(Integer yellowred) {
        this.yellowred = yellowred;
    }

    public Integer getRed() {
        return red;
    }

    public void setRed(Integer red) {
        this.red = red;
    }

}
