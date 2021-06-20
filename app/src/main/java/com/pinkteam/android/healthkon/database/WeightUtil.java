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

public class WeightUtil {

    SQLiteDatabase mDatabase;
    Context mContext;
    public WeightUtil(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new dbHeathHelper(mContext).getWritableDatabase();
    }

    private ContentValues weightContentValues(Weight weight){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHealthSchema.WeightTable.Value, weight.getmValue());
        contentValues.put(dbHealthSchema.WeightTable.Date, String.valueOf(weight.getmDate()));
        return contentValues;
    }

    //Inssẻt
    public long add(int Value, Date Date){
        Weight mWeight = new Weight(Value, Date);
        ContentValues contentValues = weightContentValues(mWeight);
        long result = mDatabase.insert("weight",null,contentValues);

        return result;
    }

    //Update
    public long update(Weight weight){
        ContentValues contentValues = weightContentValues(weight);
        long result= mDatabase.update("weight",contentValues, dbHealthSchema.WeightTable.Id +"=?",new String[]{String.valueOf(weight.getmId())});
        return result;
    }
    //delete
    public long delete(int id){
        String weightID = Integer.toString(id);
        return mDatabase.delete("weight", dbHealthSchema.WeightTable.Id +"=?", new String[]{weightID});
    }
    //view data
    public Cursor viewData(){
        String view = " SELECT * FROM " + dbHealthSchema.HeightTable.TABLE_NAME;
        Cursor cursor = mDatabase.rawQuery(view, null);

        return cursor;
    }
    // Truy van toan bo du lieu do ve 1 danh sach
    public List<Weight> getAllWeight(){
        List <Weight> weights = new ArrayList<>();
        String SELECT = "SELECT * FROM " + "weight";
        Cursor cursor = mDatabase.rawQuery(SELECT, null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String id = cursor.getString(cursor.getColumnIndex(dbHealthSchema.WeightTable.Id));
                String value = cursor.getString(cursor.getColumnIndex(dbHealthSchema.WeightTable.Value));
                long date = cursor.getLong(cursor.getColumnIndex(dbHealthSchema.HeightTable.Date));

                Weight weight = new Weight();
                weight.setmId(Integer.parseInt(id));
                weight.setmValue(Integer.parseInt(value));
                weight.setmDate(new Date(date));

                weights.add(weight);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return weights;
    }
}
