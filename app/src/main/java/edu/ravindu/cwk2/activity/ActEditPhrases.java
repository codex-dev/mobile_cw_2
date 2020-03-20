package edu.ravindu.cwk2.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.database.DatabaseManager;

import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE;

public class ActEditPhrases extends ActCommon {

    private DatabaseManager dbManager;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    private TextView btnEdit, btnSave;
    private ListView listPhrases;
    private TextInputLayout tilEditPhrase;
    private TextInputEditText etEditPhrase;

    final String[] arrFrom = new String[]{PHRASE};
    final int[] arrTo = new int[]{R.id.tvPhrase};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_phrases);
        setupActionbar(getString(R.string.btn_edit_phrases), true);
        hideKeyboard(findViewById(R.id.uiEditPhrases), this);

        initViews();
        setupDbManager();
        showList();
    }

    private void initViews() {
        listPhrases = findViewById(R.id.listPhrases);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        tilEditPhrase = findViewById(R.id.tilEditPhrase);
        etEditPhrase = findViewById(R.id.etEditPhrase);
    }

    private void setupDbManager() {
        dbManager = new DatabaseManager(this);
        dbManager.open();
        cursor = dbManager.findRecords();
    }

    private void showList() {
        adapter = new SimpleCursorAdapter(this, R.layout.edit_phrase_list_item, cursor, arrFrom, arrTo, 0);
        adapter.notifyDataSetChanged();
        listPhrases.setEmptyView(findViewById(R.id.tvEmptyList));
        listPhrases.setAdapter(adapter);
    }
}
