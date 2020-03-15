package edu.ravindu.cwk2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.ravindu.cwk2.R;

public class ActHome extends ActCommon implements View.OnClickListener {

    private TextView btnAddPhrases, btnDisplayPhrases, btnEditPhrases, btnLangSub, btnTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        setupActionbar(getString(R.string.app_name), false);

        initViews();
        setEventListeners();
    }

    private void initViews() {
        btnAddPhrases = findViewById(R.id.btnAddPhrases);
        btnDisplayPhrases = findViewById(R.id.btnDisplayPhrases);
        btnEditPhrases = findViewById(R.id.btnEditPhrases);
        btnLangSub = findViewById(R.id.btnLangSub);
        btnTranslate = findViewById(R.id.btnTranslate);
    }

    private void setEventListeners() {
        btnAddPhrases.setOnClickListener(this);
        btnDisplayPhrases.setOnClickListener(this);
        btnEditPhrases.setOnClickListener(this);
        btnLangSub.setOnClickListener(this);
        btnTranslate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddPhrases:
                showActivity(ActAddPhrases.class, null);
                break;
            case R.id.btnDisplayPhrases:
                showActivity(ActDisplayPhrases.class, null);
                break;
            case R.id.btnEditPhrases:
                showActivity(ActEditPhrases.class, null);
                break;
            case R.id.btnLangSub:
                showActivity(ActLangSub.class, null);
                break;
            case R.id.btnTranslate:
                showActivity(ActTranslate.class, null);
                break;
        }
    }

}
