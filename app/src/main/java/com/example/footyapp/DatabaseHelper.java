package com.example.footyapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.footyapp.pojos.Table;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dataBaseManager";

    //Tables names
    public static final String TABLE_MATCH = "match_table";
    public static final String TABLE_TEAM_INFO = "team_table_info";
    public static final String TABLE_H2H = "h2h_table";
    public static final String TABLE_League = "league_table";
    public static final String TABLE_UserChoice = "user_choice_table";
    public static final String TABLE_RESULTS = "results_table";


    // Common column name
    public static final String COL_ID = "id";
    public static final String COL_TeamName = "team_name";
    public static final String COL_TeamID = "teamID";
    public static final String COL_Competition = "competition";

    //Match Table - column names
    public static final String COL_city = "city";
    public static final String COL_date = "date";
    public static final String COL_teamA = "teamA";
    public static final String COL_teamB = "teamB";
    public static final String COL_time = "time";
    public static final String COL_Stadium = "stadium";

    //Team INFO Table - column names
    public static final String COL_Competition_1 = "competition_1";
    public static final String COL_Competition_2 = "competition_2";
    public static final String COL_Competition_3 = "competition_3";
    public static final String COL_Competition_4 = "competition_4";
    public static final String COL_Competition_5 = "competition_5";
    public static final String COL_CREST_URL = "crest_url";
    public static final String COL_TEAM_ADDRESS = "address";
    public static final String COL_WEBSITE = "website";
    public static final String COL_FOUNDED = "founded";
    public static final String COL_CLUB_COLORS = "club_colors";

    /*-----optional----*/
    //public static final String COL_appearanceNumber = "appearanceNumber";


    //H2H-(head to head) Table - column names
    //public static final String COL_Team = "team";
    public static final String COL_game1 = "game1";
    public static final String COL_game2 = "game2";
    public static final String COL_game3 = "game3";
    public static final String COL_game4 = "game4";

    //League Table - column names
    //public static final String COL_Team = "team";
    public static final String COL_Position = "position";
    //public static final String COL_Team = "team";
    public static final String COL_Points = "points";
    public static final String COL_Played = "played";
    public static final String COL_Wins = "wins";
    public static final String COL_Draws = "draws";
    public static final String COL_Losses = "losses";

    // Table Create Statements

    // Match table create statement
    private static final String CREATE_TABLE_MATCH =
            "CREATE TABLE " + DatabaseHelper.TABLE_MATCH + " (" +
                    DatabaseHelper.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseHelper.COL_date + " TEXT," +
                    DatabaseHelper.COL_teamA + " TEXT," +
                    DatabaseHelper.COL_teamB + " TEXT," +
                    DatabaseHelper.COL_city + " TEXT," +
                    DatabaseHelper.COL_time + " TEXT," +
                    DatabaseHelper.COL_Stadium + " TEXT)";



    //H2H table create statement
    private static final String CREATE_TABLE_H2H =
            "CREATE TABLE " + DatabaseHelper.TABLE_H2H + " (" +
                    DatabaseHelper.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseHelper.COL_TeamName+ " TEXT," +
                    DatabaseHelper. COL_game1 + " TEXT," +
                    DatabaseHelper.COL_game2 + " TEXT," +
                    DatabaseHelper.COL_game3 + " TEXT," +
                    DatabaseHelper.COL_game4 + " TEXT)";

    //League table create statement
    private static final String CREATE_TABLE_League =
            "CREATE TABLE " + DatabaseHelper.TABLE_League + " (" +
                    DatabaseHelper.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseHelper.COL_Position + " INTEGER," +
                    DatabaseHelper.COL_TeamName+ " TEXT," +
                    DatabaseHelper.COL_Points + " INTEGER," +
                    DatabaseHelper.COL_Wins + " INTEGER," +
                    DatabaseHelper.COL_Draws + " INTEGER," +
                    DatabaseHelper.COL_Losses + " INTEGER)";

    //Team Info table create statement
    private static final String CREATE_TABLE_TEAM_INFO =
            "CREATE TABLE " + DatabaseHelper.TABLE_TEAM_INFO + " (" +
                    DatabaseHelper.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseHelper.COL_TeamName+ " TEXT," +
                    DatabaseHelper.COL_TeamID + " TEXT," +
                    DatabaseHelper.COL_Stadium + " TEXT," +
                    DatabaseHelper.COL_CREST_URL + " TEXT," +
                    DatabaseHelper.COL_TEAM_ADDRESS + " TEXT," +
                    DatabaseHelper.COL_WEBSITE + " TEXT," +
                    DatabaseHelper.COL_FOUNDED + " TEXT," +
                    DatabaseHelper.COL_CLUB_COLORS + " TEXT," +
                    DatabaseHelper.COL_Competition_1 + " TEXT," +
                    DatabaseHelper.COL_Competition_2 + " TEXT," +
                    DatabaseHelper.COL_Competition_3 + " TEXT," +
                    DatabaseHelper.COL_Competition_4 + " TEXT," +
                    DatabaseHelper.COL_Competition_5 + " TEXT)";



    private static final String CREATE_TABLE_UserChoices =
            "CREATE TABLE " + DatabaseHelper.TABLE_UserChoice + " (" +
                    DatabaseHelper.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DatabaseHelper.COL_Competition + " TEXT," +
                    DatabaseHelper.COL_TeamName+ " TEXT," +
                    DatabaseHelper.COL_TeamID+ " TEXT)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    /*    db.execSQL(CREATE_TABLE_MATCH);
        db.execSQL(CREATE_TABLE_TEAM);
        db.execSQL(CREATE_TABLE_H2H);

     */
        db.execSQL(CREATE_TABLE_UserChoices);
        db.execSQL(CREATE_TABLE_TEAM_INFO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    /*
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_H2H);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UserInfo);

     */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UserChoice);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM_INFO);
    }


    /********************************************/
    /*  ------------------------------------    */
    /*  ----Below code need to be fixed-----    */
    /*      -- Adjust to 4 tables!!!--          */
    /********************************************/

    public boolean insertData(String city,String date,String teamA,String teamB,String time, String stadium){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(COL_date,date);
        cv.put(COL_teamA,teamA);
        cv.put(COL_teamB,teamB);
        cv.put(COL_city,city);
        cv.put(COL_time,time);
        cv.put(COL_Stadium, stadium);
        if (db.insert(TABLE_MATCH,null,cv) == -1)
            return false;
        return true;
    }


    /*save user choices for favorite competition and team*/
    /* data is saved in TABLE_UserChoices*/

    public boolean insertUserChoice(String competition, String team, String teamId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(COL_Competition,competition);
        cv.put(COL_TeamName,team);
        cv.put(COL_TeamID, teamId);
        if (db.insert(TABLE_UserChoice,null,cv) == -1)
            return false;
        return true;
    }




    public boolean insertTeamInfoData(String team_name, String team_id, String stadium, String crestUrl, String address, String website, String founded,
                                      String clubColors, String competition_1, String competition_2, String competition_3, String competition_4,String competition_5){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(COL_TeamName,team_name);
        cv.put(COL_TeamID,team_id);
        cv.put(COL_Stadium, stadium);
        cv.put(COL_CREST_URL, crestUrl);
        cv.put(COL_TEAM_ADDRESS, address);
        cv.put(COL_WEBSITE, website);
        cv.put(COL_FOUNDED, founded);
        cv.put(COL_CLUB_COLORS, clubColors);
        cv.put(COL_Competition_1, competition_1);
        cv.put(COL_Competition_2, competition_2);
        cv.put(COL_Competition_3, competition_3);
        cv.put(COL_Competition_4, competition_4);
        cv.put(COL_Competition_5, competition_5);
        if (db.insert(TABLE_TEAM_INFO,null,cv) == -1)
            return false;
        return true;
    }

    public Cursor getTeamInfo(){
        String query = "select * from "+TABLE_TEAM_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }



    public Cursor getUserInfo()
    {
        String query = "select * from "+TABLE_UserChoice;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }

    public Cursor getUserChoice()
    {
        String query = "select * from "+TABLE_UserChoice;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }

    public Cursor getResults()
    {
        String query = "select * from "+TABLE_RESULTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }

    public Cursor getUserTeam()
    {
        String query = "select * from "+TABLE_UserChoice;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }


    public Cursor getByDate(String date)
    {
        String query = "select * from "+TABLE_MATCH+" WHERE "+COL_date+" like  '%"+date+"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }

    public Cursor getByTeamA(String team)
    {
        String query = "select * from "+TABLE_MATCH+" WHERE "+COL_teamA+" like  '%"+team+"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }
    public Cursor getByTeamB(String team)
    {
        String query = "select * from "+TABLE_MATCH+" WHERE "+COL_teamB+" like  '%"+team+"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(query,null);
        return res;
    }



    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_MATCH,null);
        return res;
    }


    public boolean updateData(String id,String city,String date,String teamA,String teamB,String time, String stadium){ //id is the changed match of the game so inorder to change it we need uneiuqe id for every match!
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  = new ContentValues();
        cv.put(COL_ID,id);
        cv.put(COL_city,city);
        cv.put(COL_date,date);
        cv.put(COL_teamA,teamA);
        cv.put(COL_teamB,teamB);
        cv.put(COL_time,time);
        cv.put(COL_Stadium, stadium);
        if ( db.update(TABLE_MATCH,cv,"id = ?",new String[]{id}) > 0)
            return true;
        return false;
    }

    public Integer deleteData(String id){  //we only need id to delete
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MATCH,"id = ?",new String[] {id});
    }

    public Integer deleteTABLE_UserInfo(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_UserChoice,"id = ?",new String[] {id});
    }

}