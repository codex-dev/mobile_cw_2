package edu.ravindu.cwk2.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.database.DatabaseManager;

import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE;

public class ActDisplayPhrases extends ActCommon {

    private DatabaseManager dbManager;
    private Cursor cursor;
    private ListView listPhrases;
    private SimpleCursorAdapter adapter;

    final String[] arrFrom = new String[]{PHRASE};
    final int[] arrTo = new int[]{R.id.tvPhrase};

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
        listPhrases = findViewById(R.id.listPhrases);
    }

    private void setupDbManager() {
        dbManager = new DatabaseManager(this);
        dbManager.open();
        cursor = dbManager.findRecords();
    }

    private void showList() {
        adapter = new SimpleCursorAdapter(this, R.layout.display_phrase_list_item, cursor, arrFrom, arrTo, 0);
        adapter.notifyDataSetChanged();
        listPhrases.setEmptyView(findViewById(R.id.tvEmptyList));
        listPhrases.setAdapter(adapter);
    }
}
