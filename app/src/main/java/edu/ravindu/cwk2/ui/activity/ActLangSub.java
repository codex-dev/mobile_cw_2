package edu.ravindu.cwk2.ui.activity;

import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.database.DatabaseManager;
import edu.ravindu.cwk2.model.Language;
import edu.ravindu.cwk2.ui.adapter.LanguageListAdapter;
import edu.ravindu.cwk2.ui.event_listener.LangListListener;

import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_CODE;
import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_ID;
import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_NAME;
import static edu.ravindu.cwk2.database.DatabaseHelper.SUB_STATUS;

public class ActLangSub extends ActCommon {

    private static final String TAG = "ActLangSub";
    private TextView btnUpdate;
    private DatabaseManager dbManager;
    private Cursor cursor;
    private ListView lvLanguages;
    private ArrayList<Language> listLanguages;
    private HashMap<Integer, Boolean> mapSubModifiedLangs;

    private LanguageTranslator translationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lang_sub);
        setupActionbar(getString(R.string.btn_lang_sub), true);

        listLanguages = new ArrayList<>();
        mapSubModifiedLangs = new HashMap<>();

        initViews();
        setupDbManager();
        getLanguagesFromDb();
    }

    private void initViews() {
        lvLanguages = findViewById(R.id.lvLanguages);
        btnUpdate = findViewById(R.id.btnUpdate);

        // set event listener for update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapSubModifiedLangs.size() > 0) {
                    saveModificationsToDb();
                } else {
                    Toast.makeText(ActLangSub.this, "No new modifications to update", Toast.LENGTH_SHORT).show();
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

    private void getLanguagesFromDb() {
        try {
            cursor = dbManager.findAllLanguages();
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    Language lang = new Language();
                    lang.setLanguageId(cursor.getInt(cursor.getColumnIndex(LANG_ID)));
                    lang.setLanguageCode(cursor.getString(cursor.getColumnIndex(LANG_CODE)));
                    lang.setLanguageName(cursor.getString(cursor.getColumnIndex(LANG_NAME)));
                    lang.setSubscribeStatus(cursor.getString(cursor.getColumnIndex(SUB_STATUS)));
                    listLanguages.add(lang);
                }
            } else {
                getLanguagesFromService();
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                cursor.close();
                showList();
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void showList() {
        ArrayAdapter adapter = new LanguageListAdapter(this, R.layout.sub_lang_list_item, listLanguages, new LangListListener() {
            @Override
            public void onItemCheckedChanged(int id, boolean isChecked) {
                mapSubModifiedLangs.put(id, isChecked);
            }
        });
        lvLanguages.setEmptyView(findViewById(R.id.tvEmptyList));
        lvLanguages.setAdapter(adapter);
    }

    private void saveModificationsToDb() {
        int result = 0;
        for (Map.Entry<Integer, Boolean> entry : mapSubModifiedLangs.entrySet()) {
            String checkStatus = entry.getValue() ? "Y" : "N";
            result += dbManager.updateLanguage(entry.getKey(), checkStatus);
        }

        if (result == mapSubModifiedLangs.size()) {
            Toast.makeText(ActLangSub.this, "Subscription Successful", Toast.LENGTH_SHORT).show();
            mapSubModifiedLangs.clear();
        }

    }

    private void getLanguagesFromService() {
        //Initialize language translation service
        Authenticator authenticator = new IamAuthenticator(getString(R.string.language_translator_apikey));
        translationService = new LanguageTranslator(getString(R.string.version_date), authenticator);
        translationService.setServiceUrl(getString(R.string.language_translator_url));

        new TranslationTask().execute();
    }

    private class TranslationTask extends AsyncTask<Void, String, String> {

        private List<IdentifiableLanguage> listIdentifiableLanguages;

        @Override
        protected void onPreExecute() {
            Toast.makeText(ActLangSub.this, "Retrieving Languages...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            IdentifiableLanguages languages = translationService.listIdentifiableLanguages().execute().getResult();
            listIdentifiableLanguages = languages.getLanguages();
            return "Languages Retrieved";
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(ActLangSub.this, s, Toast.LENGTH_SHORT).show();

            for (IdentifiableLanguage language : listIdentifiableLanguages) {
                dbManager.insertLanguage(language.getLanguage(), language.getName(), "N");
            }

            getLanguagesFromDb();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
