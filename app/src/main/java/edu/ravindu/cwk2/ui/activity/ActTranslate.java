package edu.ravindu.cwk2.ui.activity;

import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.BadRequestException;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.ArrayList;

import edu.ravindu.cwk2.R;
import edu.ravindu.cwk2.database.DatabaseManager;
import edu.ravindu.cwk2.model.Language;
import edu.ravindu.cwk2.model.Phrase;
import edu.ravindu.cwk2.ui.adapter.TranslateListAdapter;
import edu.ravindu.cwk2.ui.event_listener.PhraseListListener;

import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_CODE;
import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_ID;
import static edu.ravindu.cwk2.database.DatabaseHelper.LANG_NAME;
import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE_ID;
import static edu.ravindu.cwk2.database.DatabaseHelper.PHRASE_TEXT;
import static edu.ravindu.cwk2.database.DatabaseHelper.SUB_STATUS;

public class ActTranslate extends ActCommon {

    private static final String TAG = "ActTranslate";
    private DatabaseManager dbManager;
    private Cursor cursor;
    private Switch switchSelection;
    private Spinner spnSubLang;
    private ListView lvPhrases;
    private TextView btnTranslate, tvTranslation;
    private ImageView btnT2Speech;
    private ArrayList<Language> listSubLang;
    private ArrayList<String> listLangNames;
    private ArrayList<Phrase> listPhrases;

    // Translation
    private LanguageTranslator translationService;

    //Text to Speech
    private StreamPlayer player = new StreamPlayer();
    private TextToSpeech text2SpeechService;

    private String switchValue = "one";
    private String selectedPhrase;
    private String selectedLangCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_translate);
        setupActionbar(getString(R.string.btn_translate), true);

        listSubLang = new ArrayList<>();
        listLangNames = new ArrayList<>();
        listPhrases = new ArrayList<>();

        initViews();
        setupDbManager();
        getSubbedLangsFromDb();
        getPhrasesFromDb();
        setEventListeners();
    }

    private void initViews() {
        switchSelection = findViewById(R.id.switchSelection);
        spnSubLang = findViewById(R.id.spnSubLang);
        lvPhrases = findViewById(R.id.lvPhrases);
        btnTranslate = findViewById(R.id.btnTranslate);
        btnT2Speech = findViewById(R.id.btnT2Speech);
        tvTranslation = findViewById(R.id.tvTranslation);

        initializeTranslationService();
        initializeText2SpeechService();
    }

    private void initializeTranslationService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.language_translator_apikey));
        translationService = new LanguageTranslator("2018-05-01", authenticator);
        translationService.setServiceUrl(getString(R.string.language_translator_url));
    }

    private void initializeText2SpeechService() {
        Authenticator authenticator = new IamAuthenticator(getString(R.string.text_speech_apikey));
        text2SpeechService = new TextToSpeech(authenticator);
        text2SpeechService.setServiceUrl(getString(R.string.text_speech_url));
    }

    private void setupDbManager() {
        dbManager = new DatabaseManager(this);
        try {
            dbManager.open();
        } catch (SQLException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void getSubbedLangsFromDb() {
        try {
            cursor = dbManager.findSubscribedLanguages();
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    Language lang = new Language();
                    lang.setLanguageId(cursor.getInt(cursor.getColumnIndex(LANG_ID)));
                    lang.setLanguageCode(cursor.getString(cursor.getColumnIndex(LANG_CODE)));
                    lang.setLanguageName(cursor.getString(cursor.getColumnIndex(LANG_NAME)));
                    lang.setSubscribeStatus(cursor.getString(cursor.getColumnIndex(SUB_STATUS)));
                    listSubLang.add(lang);
                    listLangNames.add(lang.getLanguageName());
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        } finally {
            try {
                cursor.close();
                populateSpinner();
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void populateSpinner() {
        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, listLangNames);
        spnSubLang.setAdapter(langAdapter);
    }

    private void getPhrasesFromDb() {
        try {
            cursor = dbManager.findAllPhrases();
            if (cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
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
                showList();
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    private void showList() {
        ArrayAdapter adapter = new TranslateListAdapter(this, R.layout.display_phrase_list_item, listPhrases, new PhraseListListener() {
            @Override
            public void onPhraseItemClick(int position, String text) {
                selectedPhrase = text;
            }
        });

        lvPhrases.setEmptyView(findViewById(R.id.tvEmptyList));
        lvPhrases.setAdapter(adapter);
    }

    private void setEventListeners() {
        switchSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    switchValue = "all";
                    switchSelection.setText(getString(R.string.translate_all_phrases));
                } else {
                    switchValue = "one";
                    switchSelection.setText(getString(R.string.translate_single_phrase));
                }
            }
        });

        spnSubLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLangCode = listSubLang.get(position).getLanguageCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchValue.equals("all")) {
                    //TODO translate all phrases & save to DB
                } else if (switchValue.equals("one")) {
                    if (selectedLangCode == null) {
                        Toast.makeText(ActTranslate.this, "Please select language", Toast.LENGTH_SHORT).show();
                    } else if (selectedPhrase == null) {
                        Toast.makeText(ActTranslate.this, "Please select a phrase", Toast.LENGTH_SHORT).show();
                    } else {
                        translateSinglePhrase();
                    }
                }
            }
        });

        btnT2Speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchValue.equals("one")) {
                    if (tvTranslation.getText() != null && !tvTranslation.getText().toString().trim().isEmpty()) {
                        pronounceTranslation(tvTranslation.getText().toString().trim());
                    } else {
                        Toast.makeText(ActTranslate.this, "Please translate a phrase first", Toast.LENGTH_SHORT).show();
                    }
                } else if(switchValue.equals("all")){
                    //TODO speak phrase translation selected from list
                }
            }
        });

        selectedLangCode = listSubLang.get(0).getLanguageCode(); // default value
        switchSelection.setChecked(false); // default value
    }

    private void translateSinglePhrase() {
        new TranslationTask().execute(selectedPhrase, selectedLangCode);
    }

    private void pronounceTranslation(String translatedText) {
        new Text2SpeechTask().execute(translatedText);
    }

    private class TranslationTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(ActTranslate.this, "Please wait...", Toast.LENGTH_SHORT).show();
            btnTranslate.setBackground(getDrawable(R.drawable.bg_btn_disabled));
            btnTranslate.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                TranslateOptions translateOptions = new TranslateOptions.Builder()
                        .addText(params[0])  // for a single text
