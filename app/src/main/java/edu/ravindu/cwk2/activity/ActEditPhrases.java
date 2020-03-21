package edu.ravindu.cwk2.activity;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.database.DatabaseManager;
import edu.ravindu.cwk2.util.CustomCusorAdapter;

import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE;

public class ActEditPhrases extends ActCommon implements View.OnClickListener {

    private TextView btnEdit, btnSave;
    private ListView listPhrases;
    private TextInputLayout tilEditPhrase;
    private TextInputEditText etEditPhrase;

    final String[] arrFrom = new String[]{"phrase", PHRASE};
    final int[] arrTo = new int[]{R.id.tvPhrase};
    private String selectedPhrase;
    private int selectedPhraseId;

    private DatabaseManager dbManager;
    private Cursor cursor;
    private CustomCusorAdapter adapter;
    private DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            if (adapter != null) {
                selectedPhraseId = adapter.getSelectedPosition();
                selectedPhrase = adapter.getSelectedPhrase();

                btnEdit.setEnabled(true);
                btnEdit.setBackground(getDrawable(R.drawable.btn_selector));
//            Toast.makeText(ActEditPhrases.this, "Selected " + selection, Toast.LENGTH_SHORT).show();
            }
        }
    };

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
        listPhrases = findViewById(R.id.listPhrases);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        tilEditPhrase = findViewById(R.id.tilEditPhrase);
        etEditPhrase = findViewById(R.id.etEditPhrase);

        setInitialState();
    }

    private void setInitialState() {
        // set initial state for views
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
                } else if (etEditPhrase.getText() != null) {
                    etEditPhrase.setSelection(etEditPhrase.getText().length());
                    btnSave.setEnabled(true);
                    btnSave.setBackground(getDrawable(R.drawable.btn_selector));
                }
            }
        });
    }

    private void setupDbManager() {
        dbManager = new DatabaseManager(this);
        dbManager.open();
    }

    private void showList() {
        cursor = dbManager.findRecords();
        adapter = new CustomCusorAdapter(this, R.layout.edit_phrase_list_item, cursor, arrFrom, arrTo, 0);
        adapter.registerDataSetObserver(observer);

        listPhrases.setEmptyView(findViewById(R.id.tvEmptyList));
        listPhrases.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null)
            dbManager.close();
        if (cursor != null)
            cursor.close();
        if (adapter != null)
            adapter.unregisterDataSetObserver(observer);
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
        etEditPhrase.setText(selectedPhrase);

        Toast.makeText(ActEditPhrases.this, "Edit Button Clicked", Toast.LENGTH_SHORT).show();
    }

    private void saveModifiedPhrase() {
        Toast.makeText(ActEditPhrases.this, "Save Button Clicked", Toast.LENGTH_SHORT).show();

//      update text


        if (cursor != null)
            cursor.close();
        if (adapter != null)
            adapter.unregisterDataSetObserver(observer);
        setInitialState(); // reset to initial state
        showList(); // refresh list
    }
}

/*
 * References -
 * https://stackoverflow.com/a/51885148 - Detect when BaseAdapter.notifyDataSetChanged() finished
 * https://stackoverflow.com/a/32591819 - Get selected item from ListView bound with SimpleCursorAdapter
 * https://stackoverflow.com/a/9097790 - Adding an onclicklistener to listview (android)
 * https://stackoverflow.com/a/13415566 - ListView with OnItemClickListener
 * */
