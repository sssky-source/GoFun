package com.coolweather.gofun.LocalDb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.gofun.bean.User;

public class SqliteUtil {

    private static MyDatabaseHelper dbhelper;
    private static SQLiteDatabase db;
    private Context context;

    public SqliteUtil(Context context){
        this.context = context;
        dbhelper = new MyDatabaseHelper(context,"UserList.db",null,1);
        db = dbhelper.getWritableDatabase();
    }

    public String getToken(){
        Cursor cursor = db.query("UserTable",null,null,null,null,null,null);
        String token = null;
        if(cursor.moveToFirst()) {
            do {
                Integer ID = Integer.valueOf(cursor.getString(cursor.getColumnIndex("id")));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                token = cursor.getString(cursor.getColumnIndex("token"));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return token;
    }
}
