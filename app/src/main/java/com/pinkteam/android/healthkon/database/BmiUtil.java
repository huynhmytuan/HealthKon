package com.pinkteam.android.healthkon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pinkteam.android.healthkon.Models.*;
import com.pinkteam.android.healthkon.database.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BmiUtil {

    SQLiteDatabase mDatabase;
    Context mContext;
    public BmiUtil(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new dbHeathHelper(mContext).getWritableDatabase();
    }
    private ContentValues bmiContentValues(BMI bmi){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHealthSchema.BmiTable.Date, String.valueOf(bmi.getmDate()));
        contentValues.put(dbHealthSchema.BmiTable.CurBMI,bmi.getmCurbmi());
        contentValues.put(dbHealthSchema.BmiTable.Target_weight,bmi.getmTarge_tweight());

        return contentValues;
    }

    //Insert
    public long add(Date Date, int CurBMI, int Target_weight){
        BMI mBMI = new BMI(Date,CurBMI,Target_weight);
        ContentValues contentValues = bmiContentValues(mBMI);
        long result = mDatabase.insert("bmi",null,contentValues);

        return result;
    }

    //Update
    public long update(BMI bmi){
        ContentValues contentValues = bmiContentValues(bmi);
        long result= mDatabase.update("bmi",contentValues, dbHealthSchema.BmiTable.Id +"=?",new String[]{String.valueOf(bmi.getmId())});
        return result;
    }
    //delete
    public long delete(int id){
        String bmiID = Integer.toString(id);
        return mDatabase.delete("bmi", dbHealthSchema.BmiTable.Id +"=?", new String[]{bmiID});
    }
    //view data
    public Cursor viewData(){
        String view = " SELECT * FROM " + dbHealthSchema.BmiTable.TABLE_NAME;
        Cursor cursor = mDatabase.rawQuery(view, null);
        return cursor;
    }
    // Truy van toan bo du lieu do ve 1 danh sach
    public List<BMI> getAllBMI(){
        List <BMI> bmis = new ArrayList<>();
        Cursor cursor = viewData();
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String id = cursor.getString(cursor.getColumnIndex(dbHealthSchema.BmiTable.Id));
                String date = cursor.getString(cursor.getColumnIndex(dbHealthSchema.BmiTable.Date));
                String curbmi = cursor.getString(cursor.getColumnIndex(dbHealthSchema.BmiTable.CurBMI));
                String target_weight = cursor.getString(cursor.getColumnIndex(dbHealthSchema.BmiTable.Target_weight));

                BMI bmi = new BMI();
                bmi.setmId(Integer.parseInt(id));
                bmi.setmDate(new Date(date));
                bmi.setmCurbmi(Integer.parseInt(curbmi));
                bmi.setmTarge_tweight(Integer.parseInt(target_weight));

                bmis.add(bmi);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return bmis;
    }
}
