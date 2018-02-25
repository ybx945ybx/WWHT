package com.a55haitao.wwht.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.a55haitao.wwht.BuildConfig;

/**
 * Created by Haotao_Fujie on 16/10/14.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "tb";
    public String createSql;

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, String createSql) {
        super(context,name,factory,version);
        this.createSql = createSql;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        if (createSql != null) {
            db.execSQL(createSql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    public void deleteAll (SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
    }
    */

}
