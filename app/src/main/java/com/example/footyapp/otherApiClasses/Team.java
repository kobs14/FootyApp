
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Team {

    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private Object code;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("is_national")
    @Expose
    private Boolean isNational;
    @SerializedName("founded")
    @Expose
    private Integer founded;
    @SerializedName("venue_name")
    @Expose
    private String venueName;
    @SerializedName("venue_surface")
    @Expose
    private String venueSurface;
    @SerializedName("venue_address")
    @Expose
    private String venueAddress;
    @SerializedName("venue_city")
    @Expose
    private String venueCity;
    @SerializedName("venue_capacity")
    @Expose
    private Integer venueCapacity;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getIsNational() {
        return isNational;
    }

    public void setIsNational(Boolean isNational) {
        this.isNational = isNational;
    }

    public Integer getFounded() {
        return founded;
    }

    public void setFounded(Integer founded) {
        this.founded = founded;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueSurface() {
        return venueSurface;
    }

    public void setVenueSurface(String venueSurface) {
        this.venueSurface = venueSurface;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }

    public Integer getVenueCapacity() {
        return venueCapacity;
    }

    public void setVenueCapacity(Integer venueCapacity) {
        this.venueCapacity = venueCapacity;
    }

}
