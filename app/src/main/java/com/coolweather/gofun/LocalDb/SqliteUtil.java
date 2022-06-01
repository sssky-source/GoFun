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

    public String getToken(String element){
        Cursor cursor = db.query("UserTable",null,null,null,null,null,null);
        String token = null;
        String email = null;
        if(cursor.moveToFirst()) {
            do {
                Integer ID = Integer.valueOf(cursor.getString(cursor.getColumnIndex("id")));
                email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                token = cursor.getString(cursor.getColumnIndex("token"));

            } while (cursor.moveToNext());
        }
        cursor.close();
        String output = null;
        switch (element){
            case "email":
                output = email;
                break;
            case "token":
                output = token;
                break;
            default:
        }
        return output;

    }
}
