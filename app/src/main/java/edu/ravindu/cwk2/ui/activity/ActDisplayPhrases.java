package edu.ravindu.cwk2.ui.activity;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.database.DatabaseManager;
import edu.ravindu.cwk2.model.Phrase;
import edu.ravindu.cwk2.ui.adapter.DisplayListAdapter;

import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE_ID;
import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE_TEXT;

public class ActDisplayPhrases extends ActCommon {

    private static final String TAG = "ActDisplayPhrases";
    private DatabaseManager dbManager;
    private Cursor cursor;
    private ListView lvPhrases;
    private ArrayList<Phrase> listPhrases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_phrases);
        setupActionbar(getString(R.string.btn_display_phrases), true);

        initViews();
        setupDbManager();
        showList();
    }

    private void initViews() {
        lvPhrases = findViewById(R.id.lvPhrases);
    }

    private void setupDbManager() {
        dbManager = new DatabaseManager(this);
        try {
            dbManager.open();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void showList() {
        getPhrasesFromDb();
        ArrayAdapter adapter = new DisplayListAdapter(this, R.layout.display_phrase_list_item, listPhrases);
        lvPhrases.setEmptyView(findViewById(R.id.tvEmptyList));
        lvPhrases.setAdapter(adapter);
    }

    private void getPhrasesFromDb() {
        listPhrases = new ArrayList<>();
        try {
            cursor = dbManager.findAllPhrases();
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    Phrase p = new Phrase();
                    p.setPhraseId(cursor.getInt(cursor.getColumnIndex(PHRASE_ID)));
                    p.setPhrase(cursor.getString(cursor.getColumnIndex(PHRASE_TEXT)));
                    listPhrases.add(p);
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                cursor.close();
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
