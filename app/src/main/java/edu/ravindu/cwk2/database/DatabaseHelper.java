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

    // Phrase table & it's fields
    public static final String PHRASE_TABLE = "PHRASE";
    public static final String PHRASE_ID = "phraseId";
    public static final String PHRASE_TEXT = "phraseText";

    // Lang table & it's fields
    public static final String LANG_TABLE = "LANG";
    public static final String LANG_ID = "langId";
    public static final String LANG_CODE = "langCode";
    public static final String LANG_NAME = "langName";
    public static final String SUB_STATUS = "sub_status";

    // Translation table & it's fields
    public static final String TRANSL_TABLE = "TRANSL";
    public static final String TRANSL_ID = "translId";
    public static final String TRANSL_LANG_ID = "langId";
    public static final String TRANSL_PHRASE_ID = "phraseId";
    public static final String TRANSL_TEXT = "translation";

    // create Phrase table query
    private static final String CREATE_PHRASE_TABLE =
            "CREATE TABLE " + PHRASE_TABLE + "("
                    + PHRASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PHRASE_TEXT + " TEXT NOT NULL"
                    + ");";

    private static final String CREATE_LANG_TABLE =
            "CREATE TABLE " + LANG_TABLE + "("
                    + LANG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LANG_CODE + " TEXT NOT NULL,"
                    + LANG_NAME + " TEXT NOT NULL,"
                    + SUB_STATUS + " TEXT NOT NULL"
                    + ");";

    private static final String CREATE_TRANSL_TABLE =
            "CREATE TABLE " + TRANSL_TABLE + "("
                    + TRANSL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TRANSL_LANG_ID + " INTEGER NOT NULL,"
                    + TRANSL_PHRASE_ID + " INTEGER NOT NULL,"
                    + TRANSL_TEXT + " INTEGER NOT NULL,"
                    + "FOREIGN KEY(" + TRANSL_LANG_ID + ") REFERENCES " + LANG_TABLE + "(" + LANG_ID + "),"
                    + "FOREIGN KEY(" + TRANSL_PHRASE_ID + ") REFERENCES " + PHRASE_TABLE + "(" + PHRASE_ID + ")"
                    + ");";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PHRASE_TABLE);
        sqLiteDatabase.execSQL(CREATE_LANG_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRANSL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PHRASE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LANG_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSL_TABLE);
        onCreate(sqLiteDatabase);
    }
}

/*
 * References -
 * https://www.journaldev.com/9438/android-sqlite-database-example-tutorial
 * https://stackoverflow.com/a/1884841
 * */