//                    .text() // for a list of text
                        .source(com.ibm.watson.language_translator.v3.util.Language.ENGLISH)
                        .target(params[1])
                        .build();
                TranslationResult result = translationService.translate(translateOptions)
                        .execute()
                        .getResult();

                String firstTranslation = result.getTranslations()
                        .get(0)
                        .getTranslation();
                return firstTranslation;
            } catch (NotFoundException | BadRequestException ex) {
                // NotFoundException - when translation isn't available for target language
                // BadRequestException - when source & target languages are same
                return null;
            }
        }

        @Override
        protected void onPostExecute(String translatedText) {
            if (translatedText != null) {
                tvTranslation.setVisibility(View.VISIBLE);
                tvTranslation.setText(translatedText);
                btnT2Speech.setBackground(getDrawable(R.drawable.btn_selector));
            } else {
                Toast.makeText(ActTranslate.this, "Sorry, Translation unavailable!", Toast.LENGTH_SHORT).show();
            }
            btnTranslate.setBackground(getDrawable(R.drawable.btn_selector));
            btnTranslate.setEnabled(true);
        }
    }

    private class Text2SpeechTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(ActTranslate.this, "Please wait...", Toast.LENGTH_SHORT).show();
            btnT2Speech.setBackground(getDrawable(R.drawable.bg_btn_disabled));
            btnT2Speech.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
                    .text(params[0])
                    .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                    .accept(HttpMediaType.AUDIO_WAV)
                    .build();
            player.playStream(text2SpeechService.synthesize(synthesizeOptions)
                    .execute()
                    .getResult());
            return "Complete";
        }

        @Override
        protected void onPostExecute(String s) {
            btnT2Speech.setBackground(getDrawable(R.drawable.btn_selector));
            btnT2Speech.setEnabled(true);
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
