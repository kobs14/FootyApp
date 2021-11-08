
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dribbles {

    @SerializedName("attempts")
    @Expose
    private Integer attempts;
    @SerializedName("success")
    @Expose
    private Integer success;

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}
