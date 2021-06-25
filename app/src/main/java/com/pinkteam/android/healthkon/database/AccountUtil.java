package com.pinkteam.android.healthkon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pinkteam.android.healthkon.Models.*;
import com.pinkteam.android.healthkon.database.*;
import java.util.ArrayList;
import java.util.List;

public class AccountUtil {

    SQLiteDatabase mDatabase;
    Context mContext;
    public AccountUtil(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new dbHeathHelper(mContext).getWritableDatabase();
    }

    private ContentValues accountContentValues(Account account){
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHealthSchema.AccountTable.Username,account.getmUsername());
        contentValues.put(dbHealthSchema.AccountTable.Password,account.getmPassword());
        return contentValues;
    }
    //Insert
    public long add(String UserName, String PassWord){
        Account mAccount = new Account(UserName, PassWord);
        ContentValues contentValues = accountContentValues(mAccount);

        long result = mDatabase.insert("account",null,contentValues);
        return result;
    }

    //Update
    public long update(Account account){
        ContentValues contentValues = accountContentValues(account);
        long result= mDatabase.update("account",contentValues, dbHealthSchema.AccountTable.Id +"=?",new String[]{String.valueOf(account.getmId())});
        return result;
    }
    //delete
    public long delete(int id){
        String accountID = Integer.toString(id);
        return mDatabase.delete("account", dbHealthSchema.AccountTable.Id +"=?", new String[]{accountID});
    }
    //view data
    public Cursor viewData(){
        String view = " SELECT * FROM " + dbHealthSchema.AccountTable.TABLE_NAME;
        Cursor cursor = mDatabase.rawQuery(view, null);

        return cursor;
    }
    // Truy van toan bo du lieu do ve 1 danh sach toan bo user
    public List<Account> getAllAccount(){
        List <Account> accounts = new ArrayList<>();
        Cursor cursor = viewData();
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                String id = cursor.getString(cursor.getColumnIndex(dbHealthSchema.AccountTable.Id));
                String username = cursor.getString(cursor.getColumnIndex(dbHealthSchema.AccountTable.Username));
                String password = cursor.getString(cursor.getColumnIndex(dbHealthSchema.AccountTable.Password));

                Account account = new Account();
                account.setmId(Integer.parseInt(id));
                account.setmUsername(username);
                account.setmPassword(password);

                accounts.add(account);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return accounts;
    }
}
