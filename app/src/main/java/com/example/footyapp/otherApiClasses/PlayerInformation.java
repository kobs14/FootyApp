
package com.example.footyapp.otherApiClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerInformation {

    @SerializedName("player_id")
    @Expose
    private Integer playerId;
    @SerializedName("player_name")
    @Expose
    private String playerName;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("number")
    @Expose
    private Object number;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("birth_place")
    @Expose
    private String birthPlace;
    @SerializedName("birth_country")
    @Expose
    private String birthCountry;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("injured")
    @Expose
    private Object injured;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("team_id")
    @Expose
    private Integer teamId;
    @SerializedName("team_name")
    @Expose
    private String teamName;
    @SerializedName("league_id")
    @Expose
    private Integer leagueId;
    @SerializedName("league")
    @Expose
    private String league;
    @SerializedName("season")
    @Expose
    private String season;
    @SerializedName("captain")
    @Expose
    private Integer captain;
    @SerializedName("shots")
    @Expose
    private Shots shots;
    @SerializedName("goals")
    @Expose
    private Goals goals;
    @SerializedName("passes")
    @Expose
    private Passes passes;
    @SerializedName("tackles")
    @Expose
    private Tackles tackles;
    @SerializedName("duels")
    @Expose
    private Duels duels;
    @SerializedName("dribbles")
    @Expose
    private Dribbles dribbles;
    @SerializedName("fouls")
    @Expose
    private Fouls fouls;
    @SerializedName("cards")
    @Expose
    private Cards cards;
    @SerializedName("penalty")
    @Expose
    private Penalty penalty;
    @SerializedName("games")
    @Expose
    private Games games;
    @SerializedName("substitutes")
    @Expose
    private Substitutes substitutes;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Object getNumber() {
        return number;
    }

    public void setNumber(Object number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Object getInjured() {
        return injured;
    }

    public void setInjured(Object injured) {
        this.injured = injured;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Integer getCaptain() {
        return captain;
    }

    public void setCaptain(Integer captain) {
        this.captain = captain;
    }

    public Shots getShots() {
        return shots;
    }

    public void setShots(Shots shots) {
        this.shots = shots;
    }

    public Goals getGoals() {
        return goals;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }

    public Passes getPasses() {
        return passes;
    }

    public void setPasses(Passes passes) {
        this.passes = passes;
    }

    public Tackles getTackles() {
        return tackles;
    }

    public void setTackles(Tackles tackles) {
        this.tackles = tackles;
    }

    public Duels getDuels() {
        return duels;
    }

    public void setDuels(Duels duels) {
        this.duels = duels;
    }

    public Dribbles getDribbles() {
        return dribbles;
    }

    public void setDribbles(Dribbles dribbles) {
        this.dribbles = dribbles;
    }

    public Fouls getFouls() {
        return fouls;
    }

    public void setFouls(Fouls fouls) {
        this.fouls = fouls;
    }

    public Cards getCards() {
        return cards;
    }

    public void setCards(Cards cards) {
        this.cards = cards;
    }

    public Penalty getPenalty() {
        return penalty;
    }

    public void setPenalty(Penalty penalty) {
        this.penalty = penalty;
    }

    public Games getGames() {
        return games;
    }

    public void setGames(Games games) {
        this.games = games;
    }

    public Substitutes getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(Substitutes substitutes) {
        this.substitutes = substitutes;
    }

}
