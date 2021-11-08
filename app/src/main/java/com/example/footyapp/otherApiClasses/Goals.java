
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Goals {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("conceded")
    @Expose
    private Integer conceded;
    @SerializedName("assists")
    @Expose
    private Integer assists;
    @SerializedName("saves")
    @Expose
    private Integer saves;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getConceded() {
        return conceded;
    }

    public void setConceded(Integer conceded) {
        this.conceded = conceded;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getSaves() {
        return saves;
    }

    public void setSaves(Integer saves) {
        this.saves = saves;
    }

}
