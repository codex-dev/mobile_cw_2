package edu.ravindu.cwk2.activity;

import android.os.Bundle;

import edu.ravindu.cwk2.R;

public class ActDisplayPhrases extends ActCommon {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_display_phrases);
        setupActionbar(getString(R.string.btn_display_phrases), true);

    }
}
