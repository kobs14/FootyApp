
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tackles {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("blocks")
    @Expose
    private Integer blocks;
    @SerializedName("interceptions")
    @Expose
    private Integer interceptions;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getBlocks() {
        return blocks;
    }

    public void setBlocks(Integer blocks) {
        this.blocks = blocks;
    }

    public Integer getInterceptions() {
        return interceptions;
    }

    public void setInterceptions(Integer interceptions) {
        this.interceptions = interceptions;
    }

}
