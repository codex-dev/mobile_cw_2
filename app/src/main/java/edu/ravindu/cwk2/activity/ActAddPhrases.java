package edu.ravindu.cwk2.activity;

import android.os.Bundle;

import edu.ravindu.cwk2.R;

public class ActAddPhrases extends ActCommon {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_add_phrases);
        setupActionbar(getString(R.string.btn_add_phrases), true);
    }
}
