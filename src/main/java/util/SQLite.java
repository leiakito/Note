package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/*
    Author:leia
    Write The Code Change The World    
*/public class SQLite extends SQLiteOpenHelper {
    public SQLite(@Nullable Context context) {
        super(context, "leia.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table message (id integer primary key autoincrement ,title varchar(255),writeTime varchar(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
