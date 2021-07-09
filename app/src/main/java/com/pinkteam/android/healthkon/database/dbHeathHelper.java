package com.pinkteam.android.healthkon.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.pinkteam.android.healthkon.database.*;
import androidx.annotation.Nullable;

public class dbHeathHelper extends SQLiteOpenHelper {

    public dbHeathHelper(@Nullable Context context) {
        super(context,"HealthDb", null, 2);
        this.context=context;
    }
    private final Context context;

    private static final String CREATE_TABLE_USER;
    static {
        CREATE_TABLE_USER = " CREATE TABLE " + dbHealthSchema.UserTable.TABLE_NAME + " (" +
                dbHealthSchema.UserTable.Id + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                dbHealthSchema.UserTable.NAME + " TEXT NOT NULL, " +
                dbHealthSchema.UserTable.AGE + " TEXT, " +
                dbHealthSchema.UserTable.GENDER + " TEXT, " +
                dbHealthSchema.UserTable.EMAIL + " TEXT, " +
                dbHealthSchema.UserTable.PHONE + " TEXT )";
    }

    private static final String CREATE_TABLE_ACCOUNT;
    static {
        CREATE_TABLE_ACCOUNT=" CREATE TABLE " + dbHealthSchema.AccountTable.TABLE_NAME + " (" +
            dbHealthSchema.AccountTable.Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            dbHealthSchema.AccountTable.Username + " TEXT NOT NULL, " +
            dbHealthSchema.AccountTable.Password + " TEXT NOT NULL )";
    }

    private static final String CREATE_TABLE_HEIGHT;
    static {
        CREATE_TABLE_HEIGHT=" CREATE TABLE " + dbHealthSchema.HeightTable.TABLE_NAME + " (" +
            dbHealthSchema.HeightTable.Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            dbHealthSchema.HeightTable.Value + " INTEGER, " +
            dbHealthSchema.HeightTable.Date + " REAL )";
    }

    private static final String CREATE_TABLE_WEIGHT;
    static {
        CREATE_TABLE_WEIGHT=" CREATE TABLE " + dbHealthSchema.WeightTable.TABLE_NAME + " (" +
                dbHealthSchema.WeightTable.Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                dbHealthSchema.WeightTable.Value + " INTEGER, " +
                dbHealthSchema.WeightTable.Date + " REAL )";
    }

    private static final String CREATE_TABLE_TOP;
    static {
        CREATE_TABLE_TOP = " CREATE TABLE " + dbHealthSchema.TopTable.TABLE_NAME + " (" +
                dbHealthSchema.TopTable.Id + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                dbHealthSchema.TopTable.Username + " TEXT NOT NULL, " +
                dbHealthSchema.TopTable.Distance + " REAL, " +
                dbHealthSchema.TopTable.Start_Day + " REAL, " +
                dbHealthSchema.TopTable.End_Day + " REAL, " +
                dbHealthSchema.TopTable.Top + " TEXT )";
    }

    private static final String CREATE_TABLE_JOURNEY;
    static {
        CREATE_TABLE_JOURNEY = " CREATE TABLE " + dbHealthSchema.JourneyTable.TABLE_NAME + " (" +
                dbHealthSchema.JourneyTable.JourneyId + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                dbHealthSchema.JourneyTable.Duration + " BIGINT NOT NULL, " +
                dbHealthSchema.JourneyTable.Distance + " REAL NOT NULL, " +
                dbHealthSchema.JourneyTable.Date + " REAL NOT NULL, " +
                dbHealthSchema.JourneyTable.Name + " TEXT NOT NULL, " +
                dbHealthSchema.JourneyTable.Rating + " INTEGER NOT NULL, " +
                dbHealthSchema.JourneyTable.Comment + " VARCHAR(256) , " +
                dbHealthSchema.JourneyTable.Image + " VARCHAR(256))";
    }

    private static final String CREATE_TABLE_LOCATION;
    static {
        CREATE_TABLE_LOCATION = " CREATE TABLE " + dbHealthSchema.LocationTable.TABLE_NAME + " (" +
                dbHealthSchema.LocationTable.LocationID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                dbHealthSchema.LocationTable.JourneyID + " INTEGER NOT NULL, " +
                dbHealthSchema.LocationTable.Altitude + " REAL NOT NULL, " +
                dbHealthSchema.LocationTable.Longitude + " REAL NOT NULL, " +
                dbHealthSchema.LocationTable.Latitude + " REAL NOT NULL, " +
        " CONSTRAINT fk1 FOREIGN KEY ("+ dbHealthSchema.LocationTable.JourneyID +") REFERENCES "+ dbHealthSchema.JourneyTable.TABLE_NAME + " ("+ dbHealthSchema.JourneyTable.JourneyId+") ON DELETE CASCADE);";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_HEIGHT);
        db.execSQL(CREATE_TABLE_WEIGHT);
        db.execSQL(CREATE_TABLE_TOP);
        db.execSQL(CREATE_TABLE_JOURNEY);
        db.execSQL(CREATE_TABLE_LOCATION);
        Log.d("SQL", " onCreate: ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("SQL-data", "Database has been created! ");
        Log.d("SQL-data", " New ver: "+newVersion);
    }
}
