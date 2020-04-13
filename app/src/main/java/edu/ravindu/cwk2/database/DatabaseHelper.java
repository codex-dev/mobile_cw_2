package edu.ravindu.cwk2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Created by Ravindu Fernando on 2020-03-17 at 10:46 PM
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "CWK_2.DB";  // database information
    private static final int DB_VERSION = 1; // database version

    public static final String TABLE_NAME = "PHRASES";
    public static final String PHRASE_ID = "phraseId";
    public static final String PHRASE_TEXT = "phraseText";

    // create table query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + PHRASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PHRASE_TEXT + " TEXT NOT NULL" +
            ");";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

/*
 * References -
 * https://www.journaldev.com/9438/android-sqlite-database-example-tutorial
 *
 * */
