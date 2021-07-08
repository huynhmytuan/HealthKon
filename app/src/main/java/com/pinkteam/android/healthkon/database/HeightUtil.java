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

public class HeightUtil {

    SQLiteDatabase mDatabase;
    Context mContext;
    public HeightUtil(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new dbHeathHelper(mContext).getWritableDatabase();
    }

    private ContentValues heightContentValues(Height height){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHealthSchema.HeightTable.Value,height.getmValue());
        contentValues.put(dbHealthSchema.HeightTable.Date, String.valueOf(height.getmDate()));
        return contentValues;
    }

    //Insert
    public long add(int Value, Date Date){
        Height mHeight = new Height(Value, Date);
        ContentValues contentValues = heightContentValues(mHeight);
        long result = mDatabase.insert("height",null,contentValues);

        return result;
    }

    //Update
    public long update(Height height){
        ContentValues contentValues = heightContentValues(height);
        long result= mDatabase.update("height",contentValues, dbHealthSchema.HeightTable.Id +"=?",new String[]{String.valueOf(height.getmId())});
        return result;
    }
    //delete
    public long delete(int id){
        String heightID = Integer.toString(id);
        return mDatabase.delete("height", dbHealthSchema.HeightTable.Id +"=?", new String[]{heightID});
    }
    //view data
    public Cursor viewData(){
        String view = " SELECT * FROM " + dbHealthSchema.HeightTable.TABLE_NAME;
        Cursor cursor = mDatabase.rawQuery(view, null);
        return cursor;
    }
    // Truy van toan bo du lieu do ve 1 danh sach
    public List<Height> getAllHeight(){
        List <Height> heights = new ArrayList<>();
        Cursor cursor = viewData();
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String id = cursor.getString(cursor.getColumnIndex(dbHealthSchema.HeightTable.Id));
                String value = cursor.getString(cursor.getColumnIndex(dbHealthSchema.HeightTable.Value));
                String date = cursor.getString(cursor.getColumnIndex(dbHealthSchema.HeightTable.Date));

                Height height = new Height();
                height.setmId(Integer.parseInt(id));
                height.setmValue(Integer.parseInt(value));
                height.setmDate(new Date(date));

                heights.add(height);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return heights;
    }

    public Height getLastestHeight(){
        Height lastestHeight = new Height();
        Cursor cursor = viewData();
        cursor.moveToLast();
        if(cursor.getCount()>0){
            String id = cursor.getString(cursor.getColumnIndex(dbHealthSchema.HeightTable.Id));
            String value = cursor.getString(cursor.getColumnIndex(dbHealthSchema.HeightTable.Value));
            String date = cursor.getString(cursor.getColumnIndex(dbHealthSchema.HeightTable.Date));

            lastestHeight.setmId(Integer.parseInt(id));
            lastestHeight.setmValue(Integer.parseInt(value));
            lastestHeight.setmDate(new Date(date));
        }
        return lastestHeight;
    }
}
