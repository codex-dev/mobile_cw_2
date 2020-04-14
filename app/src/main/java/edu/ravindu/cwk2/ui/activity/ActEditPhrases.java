package edu.ravindu.cwk2.ui.activity;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.database.DatabaseManager;
import edu.ravindu.cwk2.model.Phrase;
import edu.ravindu.cwk2.ui.adapter.EditListAdapter;
import edu.ravindu.cwk2.ui.event_listener.ClickListener;

import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE_ID;
import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE_TEXT;

public class ActEditPhrases extends ActCommon implements View.OnClickListener {

    private static final String TAG = "ActEditPhrases";
    private TextView btnEdit, btnSave;
    //    private TextInputLayout tilEditPhrase;
    private TextInputEditText etEditPhrase;
    private ListView lvPhrases;
    private ArrayAdapter adapter;
    private ArrayList<Phrase> listPhrases;
    private DatabaseManager dbManager;
    private Cursor cursor;
    private Phrase selectedPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit_phrases);
        setupActionbar(getString(R.string.btn_edit_phrases), true);
        hideKeyboard(findViewById(R.id.uiEditPhrases), this);

        initViews();
        setEventListeners();
        setupDbManager();
        showList();
    }

    private void initViews() {
        lvPhrases = findViewById(R.id.lvPhrases);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
//        tilEditPhrase = findViewById(R.id.tilEditPhrase);
        etEditPhrase = findViewById(R.id.etEditPhrase);

        setInitialState();
    }

    private void setInitialState() {
        etEditPhrase.setText(null);
        etEditPhrase.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(false);
        btnEdit.setBackground(getDrawable(R.drawable.bg_btn_disabled));
        btnSave.setBackground(getDrawable(R.drawable.bg_btn_disabled));
    }

    private void setEventListeners() {
        btnEdit.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        etEditPhrase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isEmptyText(etEditPhrase)) { // disable save button if text field is empty
                    btnSave.setEnabled(false);
                    btnSave.setBackground(getDrawable(R.drawable.bg_btn_disabled));
                } else {
                    etEditPhrase.setSelection(etEditPhrase.getText().length());
                    btnSave.setEnabled(true);
                    btnSave.setBackground(getDrawable(R.drawable.btn_selector));
                }
            }
        });
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
        adapter = new EditListAdapter(this, R.layout.edit_phrase_list_item, listPhrases, new ClickListener() {
            @Override
            public void onListItemClickListener(int position, String text) {
                selectedPhrase = listPhrases.get(position);
                btnEdit.setEnabled(true);
                btnEdit.setBackground(getDrawable(R.drawable.btn_selector));
            }
        });

        lvPhrases.setEmptyView(findViewById(R.id.tvEmptyList));
        lvPhrases.setAdapter(adapter);
    }

    private void getPhrasesFromDb() {
        listPhrases = new ArrayList<>();
        try {
            cursor = dbManager.findPhrases();

            if (cursor.getCount() > 0) {
                for(int i=0; i<cursor.getCount(); i++){
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
        if (dbManager != null)
            dbManager.close();
        if (cursor != null)
            cursor.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEdit:
                editPhrase();
                break;
            case R.id.btnSave:
                saveModifiedPhrase();
                break;
        }
    }

    private void editPhrase() {
        if (!etEditPhrase.isEnabled()) {
            etEditPhrase.setEnabled(true);
        }
        etEditPhrase.setText(selectedPhrase.getPhrase());
    }

    private void saveModifiedPhrase() {
        int result = dbManager.updatePhrase(selectedPhrase.getPhraseId(), getTrimmedText(etEditPhrase));

        if (result == 1) {
            Toast.makeText(ActEditPhrases.this, "Phrase updated", Toast.LENGTH_SHORT).show();
            setInitialState(); // reset to initial state
            showList(); // refresh list
        }
    }
}

/*
 * References -
 * https://stackoverflow.com/a/51885148 - Detect when BaseAdapter.notifyDataSetChanged() finished
 * https://stackoverflow.com/a/32591819 - Get selected item from ListView bound with SimpleCursorAdapter
 * https://stackoverflow.com/a/9097790 - Adding an onclicklistener to listview (android)
 * https://stackoverflow.com/a/13415566 - ListView with OnItemClickListener
 * */
