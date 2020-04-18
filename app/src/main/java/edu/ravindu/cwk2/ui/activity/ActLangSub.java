package edu.ravindu.cwk2.ui.activity;

import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.database.DatabaseManager;
import edu.ravindu.cwk2.model.Language;
import edu.ravindu.cwk2.ui.adapter.LanguageListAdapter;
import edu.ravindu.cwk2.ui.event_listener.ClickListener;

import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_CODE;
import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_ID;
import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_NAME;

public class ActLangSub extends ActCommon {

    private static final String TAG = "ActLangSub";
    private TextView btnUpdate;
    private DatabaseManager dbManager;
    private Cursor cursor;
    private ListView lvLanguages;
    private ArrayAdapter adapter;
    private ArrayList<Language> listLanguages;

    private LanguageTranslator translationService;
    private List<IdentifiableLanguage> listIdentifiableLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lang_sub);
        setupActionbar(getString(R.string.btn_lang_sub), true);

        initViews();
        setupDbManager();
        getLanguagesFromDb();
    }

    private void initViews() {
        lvLanguages = findViewById(R.id.lvLanguages);
        btnUpdate = findViewById(R.id.btnUpdate);
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
        adapter = new LanguageListAdapter(this, R.layout.sub_lang_list_item, listLanguages, new ClickListener() {
            @Override
            public void onListItemClickListener(int position, String text) {
                //TODO
            }
        });
        lvLanguages.setEmptyView(findViewById(R.id.tvEmptyList));
        lvLanguages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getLanguagesFromDb() {
        listLanguages = new ArrayList<>();
        try {
            cursor = dbManager.findLanguages();
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    Language lang = new Language();
                    lang.setLanguageId(cursor.getInt(cursor.getColumnIndex(LANG_ID)));
                    lang.setLanguageCode(cursor.getString(cursor.getColumnIndex(LANG_CODE)));
                    lang.setLanguageName(cursor.getString(cursor.getColumnIndex(LANG_NAME)));
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

    private void getLanguagesFromService() {
        translationService = initLanguageTranslatorService(); // translate
        new TranslationTask().execute();
    }

    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.language_translator_apikey));
        LanguageTranslator service = new LanguageTranslator(getString(R.string.version_date), authenticator);
        service.setServiceUrl(getString(R.string.language_translator_url));
        return service;
    }

    private class TranslationTask extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "/// Load languages : pre execute");
            Toast.makeText(ActLangSub.this, "Retrieval started.", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(ActLangSub.this, "Retrieving data from service...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.i(TAG, "/// Load languages : do in background");
            IdentifiableLanguages languages = translationService.listIdentifiableLanguages().execute().getResult();
            listIdentifiableLanguages = languages.getLanguages();
            return "Retrieval completed.";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i(TAG, "/// Load languages : post execute");
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
