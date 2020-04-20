package edu.ravindu.cwk2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_CODE;
import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_ID;
import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_NAME;
import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_TABLE;
import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE_ID;
import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE_TABLE;
import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE_TEXT;
import static edu.ravindu.cwk2.database.DatabaseHelper.SUB_STATUS;

/**
 * Created by Ravindu Fernando on 2020-03-17 at 10:46 PM
 */
public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertPhrase(String phrase) throws NullPointerException {
        ContentValues contentValue = new ContentValues();
        contentValue.put(PHRASE_TEXT, phrase);
        database.insert(PHRASE_TABLE, null, contentValue);
    }

    public Cursor findPhrases() throws NullPointerException {
        String[] columns = new String[]{PHRASE_ID, PHRASE_TEXT};
        Cursor cursor = database.query(PHRASE_TABLE, columns, null, null,
                null, null, PHRASE_TEXT); // order by phrase alphabetically
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updatePhrase(int id, String phrase) {
        ContentValues values = new ContentValues();
        values.put(PHRASE_TEXT, phrase);
        int updatedRowCount = database.update(PHRASE_TABLE, values, PHRASE_ID + " = ?", new String[]{String.valueOf(id)});
        return updatedRowCount;
    }

    public void deletePhrase(int id) {
//        database.delete(PHRASE_TABLE, PHRASE_ID + "=" + id, null);
        database.delete(PHRASE_TABLE, PHRASE_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void insertLanguage(String langCode, String langName, String subStatus) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(LANG_CODE, langCode);
        contentValue.put(LANG_NAME, langName);
        contentValue.put(SUB_STATUS, subStatus);
        database.insert(LANG_TABLE, null, contentValue);
    }

    public Cursor findLanguage() throws NullPointerException {
        String[] columns = new String[]{LANG_ID, LANG_CODE, LANG_NAME, SUB_STATUS};
        Cursor cursor = database.query(LANG_TABLE, columns, null, null,
                null, null, LANG_NAME); // order by phrase alphabetically
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateLanguage(int id, String subStatus) {
        ContentValues values = new ContentValues();
        values.put(SUB_STATUS, subStatus);
        int updatedRowCount = database.update(LANG_TABLE, values, LANG_ID + " = ?", new String[]{String.valueOf(id)});
        return updatedRowCount;
    }
}

/*
 * References -
 * https://www.youtube.com/watch?v=jNrLy_37eh8
 * https://www.youtube.com/watch?v=Wphc2q0dH_8
 * */