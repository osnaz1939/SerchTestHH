package com.act_ex.a1221.serchtesthh;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brost_host17 on 13.04.2017.
 */

public class CustomSqliteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "CustomSqliteOpenHelper";

    public CustomSqliteOpenHelper(Context context) {
        super(context, "db.db", null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableItems.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TableItems.DROP_TABLE);
        onCreate(db);
    }
}