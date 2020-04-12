package edu.ravindu.cwk2.ui.activity;

import android.os.Bundle;

import edu.ravindu.cwk2.R;

public class ActLangSub extends ActCommon {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lang_sub);
        setupActionbar(getString(R.string.btn_lang_sub), true);
    }
}
