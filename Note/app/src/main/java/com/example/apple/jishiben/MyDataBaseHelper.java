package com.example.apple.jishiben;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDataBaseHelper extends SQLiteOpenHelper {
    //创建数据库的语句
    final String CREATE_TABLE_SQL =
            "create table memento_tb(_id integer primary " +
                    "key autoincrement,subject,body,date)";     //建表


    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //执行建表的操作
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("---------" + oldVersion + "------->" + newVersion);
    }


}
