package com.pinkteam.android.healthkon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pinkteam.android.healthkon.database.*;
import com.pinkteam.android.healthkon.Models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TopUtil {

    SQLiteDatabase mDatabase;
    Context mContext;
    public TopUtil(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new dbHeathHelper(mContext).getWritableDatabase();
    }
    private ContentValues topContentValues(Top top){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHealthSchema.TopTable.Username,top.getmUsername());
        contentValues.put(dbHealthSchema.TopTable.Distance,top.getmDistance());
        contentValues.put(dbHealthSchema.TopTable.Start_Day, String.valueOf(top.getmStart_day()));
        contentValues.put(dbHealthSchema.TopTable.End_Day, String.valueOf(top.getmEnd_day()));
        contentValues.put(dbHealthSchema.TopTable.Top,top.getmTop());

        return contentValues;
    }

    //Insert
    public long add(String Username, int Distance, Date Start_day, Date End_day, String Top){
        Top mTop = new Top(Username,Distance,Start_day,End_day,Top);
        ContentValues contentValues = topContentValues(mTop);
        long result = mDatabase.insert("top",null,contentValues);
        return result;
    }

    //Update
    public long update(Top top){
        ContentValues contentValues = topContentValues(top);
        long result= mDatabase.update("top",contentValues, dbHealthSchema.TopTable.Id +"=?",new String[]{String.valueOf(top.getmId())});
        return result;
    }
    //delete
    public long delete(int id){
        String topID = Integer.toString(id);
        return mDatabase.delete("top", dbHealthSchema.TopTable.Id +"=?", new String[]{topID});
    }
    //view data
    public Cursor viewData(){
        String view = " SELECT * FROM " + dbHealthSchema.TopTable.TABLE_NAME;
        Cursor cursor = mDatabase.rawQuery(view, null);

        return cursor;
    }
    // Truy van toan bo du lieu do ve 1 danh sach
    public List<Top> getAllTop(){
        List <Top> tops = new ArrayList<>();
        String SELECT = "SELECT * FROM " + "top";
        Cursor cursor = mDatabase.rawQuery(SELECT, null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String id = cursor.getString(cursor.getColumnIndex(dbHealthSchema.TopTable.Id));
                String username = cursor.getString(cursor.getColumnIndex(dbHealthSchema.TopTable.Username));
                String distance = cursor.getString(cursor.getColumnIndex(dbHealthSchema.TopTable.Distance));
                long start_day = cursor.getLong(cursor.getColumnIndex(dbHealthSchema.TopTable.Start_Day));
                long end_day = cursor.getLong(cursor.getColumnIndex(dbHealthSchema.TopTable.End_Day));
                String top = cursor.getString(cursor.getColumnIndex(dbHealthSchema.TopTable.Top));

                Top top1 = new Top();
                top1.setmId(Integer.parseInt(id));
                top1.setmUsername(username);
                top1.setmDistance(Integer.parseInt(distance));
                top1.setmStart_day(new Date(start_day));
                top1.setmEnd_day(new Date(end_day));
                top1.setmTop(top);

                tops.add(top1);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return tops;
    }

}
