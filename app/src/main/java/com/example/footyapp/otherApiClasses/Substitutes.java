
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Substitutes {

    @SerializedName("in")
    @Expose
    private Integer in;
    @SerializedName("out")
    @Expose
    private Integer out;
    @SerializedName("bench")
    @Expose
    private Integer bench;

    public Integer getIn() {
        return in;
    }

    public void setIn(Integer in) {
        this.in = in;
    }

    public Integer getOut() {
        return out;
    }

    public void setOut(Integer out) {
        this.out = out;
    }

    public Integer getBench() {
        return bench;
    }

    public void setBench(Integer bench) {
        this.bench = bench;
    }

}
