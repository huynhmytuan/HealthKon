package com.pinkteam.android.healthkon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pinkteam.android.healthkon.Models.*;
import com.pinkteam.android.healthkon.database.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JourneyUtil {
    private  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    SQLiteDatabase mDatabase;
    Context mContext;
    public JourneyUtil(Context context){
        mContext = context;
        mDatabase = new dbHeathHelper(mContext).getWritableDatabase();
    }

    private ContentValues journeyContentValues(Journey journey){
        String journeyDate = dateFormat.format(journey.getmDate());
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHealthSchema.JourneyTable.Duration,journey.getmDuration());
        contentValues.put(dbHealthSchema.JourneyTable.Distance,journey.getmDistance());
        contentValues.put(dbHealthSchema.JourneyTable.Date, journeyDate);
        contentValues.put(dbHealthSchema.JourneyTable.Name,journey.getmName());
        contentValues.put(dbHealthSchema.JourneyTable.Rating,journey.getmRating());
        contentValues.put(dbHealthSchema.JourneyTable.Comment,journey.getmComment());
        contentValues.put(dbHealthSchema.JourneyTable.Image,journey.getmImage());
        return contentValues;
    }
        //Insert
        public long add(long Duration, float Distance, Date Date, String Name, int Rating, String Comment, String Image){
        Journey mJourney = new Journey(Duration,Distance,Date,Name,Rating,Comment,Image);
        ContentValues contentValues = journeyContentValues(mJourney);
        long result = mDatabase.insert("journey", null,contentValues);
        return result;
    }

        //Update
        public long update(Journey journey){
        ContentValues contentValues = journeyContentValues(journey);
        long result= mDatabase.update("journey",contentValues, dbHealthSchema.JourneyTable.JourneyId +"=?",new String[]{String.valueOf(journey.getmJourneyId())});
        return result;
    }
        //delete
        public long delete(int id){
        String journeyID = Integer.toString(id);
        LocationUtil mLU = new LocationUtil(this.mContext);
        mLU.delete(id);
        return mDatabase.delete("journey", dbHealthSchema.JourneyTable.JourneyId +"=?", new String[]{journeyID});
    }
        //view data
        public Cursor viewData(){
        String view = " SELECT * FROM " + dbHealthSchema.JourneyTable.TABLE_NAME;
        Cursor cursor = mDatabase.rawQuery(view, null);
        return cursor;
    }
        // Truy van toan bo du lieu do ve 1 danh sach
        public List<Journey> getAllJourney(){
        List <Journey> journeys = new ArrayList<>();
        Cursor cursor = viewData();
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String id = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.JourneyId));
                String duration = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Duration));
                String distance = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Distance));
                String date = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Date));
                String name = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Name));
                String rating = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Rating));
                String comment = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Comment));
                String image = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Image));

                Journey journey = new Journey();

                try{
                    journey.setmJourneyId(Integer.parseInt(id));
                    journey.setmDuration(Long.parseLong(duration));
                    journey.setmDistance(Float.parseFloat(distance));
                    journey.setmDate(dateFormat.parse(date));
                    journey.setmName(name);
                    journey.setmRating(Integer.parseInt(rating));
                    journey.setmComment(comment);
                    journey.setmImage(image);
                }
                catch (Exception e){
                    Log.d("Database_Exp:",e.getMessage());
                }
                journeys.add(journey);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return journeys;
    }
    public String getLastestRowID(){
            String rowID;
            Cursor cursor = viewData();
            cursor.moveToLast();
            if (cursor.getCount()>0){
                rowID = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.JourneyId));
            }
            else{
                rowID = "null";
            }
            return rowID;
    }

    public Journey getJourneyByID(int JourneyID){
        Journey journey = new Journey();
        String query = "SELECT * FROM "+ dbHealthSchema.JourneyTable.TABLE_NAME + " WHERE " + dbHealthSchema.JourneyTable.JourneyId + " = " + JourneyID;
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String id = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.JourneyId));
                String duration = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Duration));
                String distance = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Distance));
                String date = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Date));
                String name = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Name));
                String rating = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Rating));
                String comment = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Comment));
                String image = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Image));
                try{
                    journey.setmJourneyId(Integer.parseInt(id));
                    journey.setmDuration(Long.parseLong(duration));
                    journey.setmDistance(Float.parseFloat(distance));
                    journey.setmDate(dateFormat.parse(date));
                    journey.setmName(name);
                    journey.setmRating(Integer.parseInt(rating));
                    journey.setmComment(comment);
                    journey.setmImage(image);
                }
                catch (Exception e){
                    Log.d("Database_Exp:",e.getMessage());
                }
                cursor.moveToNext();
            }
            cursor.close();
        }

        return journey;
    }
    public List<Journey> getJourneysByDate(Date startDate, Date endDate){
        List<Journey> journeyList =  new ArrayList<>();
        String start = dateFormat.format(startDate);
        String end = dateFormat.format(endDate);

        String query = " SELECT * FROM " + dbHealthSchema.JourneyTable.TABLE_NAME +
                " WHERE " + dbHealthSchema.JourneyTable.Date + " BETWEEN \"" + start + "\" AND \"" + end +"\"";
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String id = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.JourneyId));
                String duration = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Duration));
                String distance = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Distance));
                String date = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Date));
                String name = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Name));
                String rating = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Rating));
                String comment = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Comment));
                String image = cursor.getString(cursor.getColumnIndex(dbHealthSchema.JourneyTable.Image));

                Journey journey = new Journey();

                try{
                    journey.setmJourneyId(Integer.parseInt(id));
                    journey.setmDuration(Long.parseLong(duration));
                    journey.setmDistance(Float.parseFloat(distance));
                    journey.setmDate(dateFormat.parse(date));
                    journey.setmName(name);
                    journey.setmRating(Integer.parseInt(rating));
                    journey.setmComment(comment);
                    journey.setmImage(image);
                }
                catch (Exception e){
                    Log.d("Database_Exp:",e.getMessage());
                }
                journeyList.add(journey);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return journeyList;
    }

    public ArrayList<HashMap<String,Object>> getTotalDistanceCurWeeks(){
        //List Hashmap to store data
        ArrayList<HashMap<String,Object>> resultList = new ArrayList<>();
        String query = "SELECT strftime('%W',date) as \"Week Order\","
                        + "SUM(distance) as \"Total Distance\","
                        + "strftime('%d/%m', MAX(DATE(date, 'weekday 0','-6 day')) ) as \"Week Start\","
                        + "strftime('%d/%m', MAX(DATE(date, 'weekday 0')) ) as \"Week End\""
                        + " from journey "
                        +" where "
                        + "strftime('%W',date)  >= strftime('%W',DATE('now', 'weekday 0', '-30 days'))"
                        + " group by strftime('%W',date);";
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                int week_order = cursor.getInt(cursor.getColumnIndex("Week Order"));
                float total_distance = cursor.getFloat(cursor.getColumnIndex("Total Distance"));
                String week_start = cursor.getString(cursor.getColumnIndex("Week Start"));
                String week_end = cursor.getString(cursor.getColumnIndex("Week End"));

                HashMap<String,Object> mMap = new HashMap();
                mMap.put("week_order",week_order);
                mMap.put("total_distance",total_distance);
                mMap.put("week_start",week_start);
                mMap.put("week_end",week_end);

                resultList.add(mMap);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return resultList;
    }

    public ArrayList<HashMap<String,Object>> getTotalDistance5Weeks(){
        //List Hashmap to store data
        ArrayList<HashMap<String,Object>> resultList = new ArrayList<>();
        String query = "SELECT strftime('%W',date) as \"Week Order\","
                        + "SUM(distance) as \"Total Distance\","
                        + "strftime('%d/%m', MAX(DATE(date, 'weekday 0','-6 day')) ) as \"Week Start\","
                        + "strftime('%d/%m', MAX(DATE(date, 'weekday 0')) ) as \"Week End\""
                        + " from journey "
                        +" where "
                        + "strftime('%W',date)  >= strftime('%W',DATE('now', 'weekday 0', '-30 days'))"
                        + " group by strftime('%W',date);";
        Cursor cursor = mDatabase.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                int week_order = cursor.getInt(cursor.getColumnIndex("Week Order"));
                float total_distance = cursor.getFloat(cursor.getColumnIndex("Total Distance"));
                String week_start = cursor.getString(cursor.getColumnIndex("Week Start"));
                String week_end = cursor.getString(cursor.getColumnIndex("Week End"));

                HashMap<String,Object> mMap = new HashMap();
                mMap.put("week_order",week_order);
                mMap.put("total_distance",total_distance);
                mMap.put("week_start",week_start);
                mMap.put("week_end",week_end);

                resultList.add(mMap);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return resultList;
    }

}

