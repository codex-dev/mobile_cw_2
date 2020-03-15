package edu.ravindu.cwk2.activity;

import android.os.Bundle;

import edu.ravindu.cwk2.R;

public class ActTranslate extends ActCommon {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_translate);
        setupActionbar(getString(R.string.btn_translate), true);
    }
}
