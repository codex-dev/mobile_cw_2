package edu.ravindu.cwk2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE;
import static edu.ravindu.cwk2.database.DatabaseHelper.TABLE_NAME;
import static edu.ravindu.cwk2.database.DatabaseHelper._ID;

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

    public void insertRecord(String phrase) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(PHRASE, phrase);
        database.insert(TABLE_NAME, null, contentValue);
    }

    public Cursor findRecords() {
        String[] columns = new String[]{_ID, PHRASE};
        Cursor cursor = database.query(TABLE_NAME, columns, null,
                null, null, null, PHRASE); // order by phrase alphabetically
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateRecord(int id, String phrase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PHRASE, phrase);
        int updatedRowCount = database.update(TABLE_NAME, contentValues,
                _ID + " = ?" , new String[] {String.valueOf(id)} );
        return updatedRowCount;
    }

    public void deleteRecord(int id) {
        database.delete(TABLE_NAME, _ID + "=" + id,
                null);
    }
}
