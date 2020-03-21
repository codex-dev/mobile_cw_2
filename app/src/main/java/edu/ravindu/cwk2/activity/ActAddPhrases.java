package edu.ravindu.cwk2.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.database.DatabaseManager;

public class ActAddPhrases extends ActCommon implements View.OnClickListener {

    private TextInputLayout tilAddPhrase;
    private TextInputEditText etAddPhrase;
    private TextView btnSave;

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_phrases);
        setupActionbar(getString(R.string.btn_add_phrases), true);
        hideKeyboard(findViewById(R.id.uiAddPhrases), this);

        initViews();
        setupDbManager();
        setEventListeners();
    }

    private void initViews() {
        tilAddPhrase = findViewById(R.id.tilAddPhrase);
        etAddPhrase = findViewById(R.id.etAddPhrase);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupDbManager() {
        dbManager = new DatabaseManager(this);
        dbManager.open();
    }

    private void setEventListeners() {
        btnSave.setOnClickListener(this);
        etAddPhrase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isEmptyText(etAddPhrase)) {
                    tilAddPhrase.setError(null);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSave) {
            if (isValidText()) {
                saveToDatabase();
            }
        }
    }

    private void saveToDatabase() {
        String phrase = etAddPhrase.getText().toString().trim();
        dbManager.insertRecord(phrase);
        Toast.makeText(this, "Saved to database", Toast.LENGTH_SHORT).show();
    }

    private boolean isValidText() {
        if (isEmptyText(etAddPhrase)) {
            tilAddPhrase.setError(getString(R.string.err_input_phrase));
            return false;
        }
        /*String phrase = Objects.requireNonNull(etAddPhrase.getText()).toString().trim();
        if (!phrase.matches("[a-zA-Z]+")) {
            tilAddPhrase.setError(getString(R.string.err_invalid_phrase));
            return false;
        }*/
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null)
            dbManager.close();
    }
}

/*
 * References -
 * https://stackoverflow.com/a/30953551 - Design Android EditText to show error message as described by google
 * https://stackoverflow.com/a/52401339 - Outlined Edit Text from Material Design
 * */
